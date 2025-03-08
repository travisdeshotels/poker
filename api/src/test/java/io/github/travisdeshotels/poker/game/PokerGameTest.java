package io.github.travisdeshotels.poker.game;

import io.github.travisdeshotels.poker.beans.EstimateWithPlayerName;
import io.github.travisdeshotels.poker.beans.HandStatus;
import io.github.travisdeshotels.poker.dto.HandResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PokerGameTest {
    @Test
    public void pokerGameTestWithTwoPlayers(){
        PokerGame game = new PokerGame(new ArrayList<>(Arrays.asList("1", "2", "3", "5", "8", "13")));
        String player1 = game.addPlayer("Bob");
        String player2 = game.addPlayer("Tom");
        assertEquals(2, game.getNumberOfPlayers());
        assertEquals(HandStatus.WAITING_ON_YOU, game.getHandStatus(player1));
        assertEquals(HandStatus.WAITING_ON_YOU, game.getHandStatus(player2));
        game.submitEstimate(player1, "5");
        assertEquals(HandStatus.WAITING_ON_OTHERS, game.getHandStatus(player1));
        assertEquals(HandStatus.WAITING_ON_YOU, game.getHandStatus(player2));
        game.submitEstimate(player2, "8");
        assertEquals(HandStatus.RESULT_READY, game.getHandStatus(player1));
        assertEquals(HandStatus.RESULT_READY, game.getHandStatus(player2));
        HandResult result = game.getResult(player1);
        EstimateWithPlayerName estimate = new EstimateWithPlayerName("Bob", "5");
        assert(result.getEstimateList().contains(estimate));
        EstimateWithPlayerName estimate2 = new EstimateWithPlayerName("Tom", "8");
        assert(result.getEstimateList().contains(estimate2));
        assertEquals(6.5f, result.getEstimateAverage());
        game.getResult(player2);
        assertEquals(HandStatus.WAITING_ON_YOU, game.getHandStatus(player1));
        assertEquals(HandStatus.WAITING_ON_YOU, game.getHandStatus(player2));
    }
}
