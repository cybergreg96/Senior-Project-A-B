
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.Random;

// Maze represents the tank tank maze.
class Maze {
    // These three are used in various places.
    // This is twice the bullet velocity to prevent the bullet from moving through any of the walls without a collision being detected.
    // Given the bullet velocity itself is defined to be greater than the tank velocity, this also ensures that the tank does not
    // punch through any walls without the collision being detected.
    static final double THICKNESS = TankBullet.VELOCITY * 2;
    static final int ROWS = 7;
    static final int COLUMNS = 10;

    private final Group group = new Group();
    private final TankRectangle[][] horizontalSegments = new TankRectangle[COLUMNS][ROWS + 1];
    private final TankRectangle[][] verticalSegments = new TankRectangle[COLUMNS + 1][ROWS];
    private final TankCell[][] grid = new TankCell[COLUMNS][ROWS];

    Maze() {
        makeGrid();
        eatGrid();
        drawGrid();
    }

    Node getNode() {
        return group;
    }

    private void makeGrid() {
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS; j++) {
                TankCell.MutableBoolean left = new TankCell.MutableBoolean();
                // If we are not in the first column, then the left of the current cell is the right of the one left.
                if (i > 0) {
                    left = grid[i - 1][j].getRight();
                }

                TankCell.MutableBoolean up = new TankCell.MutableBoolean();
                // If we are not in the first row, then the up of the current cell is the down of the one above.
                if (j > 0) {
                    up = grid[i][j - 1].getDown();
                }

                grid[i][j] = new TankCell(i, j, up, left);
            }
        }
    }

    private ArrayList<TankCell> getYummyCells() {
        final ArrayList<TankCell> yummyCells = new ArrayList<>();
        for (final TankCell[] cells : grid) {
            for (final TankCell tankCell : cells) {
                if (tankCell.isYummy()) {
                    yummyCells.add(tankCell);
                }
            }
        }
        return yummyCells;
    }

    // The way we generate the maze is by eating the grid. We select a random cell that is "yummy", meaning it has enough
    // removable segments. We remove one of its segments randomly and then based on which segment we removed, we move into the adjacent
    // cell and repeat. Once the cell we move into is not yummy, we select another random cell from the rest of the grid that is
    // yummy and then repeat.
    // A cell is yummy if it has more than two eatable segments. If the cell lies on the outer ring of cells, then it is yummy
    // if it has more than one eatable segment. See isYummy() on the Cell class.
    // Why all of this? I am not sure if any of it is meaningful. It was just intuition and I like the mazes it generates.
    // The mazes are very open and allow for diverse strategy.
    private void eatGrid() {
        final Random rand = new Random();
        ArrayList<TankCell> yummyCells = getYummyCells();
        TankCell tankCell = yummyCells.get(rand.nextInt(yummyCells.size()));
        while (true) {
            final int i = rand.nextInt(tankCell.getYummySegments().size());
            final TankCell.MutableBoolean seg = tankCell.getYummySegments().get(i);

            seg.value = false;
            tankCell.getYummySegments().remove(i);

            if (tankCell.getUp() == seg) {
                tankCell = grid[tankCell.getColumn()][tankCell.getRow() - 1];
            } else if (tankCell.getRight() == seg) {
                tankCell = grid[tankCell.getColumn() + 1][tankCell.getRow()];
            } else if (tankCell.getDown() == seg) {
                tankCell = grid[tankCell.getColumn()][tankCell.getRow() + 1];
            } else if (tankCell.getLeft() == seg) {
                tankCell = grid[tankCell.getColumn() - 1][tankCell.getRow()];
            }

            if (!tankCell.isYummy()) {
                yummyCells = getYummyCells();
                if (yummyCells.size() == 0) {
                    return;
                }
                tankCell = yummyCells.get(rand.nextInt(yummyCells.size()));
            }
        }
    }

    private void drawGrid() {
        for (int i = 0; i < COLUMNS; i++) {
            for (int j = 0; j < ROWS; j++) {
                final TankCell tankCell = grid[i][j];

                final TankRectangle rightSeg = tankCell.getRightSeg();
                verticalSegments[i + 1][j] = rightSeg;

                final TankRectangle downSeg = tankCell.getDownSeg();
                horizontalSegments[i][j + 1] = downSeg;

                // We are in the first column and so we need to grab the left seg too.
                if (i == 0) {
                    final TankRectangle leftSeg = tankCell.getLeftSeg();
                    verticalSegments[i][j] = leftSeg;
                }

                // We are in the first row and so we need to grab the up seg too.
                if (j == 0) {
                    final TankRectangle upSeg = tankCell.getUpSeg();
                    horizontalSegments[i][j] = upSeg;
                }
            }
        }

        for (final TankRectangle[] segs : horizontalSegments) {
            for (final TankRectangle seg : segs) {
                addSeg(seg);
            }
        }

        for (final TankRectangle[] segs : verticalSegments) {
            for (final TankRectangle seg : segs) {
                addSeg(seg);
            }
        }
    }

    private void addSeg(final TankRectangle seg) {
        if (seg != null) {
            final Polygon poly = seg.getPolygon();
            group.getChildren().add(poly);
        }
    }

    // Needed because collision detection is expensive. See the Physics class.
    // This could probably be optimized even further based on the overlaps in segments but its more than
    // fast enough as it is and it is difficult enough to understand. It is hard to explain
    // but what we are doing is finding the closest two horizontal and vertical segments and checking for
    // collisions against those.
    // This assumes that the object can only touch max two horizontal and two vertical segments maximum at once.
    // We do not return early if a collision is detected because it is possible for multiple collisions to occur.
    ArrayList<TankRectangle> getCollisionCandidates(final Point2D objCenter) {
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

        // Let's try the horizontal seg in the previous column.
        column--;
        if (column >= 0) {
            final TankRectangle seg = horizontalSegments[column][row];
            if (seg != null) {
                segs.add(seg);
            }
        }
        column++;

        if (row < ROWS) {
            final TankRectangle seg = verticalSegments[column][row];
            if (seg != null) {
                segs.add(seg);
            }
        }

        // Let's try the vertical seg in the previous row.
        row--;
        if (row >= 0) {
            final TankRectangle seg = verticalSegments[column][row];
            if (seg != null) {
                segs.add(seg);
            }
        }
        row++;

        return segs;
    }
}
