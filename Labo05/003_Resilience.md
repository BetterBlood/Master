# Task 3 - Add and exercise resilience

By now you should have understood the general principle of configuring, running and accessing applications in Kubernetes. However, the above application has no support for resilience. If a container (resp. Pod) dies, it stops working. Next, we add some resilience to the application.

## Subtask 3.1 - Add Deployments

In this task you will create Deployments that will spawn Replica Sets as health-management components.

Converting a Pod to be managed by a Deployment is quite simple.

  * Have a look at an example of a Deployment described here: <https://kubernetes.io/docs/concepts/workloads/controllers/deployment/>

  * Create Deployment versions of your application configurations (e.g. `redis-deploy.yaml` instead of `redis-pod.yaml`) and modify/extend them to contain the required Deployment parameters.

  * Again, be careful with the YAML indentation!

  * Make sure to have always 2 instances of the API and Frontend running. 

  * Use only 1 instance for the Redis-Server. Why?

    > // TODO
> //TODO check answer
> Because we are using a single Redis instance to store the data. If we have multiple instances, the data will be inconsistent.
  * Delete all application Pods (using `kubectl delete pod ...`) and replace them with deployment versions.

  * Verify that the application is still working and the Replica Sets are in place. (`kubectl get all`, `kubectl get pods`, `kubectl describe ...`)

## Subtask 3.2 - Verify the functionality of the Replica Sets

In this subtask you will intentionally kill (delete) Pods and verify that the application keeps working and the Replica Set is doing its task.

Hint: You can monitor the status of a resource by adding the `--watch` option to the `get` command. To watch a single resource:

```sh
$ kubectl get <resource-name> --watch
```

To watch all resources of a certain type, for example all Pods:

```sh
$ kubectl get pods --watch
```

You may also use `kubectl get all` repeatedly to see a list of all resources.  You should also verify if the application stays available by continuously reloading your browser window.

  * What happens if you delete a Frontend or API Pod? How long does it take for the system to react?
    > // TODO
    > The system will automatically create a new pod to replace the deleted one. The new one is directly running.
    
  * What happens when you delete the Redis Pod?

    > // TODO
    > Same as above, the system will automatically create a new pod to replace the deleted one.
    > However, the data will be lost because the new pod is empty.
  * 
  * How can you change the number of instances temporarily to 3? Hint: look for scaling in the deployment documentation

    > // TODO
    > kubectl scale deployment/<deployment-name> --replicas=3
    > ex: kubectl scale deployment/api-deployment --replicas=3
    
  * What autoscaling features are available? Which metrics are used?

    > // TODO
    > Horizontal Pod Autoscaler automatically scales the number of pods in a replication controller, deployment, 
    > replica set or stateful set based on observed CPU utilization.
    
  * How can you update a component? (see "Updating a Deployment" in the deployment documentation)

    > // TODO
> kubectl set image deployment/<deployment-name> <container-name>=<new-image-name:tag>

## Subtask 3.3 - Put autoscaling in place and load-test it

On the GKE cluster deploy autoscaling on the Frontend with a target CPU utilization of 30% and number of replicas between 1 and 4. 

Load-test using Vegeta (500 requests should be enough).

> [!NOTE]
>
> - The autoscale may take a while to trigger.
>
> - If your autoscaling fails to get the cpu utilization metrics, run the following command
>
>   - ```sh
>     $ kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
>     ```
>
>   - Then add the *resources* part in the *container part* in your `frontend-deploy` :
>
>   - ```yaml
>     spec:
>       containers:
>         - ...:
>           env:
>             - ...:
>           resources:
>             requests:
>               cpu: 10m
>     ```
>

## Deliverables

Document your observations in the lab report. Document any difficulties you faced and how you overcame them. Copy the object descriptions into the lab report.

> // TODO
>  kubectl autoscale deployment frontend-deployment --cpu-percent=30 --min=1 --max=4
> echo "GET http://104.155.124.32" | vegeta attack -duration=30s -rate=50 | tee results.bin | vegeta report
> cat results.bin | vegeta plot > plot.html

