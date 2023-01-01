package com.dev.responses;

import com.dev.objects.UserObject;

public class MatchResponse extends BasicResponse{

    private int matchId;
    private UserObject userObject;

    public MatchResponse(boolean success, Integer errorCode, int matchId, UserObject userObject) {
        super(success, errorCode);
        this.matchId = matchId;
        this.userObject = userObject;
    }
    public MatchResponse(){

    }

    public MatchResponse(int matchId, UserObject userObject) {
        this.matchId = matchId;
        this.userObject = userObject;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public UserObject getUserObject() {
        return userObject;
    }

    public void setUserObject(UserObject userObject) {
        this.userObject = userObject;
    }
}
