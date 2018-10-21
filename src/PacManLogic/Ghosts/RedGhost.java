package PacManLogic.Ghosts;

import PacManGUI.SceneInfo;
import PacManLogic.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.awt.*;

/**
 * Created by Arash on 4/8/2017.
 */
public class RedGhost extends Ghosts implements GameObject {

	boolean human = false;
	boolean comp = true;
	boolean dead = false;
	boolean caneat = false;
	int pauseCounter = 2;

	public RedGhost(Point position, MapOutline map, Player player, Biscuits biscuits) {
		super(position, map, player, biscuits);

	}

	// Breadth first search starts here

	int lastMove = 0;
	int counter = 0;

	public void AStarSearch(int x, int y) {

		Node<int[]>[] openList = new Node[700];
		for (int i = 0; i < openList.length; i++) {

			openList[i] = new Node<int[]>(null);
		}

		Node<int[]>[] closedList = new Node[700];
		for (int i = 0; i < closedList.length; i++) {

			closedList[i] = new Node<int[]>(null);
		}

		int closedListCounter = 0;
		closedList[closedListCounter] = new Node<int[]>(new int[] { getStepX(), getStepY() });

		int openListCounter = 0;

		while (true) {

			// System.out.println(closedListCounter);
			// System.out.println("X:
			// "+closedList[closedListCounter].getData()[0] + " Y: " +
			// closedList[closedListCounter].getData()[1]);

			// check the upon adjacent point

			if ((getMap().points[closedList[closedListCounter].getData()[1] - 1][closedList[closedListCounter]
					.getData()[0]] == 1)
					|| (getMap().points[closedList[closedListCounter].getData()[1] - 1][closedList[closedListCounter]
							.getData()[0]] == 2)) {

				boolean inClosedList = false;
				for (int k = 0; k < closedListCounter; k++) {
					if ((closedList[closedListCounter].getData()[1] - 1 == closedList[k].getData()[1])
							&& (closedList[closedListCounter].getData()[0] == closedList[k].getData()[0])) {
						inClosedList = true;
						break;
					}
				}
				if (!inClosedList) {

					boolean inOpenList = false;
					for (int k = 0; k < openListCounter; k++) {
						if ((closedList[closedListCounter].getData()[1] - 1 == openList[k].getData()[1])
								&& (closedList[closedListCounter].getData()[0] == openList[k].getData()[0])) {
							inOpenList = true;

							break;
						}
					}
					if (!inOpenList) {

						openList[openListCounter] = new Node<int[]>(
								new int[] { closedList[closedListCounter].getData()[0],
										closedList[closedListCounter].getData()[1] - 1 },
								x, y, getStepX(), getStepY() - 1, closedList[closedListCounter]);

						if ((openList[openListCounter].getData()[0] == x)
								&& (openList[openListCounter].getData()[1] == y)) {

							break;
						}
						openListCounter++;

					} else {
						Node<int[]>[] temporaryNode = new Node[1];
						temporaryNode[0] = new Node<int[]>(
								new int[] { closedList[closedListCounter].getData()[0],
										closedList[closedListCounter].getData()[1] - 1 },
								x, y, getStepX(), getStepY() - 1, closedList[closedListCounter].getParent());
						if (temporaryNode[0].getScore() < closedList[closedListCounter].getScore()) {
							closedList[closedListCounter] = temporaryNode[0];
						}

					}

				}
			}

			// check the Right adjacent point
			if ((getMap().points[closedList[closedListCounter].getData()[1]][closedList[closedListCounter].getData()[0]
					+ 1] == 1)
					|| (getMap().points[closedList[closedListCounter].getData()[1]][closedList[closedListCounter]
							.getData()[0] + 1] == 2)) {

				boolean inClosedList = false;
				for (int k = 0; k < closedListCounter; k++) {
					if ((closedList[closedListCounter].getData()[1] == closedList[k].getData()[1])
							&& (closedList[closedListCounter].getData()[0] + 1 == closedList[k].getData()[0])) {
						inClosedList = true;
						break;
					}
				}

				if (!inClosedList) {

					boolean inOpenList = false;
					for (int k = 0; k < openListCounter; k++) {
						if ((closedList[closedListCounter].getData()[1] == openList[k].getData()[1])
								&& (closedList[closedListCounter].getData()[0] + 1 == openList[k].getData()[0])) {
							inOpenList = true;
							break;
						}
					}
					if (!inOpenList) {

						openList[openListCounter] = new Node<int[]>(
								new int[] { closedList[closedListCounter].getData()[0] + 1,
										closedList[closedListCounter].getData()[1] },
								x, y, getStepX() + 1, getStepY(), closedList[closedListCounter]);

						if ((openList[openListCounter].getData()[0] == x)
								&& (openList[openListCounter].getData()[1] == y)) {

							break;
						}
						openListCounter++;

					} else {
						Node<int[]>[] temporaryNode = new Node[1];
						temporaryNode[0] = new Node<int[]>(
								new int[] { closedList[closedListCounter].getData()[0] + 1,
										closedList[closedListCounter].getData()[1] },
								x, y, getStepX() + 1, getStepY(), closedList[closedListCounter].getParent());
						if (temporaryNode[0].getScore() < closedList[closedListCounter].getScore()) {
							closedList[closedListCounter] = temporaryNode[0];
						}

					}

				}

			}

			// check the Bottom adjacent point

			if ((getMap().points[closedList[closedListCounter].getData()[1] + 1][closedList[closedListCounter]
					.getData()[0]] == 1)
					|| (getMap().points[closedList[closedListCounter].getData()[1] + 1][closedList[closedListCounter]
							.getData()[0]] == 2)) {

				boolean inClosedList = false;
				for (int k = 0; k < closedListCounter; k++) {
					if ((closedList[closedListCounter].getData()[1] + 1 == closedList[k].getData()[1])
							&& (closedList[closedListCounter].getData()[0] == closedList[k].getData()[0])) {
						inClosedList = true;
						break;
					}
				}

				if (!inClosedList) {

					boolean inOpenList = false;
					for (int k = 0; k < openListCounter; k++) {
						if ((closedList[closedListCounter].getData()[1] + 1 == openList[k].getData()[1])
								&& (closedList[closedListCounter].getData()[0] == openList[k].getData()[0])) {
							inOpenList = true;
							break;
						}
					}
					if (!inOpenList) {
						openList[openListCounter] = new Node<int[]>(
								new int[] { closedList[closedListCounter].getData()[0],
										closedList[closedListCounter].getData()[1] + 1 },
								x, y, getStepX(), getStepY() + 1, closedList[closedListCounter]);
						if ((openList[openListCounter].getData()[0] == x)
								&& (openList[openListCounter].getData()[1] == y)) {

							break;
						}
						openListCounter++;

					} else {
						Node<int[]>[] temporaryNode = new Node[1];
						temporaryNode[0] = new Node<int[]>(
								new int[] { closedList[closedListCounter].getData()[0],
										closedList[closedListCounter].getData()[1] + 1 },
								x, y, getStepX(), getStepY() + 1, closedList[closedListCounter].getParent());
						if (temporaryNode[0].getScore() < closedList[closedListCounter].getScore()) {
							closedList[closedListCounter] = temporaryNode[0];
						}

					}

				}

			}
			// check the Left adjacent point
			if ((getMap().points[closedList[closedListCounter].getData()[1]][closedList[closedListCounter].getData()[0]
					- 1] == 1)
					|| (getMap().points[closedList[closedListCounter].getData()[1]][closedList[closedListCounter]
							.getData()[0] - 1] == 2)) {

				boolean inClosedList = false;
				for (int k = 0; k < closedListCounter; k++) {
					if ((closedList[closedListCounter].getData()[1] == closedList[k].getData()[1])
							&& (closedList[closedListCounter].getData()[0] - 1 == closedList[k].getData()[0])) {
						inClosedList = true;
						break;
					}
				}

				if (!inClosedList) {

					boolean inOpenList = false;
					for (int k = 0; k < openListCounter; k++) {
						if ((closedList[closedListCounter].getData()[1] == openList[k].getData()[1])
								&& (closedList[closedListCounter].getData()[0] - 1 == openList[k].getData()[0])) {
							inOpenList = true;
							break;
						}
					}
					if (!inOpenList) {

						openList[openListCounter] = new Node<int[]>(
								new int[] { closedList[closedListCounter].getData()[0] - 1,
										closedList[closedListCounter].getData()[1] },
								x, y, getStepX() - 1, getStepY(), closedList[closedListCounter]);
						if ((openList[openListCounter].getData()[0] == x)
								&& (openList[openListCounter].getData()[1] == y)) {

							break;
						}
						openListCounter++;

					} else {
						Node<int[]>[] temporaryNode = new Node[1];
						temporaryNode[0] = new Node<int[]>(
								new int[] { closedList[closedListCounter].getData()[0] - 1,
										closedList[closedListCounter].getData()[1] },
								x, y, getStepX() - 1, getStepY(), closedList[closedListCounter].getParent());
						if (temporaryNode[0].getScore() < closedList[closedListCounter].getScore()) {
							closedList[closedListCounter] = temporaryNode[0];
						}

					}

				}

			}
			// Comparision of the Price and H values
			// System.out.println(++counter + " " + openListCounter);
			int loswestScore = 100;
			closedListCounter++;

			for (int i = 0; i < openListCounter; i++) {

				if (openList[i].getScore() <= loswestScore) {

					loswestScore = openList[i].getScore();
					closedList[closedListCounter] = openList[i];

				}
			}
			// System.out.println("new closed list "+
			// closedList[closedListCounter].getData()[0] + " " +
			// closedList[closedListCounter].getData()[1]);
			Node<int[]>[] tempOpenList = new Node[400];

			int tempOpenListCounter = 0;
			// System.out.println("new Closed lists");
			for (int i = 0; i < tempOpenList.length; i++) {

				tempOpenList[i] = new Node<int[]>(null);
			}

			for (int i = 0; i < openListCounter; i++) {
				boolean numberIsThere = false;

				if (openList[i] == closedList[closedListCounter]) {
					numberIsThere = true;

				}

				if (!numberIsThere) {
					tempOpenList[tempOpenListCounter] = openList[i];
					// System.out.println("the saving nr" +
					// openList[i].getData()[0] + " "
					// +openList[i].getData()[1]);
					tempOpenListCounter++;
				}
			}

			openList = tempOpenList;
			openListCounter = tempOpenListCounter;
			// System.out.println(" Open Lists");
			// for (int i= 0; i<tempOpenListCounter; i++){

			// System.out.println("X: "+openList[i].getData()[0] + " Y: " +
			// openList[i].getData()[1]);
			// }
			// System.out.println(" Open Lists finishes");
			// System.out.println(counter + " " + openListCounter);
			// System.out.println("Closed Lists");
			for (int i = 0; i < closedListCounter; i++) {

				// System.out.println("X: "+closedList[i].getData()[0] + " Y: "
				// + closedList[i].getData()[1]);
			}
			// System.out.println("Open Lists");
			// for (int i= 0; i<openListCounter; i++){

			// System.out.println("X: "+openList[i].getData()[0] + " Y: " +
			// openList[i].getData()[1]);
			// }

		}
		/// /End while
		// System.out.println("closed list counters" + closedListCounter);
		// System.out.println("the parent" +
		/// openList[openListCounter].getParent().getData()[0] + " " +
		/// openList[openListCounter].getParent().getData()[1] );
		setTotalSearched(closedListCounter);
		Node<int[]> myNode = openList[openListCounter];

		int[][] myNodesArray = new int[400][2];
		int countMyNodes = 0;

		while (myNode != null) {
			myNodesArray[countMyNodes][0] = myNode.getData()[0];
			myNodesArray[countMyNodes][1] = myNode.getData()[1];
			myNode = myNode.getParent();
			countMyNodes++;
		}

		int[][] myNodesArray2 = new int[countMyNodes][2];
		for (int j = 0; j < myNodesArray.length; j++) {

			if ((myNodesArray[j][0] != 0)) {

				myNodesArray2[j][0] = myNodesArray[j][0];
				myNodesArray2[j][1] = myNodesArray[j][1];
			}

		}
		int[][] temp = new int[1][2];
		for (int i = 0; i < myNodesArray2.length / 2; i++) {
			temp[0] = myNodesArray2[i];
			myNodesArray2[i] = myNodesArray2[myNodesArray2.length - 1 - i];
			myNodesArray2[myNodesArray2.length - 1 - i] = temp[0];
		}
		myNodesArray = myNodesArray2;

		// setTotalSearched(totalVisited);

		if ((!isEscape()) || (isEatenWhileEscape)) {

			this.setStepX(myNodesArray[1][0]);
			this.setStepY(myNodesArray[1][1]);
			this.setX((myNodesArray[1][0]) * 20);
			this.setY((myNodesArray[1][1]) * 20);

		} else if ((isEscape()) && (!isEatenWhileEscape)) {

			// Run Away
			if (pauseCounter == 2) {
				this.setStepX(myNodesArray[0][0]);
				this.setStepY(myNodesArray[0][1]);
				if (myNodesArray[0][0] - myNodesArray[1][0] == 1) {

					if (getMap().points[getStepY()][getStepX() + 1] != 0) {
						setX(getX() + 20);
						setStepX(getStepX() + 1);
						if ((this.getX() == 540) && (this.getY() == 300)) {
							this.setX(20);
							setStepX(1);
						}
					} else if (getMap().points[getStepY() + 1][getStepX()] != 0) {
						setY(getY() + 20);
						setStepY(getStepY() + 1);
					} else {
						setY(getY() - 20);
						setStepY(getStepY() - 1);
					}

				} else if (myNodesArray[1][0] - myNodesArray[0][0] == 1) {
					if (getMap().points[getStepY()][getStepX() - 1] != 0) {
						setX(getX() - 20);
						setStepX(getStepX() - 1);
						if ((this.getX() == 0) && (this.getY() == 300)) {
							this.setX(520);
							setStepX(26);
						}
					} else if (getMap().points[getStepY() - 1][getStepX()] != 0) {
						setY(getY() - 20);
						setStepY(getStepY() - 1);
					} else {
						setY(getY() + 20);
						setStepY(getStepY() + 1);
					}
				} else if (myNodesArray[1][1] - myNodesArray[0][1] == 1) {
					if (getMap().points[getStepY() - 1][getStepX()] != 0) {
						setY(getY() - 20);
						setStepY(getStepY() - 1);

					} else if (getMap().points[getStepY()][getStepX() + 1] != 0) {

						setX(getX() + 20);
						setStepX(getStepX() + 1);
						if ((this.getX() == 540) && (this.getY() == 300)) {
							this.setX(20);
							setStepX(1);
						}
					} else {
						setX(getX() - 20);
						setStepX(getStepX() - 1);

						if ((this.getX() == 0) && (this.getY() == 300)) {
							this.setX(520);
							setStepX(26);
						}
					}

				} else if (myNodesArray[0][1] - myNodesArray[1][1] == 1) {
					if (getMap().points[getStepY() + 1][getStepX()] != 0) {
						setY(getY() + 20);
						setStepY(getStepY() + 1);

					} else if (getMap().points[getStepY()][getStepX() + 1] != 0) {

						setX(getX() + 20);
						setStepX(getStepX() + 1);
						if ((this.getX() == 540) && (this.getY() == 300)) {
							this.setX(20);
							setStepX(1);
						}
					} else {
						setX(getX() - 20);
						setStepX(getStepX() - 1);
						if ((this.getX() == 0) && (this.getY() == 300)) {
							this.setX(520);
							setStepX(26);
						}
					}
				}
			}
			pauseCounter--;
			if (pauseCounter == 0) {
				pauseCounter = 2;
			}

		}

	}

