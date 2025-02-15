package io.github.travisdeshotels.poker.terminal;

import io.github.travisdeshotels.poker.terminal.util.Poker;

public class Main {
    public static void main(String[] args) {
        Poker poker = new Poker(args[0]);
        poker.start();
    }
}
