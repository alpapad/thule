#!/bin/bash

function kubectlApply() {
  # Input parameters
  kubernetesConfigurationFile=$1

  echo ""
  echo "Applying k8s file..."

  # Change the kubernetes file to force the rollout
  currentTime=$(date "+%Y%m%d%H%M%S")
  sed -i "/ROLLOUT_TIME/{
  N
  s/value:.*/value: \"$currentTime\"/
  }" ${kubernetesConfigurationFile}

  kubectl apply -f "${kubernetesConfigurationFile}"
}

function kubectlRolloutStatus() {
  # Input parameters
  kubernetesConfigurationFile=$1

  serviceName=$(awk '/app: /{print $NF;exit;}' "${kubernetesConfigurationFile}")
  startupStartTime=$(date +%s)
  maxElapsedSeconds=600

  echo ""
  echo "Waiting for deployment rollout to finish (up to a maximum of ${maxElapsedSeconds} seconds)..."
  if kubectl rollout status deployment/${serviceName} --namespace=thule --timeout=${maxElapsedSeconds}s; then
    elapsedSeconds=$(($(date +%s) - startupStartTime))
    echo "Rollout completed successfully and took ${elapsedSeconds} second(s)"
    rolloutsStatusResponseCode=0
  else
    elapsedSeconds=$(($(date +%s) - startupStartTime))
    echo "ERROR: Failed to rollout within ${elapsedSeconds} second(s)"
    rolloutsStatusResponseCode=255
  fi

  return ${rolloutsStatusResponseCode}
}

function configureMicrok8s() {
  echo ""
  echo "================================================================================"
  echo "About to configure microk8s..."

  microk8sStatusStartTime=$(date +%s)
  elapsedSeconds=$(($(date +%s) - microk8sStatusStartTime))

  echo ""
  echo -n "Waiting for microk8s to be ready to accept commands..."
  microk8sStatus=$(sudo microk8s.status --timeout 600 --wait-ready )

  elapsedSeconds=$(($(date +%s) - microk8sStatusStartTime))
  echo -e "\rWaiting for microk8s to be ready to accept commands...\033[32m done \033[0m"
  echo "Micro8ks is ready to accept commands and took ${elapsedSeconds} second(s)"

  echo ""
  echo -n "Creating kubectl alias..."
  if $(kubectl version 1>/dev/null 2>/dev/null); then
    echo -e "\rCreating kubectl alias...\033[32m already created \033[0m"
  else
    sudo snap alias microk8s.kubectl kubectl
    echo -e "\rCreating kubectl alias...\033[32m done \033[0m"
  fi

  echo ""
  echo -n "Enabling dashboard add-on..."
  if [[ $(echo "${microk8sStatus}" | grep "dashboard: enabled") != "" ]]; then
    echo -e "\rEnabling dashboard add-on...\033[32m already enabled \033[0m"
  else
    echo ""
    sudo microk8s.enable dashboard
    echo -e "Enabling dashboard add-on...\033[32m done \033[0m"
  fi

  echo ""
  echo -n "Enabling dns add-on..."
  if [[ $(echo "${microk8sStatus}" | grep "dns: enabled") != "" ]]; then
    echo -e "\rEnabling dns add-on...\033[32m already enabled \033[0m"
  else
    echo ""
    sudo microk8s.enable dns
    echo -e "Enabling dns add-on...\033[32m done \033[0m"
  fi

  echo ""
  echo -n "Enabling ingress add-on..."
  if [[ $(echo "${microk8sStatus}" | grep "ingress: enabled") != "" ]]; then
    echo -e "\rEnabling ingress add-on...\033[32m already enabled \033[0m"
  else
    echo ""
    sudo microk8s.enable ingress
    echo -e "Enabling ingress add-on...\033[32m done \033[0m"
  fi

  echo ""
  echo -n "Enabling storage add-on..."
  if [[ $(echo "${microk8sStatus}" | grep "storage: enabled") != "" ]]; then
    echo -e "\rEnabling storage add-on...\033[32m already enabled \033[0m"
  else
    echo ""
    sudo microk8s.enable storage
    echo -e "Enabling storage add-on...\033[32m done \033[0m"
  fi

  echo ""
  echo -n "Adding nexus docker registry..."
  if [[ $(sudo grep "${NEXUS_HOST}:${NEXUS_PORT_DOCKER}" /var/snap/microk8s/current/args/containerd-template.toml) != "" ]]; then
    echo -e "\rAdding nexus docker registry...\033[32m already added \033[0m"
  else
    sudo sed -i "/${NEXUS_HOST}:${NEXUS_PORT_DOCKER}/d" /var/snap/microk8s/current/args/containerd-template.toml
    sudo sed -i "/\[plugins.cri.registry.mirrors\]/a \
      \        [plugins.cri.registry.mirrors.\"${NEXUS_HOST}:${NEXUS_PORT_DOCKER}\"] \n\
      \          endpoint = [\"http://${NEXUS_HOST}:${NEXUS_PORT_DOCKER}\"]" /var/snap/microk8s/current/args/containerd-template.toml
    # Restart microk8s to effect registry changes
    sudo microk8s.stop
    sudo microk8s.start
    echo -e "\rAdding nexus docker registry...\033[32m done \033[0m"
  fi

  # Updating iptables (see https://microk8s.io/docs/troubleshooting - 'My pods cant reach the internet or each other (but my MicroK8s host machine can)'
  echo ""
  echo -n "Updating iptables..."
  if [[ $(sudo iptables -L FORWARD | grep "Chain FORWARD" | grep "ACCEPT") != "" ]]; then
    echo -e "\rUpdating iptables...\033[32m already updated \033[0m"
  else
    sudo iptables -P FORWARD ACCEPT
    echo -e "\rUpdating iptables...\033[32m done \033[0m"
  fi

  echo ""
  echo -n "Enabling skip login for dashboard..."
  if [[ $(kubectl get deployment kubernetes-dashboard --namespace=kube-system -o yaml | grep "enable-skip-login") != "" ]]; then
    echo -e "\rEnabling skip login for dashboard...\033[32m already created \033[0m"
  else
    echo ""
    kubectl get deployment kubernetes-dashboard --namespace=kube-system -o yaml | sed "/.*--auto-generate-certificates.*/ a\        - --enable-skip-login" | kubectl replace -f -
    echo -e "Enabling skip login for dashboard...\033[32m done \033[0m"
  fi

  echo ""
  echo -n "Creating ingress for dashboard..."
  if [[ $(kubectl get ingress dashboard --namespace kube-system 2>&1 | grep "not found") == "" ]]; then
    echo -e "\rCreating ingress for dashboard...\033[32m already created \033[0m"
  else
    echo ""
    kubectl apply -f "${SCRIPT_DIR_NAME}/../apply/dashboard-ingress.yml"
    echo -e "Creating ingress for dashboard...\033[32m done \033[0m"
  fi

  echo ""
  echo "Have configured microk8s"
  echo "================================================================================"
}

