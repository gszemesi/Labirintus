
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Adatbázis kezezelése. Adatbázisba beszúrás, törlés, frissítés és a rekordok
 * rendezése.
 *
 * @author Szemesi Gábor
 */
public class HighScores {

    int maxScores;
    PreparedStatement insertStatement;
    PreparedStatement deleteStatement;
    Connection connection;

    /**
     * Csatlakozás az adatbázishoz.
     */
    public HighScores(int maxScores) throws SQLException {
        this.maxScores = maxScores;
        Properties connectionProps = new Properties();
        // Add new user -> MySQL workbench (Menu: Server / Users and priviliges)
        //                             Tab: Administrative roles -> Check "DBA" option
        connectionProps.put("user", "root");
        connectionProps.put("password", "");
        connectionProps.put("serverTimezone", "UTC");
        String dbURL = "jdbc:mysql://localhost:3306/LABYRINTHHIGHSCORES";
        connection = DriverManager.getConnection(dbURL, connectionProps);

        String insertQuery = "INSERT INTO labyrinthhighscores (TIMESTAMP, NAME, LEVEL, TIME) VALUES (?, ?, ?, ?)";
        insertStatement = connection.prepareStatement(insertQuery);
        String deleteQuery = "DELETE FROM labyrinthhighscores WHERE NAME=? AND TIME=?";
        deleteStatement = connection.prepareStatement(deleteQuery);
    }

    /**
     * Az adatbázis lekérdezése.
     */
    public ArrayList<HighScore> getHighScores() throws SQLException {
        String query = "SELECT * FROM labyrinthhighscores";
        ArrayList<HighScore> highScores = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);
        while (results.next()) {
            String name = results.getString("NAME");
            int level = results.getInt("LEVEL");
            long time = results.getInt("TIME");
            highScores.add(new HighScore(name, level, time));
        }
        sortHighScores(highScores);
        return highScores;
    }

    /**
     * Az adatbázisba való beszúrás.
     */
    public void putHighScore(String name, int level, long time) throws SQLException {
        ArrayList<HighScore> highScores = getHighScores();
        if (highScores.size() < maxScores) {
            insertScore(name, level, time);
        } else {
            HighScore leastScore = highScores.get(highScores.size() - 1);
            if (leastScore.getTime() > time) {
                deleteScores(highScores.get(highScores.size() - 1).getName(), highScores.get(highScores.size() - 1).getTime());
                insertScore(name, level, time);
            }

        }
    }

    /**
     * Rendezi az adatokat csökkenő sorrendbe.
     *
     * @param highScores
     */
    private void sortHighScores(ArrayList<HighScore> highScores) {
        Collections.sort(highScores, new Comparator<HighScore>() {
            @Override
            public int compare(HighScore t, HighScore t1) {
                if (t1.getLevel() == 1 && t.getLevel() == 1) {
                    return Long.compare(t1.getTime(), t.getTime());
                } else if (t1.getLevel() - t.getLevel() == 0) {
                    return Long.compare(t.getTime(), t1.getTime());
                } else {
                    return t1.getLevel() - t.getLevel();
                }
            }
        }
        );
    }

    /**
     * Az adatbázisba való beszúrás és tranzakciót végrehajtja.
     * @param name név
     * @param level elért szint
     * @param time játékban eltelt idő
     */
    private void insertScore(String name, int level, long time) throws SQLException {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        insertStatement.setTimestamp(1, ts);
        insertStatement.setString(2, name);
        insertStatement.setInt(3, level);
        insertStatement.setDouble(4, time);
        insertStatement.executeUpdate();
    }

    /**
     * Kitöröl a paraméterül átadott rekordot.
     *
     * @param name név
     * @param time idő
     */
    private void deleteScores(String name, long time) throws SQLException {
        deleteStatement.setString(1, name);
        deleteStatement.setDouble(2, time);
        deleteStatement.executeUpdate();
    }

    @Override
    public String toString() {
        String print = "";
        try {
            ArrayList<HighScore> highScores = getHighScores();
            for (int i = 0; i < highScores.size(); i++) {
                print += (String.valueOf(i + 1) + ". " + highScores.get(i));
            }
        } catch (SQLException e) {
            System.out.println("Exception!");
            Thread.currentThread().interrupt();
            e.printStackTrace();

        }
        return print.equals("") ? "There are no players in the database!" : print;
    }
}
