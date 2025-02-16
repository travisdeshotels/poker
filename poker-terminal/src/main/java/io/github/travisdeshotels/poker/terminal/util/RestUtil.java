package io.github.travisdeshotels.poker.terminal.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.travisdeshotels.poker.terminal.beans.Estimate;
import io.github.travisdeshotels.poker.terminal.beans.HandResult;
import io.github.travisdeshotels.poker.terminal.beans.JoinResponse;
import io.github.travisdeshotels.poker.terminal.beans.StartPokerResponse;
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

    public StartPokerResponse createGame(String playerName){
        ObjectMapper mapper = new ObjectMapper();
        StartPokerResponse responseBody = null;
        try {
            String data = mapper.writeValueAsString(new Object(){
                @Getter
                @Setter
                String name = playerName;
            });
            if (this.postData(this.API_URL, data) == 201){
                responseBody = mapper.readValue(this.inputStream, StartPokerResponse.class);
            } else {
                out("Oops!");
            }
        } catch (PokerApiException | IOException e) {
            throw new RuntimeException(e);
        }
        return responseBody;
    }

    public JoinResponse joinGame(String gameId, String playerName){
        ObjectMapper mapper = new ObjectMapper();
        JoinResponse responseBody = null;
        try {
            String data = mapper.writeValueAsString(new Object(){
                @Getter String name = playerName;
            });
           if (this.postData(this.API_URL + "/join/" + gameId, data) == 200) {
                responseBody = mapper.readValue(this.inputStream, JoinResponse.class);
           }
        } catch (IOException | PokerApiException e) {
            throw new RuntimeException(e);
        }
        return responseBody;
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

    public String getStatus(String gameId, String playerId){
        try {
            Map response = (Map) this.getData(this.API_URL + "/status/" + gameId + "/" + playerId, Map.class);
            return (String) response.get("handStatus");
        } catch (PokerApiException e) {
            throw new RuntimeException(e);
        }
    }

    public HandResult getResult(String gameId, String playerId){
        try {
            return (HandResult) this.getData(this.API_URL + "/result/" + gameId + "/" + playerId, HandResult.class);
        } catch (PokerApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void submitResponse(String gameId, String playerId, String pointValue){
        //url + gameid POST
        ObjectMapper mapper = new ObjectMapper();
        Estimate estimate = new Estimate(playerId, pointValue);
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