function configureThule() {
  echo ""
  echo "================================================================================"
  echo "About to configure thule..."

  microk8sStatusStartTime=$(date +%s)
  elapsedSeconds=$(($(date +%s) - microk8sStatusStartTime))

  echo ""
  echo -n "Creating namespace for thule..."
  echo ""
  kubectl apply -f "${SCRIPT_DIR_NAME}/../apply/thule-namespace.yml"
  echo -e "Creating namespace for thule...\033[32m done \033[0m"

  echo ""
  echo -n "Creating secrets for thule..."
  echo ""
  kubectl apply -f "${SCRIPT_DIR_NAME}/../apply/thule-application-secrets.yml"
  echo -e "Creating secrets for thule...\033[32m done \033[0m"

  echo ""
  echo "Have configured thule"
  echo "================================================================================"
}

function showMicrok8sStatus() {
  echo ""
  echo "================================================================================"
  echo "Microk8s status..."
  echo ""
  sudo microk8s.status --timeout 600 --wait-ready

  echo ""
  echo "Nodes, services, pods..."
  kubectl get nodes,services,pods --all-namespaces -o wide

  defaultToken=$(kubectl --namespace=kube-system get secret | grep default-token | cut -d " " -f1)
  signinBearerToken=$(kubectl --namespace=kube-system describe secret "$defaultToken" | grep token: | tr -s " " | cut -d " " -f2)
  echo ""
  echo "Dashboard URL : http://${K8S_HOST}/dashboard/"
  echo "Sign-in bearer token : ${signinBearerToken}"
  echo "================================================================================"
}
