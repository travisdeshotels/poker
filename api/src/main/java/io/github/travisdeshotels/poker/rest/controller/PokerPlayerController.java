package io.github.travisdeshotels.poker.rest.controller;

import io.github.travisdeshotels.poker.dto.Estimate;
import io.github.travisdeshotels.poker.dto.JoinResponse;
import io.github.travisdeshotels.poker.dto.Player;
import io.github.travisdeshotels.poker.game.PokerGame;
import io.github.travisdeshotels.poker.util.PokerGameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PokerPlayerController {
    PokerGameUtil util;

    @Autowired
    public PokerPlayerController(PokerGameUtil util){
        this.util = util;
    }

    @RequestMapping(method = RequestMethod.POST, value="/join/{id}")
    public ResponseEntity<JoinResponse> joinPoker(@PathVariable("id") String gameId, @RequestBody Player player){
        PokerGame game = util.getGame(gameId);
        String playerId = game.addPlayer(player.getName());
        JoinResponse response = new JoinResponse(playerId, game.getNumberOfPlayers());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value="/{id}")
    public void submitEstimate(@PathVariable("id") String gameId, @RequestBody Estimate estimate){
        log.info("Game: {} Player: {} Estimate: {} submitted", gameId, estimate.getPlayerId(), estimate.getPointValue());
        util.getGame(gameId).submitEstimate(estimate.getPlayerId(), estimate.getPointValue());
    }
}
