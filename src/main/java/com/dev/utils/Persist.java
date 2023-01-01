
package com.dev.utils;

import com.dev.objects.MatchObject;
import com.dev.objects.TeamObject;
import com.dev.objects.UserObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

@Component
public class Persist {

    private Connection connection;

    final List<String> teams = Arrays.asList("Barcelona", "Real Madrid", "Bayren", "Benfica",
            "Chelsea", "Inter", "Liverpool", "Milan", "Paris", "Tottenham", "Dortmund", "Napoli");

    private final SessionFactory sessionFactory;

    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }


    @PostConstruct
    public void createConnectionToDatabase() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/football_project", "root", "1234");
            System.out.println("Successfully connected to DB");
            System.out.println();
            checkIfTableLeagueIsEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addTeam(List<String> teams) {
        Session session = sessionFactory.openSession();
        for (String teamName : teams) {
            TeamObject teamObject = new TeamObject(teamName);
            session.save(teamObject);
        }
        session.close();
    }

    public void checkIfTableLeagueIsEmpty() {
        Session session = sessionFactory.openSession();
        List<TeamObject> teamObjects = session.createQuery("FROM TeamObject ").list();
        if (teamObjects.isEmpty()) {
            addTeam(teams);
        }
        session.close();
    }

    public List<TeamObject> getAllTeams() {
        Session session = sessionFactory.openSession();
        List<TeamObject> teamObjects = session.createQuery("FROM TeamObject ").list();
        session.close();
        return teamObjects;
    }

    public List<MatchObject> getMatches() {
        Session session = sessionFactory.openSession();
        List<MatchObject> matchObjects = session.createQuery("FROM MatchObject ").list();
        session.close();
        return matchObjects;
    }

    public List<MatchObject> getLiveMatches() {
        Session session = sessionFactory.openSession();
        List<MatchObject> liveMatchObjects = session.createQuery("FROM MatchObject where running=true ").list();
        session.close();
        return liveMatchObjects;
    }

    public List<MatchObject> getEndMatches() {
        Session session = sessionFactory.openSession();
        List<MatchObject> endMatchObjects = session.createQuery("FROM MatchObject where running=false ").list();
        session.close();
        return endMatchObjects;
    }


    public MatchObject addMatch(TeamObject team1, TeamObject team2,UserObject userIdThatUpdate) {
        Session session = sessionFactory.openSession();
        MatchObject matchObject = new MatchObject(team1, team2, 0, 0, true,userIdThatUpdate);
        session.save(matchObject);
        session.close();
        return matchObject;
    }

    public MatchObject findMatchById(int matchId) {
        Session session = sessionFactory.openSession();
        MatchObject matchObject = session.load(MatchObject.class, matchId);
        session.close();
        return matchObject;
    }

    public void updateMatch(int matchId, int goalsTeam1, int goalsTeam2) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        MatchObject endMatchObject = session.load(MatchObject.class, matchId);
        endMatchObject.setTeam1_goals(goalsTeam1);
        endMatchObject.setTeam2_goals(goalsTeam2);
        session.update(endMatchObject);
        tx.commit();
        session.close();
    }

    public void finishedMatch(int matchId) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        MatchObject matchObject = (MatchObject) session.createQuery("FROM MatchObject where id=:matchId")
                .setParameter("matchId", matchId).uniqueResult();
        matchObject.setRunning(false);
        session.update(matchObject);
        tx.commit();
        session.close();
    }

    public UserObject addUser(String username, String token) {
        Session session = sessionFactory.openSession();
        UserObject user = new UserObject(username, token);
        session.save(user);
        session.close();
        return user;

    }


    public boolean usernameAvailable(String username) {
        boolean available = false;
        Session session = sessionFactory.openSession();
        UserObject foundUser = (UserObject) session.createQuery("FROM UserObject where username=:username")
                .setParameter("username", username).uniqueResult();
        session.close();
        if (foundUser == null) {
            available = true;
        }
        return available;
    }


    public UserObject getUserByToken(String token) {
        Session session = sessionFactory.openSession();
        UserObject foundUser = (UserObject) session.createQuery("FROM UserObject where token =:token").
                setParameter("token", token).uniqueResult();
        session.close();
        return foundUser;
    }

    public String getUserByCreds(String username, String token) {
        String response = null;
        Session session = sessionFactory.openSession();
        UserObject foundUser = (UserObject) session.createQuery("FROM UserObject where username =:userName and token=:token").
                setParameter("userName", username).setParameter("token", token).uniqueResult();
        session.close();
        if (foundUser != null) {
            response = token;
        }
        session.close();
        return response;
    }

    public UserObject getUserByUserId(int userId){
        UserObject foundUser = null;
        Session session = sessionFactory.openSession();
         foundUser = (UserObject) session.createQuery("FROM UserObject where id =:userId").
                setParameter("userId", userId).uniqueResult();
        session.close();
        return foundUser;
    }

}

