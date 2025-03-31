# Poker API

## Docker
```shell
docker build -t pokerimg .; \
docker run --name poker -p 8080:8080 -d pokerimg; \
docker container logs --follow poker
```