```````sh
// TODO object descriptions
kubectl describe all                                                                                     t
Name:             api-deployment-7bff879bc7-fcp4r
Namespace:        default
Priority:         0
Service Account:  default
Node:             gke-gke-cluster-1-default-pool-622847df-z3p7/10.132.0.28
Start Time:       Thu, 16 May 2024 14:28:09 +0200
Labels:           app=todo
                  component=api
                  pod-template-hash=7bff879bc7
Annotations:      <none>
Status:           Running
IP:               10.116.4.10
IPs:
  IP:           10.116.4.10
Controlled By:  ReplicaSet/api-deployment-7bff879bc7
Containers:
  api:
    Container ID:   containerd://e13c3cc7353edae2bbc310067176696dfe070c0a7e0aeba7034fdb39f32302d8
    Image:          icclabcna/ccp2-k8s-todo-api
    Image ID:       docker.io/icclabcna/ccp2-k8s-todo-api@sha256:13cb50bc9e93fdf10b4608f04f2966e274470f00c0c9f60815ec8fc987cd6e03
    Port:           8081/TCP
    Host Port:      0/TCP
    State:          Running
      Started:      Thu, 16 May 2024 14:28:10 +0200
    Ready:          True
    Restart Count:  0
    Environment:
      REDIS_ENDPOINT:  redis-svc
      REDIS_PWD:       ccp2
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from kube-api-access-lgvfh (ro)
Conditions:
  Type              Status
  Initialized       True
  Ready             True
  ContainersReady   True
  PodScheduled      True
Volumes:
  kube-api-access-lgvfh:
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
  Type    Reason     Age   From               Message
  ----    ------     ----  ----               -------
  Normal  Scheduled  57m   default-scheduler  Successfully assigned default/api-deployment-7bff879bc7-fcp4r to gke-gke-cluster-1-default-pool-622847df-z3p7
  Normal  Pulling    57m   kubelet            Pulling image "icclabcna/ccp2-k8s-todo-api"
  Normal  Pulled     57m   kubelet            Successfully pulled image "icclabcna/ccp2-k8s-todo-api" in 170ms (171ms including waiting)
  Normal  Created    57m   kubelet            Created container api
  Normal  Started    57m   kubelet            Started container api


Name:             api-deployment-7bff879bc7-rpmdx
Namespace:        default
Priority:         0
Service Account:  default
Node:             gke-gke-cluster-1-default-pool-7941f1b2-29ng/10.132.0.25
Start Time:       Thu, 16 May 2024 14:28:09 +0200
Labels:           app=todo
                  component=api
                  pod-template-hash=7bff879bc7
Annotations:      <none>
Status:           Running
IP:               10.116.1.8
IPs:
  IP:           10.116.1.8
Controlled By:  ReplicaSet/api-deployment-7bff879bc7
Containers:
  api:
    Container ID:   containerd://04e006883dad814609d1021d0f238b6db684ff26959aa3af32017f1be43c6d63
    Image:          icclabcna/ccp2-k8s-todo-api
    Image ID:       docker.io/icclabcna/ccp2-k8s-todo-api@sha256:13cb50bc9e93fdf10b4608f04f2966e274470f00c0c9f60815ec8fc987cd6e03
    Port:           8081/TCP
    Host Port:      0/TCP
    State:          Running
      Started:      Thu, 16 May 2024 14:28:21 +0200
    Ready:          True
    Restart Count:  0
    Environment:
      REDIS_ENDPOINT:  redis-svc
      REDIS_PWD:       ccp2
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from kube-api-access-sxzkg (ro)
Conditions:
  Type              Status
  Initialized       True
  Ready             True
  ContainersReady   True
  PodScheduled      True
Volumes:
  kube-api-access-sxzkg:
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
  Type    Reason     Age   From               Message
  ----    ------     ----  ----               -------
  Normal  Scheduled  57m   default-scheduler  Successfully assigned default/api-deployment-7bff879bc7-rpmdx to gke-gke-cluster-1-default-pool-7941f1b2-29ng
  Normal  Pulling    57m   kubelet            Pulling image "icclabcna/ccp2-k8s-todo-api"
  Normal  Pulled     57m   kubelet            Successfully pulled image "icclabcna/ccp2-k8s-todo-api" in 10.96s (10.961s including waiting)
  Normal  Created    57m   kubelet            Created container api
  Normal  Started    57m   kubelet            Started container api


Name:             frontend-deployment-6796dd84d8-dfps8
Namespace:        default
Priority:         0
Service Account:  default
Node:             gke-gke-cluster-1-default-pool-622847df-z3p7/10.132.0.28
Start Time:       Thu, 16 May 2024 14:27:51 +0200
Labels:           app=todo
                  component=frontend
                  pod-template-hash=6796dd84d8
Annotations:      <none>
Status:           Running
IP:               10.116.4.8
IPs:
  IP:           10.116.4.8
Controlled By:  ReplicaSet/frontend-deployment-6796dd84d8
Containers:
  frontend:
    Container ID:   containerd://db03d417fd5a0225b45747a8e65b6eb0975530cc656b9fc5d77d2078d08e64c4
    Image:          icclabcna/ccp2-k8s-todo-frontend
    Image ID:       docker.io/icclabcna/ccp2-k8s-todo-frontend@sha256:5892b8f75a4dd3aa9d9cf527f8796a7638dba574ea8e6beef49360a3c67bbb44
    Port:           8080/TCP
    Host Port:      0/TCP
    State:          Running
      Started:      Thu, 16 May 2024 14:27:52 +0200
    Ready:          True
    Restart Count:  0
    Requests:
      cpu:  10m
    Environment:
      API_ENDPOINT_URL:  http://api-svc:8081
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from kube-api-access-2rv9j (ro)
Conditions:
  Type              Status
  Initialized       True
  Ready             True
  ContainersReady   True
  PodScheduled      True
Volumes:
  kube-api-access-2rv9j:
    Type:                    Projected (a volume that contains injected data from multiple sources)
    TokenExpirationSeconds:  3607
    ConfigMapName:           kube-root-ca.crt
    ConfigMapOptional:       <nil>
    DownwardAPI:             true
QoS Class:                   Burstable
Node-Selectors:              <none>
Tolerations:                 node.kubernetes.io/not-ready:NoExecute op=Exists for 300s
                             node.kubernetes.io/unreachable:NoExecute op=Exists for 300s
Events:
  Type    Reason     Age   From               Message
  ----    ------     ----  ----               -------
  Normal  Scheduled  58m   default-scheduler  Successfully assigned default/frontend-deployment-6796dd84d8-dfps8 to gke-gke-cluster-1-default-pool-622847df-z3p7
  Normal  Pulling    58m   kubelet            Pulling image "icclabcna/ccp2-k8s-todo-frontend"
  Normal  Pulled     58m   kubelet            Successfully pulled image "icclabcna/ccp2-k8s-todo-frontend" in 200ms (200ms including waiting)
  Normal  Created    58m   kubelet            Created container frontend
  Normal  Started    58m   kubelet            Started container frontend


Name:             redis-deployment-6fb96d5dff-jrwsm
Namespace:        default
Priority:         0
Service Account:  default
Node:             gke-gke-cluster-1-default-pool-622847df-z3p7/10.132.0.28
Start Time:       Thu, 16 May 2024 14:28:01 +0200
Labels:           app=todo
                  component=redis
                  pod-template-hash=6fb96d5dff
Annotations:      <none>
Status:           Running
IP:               10.116.4.9
IPs:
  IP:           10.116.4.9
Controlled By:  ReplicaSet/redis-deployment-6fb96d5dff
Containers:
  redis:
    Container ID:  containerd://d818653843e003bea90f631fbbbde33efbac67860d0852576ae4acf9bc6dde3d
    Image:         redis
    Image ID:      docker.io/library/redis@sha256:5a93f6b2e391b78e8bd3f9e7e1e1e06aeb5295043b4703fb88392835cec924a0
    Port:          6379/TCP
    Host Port:     0/TCP
    Args:
      redis-server
      --requirepass ccp2
      --appendonly yes
    State:          Running
      Started:      Thu, 16 May 2024 14:28:07 +0200
    Ready:          True
    Restart Count:  0
    Environment:    <none>
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from kube-api-access-pxhh2 (ro)
Conditions:
  Type              Status
  Initialized       True
  Ready             True
  ContainersReady   True
  PodScheduled      True
Volumes:
  kube-api-access-pxhh2:
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
  Type    Reason     Age   From               Message
  ----    ------     ----  ----               -------
  Normal  Scheduled  57m   default-scheduler  Successfully assigned default/redis-deployment-6fb96d5dff-jrwsm to gke-gke-cluster-1-default-pool-622847df-z3p7
  Normal  Pulling    57m   kubelet            Pulling image "redis"
  Normal  Pulled     57m   kubelet            Successfully pulled image "redis" in 5.637s (5.637s including waiting)
  Normal  Created    57m   kubelet            Created container redis
  Normal  Started    57m   kubelet            Started container redis


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
Endpoints:         10.116.1.8:8081,10.116.4.10:8081
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
Endpoints:                10.116.4.8:8080
Session Affinity:         None
External Traffic Policy:  Cluster
Events:
  Type    Reason                Age   From                Message
  ----    ------                ----  ----                -------
  Normal  EnsuringLoadBalancer  104m  service-controller  Ensuring load balancer
  Normal  EnsuredLoadBalancer   103m  service-controller  Ensured load balancer


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
Endpoints:         10.116.4.9:6379
Session Affinity:  None
Events:            <none>


Name:                   api-deployment
Namespace:              default
CreationTimestamp:      Thu, 16 May 2024 14:28:08 +0200
Labels:                 app=todo
                        component=api
Annotations:            deployment.kubernetes.io/revision: 1
Selector:               app=todo,component=api
Replicas:               2 desired | 2 updated | 2 total | 2 available | 0 unavailable
StrategyType:           RollingUpdate
MinReadySeconds:        0
RollingUpdateStrategy:  25% max unavailable, 25% max surge
Pod Template:
  Labels:  app=todo
           component=api
  Containers:
   api:
    Image:      icclabcna/ccp2-k8s-todo-api
    Port:       8081/TCP
    Host Port:  0/TCP
    Environment:
      REDIS_ENDPOINT:  redis-svc
      REDIS_PWD:       ccp2
    Mounts:            <none>
  Volumes:             <none>
Conditions:
  Type           Status  Reason
  ----           ------  ------
  Available      True    MinimumReplicasAvailable
  Progressing    True    NewReplicaSetAvailable
OldReplicaSets:  <none>
NewReplicaSet:   api-deployment-7bff879bc7 (2/2 replicas created)
Events:
  Type    Reason             Age   From                   Message
  ----    ------             ----  ----                   -------
  Normal  ScalingReplicaSet  57m   deployment-controller  Scaled up replica set api-deployment-7bff879bc7 to 2


Name:                   frontend-deployment
Namespace:              default
CreationTimestamp:      Thu, 16 May 2024 14:27:51 +0200
Labels:                 app=todo
                        component=frontend
Annotations:            deployment.kubernetes.io/revision: 1
Selector:               app=todo,component=frontend
Replicas:               1 desired | 1 updated | 1 total | 1 available | 0 unavailable
StrategyType:           RollingUpdate
MinReadySeconds:        0
RollingUpdateStrategy:  25% max unavailable, 25% max surge
Pod Template:
  Labels:  app=todo
           component=frontend
  Containers:
   frontend:
    Image:      icclabcna/ccp2-k8s-todo-frontend
    Port:       8080/TCP
    Host Port:  0/TCP
    Requests:
      cpu:  10m
    Environment:
      API_ENDPOINT_URL:  http://api-svc:8081
    Mounts:              <none>
  Volumes:               <none>
Conditions:
  Type           Status  Reason
  ----           ------  ------
  Available      True    MinimumReplicasAvailable
  Progressing    True    NewReplicaSetAvailable
OldReplicaSets:  <none>
NewReplicaSet:   frontend-deployment-6796dd84d8 (1/1 replicas created)
Events:
  Type    Reason             Age    From                   Message
  ----    ------             ----   ----                   -------
  Normal  ScalingReplicaSet  58m    deployment-controller  Scaled up replica set frontend-deployment-6796dd84d8 to 2
  Normal  ScalingReplicaSet  3m16s  deployment-controller  Scaled down replica set frontend-deployment-6796dd84d8 to 1 from 2


Name:                   redis-deployment
Namespace:              default
CreationTimestamp:      Thu, 16 May 2024 14:28:01 +0200
Labels:                 app=todo
                        component=redis
Annotations:            deployment.kubernetes.io/revision: 1
Selector:               app=todo,component=redis
Replicas:               1 desired | 1 updated | 1 total | 1 available | 0 unavailable
StrategyType:           RollingUpdate
MinReadySeconds:        0
RollingUpdateStrategy:  25% max unavailable, 25% max surge
Pod Template:
  Labels:  app=todo
           component=redis
  Containers:
   redis:
    Image:      redis
    Port:       6379/TCP
    Host Port:  0/TCP
    Args:
      redis-server
      --requirepass ccp2
      --appendonly yes
    Environment:  <none>
    Mounts:       <none>
  Volumes:        <none>
Conditions:
  Type           Status  Reason
  ----           ------  ------
  Available      True    MinimumReplicasAvailable
  Progressing    True    NewReplicaSetAvailable
OldReplicaSets:  <none>
NewReplicaSet:   redis-deployment-6fb96d5dff (1/1 replicas created)
Events:
  Type    Reason             Age   From                   Message
  ----    ------             ----  ----                   -------
  Normal  ScalingReplicaSet  57m   deployment-controller  Scaled up replica set redis-deployment-6fb96d5dff to 1


Name:           api-deployment-7bff879bc7
Namespace:      default
Selector:       app=todo,component=api,pod-template-hash=7bff879bc7
Labels:         app=todo
                component=api
                pod-template-hash=7bff879bc7
Annotations:    deployment.kubernetes.io/desired-replicas: 2
                deployment.kubernetes.io/max-replicas: 3
                deployment.kubernetes.io/revision: 1
Controlled By:  Deployment/api-deployment
Replicas:       2 current / 2 desired
Pods Status:    2 Running / 0 Waiting / 0 Succeeded / 0 Failed
Pod Template:
  Labels:  app=todo
           component=api
           pod-template-hash=7bff879bc7
  Containers:
   api:
    Image:      icclabcna/ccp2-k8s-todo-api
    Port:       8081/TCP
    Host Port:  0/TCP
    Environment:
      REDIS_ENDPOINT:  redis-svc
      REDIS_PWD:       ccp2
    Mounts:            <none>
  Volumes:             <none>
Events:
  Type    Reason            Age   From                   Message
  ----    ------            ----  ----                   -------
  Normal  SuccessfulCreate  57m   replicaset-controller  Created pod: api-deployment-7bff879bc7-rpmdx
  Normal  SuccessfulCreate  57m   replicaset-controller  Created pod: api-deployment-7bff879bc7-fcp4r


Name:           frontend-deployment-6796dd84d8
Namespace:      default
Selector:       app=todo,component=frontend,pod-template-hash=6796dd84d8
Labels:         app=todo
                component=frontend
                pod-template-hash=6796dd84d8
Annotations:    deployment.kubernetes.io/desired-replicas: 1
                deployment.kubernetes.io/max-replicas: 2
                deployment.kubernetes.io/revision: 1
Controlled By:  Deployment/frontend-deployment
Replicas:       1 current / 1 desired
Pods Status:    1 Running / 0 Waiting / 0 Succeeded / 0 Failed
Pod Template:
  Labels:  app=todo
           component=frontend
           pod-template-hash=6796dd84d8
  Containers:
   frontend:
    Image:      icclabcna/ccp2-k8s-todo-frontend
    Port:       8080/TCP
    Host Port:  0/TCP
    Requests:
      cpu:  10m
    Environment:
      API_ENDPOINT_URL:  http://api-svc:8081
    Mounts:              <none>
  Volumes:               <none>
Events:
  Type    Reason            Age    From                   Message
  ----    ------            ----   ----                   -------
  Normal  SuccessfulCreate  58m    replicaset-controller  Created pod: frontend-deployment-6796dd84d8-dfps8
  Normal  SuccessfulCreate  58m    replicaset-controller  Created pod: frontend-deployment-6796dd84d8-2phtd
  Normal  SuccessfulDelete  3m15s  replicaset-controller  Deleted pod: frontend-deployment-6796dd84d8-2phtd


Name:           redis-deployment-6fb96d5dff
Namespace:      default
Selector:       app=todo,component=redis,pod-template-hash=6fb96d5dff
Labels:         app=todo
                component=redis
                pod-template-hash=6fb96d5dff
Annotations:    deployment.kubernetes.io/desired-replicas: 1
                deployment.kubernetes.io/max-replicas: 2
                deployment.kubernetes.io/revision: 1
Controlled By:  Deployment/redis-deployment
Replicas:       1 current / 1 desired
Pods Status:    1 Running / 0 Waiting / 0 Succeeded / 0 Failed
Pod Template:
  Labels:  app=todo
           component=redis
           pod-template-hash=6fb96d5dff
  Containers:
   redis:
    Image:      redis
    Port:       6379/TCP
    Host Port:  0/TCP
    Args:
      redis-server
      --requirepass ccp2
      --appendonly yes
    Environment:  <none>
    Mounts:       <none>
  Volumes:        <none>
Events:
  Type    Reason            Age   From                   Message
  ----    ------            ----  ----                   -------
  Normal  SuccessfulCreate  57m   replicaset-controller  Created pod: redis-deployment-6fb96d5dff-jrwsm


Name:                                                  frontend-deployment
Namespace:                                             default
Labels:                                                <none>
Annotations:                                           <none>
CreationTimestamp:                                     Thu, 16 May 2024 15:17:28 +0200
Reference:                                             Deployment/frontend-deployment
Metrics:                                               ( current / target )
  resource cpu on pods  (as a percentage of request):  0% (0) / 30%
Min replicas:                                          1
Max replicas:                                          4
Deployment pods:                                       1 current / 1 desired
Conditions:
  Type            Status  Reason            Message
  ----            ------  ------            -------
  AbleToScale     True    ReadyForNewScale  recommended size matches current size
  ScalingActive   True    ValidMetricFound  the HPA was able to successfully calculate a replica count from cpu resource utilization (percentage of request)
  ScalingLimited  True    TooFewReplicas    the desired replica count is less than the minimum replica count
Events:
  Type     Reason                   Age    From                       Message
  ----     ------                   ----   ----                       -------
  Warning  FailedGetResourceMetric  8m16s  horizontal-pod-autoscaler  No recommendation
  Normal   SuccessfulRescale        3m16s  horizontal-pod-autoscaler  New size: 1; reason: cpu resource utilization (percentage of request) below target

```````

