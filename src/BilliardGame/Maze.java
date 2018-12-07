package BilliardGame;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;
import java.util.ArrayList;
import java.util.Random;

/*
 * references: https://github.com/nhooyr/java-tanktank
 */

// Maze represents the tank maze.
class Maze 
{
	// These three are used in various places.
	// This is twice the bullet velocity to prevent the bullet from moving
	// through any of the walls without a collision being detected.
	// Given the bullet velocity itself is defined to be greater than the tank
	// velocity, this also ensures that the tank does not
	// punch through any walls without the collision being detected.
	static final double THICKNESS = FireBall.VELOCITY;
	static final int ROWS = 15;
	static final int COLUMNS = 15;

	private final Group group = new Group();
	private final PlayerRectangle[][] horizontalSegments = new PlayerRectangle[COLUMNS][ROWS + 1];
	private final PlayerRectangle[][] verticalSegments = new PlayerRectangle[COLUMNS + 1][ROWS];
	private final PlayerRectangle[][] diagonalSegments = new PlayerRectangle[COLUMNS + 1][ROWS + 1]; //TODO remove?
	private final Cell[][] grid = new Cell[COLUMNS][ROWS];

	Maze() 
	{
		makeGrid();
		eatGrid();
		drawGrid();
	}

	Node getNode() 
	{
		return group;
	}

	private void makeGrid() 
	{
		for (int i = 0; i < COLUMNS; i++) 
		{
			for (int j = 0; j < ROWS; j++) 
			{
				Cell.MutableBoolean left = new Cell.MutableBoolean();
				// If we are not in the first column, then the left of the
				// current cell is the right of the one left.
				if (i > 0) 
				{
					left = grid[i - 1][j].getRight();
				}

				Cell.MutableBoolean up = new Cell.MutableBoolean();
				// If we are not in the first row, then the up of the current
				// cell is the down of the one above.
				if (j > 0) 
				{
					up = grid[i][j - 1].getDown();
				}
				grid[i][j] = new Cell(i, j, up, left);
			}
		}
	}

	private ArrayList<Cell> getYummyCells() 
	{
		final ArrayList<Cell> yummyCells = new ArrayList<>();
		for (final Cell[] cells : grid) 
		{
			for (final Cell tankCell : cells) 
			{
				if (tankCell.isYummy()) 
				{
					yummyCells.add(tankCell);
				}
			}
		}
		return yummyCells;
	}

	// The way we generate the maze is by eating the grid. We select a random
	// cell that is "yummy", meaning it has enough
	// removable segments. We remove one of its segments randomly and then based
	// on which segment we removed, we move into the adjacent
	// cell and repeat. Once the cell we move into is not yummy, we select
	// another random cell from the rest of the grid that is
	// yummy and then repeat.
	// A cell is yummy if it has more than two eatable segments. If the cell
	// lies on the outer ring of cells, then it is yummy
	// if it has more than one eatable segment. See isYummy() on the Cell class.
	// Why all of this? I am not sure if any of it is meaningful. It was just
	// intuition and I like the mazes it generates.
	// The mazes are very open and allow for diverse strategy.
	private void eatGrid() 
	{
		final Random rand = new Random();
		ArrayList<Cell> yummyCells = getYummyCells();
		Cell tankCell = yummyCells.get(rand.nextInt(yummyCells.size()));
		while (true) 
		{
			
			final int i = rand.nextInt(tankCell.getYummySegments().size());
			final Cell.MutableBoolean seg = tankCell.getYummySegments().get(i);

			seg.value = false;
			tankCell.getYummySegments().remove(i);

			if (tankCell.getUp() == seg)
			{
				tankCell = grid[tankCell.getColumn()][tankCell.getRow() - 1];
			} else if (tankCell.getRight() == seg) 
			{
				tankCell = grid[tankCell.getColumn() + 1][tankCell.getRow()];
			} else if (tankCell.getDown() == seg) 
			{
				tankCell = grid[tankCell.getColumn()][tankCell.getRow() + 1];
			} else if (tankCell.getLeft() == seg) 
			{
				tankCell = grid[tankCell.getColumn() - 1][tankCell.getRow()];
			}
			
			if (!tankCell.isYummy()) 
			{
				yummyCells = getYummyCells();
				if (yummyCells.size() == 0) 
				{
					return;
				}
				tankCell = yummyCells.get(rand.nextInt(yummyCells.size()));
			}
		}
	}

