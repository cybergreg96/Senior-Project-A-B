package BilliardGame;

import javafx.geometry.Point2D;
/*
 * references: https://github.com/nhooyr/java-tanktank
 */
import javafx.scene.paint.Color;

import java.util.ArrayList;

// Cell represents a cell within the Maze.
class Cell 
{
	static final double LENGTH = 3.5 * Bunny.BODY_HEIGHT;
	private static final Color COLOR = Color.BLACK;

	private final int row;
	private final int column;
	private final ArrayList<MutableBoolean> yummySegments = new ArrayList<>();
	private final double x;
	private final double y;

	// True means the segment is opaque.
	private final MutableBoolean up;
	private final MutableBoolean left;
	private final MutableBoolean right = new MutableBoolean();
	private final MutableBoolean down = new MutableBoolean();

	// tank cell constructor. used to create a 15x 15 square grid where each
	// box/cell is initially given 4 walls. TankTank object may be placed in a
	// cell and walls are added to and eaten off to create a maze.
	Cell(final int column, final int row, final MutableBoolean up, final MutableBoolean left)
	{
		this.column = column;
		this.row = row;
		this.x = column * Cell.LENGTH;
		this.y = row * Cell.LENGTH;

		this.up = up;
		this.left = left;

		setYummySides();
	}

	// sets sides of a tank cell to yummy and allows them to be eaten off of the
	// tank cell. this will help create the maze from the 15 x 15 grid
	private void setYummySides() {
		// If up is true and this cell is not at the top row then it is yummy.
		if (up.value && (this.getColumn() == 7 && this.getRow() == 7)) 
		{

		}
		else 
		{
			if (up.value && row != 0)
			{
				yummySegments.add(up);
			}

			// If right is true and this cell is not at the right edge then it
			// is yummy.
			if (right.value && column != Maze.COLUMNS - 1) 
			{
				yummySegments.add(right);
			}

			// If down is true and this cell is not at the bottom row then it is
			// yummy.
			if (down.value && row != Maze.ROWS - 1) 
			{
				yummySegments.add(down);
			}

			// If left is true and this cell is not at the left edge then it is
			// yummy.
			if (left.value && column != 0) 
			{
				yummySegments.add(left);
			}
		}
	}

	// returns row of a tank cell object
	int getRow() 
	{
		return row;
	}

	// returns column of a tank cell object
	int getColumn() 
	{
		return column;
	}

	// returns true if a tank cell's top wall is drawn or placed on grid
	MutableBoolean getUp()
	{
		return up;
	}

	// returns true if a tank cell's left wall is drawn or placed on grid
	MutableBoolean getLeft() 
	{
		return left;
	}

	// returns true if a tank cell's right wall is drawn or placed on grid
	MutableBoolean getRight() 
	{
		return right;
	}

	// returns true if a tank cell's bottom wall is drawn or placed on grid
	MutableBoolean getDown() 
	{
		return down;
	}

	// returns an arraylist of all yummy segments of the 15 x 15 grid that will
	// be eaten off.
	ArrayList<MutableBoolean> getYummySegments() 
	{
		return yummySegments;
	}

	// isYummy tells the maze generation algorithm whether this cell can have
	// more segments removed, or "eaten".
	boolean isYummy()
	{
		int yummyThreshold = 2;
		if (column == 0 || row == 0 || column == Maze.COLUMNS - 1 || row == Maze.ROWS - 1) 
		{
			// We make bordering cells more yummy to ensure we do not any
			// encircled areas and to allow for a open outer
			// area. An interesting map element.
			// No proof that this ensures no encircling areas but it is my
			// intuition and seems to work in practice.
			yummyThreshold = 1;
		}
		return yummySegments.size() > yummyThreshold;
	}

	// to eat walls off, the grid starts at top left corner and works its way
	// down each column and row eating marked yummy segments by only taking
	// cells top or left segments as long as it is not a cell on left edge of
	// maze

	// gets the up segment of a tank cell
	PlayerRectangle getUpSeg() 
	{
		return getSeg(this.up, x, y, Cell.LENGTH, Maze.THICKNESS);
	}

	// gets the left segment of a tank cell
	PlayerRectangle getLeftSeg() 
	{
		return getSeg(this.left, x, y, Maze.THICKNESS, Cell.LENGTH);
	}
	
	PlayerRectangle getDiagonalSeg() 
	{
		return getDiagonal(this.left, x, y, Maze.THICKNESS, Cell.LENGTH);
	}

	// We add maze thickness to the lengths in the down segment and right
	// segment to prevent gaping squares from appearing
	// where an invisible up segment or left segment would be.
	PlayerRectangle getDownSeg() 
	{
		return getSeg(this.down, x, y + Cell.LENGTH, Cell.LENGTH + Maze.THICKNESS, Maze.THICKNESS);
	}

	// We add maze thickness to the lengths in the down segment and right
	// segment to prevent gaping squares from appearing
	// where an invisible down segment or right segment would be.
	PlayerRectangle getRightSeg() 
	{
		return getSeg(this.right, x + Cell.LENGTH, y, Maze.THICKNESS, Cell.LENGTH + Maze.THICKNESS);
	}

	// places maze segments down based on whether there visibility value is to
	// true. This is where each walls color is set and where there x and y
	// coordinates are set where they will be placed
	private PlayerRectangle getSeg(final MutableBoolean visibility, final double x, final double y, final double width, final double height) 
	{
		PlayerRectangle rect = null;
		if (visibility.value) 
		{
			rect = new PlayerRectangle(x, y, width, height);
			rect.getPolygon().setFill(COLOR);
		}
		return rect;
	}
	
	private PlayerRectangle getDiagonal(final MutableBoolean visibility, final double x, final double y, final double width, final double height) 
	{
		PlayerRectangle rect = null;
		if (visibility.value) 
		{
			rect = new PlayerRectangle(x + (width/2), y + (height/2), width, height);
			
			int theta = 0;
			if(Math.random() < .5)
			{
				theta = 45;
			}
			else
			{
				theta = -45;
			}
			rect.rotate(new Point2D(x,y), theta);
			rect.getPolygon().setFill(COLOR);
			
			rect.angle = theta;
		}
		return rect;
	}
	

	static class MutableBoolean 
	{

		boolean value;

		MutableBoolean() 
		{
			this.value = true;
		}
	}
}
