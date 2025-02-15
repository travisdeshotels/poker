package io.github.travisdeshotels.poker.rest.controller;

import io.github.travisdeshotels.poker.beans.Estimate;
import io.github.travisdeshotels.poker.beans.HandStatus;
import io.github.travisdeshotels.poker.beans.JoinResponse;
import io.github.travisdeshotels.poker.beans.Player;
import io.github.travisdeshotels.poker.beans.StartPokerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class PokerController {
    private Map<String, Map<String, String>> games = new HashMap<>();
    private boolean handIsEstimated = false;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<StartPokerResponse> startPoker(@RequestBody Player player){
        Map<String, String> newGame = new HashMap<>();
        newGame.put(player.getName(), null);
        String gameId = UUID.randomUUID().toString().split("-")[0];
        games.put(gameId, newGame);
        log.info("Game {} started", gameId);

        return new ResponseEntity<>(new StartPokerResponse(gameId), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value="/join/{id}")
    public JoinResponse joinPoker(@PathVariable("id") String gameId, @RequestBody Player player){
        Map<String, String> game = games.get(gameId);
        game.put(player.getName(), null);
        log.info("Player {} joined", player.getName());
        log.info("Number of players is {}", game.size());
        return new JoinResponse(game.size());
    }

    @RequestMapping(method = RequestMethod.POST, value="/{id}")
    public void submitEstimate(@PathVariable("id") String gameId, @RequestBody Estimate estimate){
        Map<String, String> game = games.get(gameId);
        if (handIsEstimated){
            log.info("Clearing the current hand");
            for (Map.Entry<String, String> entry : game.entrySet()){
                entry.setValue(null);
            }
            handIsEstimated = false;
        }
        game.put(estimate.getPlayer(), estimate.getPointValue());
        log.info("Player {} has submitted {}", estimate.getPlayer(), estimate.getPointValue());
    }

    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    public ResponseEntity<HandStatus> viewStatus(@PathVariable("id") String gameId){
        Map<String, String> game = games.get(gameId);
        List filtered = game.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .toList();
        HandStatus status = new HandStatus();
        if (game.size() == filtered.size()){
            handIsEstimated = true;
            int estimateTotal = 0;
            List<Estimate> estimates = new ArrayList<>();
            for (Map.Entry<String, String> entry : game.entrySet()){
                estimates.add(new Estimate(entry.getKey(), entry.getValue()));
                estimateTotal += Integer.parseInt(entry.getValue());
            }
            status.setEstimateList(estimates);
            status.setPlayersTotal(game.size());
            status.setEstimateAverage((float) estimateTotal / game.size());
        } else{
            status.setPlayersTotal(game.size());
            status.setPlayersWithoutEstimate(filtered.size());
        }

        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/{id}")
    public void endPoker(@PathVariable("id") String gameId){
        games.remove(gameId);
    }
}
