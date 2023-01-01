package com.dev.objects;

public class TableLeagueRow extends TeamObject {

    private int goalsThatGot;

    private int goalsThatGive;

    private int goalDifference;

    private int win;
    private int loose;
    private int draw;
    private int points;

    public TableLeagueRow(int id,  String name, int goalsThatGot, int goalsThatGive, int goalDifference, int win, int loose, int draw, int points) {
        super(id,name);
        this.goalsThatGot = goalsThatGot;
        this.goalsThatGive = goalsThatGive;
        this.goalDifference = goalDifference;
        this.draw = draw;
        this.loose = loose;
        this.win = win;
        this.points = points;
    }

    public TableLeagueRow(int goalsThatGot, int goalsThatGive, int goalDifference, int win, int loose, int draw, int points) {
        this.goalsThatGot = goalsThatGot;
        this.goalsThatGive = goalsThatGive;
        this.goalDifference = goalDifference;
        this.draw = draw;
        this.loose = loose;
        this.win = win;
        this.points = points;
    }


    public TableLeagueRow(int id, String name) {
        super(id,name);
    }


    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLoose() {
        return loose;
    }

    public void setLoose(int loose) {
        this.loose = loose;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }


    public int getGoalsThatGot() {
        return goalsThatGot;
    }

    public void setGoalsThatGot(int goalsThatGot) {
        this.goalsThatGot = goalsThatGot;
    }

    public int getGoalsThatGive() {
        return goalsThatGive;
    }

    public void setGoalsThatGive(int goalsThatGive) {
        this.goalsThatGive = goalsThatGive;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    public void setGoalDifference(int goalDifference) {
        this.goalDifference = goalDifference;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
