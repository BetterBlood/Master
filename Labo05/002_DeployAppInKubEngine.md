# Task 2 - Deploy the application in Kubernetes Engine

In this task you will deploy the application in the public cloud service Google Kubernetes Engine (GKE).

## Subtask 2.1 - Create Project

Log in to the Google Cloud console at <http://console.cloud.google.com/>, navigate to the __Resource Manager__ (<https://console.cloud.google.com/cloud-resource-manager>) and create a new project. 

## Subtask 2.2 - Create a cluster

Go to the Google Kubernetes Engine (GKE) console (<https://console.cloud.google.com/kubernetes/>). If necessary, enable the Kubernetes Engine API. Then create a cluster. 

* Choose a __GKE Standard__ cluster. (Please ensure that you are not using the Autopilot. The button to switch to Standard could be a bit tricky to find...)
* Give it a __name__ of the form _gke-cluster-1_
* Select a __region__ close to you.
* Set the __number of nodes__ to 2. 
* Set the __instance type__ to micro instances.
* Set the __boot disk size__ to 10 GB.
* Keep the other settings at their default values.

## Subtask 2.3 - Deploy the application on the cluster

Once the cluster is created, the GKE console will show a __Connect__ button next to the cluster in the cluster list. Click on it. A dialog will appear with a command-line command. Copy/paste the command and execute it on your local machine. This will download the configuration info of the cluster to your local machine (this is known as a _context_). It also changes the current context of your `kubectl` tool to the new cluster.

To see the available contexts, type :

```sh
$ kubectl config get-contexts
```

You should see two contexts, one for the Minikube cluster and one for the GKE cluster. The current context has a star `*` in front of it. The `kubectl` commands that you type from now on will go to the cluster of the current context.

With that you can use `kubectl` to manage your GKE cluster just as you did in task 1. Repeat the application deployment steps of task 1 on your GKE cluster.

Should you want to switch contexts, use :

```sh
$ kubectl config use-context <context>
```

## Subtask 2.4 - Deploy the ToDo-Frontend Service

On the Minikube cluster we did not have the possibility to expose a service on an external port, that is why we did not create a Service for the Frontend. Now, with the GKE cluster, we are able to do that.

Using the `redis-svc.yaml` file as an example, create the `frontend-svc.yaml` configuration file for the Frontend Service.

Unlike the Redis and API Services the Frontend needs to be accessible from outside the Kubernetes cluster as a regular web server on port 80.

  * We need to change a configuration parameter. Our cluster runs on the GKE cloud and we want to use a GKE load balancer to expose our service.
  * Read the section "Publishing Services - Service types" of the K8s documentation 
    <https://kubernetes.io/docs/concepts/services-networking/service/#publishing-services-service-types>
  * Deploy the Service using `kubectl`.

This will trigger the creation of a load balancer on GKE. This might take some minutes. You can monitor the creation of the load balancer using `kubectl describe`.

### Verify the ToDo application

Now you can verify if the ToDo application is working correctly.

  * Find out the public URL of the Frontend Service load balancer using `kubectl describe`.
  * Access the public URL of the Service with a browser. You should be able to access the complete application and create a new ToDo.


## Deliverables

Document any difficulties you faced and how you overcame them. Copy the object descriptions into the lab report (if they are unchanged from the previous task just say so).

> // TODO

```````
// TODO object descriptions
kubectl describe all
Name:             api
Namespace:        default
Priority:         0
Service Account:  default
Node:             gke-gke-cluster-1-default-pool-622847df-z3p7/10.132.0.28
Start Time:       Thu, 16 May 2024 13:51:22 +0200
Labels:           app=todo
                  component=api
Annotations:      <none>
Status:           Running
IP:               10.116.4.6
IPs:
  IP:  10.116.4.6
Containers:
  api:
    Container ID:   containerd://a227e976e4d4bbcb53bcf10c2385fa1d709b7c0d060fef963e07304d6c866fdc   
    Image:          icclabcna/ccp2-k8s-todo-api
    Image ID:       docker.io/icclabcna/ccp2-k8s-todo-api@sha256:13cb50bc9e93fdf10b4608f04f2966e274470f00c0c9f60815ec8fc987cd6e03
    Port:           8081/TCP
    Host Port:      0/TCP
    State:          Running
      Started:      Thu, 16 May 2024 13:51:48 +0200
    Ready:          True
    Restart Count:  0
    Environment:
      REDIS_ENDPOINT:  redis-svc
      REDIS_PWD:       ccp2
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from kube-api-access-djbbr (ro)
Conditions:
  Type              Status
  Initialized       True
  Ready             True
  ContainersReady   True
  PodScheduled      True
Volumes:
  kube-api-access-djbbr:
    Type:                    Projected (a volume that contains injected data from multiple sources) 
    TokenExpirationSeconds:  3607
    ConfigMapName:           kube-root-ca.crt
    ConfigMapOptional:       <nil>
    DownwardAPI:             true
QoS Class:                   BestEffort
Node-Selectors:              <none>
Tolerations:                 node.kubernetes.io/not-ready:NoExecute op=Exists for 300s
                             node.kubernetes.io/unreachable:NoExecute op=Exists for 300s
Events:
  Type    Reason     Age    From               Message
  ----    ------     ----   ----               -------
  Normal  Scheduled  5m44s  default-scheduler  Successfully assigned default/api to gke-gke-cluster-1-default-pool-622847df-z3p7
  Normal  Pulling    5m43s  kubelet            Pulling image "icclabcna/ccp2-k8s-todo-api"
  Normal  Pulled     5m19s  kubelet            Successfully pulled image "icclabcna/ccp2-k8s-todo-api" in 24.255s (24.255s including waiting)
  Normal  Created    5m19s  kubelet            Created container api
  Normal  Started    5m18s  kubelet            Started container api


Name:             frontend
Namespace:        default
Priority:         0
Service Account:  default
Node:             gke-gke-cluster-1-default-pool-622847df-z3p7/10.132.0.28
Start Time:       Thu, 16 May 2024 13:52:25 +0200
Labels:           app=todo
                  component=frontend
Annotations:      <none>
Status:           Running
IP:               10.116.4.7
IPs:
  IP:  10.116.4.7
Containers:
  frontend:
    Container ID:   containerd://60e7b722dc2ff6ac8a3b2d50cac2ee358307cbdca6b06b6528ed6a81e9612bd2   
    Image:          icclabcna/ccp2-k8s-todo-frontend
    Image ID:       docker.io/icclabcna/ccp2-k8s-todo-frontend@sha256:5892b8f75a4dd3aa9d9cf527f8796a7638dba574ea8e6beef49360a3c67bbb44
    Port:           8080/TCP
    Host Port:      0/TCP
    State:          Running
      Started:      Thu, 16 May 2024 13:52:36 +0200
    Ready:          True
    Restart Count:  0
    Environment:
      API_ENDPOINT_URL:  http://api-svc:8081
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from kube-api-access-s4rxj (ro)
Conditions:
  Type              Status
  Initialized       True
  Ready             True
  ContainersReady   True
  PodScheduled      True
Volumes:
  kube-api-access-s4rxj:
    Type:                    Projected (a volume that contains injected data from multiple sources) 
    TokenExpirationSeconds:  3607
    ConfigMapName:           kube-root-ca.crt
    ConfigMapOptional:       <nil>
    DownwardAPI:             true
QoS Class:                   BestEffort
Node-Selectors:              <none>
Tolerations:                 node.kubernetes.io/not-ready:NoExecute op=Exists for 300s
                             node.kubernetes.io/unreachable:NoExecute op=Exists for 300s
Events:
  Type    Reason     Age    From               Message
  ----    ------     ----   ----               -------
  Normal  Scheduled  4m42s  default-scheduler  Successfully assigned default/frontend to gke-gke-cluster-1-default-pool-622847df-z3p7
  Normal  Pulling    4m41s  kubelet            Pulling image "icclabcna/ccp2-k8s-todo-frontend"     
  Normal  Pulled     4m32s  kubelet            Successfully pulled image "icclabcna/ccp2-k8s-todo-frontend" in 9.838s (9.839s including waiting)
  Normal  Created    4m31s  kubelet            Created container frontend
  Normal  Started    4m31s  kubelet            Started container frontend


Name:             redis
Namespace:        default
Priority:         0
Service Account:  default
Node:             gke-gke-cluster-1-default-pool-7941f1b2-29ng/10.132.0.25
Start Time:       Thu, 16 May 2024 13:50:25 +0200
Labels:           app=todo
                  component=redis
Annotations:      <none>
Status:           Running
IP:               10.116.1.6
IPs:
  IP:  10.116.1.6
Containers:
  redis:
    Container ID:  containerd://38a9b4269d282928194a94de4a920628dc677f97c6d8cb852d8b9cc2b453e2ee    
    Image:         redis
    Image ID:      docker.io/library/redis@sha256:5a93f6b2e391b78e8bd3f9e7e1e1e06aeb5295043b4703fb88392835cec924a0
    Port:          6379/TCP
    Host Port:     0/TCP
    Args:
      redis-server
      --requirepass ccp2
      --appendonly yes
    State:          Running
      Started:      Thu, 16 May 2024 13:50:31 +0200
    Ready:          True
    Restart Count:  0
    Environment:    <none>
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from kube-api-access-xx2nj (ro)
Conditions:
  Type              Status
  Initialized       True
  Ready             True
  ContainersReady   True
  PodScheduled      True
Volumes:
  kube-api-access-xx2nj:
    Type:                    Projected (a volume that contains injected data from multiple sources) 
    TokenExpirationSeconds:  3607
    ConfigMapName:           kube-root-ca.crt
    ConfigMapOptional:       <nil>
    DownwardAPI:             true
QoS Class:                   BestEffort
Node-Selectors:              <none>
Tolerations:                 node.kubernetes.io/not-ready:NoExecute op=Exists for 300s
                             node.kubernetes.io/unreachable:NoExecute op=Exists for 300s
Events:
  Type    Reason     Age    From               Message
  ----    ------     ----   ----               -------
  Normal  Scheduled  6m42s  default-scheduler  Successfully assigned default/redis to gke-gke-cluster-1-default-pool-7941f1b2-29ng
  Normal  Pulling    6m42s  kubelet            Pulling image "redis"
  Normal  Pulled     6m37s  kubelet            Successfully pulled image "redis" in 5.006s (5.007s including waiting)
  Normal  Created    6m36s  kubelet            Created container redis
  Normal  Started    6m36s  kubelet            Started container redis


Name:              api-svc
Namespace:         default
Labels:            component=api
Annotations:       cloud.google.com/neg: {"ingress":true}
Selector:          app=todo,component=api
Type:              ClusterIP
IP Family Policy:  SingleStack
IP Families:       IPv4
IP:                10.120.75.76
IPs:               10.120.75.76
Port:              api  8081/TCP
TargetPort:        8081/TCP
Endpoints:         10.116.4.6:8081
Session Affinity:  None
Events:            <none>


Name:                     frontend-svc
Namespace:                default
Labels:                   component=frontend
Annotations:              cloud.google.com/neg: {"ingress":true}
Selector:                 app=todo,component=frontend
Type:                     LoadBalancer
IP Family Policy:         SingleStack
IP Families:              IPv4
IP:                       10.120.76.217
IPs:                      10.120.76.217
LoadBalancer Ingress:     35.205.253.137
Port:                     frontend  80/TCP
TargetPort:               8080/TCP
NodePort:                 frontend  32040/TCP
Endpoints:                10.116.4.7:8080
Session Affinity:         None
External Traffic Policy:  Cluster
Events:
  Type    Reason                Age   From                Message
  ----    ------                ----  ----                -------
  Normal  EnsuringLoadBalancer  15m   service-controller  Ensuring load balancer
  Normal  EnsuredLoadBalancer   14m   service-controller  Ensured load balancer


Name:              kubernetes
Namespace:         default
Labels:            component=apiserver
                   provider=kubernetes
Annotations:       <none>
Selector:          <none>
Type:              ClusterIP
IP Family Policy:  SingleStack
IP Families:       IPv4
IP:                10.120.64.1
IPs:               10.120.64.1
Port:              https  443/TCP
TargetPort:        443/TCP
Endpoints:         10.132.0.23:443
Session Affinity:  None
Events:            <none>


Name:              redis-svc
Namespace:         default
Labels:            component=redis
Annotations:       cloud.google.com/neg: {"ingress":true}
Selector:          app=todo,component=redis
Type:              ClusterIP
IP Family Policy:  SingleStack
IP Families:       IPv4
IP:                10.120.69.42
IPs:               10.120.69.42
Port:              redis  6379/TCP
TargetPort:        6379/TCP
Endpoints:         10.116.1.6:6379
Session Affinity:  None
Events:            <none>

```````

```yaml
# frontend-svc.yaml
apiVersion: v1
kind: Service
metadata:
  labels:
    component: frontend
  name: frontend-svc
spec:
  ports:
    - port: 80
      targetPort: 8080
      name: frontend
  selector:
    app: todo
    component: frontend
  type: LoadBalancer

```

Take a screenshot of the cluster details from the GKE console. Copy the output of the `kubectl describe` command to describe your load balancer once completely initialized.

> // TODO
> ![GKE Cluster](img/cluster-details.png)
```````
// TODO object descriptions
kubectl describe svc frontend-svc
Name:                     frontend-svc
Namespace:                default
Labels:                   component=frontend
Annotations:              cloud.google.com/neg: {"ingress":true}
Selector:                 app=todo,component=frontend
Type:                     LoadBalancer
IP Family Policy:         SingleStack
IP Families:              IPv4
IP:                       10.120.76.217
IPs:                      10.120.76.217
LoadBalancer Ingress:     35.205.253.137
Port:                     frontend  80/TCP
TargetPort:               8080/TCP
NodePort:                 frontend  32040/TCP
Endpoints:                10.116.4.7:8080
Session Affinity:         None
External Traffic Policy:  Cluster
Events:
  Type    Reason                Age   From                Message
  ----    ------                ----  ----                -------
  Normal  EnsuringLoadBalancer  17m   service-controller  Ensuring load balancer
  Normal  EnsuredLoadBalancer   17m   service-controller  Ensured load balancer

```````