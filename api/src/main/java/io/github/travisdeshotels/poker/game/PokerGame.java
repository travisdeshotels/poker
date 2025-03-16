package io.github.travisdeshotels.poker.game;

import io.github.travisdeshotels.poker.beans.EstimateWithPlayerName;
import io.github.travisdeshotels.poker.beans.PlayerData;
import io.github.travisdeshotels.poker.dto.HandResult;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class PokerGame {
    Map<String, PlayerData> playerDataMap;
    String hostPlayerId;
    List<String> validPointValuesList;

    public PokerGame(){
        playerDataMap =  new HashMap<>();
        log.info("Game has started");
    }

    public PokerGame(List<String> validPointValuesList){
        playerDataMap =  new HashMap<>();
        this.validPointValuesList = validPointValuesList;
    }

    public String setHost(){
        this.hostPlayerId = UUID.randomUUID().toString().split("-")[0];
        return this.hostPlayerId;
    }

    public String addPlayer(String playerName){
        String playerId = UUID.randomUUID().toString().split("-")[0];
        PlayerData data = new PlayerData(playerName, null);
        playerDataMap.put(playerId, data);
        log.info("Player {} joined id is {}", playerName, playerId);
        return playerId;
    }

    public int getNumberOfPlayers(){
        return playerDataMap.size();
    }

    private boolean playerHasEstimatedTheHand(String playerId){
        return playerDataMap.get(playerId).getPointValue() != null;
    }

    public void submitEstimate(String playerId, String estimate){
        if (playerHasEstimatedTheHand(playerId)){
            log.info("Ignoring player {} estimate of {}", playerId, estimate);
        } else if (this.validPointValuesList.contains(estimate)) {
            playerDataMap.get(playerId).setPointValue(estimate);
        } else {
            log.warn("Player {} submitted invalid estimate of {}. Ignoring", playerId, estimate);
        }
    }

    public HandResult getResult(String hostId){
        if (!hostId.equals(this.hostPlayerId)){
            log.warn("Player {} attempted to view the results!", hostId);
            return null;
        }
        int sumOfEstimates = 0;
        List<EstimateWithPlayerName> estimates = new ArrayList<>();
        for(Map.Entry<String, PlayerData> entry : playerDataMap.entrySet()){
            sumOfEstimates += Integer.parseInt(entry.getValue().getPointValue());
            estimates.add(new EstimateWithPlayerName(entry.getValue().getPlayerName(), entry.getValue().getPointValue()));
        }

        return new HandResult(estimates, (float) sumOfEstimates / estimates.size());
    }

    public boolean getHandStatus(String hostId){
        if (hostId.equals(this.hostPlayerId)){
            return allPlayersHaveSubmittedAnEstimate();
        } else {
            return false;
        }
    }

    private boolean allPlayersHaveSubmittedAnEstimate(){
        for (Map.Entry<String, PlayerData> entry : playerDataMap.entrySet()){
            if (entry.getValue().getPointValue() == null){
                return false;
            }
        }
        return true;
    }

    public boolean resetHand(String hostId){
        if (!this.hostPlayerId.equals(hostId)){
            return false;
        }
        for (Map.Entry<String, PlayerData> entry : playerDataMap.entrySet()){
            PlayerData player = entry.getValue();
            player.setPointValue(null);
        }
        return true;
    }
}
