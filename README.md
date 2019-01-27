# Trade Matching
[![CircleCI](https://circleci.com/gh/alank976/TradeMatching.svg?style=svg)](https://circleci.com/gh/alank976/TradeMatching)

A toy project implementing trade matching logic by using reactive programming (reactor+Webflux) so that I can get my hands dirty about the following technology stack:
- reactor
- Spring webflux
- gradle (Kotlin DSL)
- docker
- graalVM

# How to run
- `docker -d run mongo`
- `docker -d -p 8888:8080 run alank976/trade-matching:latest`