	private void drawGrid() 
	{
		for (int i = 0; i < COLUMNS; i++) 
		{
			for (int j = 0; j < ROWS; j++) 
			{
				final Cell tankCell = grid[i][j];

				if (((j == 0 || j == 1 || j == 2) && (i == 0))) 
				{
					
				}
				else if(((j == ROWS-1 || j == ROWS-2 || j == ROWS-3) && (i == COLUMNS-1))){
					
				}
				else if((j == 7 || j ==8|| j ==6 || j==9 ||j==5) && (i ==7 || i==8 || i ==9 || i==5 || i==6 || i==4 || i==10)){
					
				}
				
				 
				if(j==5 && (i ==7 || i==8 || i ==9 || i==5 || i==6 || i==4 || i==10))
				{
					final PlayerRectangle upSeg = tankCell.getUpSeg();
					horizontalSegments[i][j] = upSeg;
				}
				
				if(j==9 && (i ==7 || i==8 || i ==9 || i==5 || i==6 || i==4 || i==10))
				{
					final PlayerRectangle downSeg = tankCell.getDownSeg();
					horizontalSegments[i][j + 1] = downSeg;
				}
				
				if(i==4 && (j == 7 || j ==8|| j ==6 || j==9 ||j==5))
				{
					final PlayerRectangle leftSeg = tankCell.getLeftSeg();
					verticalSegments[i][j] = leftSeg;
				}
				
				if(i==10 && (j == 7 || j ==8|| j ==6 || j==9 ||j==5))
				{
					final PlayerRectangle rightSeg = tankCell.getRightSeg();
					verticalSegments[i + 1][j] = rightSeg;
				}
				
				// We are in the first column and so we need to grab the left
				// seg too.
				if (i == 0) 
				{
					
				}

				// We are in the first row and so we need to grab the up seg
				// too.
				if (j == 0) 
				{
					
				}
				
				if (i == COLUMNS-1) 
				{
					
				}

				// We are in the first row and so we need to grab the up seg
				// too.
				if (j == ROWS-1) 
				{
					
				}
				
				else if (i!=14)
				{
					if(Math.random() < 0.4) {
					final PlayerRectangle diagonalSeg = tankCell.getDiagonalSeg();
					diagonalSegments[i + 1][j] = diagonalSeg;
					}else {
						final PlayerRectangle rightSeg = tankCell.getRightSeg();
						verticalSegments[i + 1][j] = rightSeg;
					}
				
					final PlayerRectangle downSeg = tankCell.getDownSeg();
					horizontalSegments[i][j + 1] = downSeg;
				}
			}
		}
		
		for (final PlayerRectangle[] segs : diagonalSegments)
		{
			for (final PlayerRectangle seg : segs) {
				addSeg(seg);
			}
		}

		for (final PlayerRectangle[] segs : horizontalSegments)
		{
			for (final PlayerRectangle seg : segs) {
				addSeg(seg);
				
				if(seg != null)
				{
					seg.angle = 0;
				}
			}
		}

		for (final PlayerRectangle[] segs : verticalSegments) 
		{
			for (PlayerRectangle seg : segs) 
			{
				addSeg(seg);
				
				if(seg != null)
				{
					seg.angle = 90;
				}
			}
		}
		
		System.out.println();
	}

	private void addSeg(final PlayerRectangle seg) 
	{
		if (seg != null)
		{
			final Polygon poly = seg.getPolygon();
			group.getChildren().add(poly);
		}
	}

	ArrayList<PlayerRectangle> getCollisionCandidates(final Point2D objCenter) 
	{
		// Coordinates if the units were cells.
		 double cellX = objCenter.getX() / Cell.LENGTH;
		 double cellY = objCenter.getY() / Cell.LENGTH;

		if (cellX < 0) {
			cellX = 0;
		}else if( cellX >= 15) {
			cellX = 15;
		}
		

		if (cellY < 0) {
			cellY = 0;
		}else if( cellY >= 15) {
			cellY = 15;
		}
		// Closest column.
		int column = (int) Math.round(cellX);
		// Closest row.
		int row = (int) Math.round(cellY);

		final ArrayList<PlayerRectangle> segs = new ArrayList<>(2);

		if (column < COLUMNS) {
			//check for horizontal segment
			final PlayerRectangle hSeg = horizontalSegments[column][row];
			if (hSeg != null) {
				segs.add(hSeg);
			}
			
			//check for diagonal segment
			final PlayerRectangle dSeg = diagonalSegments[column][row];
			if (dSeg != null) {
				segs.add(dSeg);
			}
		}

		//checks horizontal and diagonal segment in the previous column.
		column--;
		if (column >= 0) 
		{
			final PlayerRectangle hSeg = horizontalSegments[column][row];
			if (hSeg != null) 
			{
				segs.add(hSeg);
			}
		}
		column++;
		
		//checks horizontal and diagonal segment in the next column.
		column++;
		if (column < COLUMNS) 
		{
			final PlayerRectangle hSeg = horizontalSegments[column][row];
			if (hSeg != null) 
			{
				segs.add(hSeg);
			}
		}
		column--;

		if (row < ROWS) 
		{
			//check for vertical segment
			final PlayerRectangle vSeg = verticalSegments[column][row];
			if (vSeg != null) 
			{
				segs.add(vSeg);
			}
		}

		// checks the vertical segment in the previous row.
		row--;
		if (row >= 0) 
		{
			//check for vertical segment
			final PlayerRectangle vSeg = verticalSegments[column][row];
			if (vSeg != null) 
			{
				segs.add(vSeg);
			}
		}
		row++;
		
		// checks the vertical segment in the next row.
		row++;
		if (row < ROWS) 
		{
			//check for vertical segment
			final PlayerRectangle vSeg = verticalSegments[column][row];
			if (vSeg != null) 
			{
				segs.add(vSeg);
			}
		}
		row--;
		
		//making much wider sweep in cells for diagonals. We check in 3 cell radius
		for(int i = column - 3; i < column + 3; i++)
		{
			if(i >= 0 && i < COLUMNS)
			{
				for(int j = row - 3; j < row + 3; j++)
				{
					if(j >= 0 && j < ROWS)
					{
						//check for vertical segment
						final PlayerRectangle dSeg = diagonalSegments[i][j];
						if (dSeg != null) 
						{
							segs.add(dSeg);
						}
					}
				}
			}
		}

		return segs;
	}
}
