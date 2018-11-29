package BilliardGame;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.paint.Color;


import BilliardGame.TankRectangle;
import BilliardGame.TankCell;


class Maze {

	private static final Color COLOR = Color.BLACK;
	static final int ROWS = 15;
	static final int COLUMNS = 15;
	static final double THICKNESS = 3*1.5;
	private final Group group = new Group();
	private final TankRectangle[][] horizontalSegments = new TankRectangle[COLUMNS][ROWS + 1];
	private final TankRectangle[][] verticalSegments = new TankRectangle[COLUMNS + 1][ROWS];
	
	Maze(){
		drawGrid();
	}
	private void drawGrid() {
		
		for (int i = 0; i < COLUMNS; i++) 
		{
			for (int j = 0; j < ROWS; j++) 
			{
				TankRectangle rect = new TankRectangle(50*i, 75*j, 40, 5);
				rect.getPolygon().setFill(COLOR);
				if(Math.random() < 0.5) {
					if(Math.random() < 0.5) {
						rect.rotate(new Point2D(100*i, 75*j), -200);
					}else {
						rect.rotate(new Point2D(100*i, 75*j), 200);
					}
					
				}
				if(Math.random() < 0.5) {
					verticalSegments[i + 1][j] = rect;
					
				}
				
				
				
				
				
				
				
			}
		}
		
		for (final TankRectangle[] segs : horizontalSegments)
		{
			for (final TankRectangle seg : segs) {
				addSeg(seg);
			}
		}
		for (final TankRectangle[] segs : verticalSegments) 
		{
			for (final TankRectangle seg : segs) 
			{
				addSeg(seg);
			}
		}
			
	}
	ArrayList<TankRectangle> getCollisionCandidates(final Point2D objCenter) 
	{
		// Coordinates if the units were cells.
		final double cellX = objCenter.getX() / TankCell.LENGTH;
		final double cellY = objCenter.getY() / TankCell.LENGTH;

		// Closest column.
		int column = (int) Math.round(cellX);
		// Closest row.
		int row = (int) Math.round(cellY);

		final ArrayList<TankRectangle> segs = new ArrayList<>(2);

		if (column < COLUMNS) {
			final TankRectangle seg = horizontalSegments[column][row];
			if (seg != null) {
				segs.add(seg);
			}
		}

		//checks horizontal segment in the previous column.
		column--;
		if (column >= 0) 
		{
			final TankRectangle seg = horizontalSegments[column][row];
			if (seg != null) 
			{
				segs.add(seg);
			}
		}
		column++;

		if (row < ROWS) 
		{
			final TankRectangle seg = verticalSegments[column][row];
			if (seg != null) 
			{
				segs.add(seg);
			}
		}

		// checks the vertical segment in the previous row.
		row--;
		if (row >= 0) 
		{
			final TankRectangle seg = verticalSegments[column][row];
			if (seg != null) 
			{
				segs.add(seg);
			}
		}
		row++;

		return segs;
	}
	
	Node getNode() 
	{
		return group;
	}

	private void addSeg(final TankRectangle seg) 
	{
		if (seg != null)
		{
			final Polygon poly = seg.getPolygon();
			group.getChildren().add(poly);
		}
	}
	
}