	// Breadth first search ends here

	public boolean isDead() {
		return dead;

	}

	public boolean eat() {
		return caneat;

	}

	@Override
	public void update(KeyCode keyCode) {
		if (keyCode.equals(keyCode.ENTER)) {
			switchGhost();
			if (getGhost() == "red") {
				human = true;
				comp = false;
			} else if (getGhost() != "red") {
				comp = true;
				human = false;
			}
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
			if ((keyCode == KeyCode.D) && (getPlayer().getX() == getX() - 20) && (getPlayer().getY() == getY())) {
				isEatenWhileEscape = true;
			} else if ((keyCode == KeyCode.S) && (getPlayer().getX() == getX())
					&& (getPlayer().getY() == getY() - 20)) {
				isEatenWhileEscape = true;
			} else if ((keyCode == KeyCode.A) && (getPlayer().getX() == getX() + 20)
					&& (getPlayer().getY() == getY())) {
				isEatenWhileEscape = true;
			} else if ((keyCode == KeyCode.W) && (getPlayer().getX() == getX())
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
				AStarSearch(getPlayer().getStepX(), getPlayer().getStepY());

			} else if (isEatenWhileEscape) {

				AStarSearch(14, 15);
				if ((getStepX() == 14) && (getStepY() == 15))
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
			dead = true;

			if (getGhost() == "red") {
				human = true;
				comp = false;
			} else if (getGhost() != "red") {
				comp = true;
				human = false;
			}
		} else if (!isEscape()) {
			g.setFill(javafx.scene.paint.Color.rgb(255, 10, 0));
			dead = false;
			caneat = false;
		} else {
			g.setFill(javafx.scene.paint.Color.rgb(0, 155, 194));
			caneat = true;
		}
		g.fillRoundRect(this.getX(), this.getY(), 20, 20, 2, 2);
	}

}