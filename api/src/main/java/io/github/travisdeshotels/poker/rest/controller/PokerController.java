package io.github.travisdeshotels.poker.rest.controller;

import io.github.travisdeshotels.poker.beans.HandStatus;
import io.github.travisdeshotels.poker.game.PokerGame;
import io.github.travisdeshotels.poker.dto.Estimate;
import io.github.travisdeshotels.poker.dto.HandResult;
import io.github.travisdeshotels.poker.dto.HandStatusDto;
import io.github.travisdeshotels.poker.dto.JoinResponse;
import io.github.travisdeshotels.poker.dto.Player;
import io.github.travisdeshotels.poker.dto.StartPokerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class PokerController {
    Map<String, PokerGame> games;

    public PokerController(){
        games = new HashMap<>();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<StartPokerResponse> startPoker(@RequestBody Player player){
        PokerGame game = new PokerGame();
        String playerId = game.addPlayer(player.getName());
        String gameId = UUID.randomUUID().toString().split("-")[0];
        games.put(gameId, game);
        return new ResponseEntity<>(new StartPokerResponse(gameId, playerId), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, value="/join/{id}")
    public ResponseEntity<JoinResponse> joinPoker(@PathVariable("id") String gameId, @RequestBody Player player){
        PokerGame game = games.get(gameId);
        String playerId = game.addPlayer(player.getName());
        JoinResponse response = new JoinResponse(playerId, game.getNumberOfPlayers());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value="/{id}")
    public void submitEstimate(@PathVariable("id") String gameId, @RequestBody Estimate estimate){
        games.get(gameId).submitEstimate(estimate.getPlayerId(), estimate.getPointValue());
    }

    @RequestMapping(method = RequestMethod.GET, value="/status/{gameId}/{playerId}")
    public ResponseEntity<HandStatusDto> viewStatus(@PathVariable("gameId") String gameId,
                                                    @PathVariable("playerId") String playerId){
        HandStatus status = games.get(gameId).getHandStatus(playerId);
        return new ResponseEntity<>(new HandStatusDto(status), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value="/result/{gameId}/{playerId}")
    public ResponseEntity<HandResult> getResult(@PathVariable("gameId") String gameId,
                                                @PathVariable("playerId") String playerId){
        return new ResponseEntity<>(games.get(gameId).getResult(playerId), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/{id}")
    public void endPoker(@PathVariable("id") String gameId){
    }
}
