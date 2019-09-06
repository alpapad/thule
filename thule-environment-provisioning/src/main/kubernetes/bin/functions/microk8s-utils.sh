#!/bin/bash

function createService() {
  # Input parameters
  kubernetesConfigurationFile=$1

  echo ""
  echo "Creating service..."

  kubectl create -f "${kubernetesConfigurationFile}"

  serviceName=$(basename "${kubernetesConfigurationFile}" | sed "s/.yml.*//g")
  startupStartTime=$(date +%s)
  elapsedSeconds=$(($(date +%s) - startupStartTime))
  maxElapsedSeconds=600

  echo ""
  echo -n "Waiting for service to start (up to a maximum of ${maxElapsedSeconds} seconds)..."
  serviceInfo=$(kubectl get services --output=json "${serviceName}" 2>/dev/null)
  podInfo=$(kubectl get pods --output=jsonpath="{..containers[?(@.name==\"${serviceName}\")]}" 2>/dev/null | cut -d" " -f1)
  until [[ ${elapsedSeconds} -ge ${maxElapsedSeconds} ]] || [[ "${serviceInfo}" != "" ]] && [[ "${podInfo}" != "" ]]; do
    echo -en "\rWaiting for service to start (up to a maximum of ${maxElapsedSeconds} seconds)...${elapsedSeconds}s"
    sleep 5
    elapsedSeconds=$(($(date +%s) - startupStartTime))
    serviceInfo=$(kubectl get services --output=json "${serviceName}" 2>/dev/null)
    podInfo=$(kubectl get pods --output=jsonpath="{..containers[?(@.name==\"${serviceName}\")]}" 2>/dev/null | cut -d" " -f1)
  done

  if [[ ${elapsedSeconds} -lt ${maxElapsedSeconds} ]]; then
    echo -e "\rWaiting for service to start (up to a maximum of ${maxElapsedSeconds} seconds)...\033[32m done \033[0m"
    echo "Service started successfully and took ${elapsedSeconds} second(s)"
    startupResponseCode=0
  else
    echo -e "\rWaiting for service to start (up to a maximum of ${maxElapsedSeconds} seconds)...\033[31m failed \033[0m"
    echo "ERROR: Service failed to start within ${elapsedSeconds} second(s)"
    startupResponseCode=255
  fi

  return ${startupResponseCode}
}

function deleteService() {
  # Input parameters
  kubernetesConfigurationFile=$1

  echo ""
  echo "Deleting service..."

  kubectl delete --all -f "${kubernetesConfigurationFile}"

  serviceName=$(basename "${kubernetesConfigurationFile}" | sed "s/.yml.*//g")
  shutdownStartTime=$(date +%s)
  elapsedSeconds=$(($(date +%s) - shutdownStartTime))
  maxElapsedSeconds=600

  echo ""
  echo -n "Waiting for service to shutdown (up to a maximum of ${maxElapsedSeconds} seconds)..."
  serviceInfo=$(kubectl get services --output=json "${serviceName}" 2>/dev/null)
  podInfo=$(kubectl get pods --output=jsonpath="{..containers[?(@.name==\"${serviceName}\")]}" 2>/dev/null | cut -d" " -f1)
  until [[ ${elapsedSeconds} -ge ${maxElapsedSeconds} ]] || [[ "${serviceInfo}" == "" ]] && [[ "${podInfo}" == "" ]]; do
    echo -en "\rWaiting for service to shutdown (up to a maximum of ${maxElapsedSeconds} seconds)...${elapsedSeconds}s"
    sleep 5
    elapsedSeconds=$(($(date +%s) - shutdownStartTime))
    serviceInfo=$(kubectl get services --output=json "${serviceName}" 2>/dev/null)
    podInfo=$(kubectl get pods --output=jsonpath="{..containers[?(@.name==\"${serviceName}\")]}" 2>/dev/null | cut -d" " -f1)
  done

  if [[ ${elapsedSeconds} -lt ${maxElapsedSeconds} ]]; then
    echo -e "\rWaiting for service to shutdown (up to a maximum of ${maxElapsedSeconds} seconds)...\033[32m done \033[0m"
    echo "Service shutdown successfully and took ${elapsedSeconds} second(s)"
    shutdownResponseCode=0
  else
    echo -e "\rWaiting for service to shutdown (up to a maximum of ${maxElapsedSeconds} seconds)...\033[31m failed \033[0m"
    echo "ERROR: Service failed to shutdown within ${elapsedSeconds} second(s)"
    shutdownResponseCode=255
  fi

  return ${shutdownResponseCode}
}

