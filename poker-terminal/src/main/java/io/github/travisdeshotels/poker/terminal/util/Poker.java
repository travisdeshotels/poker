package io.github.travisdeshotels.poker.terminal.util;

import io.github.travisdeshotels.poker.terminal.beans.EstimateWithPlayerName;
import io.github.travisdeshotels.poker.terminal.beans.HandResult;
import io.github.travisdeshotels.poker.terminal.beans.StartPokerResponse;
import io.github.travisdeshotels.poker.terminal.exception.PokerApiException;

import static io.github.travisdeshotels.poker.terminal.util.IoUtil.out;
import static io.github.travisdeshotels.poker.terminal.util.IoUtil.prompt;

public class Poker {
    private final RestUtil restUtil;
    private String gameId;
    private String hostId;

    public Poker(String apiUrl){
        this.restUtil = new RestUtil(apiUrl);
    }

    private void hostGame() throws PokerApiException {
        StartPokerResponse response = restUtil.createGame();
        this.hostId = response.getHostId();
        this.gameId = response.getGameId();
        out("Game started. Game id is " + this.gameId);
    }

    public void start(){
        try {
            hostGame();
            play();
        } catch (PokerApiException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void printResult(HandResult result){
        for (EstimateWithPlayerName estimate : result.getEstimateList()){
            out(estimate.getPlayerName() + " " + estimate.getPointValue());
        }
        out("Average is: " + result.getEstimateAverage());
    }

    private void play() throws InterruptedException {
        while(true){
            String estimate = prompt("Please (Q) to quit (R) to reset estimates or (C) to continue: ");
            if ("Q".equalsIgnoreCase(estimate)){
                return;
            } else if ("R".equalsIgnoreCase(estimate)){
                this.restUtil.resetHand(this.gameId, this.hostId);
            } else {
                while ("Waiting on players".equals(restUtil.getStatus(this.gameId, this.hostId))) {
                    Thread.sleep(2000);
                }
                printResult(restUtil.getResult(this.gameId, this.hostId));
                prompt("Press enter to continue:");
            }
        }
    }
}
