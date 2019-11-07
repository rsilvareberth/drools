VAR_CICD_PROJECT='citinova-devops-tools'
VAR_DEV_PROJECT='citinova-logincidadao-dev'
main-dispatch-gateway

# oc policy add-role-to-group edit system:serviceaccounts:citinova-devops-tools -n citinova-logincidadao-dev
oc policy add-role-to-group edit system:serviceaccounts:$VAR_CICD_PROJECT -n $VAR_DEV_PROJECT

# oc adm policy add-role-to-user admin $(oc whoami) -n citinova-logincidadao-dev >/dev/null 2>&1
oc adm policy add-role-to-user admin $VAR_USERNAME -n $VAR_DEV_PROJECT >/dev/null 2>&1

# oc annotate --overwrite namespace citinova-logincidadao-dev demo=openshift-cd-dev >/dev/null 2>&1
oc annotate --overwrite namespace $VAR_DEV_PROJECT demo=openshift-cd-$PRJ_SUFFIX >/dev/null 2>&1

# oc adm pod-network join-projects --to=citinova-devops-tools citinova-logincidadao-dev >/dev/null 2>&1
oc adm pod-network join-projects --to=$VAR_CICD_PROJECT $VAR_DEV_PROJECT $VAR_HOM_PROJECT  $VAR_PRD_PROJECT>/dev/null 2>&1

# oc new-build --name=fuse --image-stream=fuse7-java-openshift:1.3 --binary=true -n citinova-logincidadao-dev
oc new-build --name=fuse --image-stream=fuse7-java-openshift:1.3 --binary=true -n $VAR_DEV_PROJECT

# oc new-app fuse:latest --allow-missing-images -n citinova-logincidadao-dev
oc new-app fuse:latest --allow-missing-images -n $VAR_DEV_PROJECT

# oc set triggers dc -l app=fuse --containers=fuse --from-image=fuse:latest --manual -n --manual -n citinova-logincidadao-dev
oc set triggers dc -l app=fuse --containers=fuse --from-image=fuse:latest --manual -n $VAR_DEV_PROJECT

# dev project
# oc expose dc/fuse --port=8080 -n citinova-logincidadao-dev
oc expose dc/fuse --port=8080 -n $VAR_DEV_PROJECT

# oc expose svc/fuse -n citinova-logincidadao-dev
oc expose svc/fuse -n $VAR_DEV_PROJECT

# oc set probe dc/fuse --readiness --get-url=http://:8080/ws/demo/healthcheck --initial-delay-seconds=30 --failure-threshold=10 --period-seconds=10 -n citinova-logincidadao-dev
oc set probe dc/fuse --readiness --get-url=http://:8080/ws/demo/healthcheck --initial-delay-seconds=30 --failure-threshold=10 --period-seconds=10 -n $VAR_DEV_PROJECT

# oc set probe dc/fuse --liveness  --get-url=http://:8080/ws/demo/healthcheck --initial-delay-seconds=180 --failure-threshold=10 --period-seconds=10 -n citinova-logincidadao-dev
oc set probe dc/fuse --liveness  --get-url=http://:8080/ws/demo/healthcheck --initial-delay-seconds=180 --failure-threshold=10 --period-seconds=10 -n $VAR_DEV_PROJECT

# oc rollout cancel dc/fuse -n citinova-logincidadao-dev
oc rollout cancel dc/fuse -n $VAR_DEV_PROJECT