```yaml
# redis-deploy.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: redis-deployment
  labels:
    app: todo
    component: redis
spec:
  replicas: 1
  selector:
    matchLabels:
      app: todo
      component: redis
  template:
    metadata:
      labels:
        app: todo
        component: redis
    spec:
      containers:
        - name: redis
          image: redis
          ports:
            - containerPort: 6379
              name: redis
          args:
            - redis-server
            - --requirepass ccp2
            - --appendonly yes
```

```yaml
# api-deploy.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: api-deployment
  labels:
    app: todo
    component: api
spec:
  replicas: 2
  selector:
    matchLabels:
      app: todo
      component: api
  template:
    metadata:
      labels:
        app: todo
        component: api
    spec:
      containers:
        - name: api
          image: icclabcna/ccp2-k8s-todo-api
          ports:
            - containerPort: 8081
              name: http
          env:
            - name: REDIS_ENDPOINT
              value: redis-svc
            - name: REDIS_PWD
              value: ccp2
```

```yaml
# frontend-deploy.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: frontend-deployment
  labels:
    app: todo
    component: frontend
spec:
  replicas: 2
  selector:
    matchLabels:
      app: todo
      component: frontend
  template:
    metadata:
      labels:
        app: todo
        component: frontend
    spec:
      containers:
        - name: frontend
          image: icclabcna/ccp2-k8s-todo-frontend
          ports:
            - containerPort: 8080
              name: http
          env:
            - name: API_ENDPOINT_URL
              value: http://api-svc:8081
          resources:
            requests:
              cpu: 10m
```