function configureMicrok8s() {
  if [[ $(grep "${NEXUS_HOST}:${NEXUS_PORT_DOCKER}" /var/snap/microk8s/current/args/containerd-template.toml) != "" ]]; then
    echo ""
    echo "================================================================================"
    echo "Skipping configuration of microk8s because it is already configured"
    echo "================================================================================"
  else
    echo ""
    echo "================================================================================"
    echo "About to configure microk8s..."
    echo ""

    echo ""
    echo "Enabling add-ons dns, storage and dashboard..."
    microk8s.enable dns storage dashboard

    echo ""
    echo "Adding nexus docker registry..."
    sudo sed -i "/${NEXUS_HOST}:${NEXUS_PORT_DOCKER}/d" /var/snap/microk8s/current/args/containerd-template.toml
    sudo sed -i "/\[plugins.cri.registry.mirrors\]/a \
    \        [plugins.cri.registry.mirrors.\"${NEXUS_HOST}:${NEXUS_PORT_DOCKER}\"] \n\
    \          endpoint = [\"http://${NEXUS_HOST}:${NEXUS_PORT_DOCKER}\"]" /var/snap/microk8s/current/args/containerd-template.toml
    # Restart microk8s to effect registry changes
    microk8s.stop
    microk8s.start

    echo ""
    echo "Updating iptables (see 'My pods cant reach the internet or each other (but my MicroK8s host machine can)' in https://microk8s.io/docs/)..."
    sudo iptables -P FORWARD ACCEPT

    echo "Creating kubectl alias..."
    sudo snap alias microk8s.kubectl kubectl

    echo ""
    echo "Exposing dashboard to port ${KUBERNETES_DASHBOARD_NODEPORT}..."
    kubectl get services kubernetes-dashboard -n kube-system -o yaml | sed "s/.*type: ClusterIP.*/  type: NodePort/" | sed "/.*port:.*/ a\    nodePort: ${KUBERNETES_DASHBOARD_NODEPORT}" | kubectl replace -f -

    echo ""
    echo "Enabling skip login for dashboard..."
    kubectl get deployment kubernetes-dashboard -n kube-system -o yaml | sed "/.*--auto-generate-certificates.*/ a\        - --enable-skip-login" | kubectl replace -f -

    echo ""
    echo "Have configured microk8s"
    echo "================================================================================"
  fi

  showMicrok8sStatus
}

function showMicrok8sStatus() {
  echo ""
  echo "================================================================================"
  echo "Microk8s status..."
  echo ""
  microk8s.status

  echo ""
  echo "Nodes, services, pods..."
  microk8s.kubectl get nodes,services,pods --all-namespaces -o wide

  dashboardIpAddress=$(microk8s.kubectl get services --namespace=kube-system --no-headers kubernetes-dashboard | tr -s " " | cut -d " " -f3)
  defaultToken=$(microk8s.kubectl -n kube-system get secret | grep default-token | cut -d " " -f1)
  signinBearerToken=$(microk8s.kubectl -n kube-system describe secret "$defaultToken" | grep token: | tr -s " " | cut -d " " -f2)
  echo ""
  echo "Dashboard URL : https://${dashboardIpAddress} or https://localhost:${KUBERNETES_DASHBOARD_NODEPORT}"
  echo "Sign-in bearer token : ${signinBearerToken}"
  echo "================================================================================"
}
