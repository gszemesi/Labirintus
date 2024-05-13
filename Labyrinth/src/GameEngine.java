
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * A játék futtatásáért és írányításért felelős osztály. Az osztály jeleníti meg
 * az össze objektumot és kezeli az írányítást (action listener).
 *
 * @author Szemesi Gábor
 */
public class GameEngine extends JPanel {

    private final int FPS = 240;
    private final int PLAYER_X = 20;
    private final int PLAYER_Y = 590;
    private final int PLAYER_WIDTH = 40;
    private final int PLAYER_HEIGHT = 40;
    private final int STARTPORTAL_X = 5;
    private final int STARTPORTAL_Y = 575;
    private final int ENDPORTAL_X = 1200;
    private final int ENDPORTAL_Y = 5;
    private final int PORTAL_WIDTH = 70;
    private final int PORTAL_HEIGHT = 70;
    private int DRAGON_X;
    private int DRAGON_Y;
    private final int DRAGON_WIDTH = 60;
    private final int DRAGON_HEIGHT = 60;
    private final int MOVEMENT = 4;

    private Image background;
    private Player player;
    private Dragon dragon;
    private Level level;
    private Portal startPortal;
    private Portal endPortal;
    private Dark dark;
    private Image playerImage;
    private Image dragonImage;
    private Image portalImage;
    private Image darkImage;

    private boolean paused = false;
    private int levelNum = 0;
    private final int maxLevel = 10;

    private Timer newFrameTimer;
    private JLabel timeLabel;
    private Timer timer;
    private long startTime;
    private long pausedTime;
    private long time;

    private String direction;
    private int just_once = 1;

    private Database database;

