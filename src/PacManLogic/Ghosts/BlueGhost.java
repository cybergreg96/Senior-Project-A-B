package PacManLogic.Ghosts;

import PacManGUI.SceneInfo;
import PacManLogic.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.awt.*;

/**
 * Created by Arash on 4/22/2017.
 */
public class BlueGhost extends Ghosts implements GameObject {
	boolean human = true;
	boolean comp = false;
	public int count = 0;

	public BlueGhost(Point position, MapOutline map, Player player, Biscuits biscuits) {

		super(position, map, player, biscuits);
	}

	@Override
	public void update(KeyCode keyCode) {
		if (keyCode.equals(keyCode.ENTER)) {
			count++;
			System.out.println(count);
			if(count==2){
				count=0;
			}
		}
		if (count % 2 == 0) {
			human = true;
			comp = false;
		} else if (count % 2 == 1) {
			comp = true;
			human = false;
		}
		if (getEscapeTimeCount() > 0) {
			setEscape(true);
			setEscapeTimeCount(getEscapeTimeCount() - 1);
		} else {
			setEscape(false);
		}
		if (getBiscuits().getTotalEatenBigBiscuits() > previousBigBiscuitEaten) {
			previousBigBiscuitEaten = getBiscuits().getTotalEatenBigBiscuits();
			setEscapeTimeCount(30);
		}

		if (isEscape()) {
			if ((keyCode == KeyCode.RIGHT) && (getPlayer().getX() == getX() - 20) && (getPlayer().getY() == getY())) {
				isEatenWhileEscape = true;
			} else if ((keyCode == KeyCode.DOWN) && (getPlayer().getX() == getX())
					&& (getPlayer().getY() == getY() - 20)) {
				isEatenWhileEscape = true;
			} else if ((keyCode == KeyCode.LEFT) && (getPlayer().getX() == getX() + 20)
					&& (getPlayer().getY() == getY())) {
				isEatenWhileEscape = true;
			} else if ((keyCode == KeyCode.RIGHT) && (getPlayer().getX() == getX())
					&& (getPlayer().getY() == getY() + 20)) {
				isEatenWhileEscape = true;
			} else if ((getPlayer().getX() == getX()) && (getPlayer().getY() == getY())) {
				isEatenWhileEscape = true;
			}

		}
		if (human == false && comp == true) {
			if ((getBiscuits().getTotalEatenBiscuits() < 20) && (!isEscape())) {
				randomMovement(randomDirection());
			} else if (!isEatenWhileEscape) {
				bestFirstSearch(getPlayer().getStepX(), getPlayer().getStepY());

			} else if (isEatenWhileEscape) {

				bestFirstSearch(16, 15);
				if ((getStepX() == 16) && (getStepY() == 15))
					isEatenWhileEscape = false;
			}
		} else if (human == true && comp == false) {
			switch (keyCode) {
			case DOWN:

				moveDown();
				break;

			case LEFT:

				moveLeft();
				break;

			case RIGHT:

				moveRight();
				break;
			case UP:
				moveUp();

				break;
			}
		}

	}

	@Override
	public void draw(GraphicsContext g, SceneInfo sceneInfo) {
		if (isEatenWhileEscape) {

			g.setFill(javafx.scene.paint.Color.rgb(170, 170, 170));

		} else if (!isEscape()) {
			g.setFill(javafx.scene.paint.Color.rgb(0, 30, 200));
		} else {
			g.setFill(javafx.scene.paint.Color.rgb(0, 155, 194));
		}
		g.fillRoundRect(this.getX(), this.getY(), 20, 20, 2, 2);

	}

	int lastMove = 0;

