
import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Játék szintjének pályájának megalkotása.
 *
 * @author Szemesi Gábor
 */
public class Level {

    private final int BRICK_WIDTH_HEIGHT = 40;
    private ArrayList<Wall> walls;

    public Level(String levelPath) throws IOException {
        loadLevel(levelPath);
    }

    /**
     * A játék szintjének megalkotása. A falak létre hozása.
     *
     * @param levelPath A szint fájllának útvonala.
     */
    public void loadLevel(String levelPath) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(levelPath));
        walls = new ArrayList<>();
        int y = 0;
        String line;
        int chance = randomNumber(0, 100);
        Image image;
        if (chance < 90) {
            image = new ImageIcon("data/images/wall0.png").getImage();
        } else {
            image = new ImageIcon("data/images/wall1.png").getImage();
        }
        while ((line = br.readLine()) != null) {
            int x = 0;
            for (char blockType : line.toCharArray()) {
                if (Character.isDigit(blockType)) {
                    walls.add(new Wall(x * BRICK_WIDTH_HEIGHT, y * BRICK_WIDTH_HEIGHT, BRICK_WIDTH_HEIGHT, BRICK_WIDTH_HEIGHT, image));
                }
                x++;
            }
            y++;
        }
    }

    /**
     * A szint elemei(falak) ütköznek-e a paraméterül átadott objektummal(Sprite).
     *
     * @param obj Egy Sprite.
     * @return Logikai érték, higy ütközik-e vagy nem a megadott objektummal.
     */
    public boolean collides(Sprite obj) {
        Wall collidedWith = null;
        for (Wall wall : walls) {
            if (obj.collides(wall)) {
                collidedWith = wall;
                break;
            }
        }
        if (collidedWith != null) {
            return true;
        } else {
            return false;
        }
    }

    public void draw(Graphics g) {
        for (Wall wall : walls) {
            wall.draw(g);
        }
    }

    private int randomNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }
}
