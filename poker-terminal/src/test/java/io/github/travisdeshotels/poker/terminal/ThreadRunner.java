package io.github.travisdeshotels.poker.terminal;

import io.github.travisdeshotels.poker.terminal.util.RestUtil;

import java.util.Random;

public class ThreadRunner extends Thread{
    String playerName;
    String pointValue;
    int numberOfRuns;
    boolean host;
    String playerId;
    String gameId;

    protected ThreadRunner(String playerName,
                           String pointValue,
                           int numberOfRuns,
                           boolean host,
                           String gameId){
        this.playerName = playerName;
        this.pointValue = pointValue;
        this.numberOfRuns = numberOfRuns;
        this.host = host;
        this.gameId = gameId;
    }

    protected ThreadRunner(String playerName,
                           String pointValue,
                           int numberOfRuns,
                           String gameId){
        this.playerName = playerName;
        this.pointValue = pointValue;
        this.numberOfRuns = numberOfRuns;
        this.host = false;
        this.gameId = gameId;
    }

    public void setPlayerId(String playerId){
        this.playerId = playerId;
    }

    public void run(){
        RestUtil util = new RestUtil("http://localhost:8080");
        Random random = new Random();
        if (!host){
            this.playerId = util.joinGame(this.gameId, this.playerName).getPlayerId();
        }
        for (int i=0; i<numberOfRuns; i++){
            //submit estimate
            util.submitResponse(this.gameId, this.playerId, this.pointValue);

            int num = (random.nextInt(70) + 15) * 100;

            //wait
            while (!"Result is ready".equals(util.getStatus(this.gameId, this.playerId))){
                try {
                    Thread.sleep(num);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            //view result
            util.getResult(this.gameId, this.playerId);
            try {
                Thread.sleep(num);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