    /**
     * WASD billentyűk action listener és FrameTime Listeren meghívása. Továbbá
     * az óra elindítása és helyes megjelenítése.
     *
     */
    public GameEngine() {
        super();
        background = new ImageIcon("data/images/Dirt_background.png").getImage();
        database = new Database();

        this.getInputMap().put(KeyStroke.getKeyStroke("W"), "pressed up");
        this.getActionMap().put("pressed up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!paused) {
                    player.setVely(-MOVEMENT);
                    player.move();
                    if (level.collides(player)) {
                        player.setVely(MOVEMENT);
                        player.move();
                    }
                    player.setVelx(0);
                    player.setVely(0);
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("A"), "pressed left");
        this.getActionMap().put("pressed left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!paused) {
                    player.setVelx(-MOVEMENT);
                    player.move();
                    if (level.collides(player)) {

                        player.setVelx(MOVEMENT);
                        player.move();
                    }
                    player.setVelx(0);
                    player.setVely(0);
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("D"), "pressed right");
        this.getActionMap().put("pressed right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!paused) {
                    player.setVelx(MOVEMENT);
                    player.move();
                    if (level.collides(player)) {

                        player.setVelx(-MOVEMENT);
                        player.move();
                    }
                    player.setVelx(0);
                    player.setVely(0);
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("S"), "pressed down");
        this.getActionMap().put("pressed down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!paused) {
                    player.setVely(MOVEMENT);
                    player.move();
                    if (level.collides(player)) {

                        player.setVely(-MOVEMENT);
                        player.move();
                    }
                    player.setVelx(0);
                    player.setVely(0);
                }
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        this.getActionMap().put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                paused = !paused;
                if (timer.isRunning()) {
                    timer.stop();
                    pausedTime = System.currentTimeMillis();
                    timeLabel.setText("Paused(ESC)   -   " + elapsedTime() / 1000 / 60 + ":" + elapsedTime() / 1000 % 60 + " s" + "       " + (levelNum + 1) + ". level ");
                    timeLabel.setForeground(Color.red);
                } else {
                    startTime = startTime + (System.currentTimeMillis() - pausedTime);
                    timeLabel.setForeground(Color.black);
                    timer.start();
                }
            }
        });

        restart();
        newFrameTimer = new Timer(1000 / FPS, new NewFrameListener());
        newFrameTimer.start();

        timeLabel = new JLabel(" ");
        timeLabel.setHorizontalAlignment(JLabel.RIGHT);
        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLabel.setText(elapsedTime() / 1000 / 60 + ":" + elapsedTime() / 1000 % 60 + " s" + "       " + (levelNum < 10 ? levelNum + 1 : levelNum) + ". level ");
            }
        });
        startTime = System.currentTimeMillis();
        timer.start();
        time = 0;
    }

    /**
     * Játék újraindítása. Összes objektum újra alkotása.
     *
     */
    public void restart() {

        try {
            level = new Level("data/levels/level0" + levelNum + ".txt");
        } catch (IOException ex) {
            Logger.getLogger(GameEngine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        playerImage = new ImageIcon("data/images/knight4.png").getImage();
        player = new Player(PLAYER_X, PLAYER_Y, PLAYER_WIDTH, PLAYER_HEIGHT, playerImage);
        do {
            DRAGON_X = randomNumber(0, 1220);
            DRAGON_Y = randomNumber(0, 580);
            if ((levelNum + 1) == maxLevel) {
                dragonImage = new ImageIcon("data/images/alduin.png").getImage();
                dragon = new Dragon(DRAGON_X, DRAGON_Y, DRAGON_WIDTH * 4, DRAGON_HEIGHT * 4, dragonImage);
            } else {
                dragonImage = new ImageIcon("data/images/dragon.png").getImage();
                dragon = new Dragon(DRAGON_X, DRAGON_Y, DRAGON_WIDTH, DRAGON_HEIGHT, dragonImage);
            }
        } while ((level.collides(dragon) || player.collides(dragon)) || !(DRAGON_X > 100 && DRAGON_Y < 400));
        portalImage = new ImageIcon("data/images/portal.png").getImage();
        startPortal = new Portal(STARTPORTAL_X, STARTPORTAL_Y, PORTAL_WIDTH, PORTAL_HEIGHT, portalImage);
        //endPortal = new Portal(100, 575, PORTAL_WIDTH, PORTAL_HEIGHT, portalImage);
        if ((levelNum + 1) == maxLevel) {
            portalImage = new ImageIcon("data/images/gem.png").getImage();
            endPortal = new Portal(100, 575, PORTAL_WIDTH / 2, PORTAL_HEIGHT / 2, portalImage);
        }
        endPortal = new Portal(ENDPORTAL_X, ENDPORTAL_Y, PORTAL_WIDTH, PORTAL_HEIGHT, portalImage);
        darkImage = new ImageIcon("data/images/dark.png").getImage();
        dark = new Dark(0, -0, 1280 * 2, 1280 * 2, darkImage);

    }

    /**
     * Az össze objektum megrajzolása.
     *
     * @param grphcs A komponens objektum, amelyre rajzolni kell.
     */
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(background, 0, 0, 1280, 640, null);
        level.draw(grphcs);
        startPortal.draw(grphcs);
        endPortal.draw(grphcs);
        dragon.draw(grphcs);
        player.draw(grphcs);
        dark.draw(grphcs);
    }

    class NewFrameListener implements ActionListener {

        /**
         * Képernyő frissítése. Képernyő frissitése a játéktól függöen.
         *
         * @param ae Akció esemény.
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!paused && just_once == 1) {
                if (player.collides(dragon)) {
                    //It is not necessary to set it to 0.
                    dragon.setVelx(0);
                    dragon.setVely(0);
                    timer.stop();
                    if (time == 0) {
                        time = elapsedTime();
                        timeLabel.setText("You died!   -   " + elapsedTime() / 1000 / 60 + ":" + elapsedTime() / 1000 % 60 + " s" + "       " + (levelNum + 1) + ". level ");
                        timeLabel.setForeground(Color.red);
                        database.insertDatabase(levelNum + 1, time);
                    }
                } else if (levelNum != maxLevel) {
                    dragon.move();
                    while (level.collides(dragon)) {
                        if (randomNumber(1, 2) == 1) {
                            dragon.invertVelX();
                        } else {
                            dragon.invertVelY();
                        }
                        dragon.move();
                    }
                    dark.move(player.getX(), player.getY());
                    if (player.collides(endPortal) && just_once == 1 && levelNum != maxLevel) {
                        cinematic();
                    }
                    repaint();
                } else {
                    dragon.setVelx(0);
                    dragon.setVely(0);
                    timer.stop();
                    if (time == 0) {
                        time = elapsedTime();
                        timeLabel.setText("You win!   -   " + elapsedTime() / 1000 / 60 + ":" + elapsedTime() / 1000 % 60 + " s" + "       " + levelNum + ". level ");
                        timeLabel.setForeground(Color.green);
                        database.insertDatabase(levelNum, time);
                    }
                }
            }
        }

    }

    public JLabel getTimeLabel() {
        return timeLabel;
    }

    /**
     * A játékban eltet időt adja vissza.
     *
     * @return A játékban eltet időt adja vissza miliszekundumban.
     */
    public long elapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    public int randomNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    /**
     * Új játék indítása. Új játékot indít a 0-ás szintről, kiszedi a
     * megállítást és írja a startTime-t.
     */
    public void newGame() {
        levelNum = 0;
        paused = false;
        startTime = System.currentTimeMillis();
        timeLabel.setForeground(Color.black);
        timer.start();
        time = 0;
        restart();
    }

    /**
     * A játék megállítása.
     */
    public void pauseGame() {
        paused = true;
        if (timer.isRunning()) {
            pausedTime = System.currentTimeMillis();
            timer.stop();
            timeLabel.setText("Paused(ESC)   -   " + elapsedTime() / 1000 / 60 + ":" + elapsedTime() / 1000 % 60 + " s" + "       " + (levelNum + 1) + ". level ");
            timeLabel.setForeground(Color.red);
        }
    }

    /**
     * Portal videó.
     *
     * Ha a játékos eléri a portált, akkor abba belemegy, majd 1 másodperc múlva új játék indul.
     */
    public void cinematic() {
        if (just_once == 1) {
            just_once++;
            ExecutorService ex = Executors.newFixedThreadPool(2);
            for (int i = 0; i < 2; i++) {
                final int final_i = i;
                ex.submit(() -> {
                    try {
                        if (final_i == 0) {
                            if (levelNum <= maxLevel) {
                                levelNum++;
                            }
                            player.setX(endPortal.getX());
                            player.setY(endPortal.getY() + 30);
                            player.move();
                            repaint();
                            Thread.sleep(500);
                            player.setX(endPortal.getX() + 15);
                            player.setY(endPortal.getY() + 15);
                            player.move();
                            repaint();
                        } else {
                            Thread.sleep(1000);
                            if (levelNum < maxLevel) {
                                restart();
                            }
                            just_once = 1;
                        }

                    } catch (Exception e) {
                        System.out.println("Exception!");
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                }
                );
            }
        }
    }
}
