package MazeGame;
/*
 * This class references the project at this link: https://github.com/ThriftyNick/maze_generator
 */

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MazePlayer 
{
    public static final double RADIUS = 7D;
    private double x, y;
    private int row, col;
    
    public void render(GraphicsContext gc) 
    {
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.WHITE);
        gc.fillOval(x, y, RADIUS, RADIUS);
    }
    
    public void setPosition(double x, double y) 
    {
        this.x = x - 3;
        this.y = y - 3;
    }
    
    public Point2D getPosition()
    {
        return new Point2D(this.x, this.y);
    }
    
    public void setGraphLoc(int row, int col)
    {
    	this.row = row;
    	this.col = col;
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
