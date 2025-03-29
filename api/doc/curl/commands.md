# Commands

## Host commands

---
### Start poker
```shell
start_poker_response=$(curl -X POST localhost:8080); \
game_id=$(echo "$start_poker_response" | jq -r .gameId); \
host_id=$(echo "$start_poker_response" | jq -r .hostId);
```

### View game status
```shell
curl localhost:8080/status/"$game_id"/"$host_id" | jq
```

### View result
```shell
curl localhost:8080/result/"$game_id"/"$host_id" | jq
```

### Reset hand
```shell
curl -H "Content-Type: application/json"  -d '{"hostPlayerId": "'"$host_id"'"}' -X POST localhost:8080/end/"$game_id"
```

## Player commands

---
### Join game
```shell
join_poker_response=$(curl -d '{"name": "Bill"}' -H "Content-Type: application/json" -X POST localhost:8080/join/$game_id); \
player1_id=$(echo "$join_poker_response" | jq -r .playerId); \
players_connected=$(echo "$join_poker_response" | jq -r .numberOfPlayersConnected)
```

### Submit estimate
```shell
curl -d '{"playerId": "'$player1_id'", "pointValue": 13}' -H "Content-Type: application/json" -X POST localhost:8080/$game_id
```
