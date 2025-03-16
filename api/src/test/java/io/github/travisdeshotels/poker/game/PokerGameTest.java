package io.github.travisdeshotels.poker.game;

import io.github.travisdeshotels.poker.beans.EstimateWithPlayerName;
import io.github.travisdeshotels.poker.dto.HandResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PokerGameTest {


    private void firstHand(PokerGame game, String player1, String player2, String hostId){
        //invalid estimate is ignored
        game.submitEstimate(player1, "99");
        game.submitEstimate(player1, "5");
        //duplicate estimate is ignored
        game.submitEstimate(player1, "3");
        game.submitEstimate(player2, "8");
        assertTrue(game.getHandStatus(hostId));
        assertFalse(game.getHandStatus(player1));
        assertFalse(game.getHandStatus(player2));
        HandResult result = game.getResult(hostId);
        assertNull(game.getResult(player2));
        assertNull(game.getResult(player1));
        EstimateWithPlayerName estimate = new EstimateWithPlayerName("Bob", "5");
        assert(result.getEstimateList().contains(estimate));
        EstimateWithPlayerName estimate2 = new EstimateWithPlayerName("Tom", "8");
        assert(result.getEstimateList().contains(estimate2));
        assertEquals(6.5f, result.getEstimateAverage());
    }

    private void secondHand(PokerGame game, String player1, String player2, String hostId){
        game.submitEstimate(player1, "3");
        game.submitEstimate(player2, "1");
        assertTrue(game.getHandStatus(hostId));
        assertFalse(game.getHandStatus(player1));
        assertFalse(game.getHandStatus(player2));
        HandResult result = game.getResult(hostId);
        assertNull(game.getResult(player2));
        assertNull(game.getResult(player1));
        EstimateWithPlayerName estimate = new EstimateWithPlayerName("Bob", "3");
        assert(result.getEstimateList().contains(estimate));
        EstimateWithPlayerName estimate2 = new EstimateWithPlayerName("Tom", "1");
        assert(result.getEstimateList().contains(estimate2));
        assertEquals(2f, result.getEstimateAverage());
    }

    @Test
    public void pokerGameTestWithTwoPlayers(){
        PokerGame game = new PokerGame(new ArrayList<>(Arrays.asList("1", "2", "3", "5", "8", "13")));
        String hostId = game.setHost();
        String player1 = game.addPlayer("Bob");
        String player2 = game.addPlayer("Tom");
        assertEquals(2, game.getNumberOfPlayers());
        firstHand(game, player1, player2, hostId);
        assertFalse(game.resetHand(player1));
        assertTrue(game.resetHand(hostId));
        secondHand(game, player1, player2, hostId);
    }
}
