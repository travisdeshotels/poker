package io.github.travisdeshotels.poker.beans;

public enum PointValues {
    One("1"),
    Two("2"),
    Three("3"),
    Five("4"),
    Eight("8"),
    Thirteen("13"),
    Question("?"),
    Coffee("c");

    String name;

    PointValues(String i) {
        name = i;
    }

    public String getName(){
        return name;
    }
}
