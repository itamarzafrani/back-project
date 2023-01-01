package com.dev.utils;

import com.dev.objects.*;
import org.springframework.stereotype.Component;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Utils {


    public List<TableLeagueRow> calc(List<TeamObject> allTeams, List<MatchObject> allMatches) {
        List<TableLeagueRow> tableLeague = pushTeams(allTeams);
        for (MatchObject matchObject : allMatches) {

            int goalsTeam1 = matchObject.getTeam1_goals();
            int goalsTeam2 = matchObject.getTeam2_goals();
            for (TableLeagueRow tableRow : tableLeague) {
                if (goalsTeam1 > goalsTeam2) {
                    if (tableRow.getId() == matchObject.getTeam1().getId()) {
                        tableRow.setWin(tableRow.getWin() + 1);
                        tableRow.setGoalsThatGive(tableRow.getGoalsThatGive() + goalsTeam1);
                        tableRow.setGoalsThatGot(tableRow.getGoalsThatGot() + goalsTeam2);
                        tableRow.setGoalDifference(tableRow.getGoalDifference() + (goalsTeam1 - goalsTeam2));
                    }
                    if (tableRow.getId() == matchObject.getTeam2().getId()) {
                        tableRow.setLoose(tableRow.getLoose() + 1);
                        tableRow.setGoalsThatGive(tableRow.getGoalsThatGive() + goalsTeam2);
                        tableRow.setGoalsThatGot(tableRow.getGoalsThatGot() + goalsTeam1);
                        tableRow.setGoalDifference(tableRow.getGoalDifference() + (goalsTeam2 - goalsTeam1));
                    }
                } else if (goalsTeam1 < goalsTeam2) {
                    if (tableRow.getId() == matchObject.getTeam1().getId()) {
                        tableRow.setLoose(tableRow.getLoose() + 1);
                        tableRow.setGoalsThatGive(tableRow.getGoalsThatGive() + goalsTeam1);
                        tableRow.setGoalsThatGot(tableRow.getGoalsThatGot() + goalsTeam2);
                        tableRow.setGoalDifference(tableRow.getGoalDifference() + (goalsTeam1 - goalsTeam2));
                    }
                    if (tableRow.getId() == matchObject.getTeam2().getId()) {
                        tableRow.setWin(tableRow.getWin() + 1);
                        tableRow.setGoalsThatGive(tableRow.getGoalsThatGive() + goalsTeam2);
                        tableRow.setGoalsThatGot(tableRow.getGoalsThatGot() + goalsTeam1);
                        tableRow.setGoalDifference(tableRow.getGoalDifference() + (goalsTeam2 - goalsTeam1));
                    }
                } else {
                    if (tableRow.getId() == matchObject.getTeam1().getId()) {
                        tableRow.setDraw(tableRow.getDraw() + 1);
                        tableRow.setGoalsThatGive(tableRow.getGoalsThatGive() + goalsTeam1);
                        tableRow.setGoalsThatGot(tableRow.getGoalsThatGot() + goalsTeam2);

                    }
                    if (tableRow.getId() == matchObject.getTeam2().getId()) {
                        tableRow.setDraw(tableRow.getDraw() + 1);
                        tableRow.setGoalsThatGive(tableRow.getGoalsThatGive() + goalsTeam2);
                        tableRow.setGoalsThatGot(tableRow.getGoalsThatGot() + goalsTeam1);
                    }
                }
            }
        }

        for (TableLeagueRow tableLeagueRow : tableLeague) {
            int points;
            points = tableLeagueRow.getWin() * 3 + tableLeagueRow.getDraw();
            tableLeagueRow.setPoints(points);

        }

        return tableLeague;
    }


    public boolean checkIfMatchIsRunning(MatchObject matchObject) {
        if (matchObject.isRunning()) {
            return true;
        }
        return false;
    }

    public List<TableLeagueRow> pushTeams(List<TeamObject> allTeams) {
        List<TableLeagueRow> tableLeague = new ArrayList<>();
        for (TeamObject team : allTeams) {
            TableLeagueRow tableLeagueRow = new TableLeagueRow(team.getId(), team.getName());
            tableLeague.add(tableLeagueRow);
        }
        return tableLeague;
    }

    public String createHash(String username, String password) {
        String raw = String.format("%s_%s", username, password);
        String myHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(raw.getBytes());
            byte[] digest = md.digest();
            myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return myHash;
    }

    public TeamObject getTeamByName(String teamName, List<TeamObject> allTeams) {
        TeamObject foundTeam = null;
        for (TeamObject teamObject : allTeams) {
            if (teamName.equals(teamObject.getName())) {
                foundTeam = teamObject;
            }
        }
        return foundTeam;
    }

    public boolean validateUserName(String userName) {
        boolean validUsername = false;
        if (userName != null) {
            char ch = userName.charAt(0);
            if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z') {
                if (userName.length() >= 3 && userName.length() <= 23) {
                    validUsername = true;
                }
            }
        }
        return validUsername;
    }


    public boolean validatePassword(String password) {
        boolean validPassword = false;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;

        if (password != null) {
            char ch;
            for (int i = 0; i < password.length(); i++) {
                ch = password.charAt(i);
                if (Character.isDigit(ch)) {
                    numberFlag = true;
                } else if (Character.isUpperCase(ch)) {
                    capitalFlag = true;
                } else if (Character.isLowerCase(ch)) {
                    lowerCaseFlag = true;
                }
            }
        }
        if (numberFlag && capitalFlag && lowerCaseFlag)
            validPassword = true;

        return validPassword;
    }

    public MatchObject getMatchById(int id, List<MatchObject> allMatches) {
        MatchObject foundMatch = new MatchObject();
        for (MatchObject matchObject : allMatches) {
            if (id == matchObject.getId()) {
                foundMatch = matchObject;
            }
        }
        return foundMatch;
    }


}
