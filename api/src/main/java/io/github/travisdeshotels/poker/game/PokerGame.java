package io.github.travisdeshotels.poker.game;

import io.github.travisdeshotels.poker.beans.EstimateWithPlayerName;
import io.github.travisdeshotels.poker.beans.HandStatus;
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
    List<String> validPointValuesList;

    public PokerGame(){
        playerDataMap =  new HashMap<>();
        log.info("Game has started");
    }

    public PokerGame(List<String> validPointValuesList){
        playerDataMap =  new HashMap<>();
        this.validPointValuesList = validPointValuesList;
    }

    public String addPlayer(String playerName){
        String playerId = UUID.randomUUID().toString().split("-")[0];
        PlayerData data = new PlayerData(playerName, null, false);
        playerDataMap.put(playerId, data);
        log.info("Player {} joined id is {}", playerName, playerId);
        return playerId;
    }

    public int getNumberOfPlayers(){
        return playerDataMap.size();
    }

    public void submitEstimate(String playerId, String estimate){
        if (this.validPointValuesList.contains(estimate)) {
            playerDataMap.get(playerId).setPointValue(estimate);
        } else {
            log.warn("Player {} submitted invalid estimate of {}. Ignoring", playerId, estimate);
        }
    }

    public HandResult getResult(String playerId){
        playerDataMap.get(playerId).setViewedResult(true);
        int sumOfEstimates = 0;
        List<EstimateWithPlayerName> estimates = new ArrayList<>();
        for(Map.Entry<String, PlayerData> entry : playerDataMap.entrySet()){
            sumOfEstimates += Integer.parseInt(entry.getValue().getPointValue());
            estimates.add(new EstimateWithPlayerName(entry.getValue().getPlayerName(), entry.getValue().getPointValue()));
        }
        HandResult result = new HandResult(estimates, (float) sumOfEstimates / estimates.size());
        if (allPlayersHaveViewedResult()){
            resetHand();
        }
        return result;
    }

    public HandStatus getHandStatus(String playerId){
        if (playerDataMap.get(playerId).getPointValue() == null){
            return HandStatus.WAITING_ON_YOU;
        } else if(allPlayersHaveSubmittedAnEstimate()){
            return HandStatus.RESULT_READY;
        } else{
            return HandStatus.WAITING_ON_OTHERS;
        }
    }

    protected boolean allPlayersHaveSubmittedAnEstimate(){
        for (Map.Entry<String, PlayerData> entry : playerDataMap.entrySet()){
            if (entry.getValue().getPointValue() == null){
                return false;
            }
        }
        return true;
    }

    protected boolean allPlayersHaveViewedResult(){
        for (Map.Entry<String, PlayerData> entry : playerDataMap.entrySet()){
            if (!entry.getValue().isViewedResult()){
                return false;
            }
        }
        return true;
    }

    protected void resetHand(){
        for (Map.Entry<String, PlayerData> entry : playerDataMap.entrySet()){
            PlayerData player = entry.getValue();
            player.setPointValue(null);
            player.setViewedResult(false);
        }
    }
}
