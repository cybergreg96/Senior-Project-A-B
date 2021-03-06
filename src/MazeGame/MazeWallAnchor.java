package MazeGame;
/*
 * This class references the project at this link: https://github.com/ThriftyNick/maze_generator
 */

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MazeWallAnchor 
{
    public static final int SIZE = 10;
    private int x, y;
    private int row, col;
    private Rectangle2D boundingRect;
    
    public MazeWallAnchor(int x, int y, int row, int col) 
    {
        this.x = x;
        this.y = y;
        this.row = row;
        this.col = col;
        boundingRect = new Rectangle2D(x, y, SIZE, SIZE);
    }
    
    public void render(GraphicsContext gc)
    {
        gc.setStroke(Color.HOTPINK);
        gc.setLineWidth(1);
        gc.strokeRect(x, y, SIZE, SIZE);
    }
    
    public Rectangle2D getBoundingRect() 
    {
        return boundingRect;
    }
    
    public int getX() 
    {
        return x;
    }
    
    public int getY() 
    {
        return y;
    }
    
    public int getRow() 
    {
        return row;
    }
    
    public int getCol() 
    {
        return col;
    }
}
