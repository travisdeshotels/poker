package io.github.travisdeshotels.poker.terminal;

import io.github.travisdeshotels.poker.terminal.beans.StartPokerResponse;
import io.github.travisdeshotels.poker.terminal.util.RestUtil;

public class ThreadTest {
    private static final RestUtil util = new RestUtil("http://localhost:8080");

    public static void main(String[] args){
        game1();
        //game2();
    }

    private static void game1(){
        int runs = 4;
        StartPokerResponse response = util.createGame("p1");
        String gameId = response.getGameId();
        ThreadRunner r1 = new ThreadRunner("p1", "3", runs, true, gameId);
        r1.setPlayerId(response.getPlayerId());
        ThreadRunner r2 = new ThreadRunner("p2", "5", runs, gameId);
        ThreadRunner r3 = new ThreadRunner("p3", "3", runs, gameId);
        ThreadRunner r4 = new ThreadRunner("p4", "5", runs, gameId);
        ThreadRunner r5 = new ThreadRunner("p5", "3", runs, gameId);
        ThreadRunner r6 = new ThreadRunner("p6", "8", runs, gameId);
        ThreadRunner r7 = new ThreadRunner("p7", "3", runs, gameId);
        ThreadRunner r8 = new ThreadRunner("p8", "8", runs, gameId);
        r1.start();
        r2.start();
        r3.start();
        r4.start();
        r5.start();
//        r6.start();
//        r7.start();
//        r8.start();
    }

    private static void game2(){
        StartPokerResponse response = util.createGame("t1");
        String gameId = response.getGameId();
        ThreadRunner r1 = new ThreadRunner("t1", "3", 10, true, gameId);
        r1.setPlayerId(response.getPlayerId());
        ThreadRunner r2 = new ThreadRunner("t2", "5", 10, gameId);
        ThreadRunner r3 = new ThreadRunner("t3", "3", 10, gameId);
        ThreadRunner r4 = new ThreadRunner("t4", "5", 10, gameId);
        ThreadRunner r5 = new ThreadRunner("t5", "3", 10, gameId);
        ThreadRunner r6 = new ThreadRunner("t6", "8", 10, gameId);
        ThreadRunner r7 = new ThreadRunner("t7", "3", 10, gameId);
        ThreadRunner r8 = new ThreadRunner("t8", "8", 10, gameId);
        r1.start();
        r2.start();
        r3.start();
        r4.start();
        r5.start();
//        r6.start();
//        r7.start();
//        r8.start();
    }
}
