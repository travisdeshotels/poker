package io.github.travisdeshotels.poker.terminal.util;

import io.github.travisdeshotels.poker.terminal.beans.Estimate;
import io.github.travisdeshotels.poker.terminal.beans.HandStatus;
import io.github.travisdeshotels.poker.terminal.exception.PokerApiException;

import static io.github.travisdeshotels.poker.terminal.util.IoUtil.out;
import static io.github.travisdeshotels.poker.terminal.util.IoUtil.prompt;

public class Poker {
    private final RestUtil restUtil;
    private String gameId;
    private String playerName;

    public Poker(String apiUrl){
        this.restUtil = new RestUtil(apiUrl);
    }

    private void hostGame(String playerName) throws PokerApiException {
        this.gameId = restUtil.createGame(playerName);
        out("Game started. Game id is " + gameId);
    }

    public void start(){
        this.playerName = prompt("Enter your name");
        String response = prompt("(J)oin a game\n(C)reate a game");
        if ("J".equalsIgnoreCase(response)){
            this.gameId = prompt("Enter game id");
            int players = this.restUtil.joinGame(gameId, playerName);
            out(players + " players have joined.");
        } else if ("C".equalsIgnoreCase(response)){
            try {
                hostGame(playerName);
            } catch (PokerApiException e) {
                throw new RuntimeException(e);
            }
        } else{
            out("Invalid selection!");
            return;
        }
        try {
            play();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void play() throws InterruptedException {
        boolean estimateHasBeenSubmitted = false;
        boolean outputHasBeenPrinted = false;
        String pointValue;
        while (true){
            if(restUtil.isEstimateNeeded(this.gameId)){
                if (estimateHasBeenSubmitted){
                    out("1st wait.");
                    Thread.sleep(2000);
                } else {
                    estimateHasBeenSubmitted = true;
                    pointValue = prompt("Please enter your estimate: ");
                    this.restUtil.submitResponse(this.playerName, pointValue, this.gameId);
                }
            } else {
                //outputHasBeenPrinted = true;
                out("Hand is over");
                HandStatus result = this.restUtil.getResult(this.gameId);
                out("Total estimates: " + result.getPlayersTotal());
                for (Estimate estimate : result.getEstimateList()) {
                    out(estimate.getPointValue());
                }
                out("Average estimate: " + result.getEstimateAverage());
                pointValue = prompt("Please enter your estimate: ");
                this.restUtil.submitResponse(this.playerName, pointValue, this.gameId);
                estimateHasBeenSubmitted = true;
            }
//            } else{
//                out("2nd wait.");
//                Thread.sleep(2000);
//            }
        }
    }
}
