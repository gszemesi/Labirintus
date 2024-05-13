
import java.awt.Image;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Sötétség osztály
 *
 * @author Szemesi Gábor
 */
public class Dark extends Sprite {

    public Dark(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }

    /**
     * A sötétség mozgatása a játékossal együtt.
     */
    public void move(int x_cor, int y_cor) {
        x = x_cor - 1220;
        y = y_cor - 1360;
    }
}
