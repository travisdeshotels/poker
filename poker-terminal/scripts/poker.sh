#!/usr/bin/env zsh
USERNAME=tnd

export POKER_API_URL=$1
export GAME_ID=$2
join_poker_response=$(curl -d '{"name": "'$USERNAME'"}' -H "Content-Type: application/json" -X POST "$POKER_API_URL"/join/"$GAME_ID")
PLAYER_ID=$(echo "$join_poker_response" | jq -r .playerId)
export PLAYER_ID
zsh
