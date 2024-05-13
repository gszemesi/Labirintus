
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Az adatbázisba való beszúrást és lekérdezést kezeli.
 *
 * @author Szemesi Gábor
 */
public class Database {

    private JFrame frame;

    /**
     * Az adatbázisba beszúrja a rekodot.
     *
     * @param levelNum Játék szintjének száma.
     * @param time A játékban eltöltött idő.
     */
    public void insertDatabase(int levelNum, long time) {

        try {
            Thread.sleep(1500);
            frame = new JFrame();
            String name = JOptionPane.showInputDialog(frame, "Enter your name:");
            HighScores highScores = new HighScores(10);
            if (name != null && !name.equals("")) {
                highScores.putHighScore(name, levelNum, time);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GameEngine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.out.println("Exception!");
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    /**
     * Az adatbázist adja vissza.
     *
     * @param gameArea GameEngine JFrame
     */
    public void printDatabase(GameEngine gameArea) {
        try {
            HighScores highScores = new HighScores(10);
            System.out.println();
            JOptionPane.showMessageDialog(gameArea, highScores, "Results!", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(GameEngine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
}
