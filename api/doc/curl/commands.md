# Commands

## Start poker
```shell
game_id=$(curl -d '{"name": "Ted"}' -H "Content-Type: application/json" -X POST localhost:8080)
```

## Join game
```shell
player_count=$(curl -d '{"name": "Bill"}' -H "Content-Type: application/json" -X POST localhost:8080/join/$game_id)
```

## Submit estimate
```shell
curl -d '{"player": "Ted", "pointValue": 13}' -H "Content-Type: application/json" -X POST localhost:8080/$game_id
```

## View game status
```shell
curl localhost:8080/$game_id | jq
```
