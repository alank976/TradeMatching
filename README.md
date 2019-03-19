# Trade Matching
[![CircleCI](https://circleci.com/gh/alank976/TradeMatching.svg?style=svg)](https://circleci.com/gh/alank976/TradeMatching)

A toy project implementing trade matching logic by using reactive programming (reactor+Webflux) so that I can get my hands dirty about the following technology stack:
- reactor
- Spring webflux
- gradle (Kotlin DSL)
- docker
- alpine (the smallest JRE linux container)

## To deploy
CircleCI `deploy` build comprises of build, tag, and push image, then helm install/upgrade


## For local docker run

### `kubectl` is recommended
`kubectl run <deployment_name> --image=<gcr>/<project>/trade-matching:latest`

### basic docker build and run
```bash
docker build --tag=trade-matching .
docker run -d -p 27017:27017 mongoó
docker run -d -p 8111:8080 trade-matching:latest
```

## Others

### TLS certificate for Ingress
- `kubectl create secret tls <name> --cert=<cert path> --key=<key path>`


### Reserve a global IP in GCP
- `gcloud compute addresses create <name> --global`

### Deploy mongo replica set
- [tutorial for helm deployment](https://github.com/helm/charts/tree/master/stable/mongodb-replicaset)