package io.github.travisdeshotels.poker.rest.controller;

import io.github.travisdeshotels.poker.beans.HandStatusDto;
import io.github.travisdeshotels.poker.dto.EndHandRequest;
import io.github.travisdeshotels.poker.dto.HandResult;
import io.github.travisdeshotels.poker.dto.ResetHandResponse;
import io.github.travisdeshotels.poker.dto.StartPokerResponse;
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

import java.util.UUID;

@Slf4j
@RestController
public class PokerHostController {
    PokerGameUtil util;

    @Autowired
    public PokerHostController(PokerGameUtil util){
        this.util = util;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<StartPokerResponse> startPoker(){
        PokerGame game = new PokerGame(util.validPointValueList);
        String hostPlayerId = game.setHost();
        String gameId = UUID.randomUUID().toString().split("-")[0];
        util.addGame(gameId, game);
        return new ResponseEntity<>(new StartPokerResponse(gameId, hostPlayerId), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value="/status/{gameId}/{hostId}")
    public ResponseEntity<HandStatusDto> viewStatus(@PathVariable("gameId") String gameId,
                                                    @PathVariable("hostId") String hostId){
        String status = util.getGame(gameId).getHandStatus(hostId) ? "Results are ready" : "Waiting on players";
        return new ResponseEntity<>(new HandStatusDto(status), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value="/result/{gameId}/{hostId}")
    public ResponseEntity<HandResult> getResult(@PathVariable("gameId") String gameId,
                                                @PathVariable("hostId") String hostId){
        HandResult result = util.getGame(gameId).getResult(hostId);
        if (result == null){
            log.info("Game: {} Player: {} has attempted to view the hand result!", gameId, hostId);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else{
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value="/end/{id}")
    public ResponseEntity<ResetHandResponse> endHand(@PathVariable("id") String gameId, @RequestBody EndHandRequest request){
        ResetHandResponse response = new ResetHandResponse("Invalid request!");
        if(util.getGame(gameId).resetHand(request.getHostPlayerId())){
            response.setResponse("Hand has been reset.");
            log.info("Host {} has reset the hand.", request.getHostPlayerId());
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/{gameId}")
    public void endPoker(@PathVariable("id") String gameId){
        util.removeGame(gameId);
    }
}
