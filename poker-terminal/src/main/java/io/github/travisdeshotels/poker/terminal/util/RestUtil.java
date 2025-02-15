package io.github.travisdeshotels.poker.terminal.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.travisdeshotels.poker.terminal.beans.Estimate;
import io.github.travisdeshotels.poker.terminal.beans.HandStatus;
import io.github.travisdeshotels.poker.terminal.exception.PokerApiException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static io.github.travisdeshotels.poker.terminal.util.IoUtil.out;

public class RestUtil {
    private final String API_URL;
    private InputStream inputStream;

    public RestUtil(String url){
        this.API_URL = url;
    }

    public String createGame(String playerName){
        ObjectMapper mapper = new ObjectMapper();
        String gameId = null;
        try {
            String data = mapper.writeValueAsString(new Object(){
                @Getter
                @Setter
                String name = playerName;
            });
            if (this.postData(this.API_URL, data) == 200){
                Map responseBody = mapper.readValue(this.inputStream, Map.class);
                gameId = (String) responseBody.get("gameId");
                out("Game started! Game id is " + gameId);
            } else {
                out("Oops!");
            }
        } catch (PokerApiException | IOException e) {
            throw new RuntimeException(e);
        }
        return gameId;
    }

    public int joinGame(String gameId, String playerName){
        ObjectMapper mapper = new ObjectMapper();
        int numberOfPlayers = -1;
        try {
            String data = mapper.writeValueAsString(new Object(){
                @Getter String name = playerName;
            });
           if (this.postData(this.API_URL + "/join/" + gameId, data) == 200) {
                Map responseBody = mapper.readValue(this.inputStream, Map.class);
                numberOfPlayers = (int) responseBody.get("numberOfPlayersConnected");
           }
        } catch (IOException | PokerApiException e) {
            throw new RuntimeException(e);
        }
        return numberOfPlayers;
    }

    public int postData(String url, String data) throws PokerApiException {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            this.inputStream = conn.getInputStream();
            return conn.getResponseCode();
        } catch (IOException e) {
            throw new PokerApiException("");
        }
    }

    public int putData(String url, String data) throws PokerApiException {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();

            return conn.getResponseCode();
        } catch (IOException e){
            throw new PokerApiException("");
        }
    }

    public boolean isEstimateNeeded(String gameId){
        try {
            HandStatus status = (HandStatus) this.getData(this.API_URL + "/" + gameId, HandStatus.class);
            out(status.toString());
            return status.getEstimateList() == null || status.getPlayersWithoutEstimate() == 0;
        } catch (PokerApiException e) {
            throw new RuntimeException(e);
        }
    }

    public HandStatus getResult(String gameId){
        try {
            HandStatus status = (HandStatus) this.getData(this.API_URL + "/" + gameId, HandStatus.class);
            out(status.toString());
            return status;
        } catch (PokerApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void submitResponse(String player, String pointValue, String gameId){
        //url + gameid POST
        ObjectMapper mapper = new ObjectMapper();
        Estimate estimate = new Estimate(player, pointValue);
        try {
            this.postData(this.API_URL + "/" + gameId, mapper.writeValueAsString(estimate));
        } catch (PokerApiException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getData(String url, Class clazz) throws PokerApiException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new URL(url), clazz);
        } catch (JsonProcessingException e) {
            throw new PokerApiException("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
