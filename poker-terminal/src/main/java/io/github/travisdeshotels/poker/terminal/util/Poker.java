package io.github.travisdeshotels.poker.terminal.util;

import io.github.travisdeshotels.poker.terminal.beans.EstimateWithPlayerName;
import io.github.travisdeshotels.poker.terminal.beans.HandResult;
import io.github.travisdeshotels.poker.terminal.beans.JoinResponse;
import io.github.travisdeshotels.poker.terminal.beans.StartPokerResponse;
import io.github.travisdeshotels.poker.terminal.exception.PokerApiException;

import static io.github.travisdeshotels.poker.terminal.util.IoUtil.out;
import static io.github.travisdeshotels.poker.terminal.util.IoUtil.prompt;

public class Poker {
    private final RestUtil restUtil;
    private String gameId;
    private String playerId;

    public Poker(String apiUrl){
        this.restUtil = new RestUtil(apiUrl);
    }

    private void hostGame(String playerName) throws PokerApiException {
        StartPokerResponse response = restUtil.createGame(playerName);
        this.playerId = response.getPlayerId();
        this.gameId = response.getGameId();
        out("Game started. Game id is " + this.gameId);
    }

    public void start(){
        String playerName = prompt("Enter your name");
        String response = prompt("(J)oin a game\n(C)reate a game");
        if ("J".equalsIgnoreCase(response)){
            this.gameId = prompt("Enter game id");
            JoinResponse joinResponse = this.restUtil.joinGame(gameId, playerName);
            this.playerId = joinResponse.getPlayerId();
            out(joinResponse.getNumberOfPlayersConnected() + " players have joined.");
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

    private void printResult(HandResult result){
        for (EstimateWithPlayerName estimate : result.getEstimateList()){
            out(estimate.getPlayerName() + " " + estimate.getPointValue());
        }
        out("Average is: " + result.getEstimateAverage());
    }

    private void play() throws InterruptedException {
        while(true){
            String estimate = prompt("Please enter your estimate or Q to quit: ");
            if ("Q".equalsIgnoreCase(estimate)){
                return;
            } else {
                restUtil.submitResponse(this.gameId, this.playerId, estimate);
            }
            while (!"Result is ready".equals(restUtil.getStatus(this.gameId, this.playerId))){
                Thread.sleep(2000);
            }
            printResult(restUtil.getResult(this.gameId, this.playerId));
            prompt("Press enter to continue:");
        }
    }
}
