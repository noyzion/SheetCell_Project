package shticell.engine.sheet.impl;

import shticell.engine.sheet.coordinate.Coordinate;

public class Edge {
    private final shticell.engine.sheet.coordinate.Coordinate from; // התא שממנו יוצאת הקשת
    private final Coordinate to;   // התא שאליו נכנסת הקשת

    public Edge(Coordinate from, Coordinate to) {
        this.from = from;
        this.to = to;
    }

    public Coordinate getFrom() {
        return from;
    }

    public Coordinate getTo() {
        return to;
    }
}