package com.dev.objects;

import javax.persistence.*;

@Entity
@Table(name = "matches")
public class MatchObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @ManyToOne
    @JoinColumn(name = "team1")
    private TeamObject team1;

    @ManyToOne
    @JoinColumn(name = "team2")
    private TeamObject team2;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserObject userUpdate;
    @Column
    private int team1_goals;

    @Column
    private int team2_goals;

    @Column(name = "running")
    private boolean running;

//    @ManyToOne
//    @JoinColumn(name = "team1")
//    private TeamObject teamObject;
//    @ManyToOne
//    @JoinColumn(name = "team2")
//    private TeamObject teamObject1;


    public MatchObject(TeamObject team1, TeamObject team2, int team1_goals, int team2_goals, boolean running,UserObject userThatUpdate) {

        this.team1 = team1;
        this.team2 = team2;
        this.team1_goals = team1_goals;
        this.team2_goals = team2_goals;
        this.running = running;
        this.userUpdate = userThatUpdate;
    }

    public MatchObject() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TeamObject getTeam1() {
        return team1;
    }

    public void setTeam1(TeamObject team1) {
        this.team1 = team1;
    }

    public TeamObject getTeam2() {
        return team2;
    }

    public void setTeam2(TeamObject team2) {
        this.team2 = team2;
    }

    public int getTeam1_goals() {
        return team1_goals;
    }

    public void setTeam1_goals(int team1_goals) {
        this.team1_goals = team1_goals;
    }

    public int getTeam2_goals() {
        return team2_goals;
    }

    public void setTeam2_goals(int team2_goals) {
        this.team2_goals = team2_goals;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public UserObject getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(UserObject userUpdate) {
        this.userUpdate = userUpdate;
    }
}
