package io.github.travisdeshotels.poker.util;

import io.github.travisdeshotels.poker.game.PokerGame;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PokerGameUtil {
    Map<String, PokerGame> games;
    public final List<String> validPointValueList = Arrays.asList("1", "2", "3", "5", "8", "13");

    public PokerGameUtil(){
        this.games = new HashMap<>();
    }

    public void addGame(String gameId, PokerGame game){
        this.games.put(gameId, game);
    }

    public PokerGame getGame(String gameId){
        return games.get(gameId);
    }

    public void removeGame(String gameId){
        this.games.remove(gameId);
    }
}
