#!/bin/bash

function createService() {
  # Input parameters
  kubernetesConfigurationFile=$1

  echo ""
  echo "Creating service..."

  kubectl create -f "${kubernetesConfigurationFile}"
}

function deleteService() {
  # Input parameters
  kubernetesConfigurationFile=$1

  echo ""
  echo "Deleting service..."

  kubectl delete --all -f "${kubernetesConfigurationFile}"
}

function installMicrok8s() {
  if microk8s.status 2>/dev/null 1>/dev/null -ne 0; then
    echo ""
    echo "================================================================================"
    echo "Skipping installation of microk8s because it is already installed"
    echo "================================================================================"
  else
    echo ""
    echo "================================================================================"
    echo "About to install microk8s..."
    echo ""
    sudo snap install microk8s --classic
    microk8s.start # Install automatically starts but does not wait for it to come up, unlike the explicit start command

    echo ""
    echo "Enabling add-ons dns, storage and dashboard..."
    microk8s.enable dns storage dashboard

    echo ""
    echo "Adding nexus docker registry..."
    sudo sed -i "/pooh:8082/d" /var/snap/microk8s/current/args/containerd-template.toml
    sudo sed -i "/\[plugins.cri.registry.mirrors\]/a \
    \        [plugins.cri.registry.mirrors.\"pooh:8082\"] \n\
    \          endpoint = [\"http://pooh:8082\"]" /var/snap/microk8s/current/args/containerd-template.toml
    # Restart microk8s to effect registry changes
    microk8s.stop
    microk8s.start

    echo ""
    echo "Updating iptables (see 'My pods cant reach the internet or each other (but my MicroK8s host machine can)' in https://microk8s.io/docs/)..."
    sudo iptables -P FORWARD ACCEPT

    echo "Creating kubectl alias..."
    sudo snap alias microk8s.kubectl kubectl

    echo ""
    echo "Have installed microk8s"
    echo "================================================================================"

    showMicrok8sStatus
  fi
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
  echo "Dashboard URL : https://${dashboardIpAddress}"
  echo "Sign-in bearer token : ${signinBearerToken}"

  echo "================================================================================"
}
