#!/usr/bin/env zsh

curl -d '{"playerId": "'"$PLAYER_ID"'", "pointValue": "'"$1"'"}' -H "Content-Type: application/json" \
-X POST "$POKER_API_URL"/"$GAME_ID"
