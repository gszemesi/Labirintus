
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * LabyrinthGame ablakának létrehozása.
 * A program menüje:új játék kezdése, ranglista megnézése, kilépés menüpont.
 *
 * @author Szemesi Gábor
 */
public class LabyrinthGUI {

    private JFrame frame;
    private GameEngine gameArea;
    Database database;
    
    /**
     * Menü és játékablak létrehozása.
     */
    public LabyrinthGUI() {
        frame = new JFrame("Labyrinth");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        database=new Database();

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);

        JMenuItem NewGameMenuItem = new JMenuItem("New game");
        gameMenu.add(NewGameMenuItem);
        NewGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                gameArea.newGame();
                
            }
        });

        JMenuItem ScoresMenuItem = new JMenuItem("Scores");
        gameMenu.add(ScoresMenuItem);
        ScoresMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                gameArea.pauseGame();
                database.printDatabase(gameArea);
                
            }
        });

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        gameMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });

        gameArea = new GameEngine();
        frame.getContentPane().add(gameArea);

        frame.getContentPane().add(gameArea.getTimeLabel(), BorderLayout.NORTH);

        frame.setPreferredSize(new Dimension(1295, 715));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}