	public void bestFirstSearch(int x, int y) {

		if ((x != getStepX()) || (y != getStepY())) {

			int totalVisited = 0;
			int[] currentReached = new int[2];
			int[][] myOrangeGhostPath = new int[400][];

			int firstPriority;
			int secondPriority;

			currentReached[0] = getStepX();
			currentReached[1] = getStepY();

			// Calculation of distance between the currentReached (the current
			// searching spot) and the player

			int distanceDifferenceX = x - getStepX();
			int distanceDifferenceY = y - getStepY();

			if (Math.abs(distanceDifferenceX) > Math.abs(distanceDifferenceY)) {

				if (distanceDifferenceX > 0) {
					firstPriority = 2;

				} else {
					firstPriority = 4;
				}

				if (distanceDifferenceY > 0) {

					secondPriority = 3;
				} else {

					secondPriority = 1;
				}

			} else {

				if (distanceDifferenceY > 0) {
					firstPriority = 3;
				} else {
					firstPriority = 1;
				}

				if (distanceDifferenceX > 0) {

					secondPriority = 2;
				} else {

					secondPriority = 4;
				}
			}

			switch (firstPriority) {

			case 1:// UP
				if (((getMap().points[currentReached[1] - 1][currentReached[0]] == 1)
						|| (getMap().points[currentReached[1] - 1][currentReached[0]] == 2)) && lastMove != 3) {
					currentReached[0] = currentReached[0];
					currentReached[1] = currentReached[1] - 1;
					lastMove = 1;

				} else {
					switch (secondPriority) {
					case 2:// RIGHT
						if (((getMap().points[currentReached[1]][currentReached[0] + 1] == 1)
								|| (getMap().points[currentReached[1]][currentReached[0] + 1] == 2)) && lastMove != 4) {
							currentReached[0] = currentReached[0] + 1;
							currentReached[1] = currentReached[1];
							lastMove = 2;

							// LEFT
						} else if (((getMap().points[currentReached[1]][currentReached[0] - 1] == 1)
								|| (getMap().points[currentReached[1]][currentReached[0] - 1] == 2)) && lastMove != 2) {
							currentReached[0] = currentReached[0] - 1;
							currentReached[1] = currentReached[1];
							lastMove = 4;

							// DOWN
						} else if (((getMap().points[currentReached[1] + 1][currentReached[0]] == 1)
								|| (getMap().points[currentReached[1] + 1][currentReached[0]] == 2)) && lastMove != 1) {
							currentReached[0] = currentReached[0];
							currentReached[1] = currentReached[1] + 1;
							lastMove = 3;
						}
						break;
					case 4:// LEFT
						if (((getMap().points[currentReached[1]][currentReached[0] - 1] == 1)
								|| (getMap().points[currentReached[1]][currentReached[0] - 1] == 2)) && lastMove != 2) {
							currentReached[0] = currentReached[0] - 1;
							currentReached[1] = currentReached[1];
							lastMove = 4;

							// RIGHT
						} else if (((getMap().points[currentReached[1]][currentReached[0] + 1] == 1)
								|| (getMap().points[currentReached[1]][currentReached[0] + 1] == 2)) && lastMove != 4) {
							currentReached[0] = currentReached[0] + 1;
							currentReached[1] = currentReached[1];

							lastMove = 2;

							// DOWN
						} else if (((getMap().points[currentReached[1] + 1][currentReached[0]] == 1)
								|| (getMap().points[currentReached[1] + 1][currentReached[0]] == 2)) && lastMove != 1) {
							currentReached[0] = currentReached[0];
							currentReached[1] = currentReached[1] + 1;

							lastMove = 3;
						}
						break;

					}

				}
				break;

			case 2:// RIGHT
				if (((getMap().points[currentReached[1]][currentReached[0] + 1] == 1)
						|| (getMap().points[currentReached[1]][currentReached[0] + 1] == 2)) && lastMove != 4) {
					currentReached[0] = currentReached[0] + 1;
					currentReached[1] = currentReached[1];

					lastMove = 2;
				} else {
					switch (secondPriority) {
					case 1:// UP
						if (((getMap().points[currentReached[1] - 1][currentReached[0]] == 1)
								|| (getMap().points[currentReached[1] - 1][currentReached[0]] == 2)) && lastMove != 3) {
							currentReached[0] = currentReached[0];
							currentReached[1] = currentReached[1] - 1;

							lastMove = 1;

							// DOWN
						} else if (((getMap().points[currentReached[1] + 1][currentReached[0]] == 1)
								|| (getMap().points[currentReached[1] + 1][currentReached[0]] == 2)) && lastMove != 1) {
							currentReached[0] = currentReached[0];
							currentReached[1] = currentReached[1] + 1;
							lastMove = 3;

							// LEFT
						} else {
							currentReached[0] = currentReached[0] - 1;
							currentReached[1] = currentReached[1];
							lastMove = 4;

						}
						break;
					case 3:// DOWN
						if (((getMap().points[currentReached[1] + 1][currentReached[0]] == 1)
								|| (getMap().points[currentReached[1] + 1][currentReached[0]] == 2)) && lastMove != 1) {
							currentReached[0] = currentReached[0];
							currentReached[1] = currentReached[1] + 1;
							lastMove = 3;
						} else if (((getMap().points[currentReached[1] - 1][currentReached[0]] == 1)
								|| (getMap().points[currentReached[1] - 1][currentReached[0]] == 2)) && lastMove != 1) {
							currentReached[0] = currentReached[0];
							currentReached[1] = currentReached[1] - 1;
							lastMove = 3;
						} else {
							currentReached[0] = currentReached[0] - 1;
							currentReached[1] = currentReached[1];
							lastMove = 4;
						}
						break;

					}

				}
				break;

			case 3:// DOWN
				if (((getMap().points[currentReached[1] + 1][currentReached[0]] == 1)
						|| (getMap().points[currentReached[1] + 1][currentReached[0]] == 2)) && lastMove != 1) {
					currentReached[0] = currentReached[0];
					currentReached[1] = currentReached[1] + 1;
					lastMove = 3;
				} else {
					switch (secondPriority) {
					case 2:// RIGHT
						if (((getMap().points[currentReached[1]][currentReached[0] + 1] == 1)
								|| (getMap().points[currentReached[1]][currentReached[0] + 1] == 2)) && lastMove != 4) {
							currentReached[0] = currentReached[0] + 1;
							currentReached[1] = currentReached[1];
							lastMove = 2;
						} else if (((getMap().points[currentReached[1]][currentReached[0] - 1] == 1)
								|| (getMap().points[currentReached[1]][currentReached[0] - 1] == 2)) && lastMove != 2) {
							currentReached[0] = currentReached[0] - 1;
							currentReached[1] = currentReached[1];
							lastMove = 4;
						} else if (((getMap().points[currentReached[1] - 1][currentReached[0]] == 1)
								|| (getMap().points[currentReached[1] - 1][currentReached[0]] == 2)) && lastMove != 3) {
							currentReached[0] = currentReached[0];
							currentReached[1] = currentReached[1] - 1;
							lastMove = 1;
						}
						break;
					case 4:// LEFT
						if (((getMap().points[currentReached[1]][currentReached[0] - 1] == 1)
								|| (getMap().points[currentReached[1]][currentReached[0] - 1] == 2)) && lastMove != 2) {
							currentReached[0] = currentReached[0] - 1;
							currentReached[1] = currentReached[1];
							lastMove = 4;
						} else if (((getMap().points[currentReached[1]][currentReached[0] + 1] == 1)
								|| (getMap().points[currentReached[1]][currentReached[0] + 1] == 2)) && lastMove != 4) {
							currentReached[0] = currentReached[0] + 1;
							currentReached[1] = currentReached[1];
							lastMove = 2;
						} else if (((getMap().points[currentReached[1] - 1][currentReached[0]] == 1)
								|| (getMap().points[currentReached[1] - 1][currentReached[0]] == 2)) && lastMove != 3) {
							currentReached[0] = currentReached[0];
							currentReached[1] = currentReached[1] - 1;
							lastMove = 1;
						}
						break;

					}

				}
				break;
			case 4:// LEFT
				if (((getMap().points[currentReached[1]][currentReached[0] - 1] == 1)
						|| (getMap().points[currentReached[1]][currentReached[0] - 1] == 2)) && lastMove != 2) {
					currentReached[0] = currentReached[0] - 1;
					currentReached[1] = currentReached[1];
					lastMove = 4;
				} else {
					switch (secondPriority) {
					case 1:// UP
						if (((getMap().points[currentReached[1] - 1][currentReached[0]] == 1)
								|| (getMap().points[currentReached[1] - 1][currentReached[0]] == 2)) && lastMove != 3) {
							currentReached[0] = currentReached[0];
							currentReached[1] = currentReached[1] - 1;
							lastMove = 1;
						} else if (((getMap().points[currentReached[1] + 1][currentReached[0]] == 1)
								|| (getMap().points[currentReached[1] + 1][currentReached[0]] == 2)) && lastMove != 1) {
							currentReached[0] = currentReached[0];
							currentReached[1] = currentReached[1] + 1;
							lastMove = 3;
						} else if (((getMap().points[currentReached[1]][currentReached[0] + 1] == 1)
								|| (getMap().points[currentReached[1]][currentReached[0] + 1] == 2)) && lastMove != 4) {
							currentReached[0] = currentReached[0] + 1;
							currentReached[1] = currentReached[1];
							lastMove = 1;
						}
						break;
					case 3:// DOWN
						if (((getMap().points[currentReached[1] + 1][currentReached[0]] == 1)
								|| (getMap().points[currentReached[1] + 1][currentReached[0]] == 2)) && lastMove != 1) {
							currentReached[0] = currentReached[0];
							currentReached[1] = currentReached[1] + 1;
							lastMove = 1;
						} else if (((getMap().points[currentReached[1] - 1][currentReached[0]] == 1)
								|| (getMap().points[currentReached[1] - 1][currentReached[0]] == 2)) && lastMove != 3) {
							currentReached[0] = currentReached[0];
							currentReached[1] = currentReached[1] - 1;
							lastMove = 1;
						} else if (((getMap().points[currentReached[1]][currentReached[0] + 1] == 1)
								|| (getMap().points[currentReached[1]][currentReached[0] + 1] == 2)) && lastMove != 4) {
							currentReached[0] = currentReached[0] + 1;
							currentReached[1] = currentReached[1];
							lastMove = 2;
						}
						break;

					}

				}
				break;
			}

			totalVisited++;

			setTotalSearched(totalVisited);

			if ((!isEscape()) || (isEatenWhileEscape)) {

				this.setStepX(currentReached[0]);
				this.setStepY(currentReached[1]);
				this.setX((currentReached[0]) * 20);
				this.setY((currentReached[1]) * 20);

			} else if ((isEscape()) && (!isEatenWhileEscape)) {

				if (lastMove == 1) {

					if ((getMap().points[getStepY() + 1][getStepX()] == 1)
							|| (getMap().points[getStepY() + 1][getStepX()] == 2)) {

						this.setStepX(getStepX());
						this.setStepY(getStepY() + 1);

					} else if ((getMap().points[getStepY()][getStepX() + 1] == 1)
							|| (getMap().points[getStepY()][getStepX() + 1] == 2)) {

						this.setStepX(getStepX() + 1);
						this.setStepY(getStepY());

					} else if ((getMap().points[getStepY()][getStepX() - 1] == 1)
							|| (getMap().points[getStepY()][getStepX() - 1] == 2)) {
						this.setStepX(getStepX() - 1);
						this.setStepY(getStepY());

					}

				} else if (lastMove == 2) {

					if ((getMap().points[getStepY()][getStepX() - 1] == 1)
							|| (getMap().points[getStepY()][getStepX() - 1] == 2)) {

						this.setStepX(getStepX() - 1);
						this.setStepY(getStepY());

					} else if ((getMap().points[getStepY() - 1][getStepX()] == 1)
							|| (getMap().points[getStepY() - 1][getStepX()] == 2)) {

						this.setStepX(getStepX());
						this.setStepY(getStepY() - 1);

					} else if ((getMap().points[getStepY()][getStepX() + 1] == 1)
							|| (getMap().points[getStepY()][getStepX() + 1] == 2)) {

						this.setStepX(getStepX());
						this.setStepY(getStepY() + 1);

					}

				} else if (lastMove == 3) {

					if ((getMap().points[getStepY() - 1][getStepX()] == 1)
							|| (getMap().points[getStepY() - 1][getStepX()] == 2)) {

						this.setStepX(getStepX());
						this.setStepY(getStepY() - 1);

					} else if ((getMap().points[getStepY()][getStepX() + 1] == 1)
							|| (getMap().points[getStepY()][getStepX() + 1] == 2)) {

						this.setStepX(getStepX() + 1);
						this.setStepY(getStepY());

					} else if ((getMap().points[getStepY()][getStepX() - 1] == 1)
							|| (getMap().points[getStepY()][getStepX() - 1] == 2)) {

						this.setStepX(getStepX() - 1);
						this.setStepY(getStepY());

					}

				} else if (lastMove == 4) {

					if ((getMap().points[getStepY()][getStepX() + 1] == 1)
							|| (getMap().points[getStepY()][getStepX() + 1] == 2)) {

						this.setStepX(getStepX() + 1);
						this.setStepY(getStepY());

					}
					if ((getMap().points[getStepY() - 1][getStepX()] == 1)
							|| (getMap().points[getStepY() - 1][getStepX()] == 2)) {

						this.setStepX(getStepX());
						this.setStepY(getStepY() - 1);

					} else if ((getMap().points[getStepY() + 1][getStepX()] == 1)
							|| (getMap().points[getStepY() + 1][getStepX()] == 2)) {
						this.setStepX(getStepX());
						this.setStepY(getStepY() + 1);

					}

				}

				this.setX((getStepX()) * 20);
				this.setY((getStepY()) * 20);

			}

		}
	}

}