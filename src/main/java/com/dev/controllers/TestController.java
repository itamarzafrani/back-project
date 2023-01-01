package com.dev.controllers;

import com.dev.objects.MatchObject;
import com.dev.objects.TableLeagueRow;
import com.dev.objects.TeamObject;
import com.dev.objects.UserObject;
import com.dev.responses.BasicResponse;
import com.dev.responses.MatchResponse;
import com.dev.responses.UserResponse;
import com.dev.utils.Persist;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RestController
public class TestController {


    //BASIC-RESPONSE
    private final int USERNAME_DIDNT_FOUND = 1;

    private final int USERNAME_DIDNT_VALIDATE = 5;
    private final int PASSWORD_DIDNT_VALIDATE = 6;

    private final int USERNAME_ALREADY_TAKEN = 6;


    private final int PASSWORD_OR_USERNAME_INCORRECT = 2;

    private final int DIDNT_FOUNT_BY_USER_ID = 1;
    private final int INVALID_GOALS_INPUT = 2;
    private final int MATCH_DIDNT_RUNNING = 3;


    @Autowired
    public Utils utils;


    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }


    @RequestMapping
            (value = "/get-matches", method = {RequestMethod.GET})
    public List<MatchObject> getAllMatches() {
        List<MatchObject> matchObjectList = persist.getMatches();
        return matchObjectList;
    }

    @RequestMapping
            (value = "/get-all-teams", method = {RequestMethod.GET})
    public List<TeamObject> getAllTeams() {
        List<TeamObject> allTeams = persist.getAllTeams();
        List<MatchObject> liveMatches = persist.getLiveMatches();
        List<TeamObject> teams = new ArrayList<>();
        if (liveMatches.isEmpty())
            return allTeams;

        for (int j = 0; j < allTeams.size(); j++) {
            for (int i = 0; i < liveMatches.size(); i++) {
                if (liveMatches.get(i).getTeam1().getName().equals(allTeams.get(j).getName()) || liveMatches.get(i).getTeam2().getName().equals(allTeams.get(j).getName())) {
                    break;
                }
                if (i == liveMatches.size() - 1) {
                    teams.add(allTeams.get(j));
                }
            }
        }
        return teams;

    }

    @RequestMapping
            (value = "/get-table-league-end-matches", method = {RequestMethod.GET})
    public List<TableLeagueRow> getTableLeagueEndMatches() {
        List<TeamObject> allTeams = persist.getAllTeams();
        List<MatchObject> matchObjectList = persist.getEndMatches();
        List<TableLeagueRow> tableLeague = utils.calc(allTeams, matchObjectList);
        return tableLeague;
    }

    @RequestMapping //RUNNING MATCHES
            (value = "/get-all-live-matches", method = {RequestMethod.GET})
    public List<MatchObject> getAllLiveMatch() {
        List<MatchObject> matchObjectList = persist.getLiveMatches();
        return matchObjectList;
    }


    @RequestMapping //END MATCHES + RUNNING MATCHES
            (value = "/get-full-table-league", method = {RequestMethod.GET})
    public List<TableLeagueRow> getFullTableLeague() {
        List<TeamObject> teams = persist.getAllTeams();
        List<MatchObject> matchObjectList = persist.getMatches();
        List<TableLeagueRow> tableLeagueRowList = utils.calc(teams, matchObjectList);
        return tableLeagueRowList;
    }


    @RequestMapping //ADD MATCH TO DB (RESULT 0 - 0)
            (value = "/add-match", method = {RequestMethod.POST})
    public MatchResponse addMatch(String team1, String team2, int userIdThatUpdate) {
        MatchResponse matchResponse = new MatchResponse();
        List<TeamObject> teamObjectList = persist.getAllTeams();
        TeamObject teamN1 = utils.getTeamByName(team1, teamObjectList);
        TeamObject teamN2 = utils.getTeamByName(team2, teamObjectList);
        UserObject selectUser = persist.getUserByUserId(userIdThatUpdate);
        if (selectUser == null) {
            matchResponse.setSuccess(false);
            matchResponse.setErrorCode(DIDNT_FOUNT_BY_USER_ID);
        } else {
            MatchObject matchObject = persist.addMatch(teamN1, teamN2, selectUser);
            matchResponse.setSuccess(true);
            matchResponse.setErrorCode(null);
            matchResponse.setMatchId(matchObject.getId());
            matchResponse.setUserObject(selectUser);
        }
        return matchResponse;
    }

    @RequestMapping // UPDATE THE MATCH BY GOALS
            (value = "/update-match", method = {RequestMethod.POST})
    public MatchResponse updateMatch(int matchId, int goalsTeam1, int goalsTeam2) {
        MatchResponse matchResponse = new MatchResponse();
        if (goalsTeam1 < 0 || goalsTeam2 < 0) {
            matchResponse.setSuccess(false);
            matchResponse.setErrorCode(INVALID_GOALS_INPUT);
            matchResponse.setMatchId(matchId);
            return matchResponse;
        }
        MatchObject matchObject = persist.findMatchById(matchId);
        if (utils.checkIfMatchIsRunning(matchObject)) {
            persist.updateMatch(matchId, goalsTeam1, goalsTeam2);
            matchResponse.setSuccess(true);
            matchResponse.setErrorCode(null);
            matchResponse.setMatchId(matchId);
        } else {
            matchResponse.setSuccess(false);
            matchResponse.setErrorCode(MATCH_DIDNT_RUNNING);
            matchResponse.setMatchId(matchId);
        }
        return matchResponse;
    }

    @RequestMapping // FINISH THE MATCH
            (value = "/finish-match", method = {RequestMethod.POST})
    public void finishMatch(int matchId) {
        persist.finishedMatch(matchId);
    }

    @RequestMapping //CREAT-ACCOUNT
            (value = "/create-account", method = {RequestMethod.POST})
    public UserResponse addUser(String username, String password) {
        UserResponse userResponse = new UserResponse();
        UserObject newUser = null;
        if (utils.validateUserName(username)) {
            if (utils.validatePassword(password)) {
                if (persist.usernameAvailable(username)) {
                    String token = utils.createHash(username, password);
                    newUser = persist.addUser(username, token);
                }else {
                    userResponse = new UserResponse(false, USERNAME_ALREADY_TAKEN, null);
                return userResponse;
                }
            }else {
                userResponse = new UserResponse(false, PASSWORD_DIDNT_VALIDATE, null);
            return userResponse;
            }
        } else {
            userResponse = new UserResponse(false, USERNAME_DIDNT_VALIDATE,null );
            return userResponse;
        }
        userResponse = new UserResponse(true, null,newUser );
        return userResponse;
    }

    @RequestMapping // SIGN-IN
            (value = "/sign-in", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse signIn(String username, String password) {
        BasicResponse basicResponse = null;
        String token = utils.createHash(username, password);
        token = persist.getUserByCreds(username, token);
        if (token == null) {
            if (persist.usernameAvailable(username)) { // there is no such username
                basicResponse = new BasicResponse(false, USERNAME_DIDNT_FOUND);
            } else {
                basicResponse = new BasicResponse(false, PASSWORD_OR_USERNAME_INCORRECT);
            }
        } else {
            UserObject user = persist.getUserByToken(token);
            basicResponse = new UserResponse(true, null, user);
        }
        return basicResponse;
    }


}