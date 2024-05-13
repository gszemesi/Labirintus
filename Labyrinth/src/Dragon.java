
import java.awt.Image;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Sárkány osztály
 *
 * @author Szemesi Gábor
 */
public class Dragon extends Sprite {

    private double velx;
    private double vely;

    public Dragon(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
        velx = 1;
        vely = 1;
    }

    /**
     * A sárkány mozgatása a képernyőn beül.
     */
    public void move() {
        if ((velx < 0 && x > 0) || (velx > 0 && x + width <= 1270)) {
            x += velx;
        } else {
            invertVelX();
        }
        if ((vely < 0 && y > 0) || (vely > 0 && y + height <= 640)) {
            y += vely;
        } else {
            invertVelY();
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

    public void invertVelX() {
        velx = -velx;
    }

    public void invertVelY() {
        vely = -vely;
    }
}
