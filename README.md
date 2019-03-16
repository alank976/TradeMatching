# Trade Matching
[![CircleCI](https://circleci.com/gh/alank976/TradeMatching.svg?style=svg)](https://circleci.com/gh/alank976/TradeMatching)

A toy project implementing trade matching logic by using reactive programming (reactor+Webflux) so that I can get my hands dirty about the following technology stack:
- reactor
- Spring webflux
- gradle (Kotlin DSL)
- docker
- alpine (the smallest JRE linux container)

# Build docker image
`docker build --tag=trade-matching .`

# Push docker image


# How to run
- `docker run -d -p 27017:27017 mongo`
- `docker run -d -p 8111:8080 trade-matching:latest`

# TLS certificate for Ingress
- `kubectl create secret tls <name> --cert=<cert path> --key=<key path>`

# Reserve a global IP in GCP
- `gcloud compute addresses create <name> --global`