# Commands

## Start poker
```shell
start_poker_response=$(curl -d '{"name": "Ted"}' -H "Content-Type: application/json" -X POST localhost:8080)
game_id=$(echo $start_poker_response | jq -r .gameId)
player1_id=$(echo $start_poker_response | jq -r .playerId)
```

## Join game
```shell
join_poker_response=$(curl -d '{"name": "Bill"}' -H "Content-Type: application/json" -X POST localhost:8080/join/$game_id)
player2_id=$(echo $join_poker_response | jq -r .playerId)
players_connected=$(echo $join_poker_response | jq -r .numberOfPlayersConnected)
```

## Submit estimate
```shell
curl -d '{"playerId": "'$player1_id'", "pointValue": 13}' -H "Content-Type: application/json" -X POST localhost:8080/$game_id
```

## View game status
```shell
curl localhost:8080/status/$game_id/$player1_id | jq
```

## View result
```shell
curl localhost:8080/result/$game_id/$player_id | jq
```
