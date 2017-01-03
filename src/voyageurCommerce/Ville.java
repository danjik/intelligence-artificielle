package voyageurCommerce;

import javafx.scene.paint.Color;

/**
 * Created by Nicolas on 24/02/2016.
 */
public class Ville {
    private int x;
    private int y;
    private Color color;

    public Ville() {
        x = 0;
        y = 0;
        color =new Color(Math.random(), Math.random(), Math.random(), 1);
    }
    public Color getColor(){
    	return this.color;
    }

    public Ville(int posX, int posY) {
        x = posX;
        y = posY;
        color =new Color(Math.random(), Math.random(), Math.random(), 1);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