//
//    public User getUserByToken (String token) {
//        User user = null;
//        try {
//            PreparedStatement preparedStatement = this.connection
//                    .prepareStatement(
//                            "SELECT id, username FROM users WHERE token = ?");
//            preparedStatement.setString(1, token);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String username = resultSet.getString("username");
//                user = new User(username, token);
//                user.setId(id);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return user;
//    }
//
//    public void addNote (int userId, String content) {
//        try {
//            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO notes (content, user_id) VALUE (?, ?)");
//            preparedStatement.setString(1, content);
//            preparedStatement.setInt(2, userId);
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } finally {
//        }
//    }


//    public List<String> getNotesByUserId (int userId) {
//        List<String> notes = new ArrayList<>();
//        try {
//            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT content FROM notes WHERE user_id = ?");
//            preparedStatement.setInt(1, userId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                String content = resultSet.getString("content");
//                notes.add(content);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return notes;
//    }


//TODO: TO CHECK IF TABLE EMPTY INSERT THAT:
//            addTeam("Barcelona");
//            addTeam("Real Madrid");
//            addTeam("Bayren");
//            addTeam("Benfica");
//            addTeam("Chelsea");
//            addTeam("Inter");
//            addTeam("Liverpool");
//            addTeam("Milan");
//            addTeam("Paris");
//            addTeam("Totttenham");
//            addTeam("Dortmund");
//            addTeam("Napoli");
//    public int scoreByTeamMatchesEnd(TeamObject team) {
//        int score = 0;
//        Session session = sessionFactory.openSession();
//        List<MatchObject> matchObjects = session.createQuery("FROM MatchObject where " +
//                "team1_id = :team1 or team2_id= :team1 and running = false ").setParameter("team1", team).list();
//        session.close();
//        for (MatchObject match : matchObjects) {
//            if (match.getTeam1_goals() == match.getTeam2_goals()) {
//                score += 1;
//            }
//            if (match.getTeam1_id() == team) {
//                if (match.getTeam1_goals() > match.getTeam2_goals()) {
//                    score += 3;
//                }
//            } else {
//                if (match.getTeam1_goals() < match.getTeam2_goals()) {
//                    score += 3;
//                }
//            }
//        }
//        return score;
//    }


//    public int scoreByTeamAllMatch(TeamObject team) {
//        int score = 0;
//        Session session = sessionFactory.openSession();
//        List<MatchObject> matchObjects = session.createQuery("FROM MatchObject where " +
//                "team1_id = :team1 or team2_id= :team1 ").setParameter("team1", team).list();
//        session.close();
//        for (MatchObject match : matchObjects) {
//            if (match.getTeam1_id() == team) {
//                if (match.getTeam1_goals() > match.getTeam2_goals()) {
//                    score += 3;
//                } else if (match.getTeam1_goals() == match.getTeam2_goals()) {
//                    score += 1;
//                }
//            } else {
//                if (match.getTeam1_goals() < match.getTeam2_goals()) {
//                    score += 3;
//                } else if (match.getTeam1_goals() == match.getTeam1_goals()) {
//                    score += 1;
//                }
//            }
//        }
//        return score;
//    }

//    public List<MatchObject> getAllLiveMatches() {
//        Session session = sessionFactory.openSession();
//        List<MatchObject> liveMatches = session.createQuery("FROM MatchObject where running = true").list();
//        session.close();
//        return liveMatches;
//    }