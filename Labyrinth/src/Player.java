
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Játékos osztály
 *
 * @author Szemesi Gábor
 */
public class Player extends Sprite {

    private double velx;
    private double vely;

    public Player(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }

    /**
     * A sárkány mozgatása a képernyőn beül.
     */
    public void move() {
        if ((velx < 0 && x > 0) || (velx > 0 && x + width <= 1280)) {
            x += velx;

        }
        if ((vely < 0 && y > 0) || (vely > 0 && y + height <= 640)) {
            y += vely;
        }

    }

    public double getVelx() {
        return velx;
    }

    public void setVelx(double velx) {
        this.velx = velx;
    }

    public double getVely() {
        return vely;
    }

    public void setVely(double vely) {
        this.vely = vely;
    }
}
