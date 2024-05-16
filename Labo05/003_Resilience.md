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
// TODO autoscaling description
kubectl describe hpa
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
  Type     Reason                   Age   From                       Message
  ----     ------                   ----  ----                       -------
  Warning  FailedGetResourceMetric  21m   horizontal-pod-autoscaler  No recommendation
  Normal   SuccessfulRescale        16m   horizontal-pod-autoscaler  New size: 1; reason: cpu resource utilization (percentage of request) below target

```````

```yaml
# redis-deploy.yaml
```

```yaml
# api-deploy.yaml
```

```yaml
# frontend-deploy.yaml
```
