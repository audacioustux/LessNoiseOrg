local_resource(
    "sbt", 
    serve_cmd='sbt -J-Xmx2G "~stage"', 
    deps=["build.sbt"]
)

local_resource(
  "akka-persistence-up",
  cmd="kubectl exec -it yb-tserver-0 -- ysqlsh -h yb-tserver-service < assets/ddl-scripts/akka-r2dbc_up.sql",
  deps=["assets/ddl-scripts/akka-r2dbc_up.sql"],
)

docker_build(
  "compaas", 
  context="target/universal/stage", 
  dockerfile="Dockerfile"
)
docker_prune_settings(True)

k8s_yaml(kustomize("k8s/compaas/overlays/dev"))

analytics_settings(enable=False)
