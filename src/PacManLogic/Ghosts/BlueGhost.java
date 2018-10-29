package PacManLogic.Ghosts;

import PacManGUI.SceneInfo;
import PacManLogic.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Arash on 4/22/2017.
 */
public class BlueGhost extends Ghosts implements GameObject {
	
	public int count = 0;
	int pauseCounter = 2;
	private Image blueImg, blueLeft, blueRight, deadBird, vulnLeft, vulnRight;
	public BlueGhost(Point position, MapOutline map, Player player, Biscuits biscuits) {
		super(position, map, player, biscuits);
    	try {
			blueLeft = new Image(new FileInputStream("src/PacManImgs/BGLeft.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			blueRight = new Image(new FileInputStream("src/PacManImgs/BGRight.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			deadBird = new Image(new FileInputStream("src/PacManImgs/deadBird.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			vulnLeft = new Image(new FileInputStream("src/PacManImgs/VulnLeft.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			vulnRight = new Image(new FileInputStream("src/PacManImgs/VulnRight.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	blueImg = blueLeft;
	}

	@Override
	public void update(KeyCode keyCode, int ghostControl) {
		
		if (isEscape()) {
			blueImg = vulnLeft;
		}else if (!isEscape()) {
			blueImg = blueRight;
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
		if (ghostControl != 0) {
			if ((getBiscuits().getTotalEatenBiscuits() < 20) && (!isEscape())) {
				randomMovement(randomDirection());
			} else if (!isEatenWhileEscape) {
				biDirectionalSearch(getPlayer().getStepX(), getPlayer().getStepY());

			} else if (isEatenWhileEscape) {
				biDirectionalSearch(16, 15);
				if ((getStepX() == 16) && (getStepY() == 15))
					isEatenWhileEscape = false;
					blueImg = blueLeft;
			}

		} else if (ghostControl == 0) {
			if(isEatenWhileEscape) {
				setShouldSwitch(true);
			}
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
			blueImg = deadBird;
		}
		else if (!isEscape()) {
			switch (lastMove) {
			case 2:
				blueImg = blueLeft;
				break;
			case 4:
				blueImg = blueRight;
				break;
			}
		} else {
			switch(lastMove) {
			case 2:
				blueImg = vulnLeft;
				break;
			case 4:
				blueImg = vulnRight;
				break;
			}
		}
		g.drawImage(blueImg, this.getX(), this.getY());

	}

	

	public void biDirectionalSearch(int x, int y) {
        //  int[][] visitedPlaces = new int[1000][2];
        int checkingAdjacent = 0;
        int totalVisitedA = 0;
        int totalVisitedB = 0;


        Node < int[] > [] visitedPlacesA = new Node[400];
        Node < int[] > [] visitedPlacesB = new Node[400];
        boolean foundCommonAdjacant = false;
        for (int i = 0; i < visitedPlacesA.length; i++) {

            visitedPlacesA[i] = new Node < int[] > (null);
            visitedPlacesB[i] = new Node < int[] > (null);
        }

        int[] currentReached = new int[2];

        visitedPlacesA[totalVisitedA] = new Node < int[] > (new int[] {
                getStepX(), getStepY()
        });
        visitedPlacesB[totalVisitedB] = new Node < int[] > (new int[] {
                x,
                y
        });


        totalVisitedA++;
        totalVisitedB++;

        while (true) {



            //check the ghost's upon adjacent point
            if ((getMap().points[visitedPlacesA[checkingAdjacent].getData()[1] - 1][visitedPlacesA[checkingAdjacent].getData()[0]] == 1) || (getMap().points[visitedPlacesA[checkingAdjacent].getData()[1] - 1][visitedPlacesA[checkingAdjacent].getData()[0]] == 2)) {
                boolean allreadyChecked = false;
                for (int k = 0; k < totalVisitedA; k++) {
                    if ((visitedPlacesA[checkingAdjacent].getData()[0] == visitedPlacesA[k].getData()[0]) && (visitedPlacesA[checkingAdjacent].getData()[1] - 1 == visitedPlacesA[k].getData()[1])) {

                        allreadyChecked = true;
                        break;
                    }
                }
                if (!allreadyChecked) {


                    visitedPlacesA[totalVisitedA] = new Node < int[] > (new int[] {
                            visitedPlacesA[checkingAdjacent].getData()[0], visitedPlacesA[checkingAdjacent].getData()[1] - 1
                    }, visitedPlacesA[checkingAdjacent]);


                    currentReached[0] = visitedPlacesA[checkingAdjacent].getData()[0];
                    currentReached[1] = visitedPlacesA[checkingAdjacent].getData()[1] - 1;


                    for (int i = 0; i < totalVisitedB; i++) {
                        if ((currentReached[0] == visitedPlacesB[i].getData()[0]) && (currentReached[1] == visitedPlacesB[i].getData()[1])) {
                            foundCommonAdjacant = true;
                            break;
                        }

                    }
                    if (foundCommonAdjacant) {
                        break;

                    }


                    totalVisitedA++;

                }
            }



            //check the right adjacent point
            if ((getMap().points[visitedPlacesA[checkingAdjacent].getData()[1]][visitedPlacesA[checkingAdjacent].getData()[0] + 1] == 1) || (getMap().points[visitedPlacesA[checkingAdjacent].getData()[1]][visitedPlacesA[checkingAdjacent].getData()[0] + 1] == 2)) {
                boolean allreadyChecked = false;
                for (int k = 0; k < totalVisitedA; k++) {
                    if ((visitedPlacesA[checkingAdjacent].getData()[0] + 1 == visitedPlacesA[k].getData()[0]) && (visitedPlacesA[checkingAdjacent].getData()[1] == visitedPlacesA[k].getData()[1])) {

                        allreadyChecked = true;
                        break;
                    }
                }
                if (!allreadyChecked) {

                    // Adding the found empty place to the array
                    visitedPlacesA[totalVisitedA] = new Node < int[] > (new int[] {
                            visitedPlacesA[checkingAdjacent].getData()[0] + 1, visitedPlacesA[checkingAdjacent].getData()[1]
                    }, visitedPlacesA[checkingAdjacent]);


                    currentReached[0] = visitedPlacesA[checkingAdjacent].getData()[0] + 1;
                    currentReached[1] = visitedPlacesA[checkingAdjacent].getData()[1];


                    for (int i = 0; i < totalVisitedB; i++) {

                        if ((currentReached[0] == visitedPlacesB[i].getData()[0]) && (currentReached[1] == visitedPlacesB[i].getData()[1])) {
                            foundCommonAdjacant = true;
                            break;
                        }

                    }
                    if (foundCommonAdjacant) {
                        break;
                    }


                    totalVisitedA++;

                }
            }
            //check the bottom adjacent point
            if ((getMap().points[visitedPlacesA[checkingAdjacent].getData()[1] + 1][visitedPlacesA[checkingAdjacent].getData()[0]] == 1) || (getMap().points[visitedPlacesA[checkingAdjacent].getData()[1] + 1][visitedPlacesA[checkingAdjacent].getData()[0]] == 2)) {
                boolean allreadyChecked = false;
                for (int k = 0; k < totalVisitedA; k++) {
                    if ((visitedPlacesA[checkingAdjacent].getData()[0] == visitedPlacesA[k].getData()[0]) && (visitedPlacesA[checkingAdjacent].getData()[1] + 1 == visitedPlacesA[k].getData()[1])) {

                        allreadyChecked = true;
                        break;
                    }
                }
                if (!allreadyChecked) {


                    visitedPlacesA[totalVisitedA] = new Node < int[] > (new int[] {
                            visitedPlacesA[checkingAdjacent].getData()[0], visitedPlacesA[checkingAdjacent].getData()[1] + 1
                    }, visitedPlacesA[checkingAdjacent]);


                    currentReached[0] = visitedPlacesA[checkingAdjacent].getData()[0];
                    currentReached[1] = visitedPlacesA[checkingAdjacent].getData()[1] + 1;

                    // if ghost is next to pacman


                    for (int i = 0; i < totalVisitedB; i++) {

                        if ((currentReached[0] == visitedPlacesB[i].getData()[0]) && (currentReached[1] == visitedPlacesB[i].getData()[1])) {
                            foundCommonAdjacant = true;
                            break;
                        }

                    }
                    if (foundCommonAdjacant) {
                        break;
                    }


                    // else we should also check if there's common found empty places

                    totalVisitedA++;
                }
            }
            //check the left adjacent point
            if ((getMap().points[visitedPlacesA[checkingAdjacent].getData()[1]][visitedPlacesA[checkingAdjacent].getData()[0] - 1] == 1) || (getMap().points[visitedPlacesA[checkingAdjacent].getData()[1]][visitedPlacesA[checkingAdjacent].getData()[0] - 1] == 2)) {
                boolean allreadyChecked = false;
                for (int k = 0; k < totalVisitedA; k++) {
                    if ((visitedPlacesA[checkingAdjacent].getData()[0] - 1 == visitedPlacesA[k].getData()[0]) && (visitedPlacesA[checkingAdjacent].getData()[1] == visitedPlacesA[k].getData()[1])) {

                        allreadyChecked = true;
                        break;
                    }
                }
                if (!allreadyChecked) {

                    visitedPlacesA[totalVisitedA] = new Node < int[] > (new int[] {
                            visitedPlacesA[checkingAdjacent].getData()[0] - 1, visitedPlacesA[checkingAdjacent].getData()[1]
                    }, visitedPlacesA[checkingAdjacent]);


                    currentReached[0] = visitedPlacesA[checkingAdjacent].getData()[0] - 1;
                    currentReached[1] = visitedPlacesA[checkingAdjacent].getData()[1];


                    for (int i = 0; i < totalVisitedB; i++) {

                        if ((currentReached[0] == visitedPlacesB[i].getData()[0]) && (currentReached[1] == visitedPlacesB[i].getData()[1])) {
                            foundCommonAdjacant = true;
                            break;
                        }

                    }
                    if (foundCommonAdjacant) {
                        break;
                    }



                    totalVisitedA++;
                }
            }




            /// BBBB











            //check the Player's upon adjacent point
            if ((getMap().points[visitedPlacesB[checkingAdjacent].getData()[1] - 1][visitedPlacesB[checkingAdjacent].getData()[0]] == 1) || (getMap().points[visitedPlacesB[checkingAdjacent].getData()[1] - 1][visitedPlacesB[checkingAdjacent].getData()[0]] == 2)) {
                boolean allreadyChecked = false;
                for (int k = 0; k < totalVisitedB; k++) {
                    if ((visitedPlacesB[checkingAdjacent].getData()[0] == visitedPlacesB[k].getData()[0]) && (visitedPlacesB[checkingAdjacent].getData()[1] - 1 == visitedPlacesB[k].getData()[1])) {

                        allreadyChecked = true;
                        break;
                    }
                }
                if (!allreadyChecked) {


                    visitedPlacesB[totalVisitedB] = new Node < int[] > (new int[] {
                            visitedPlacesB[checkingAdjacent].getData()[0], visitedPlacesB[checkingAdjacent].getData()[1] - 1
                    }, visitedPlacesB[checkingAdjacent]);


                    currentReached[0] = visitedPlacesB[checkingAdjacent].getData()[0];
                    currentReached[1] = visitedPlacesB[checkingAdjacent].getData()[1] - 1;




                    for (int i = 0; i < totalVisitedA; i++) {

                        if ((currentReached[0] == visitedPlacesA[i].getData()[0]) && (currentReached[1] == visitedPlacesA[i].getData()[1])) {
                            foundCommonAdjacant = true;
                            break;
                        }

                    }
                    if (foundCommonAdjacant) {
                        break;
                    }


                    totalVisitedB++;

                }
            }



            //check the right adjacent point
            if ((getMap().points[visitedPlacesB[checkingAdjacent].getData()[1]][visitedPlacesB[checkingAdjacent].getData()[0] + 1] == 1) || (getMap().points[visitedPlacesB[checkingAdjacent].getData()[1]][visitedPlacesB[checkingAdjacent].getData()[0] + 1] == 2)) {
                boolean allreadyChecked = false;
                for (int k = 0; k < totalVisitedB; k++) {
                    if ((visitedPlacesB[checkingAdjacent].getData()[0] + 1 == visitedPlacesB[k].getData()[0]) && (visitedPlacesB[checkingAdjacent].getData()[1] == visitedPlacesB[k].getData()[1])) {

                        allreadyChecked = true;
                        break;
                    }
                }
                if (!allreadyChecked) {

                    // Adding the found empty place to the array
                    visitedPlacesB[totalVisitedB] = new Node < int[] > (new int[] {
                            visitedPlacesB[checkingAdjacent].getData()[0] + 1, visitedPlacesB[checkingAdjacent].getData()[1]
                    }, visitedPlacesB[checkingAdjacent]);


                    currentReached[0] = visitedPlacesB[checkingAdjacent].getData()[0] + 1;
                    currentReached[1] = visitedPlacesB[checkingAdjacent].getData()[1];



                    for (int i = 0; i < totalVisitedA; i++) {

                        if ((currentReached[0] == visitedPlacesA[i].getData()[0]) && (currentReached[1] == visitedPlacesA[i].getData()[1])) {
                            foundCommonAdjacant = true;
                            break;
                        }

                    }
                    if (foundCommonAdjacant) {
                        break;
                    }
                    totalVisitedB++;

                }
            }
            //check the bottom adjacent point
            if ((getMap().points[visitedPlacesB[checkingAdjacent].getData()[1] + 1][visitedPlacesB[checkingAdjacent].getData()[0]] == 1) || (getMap().points[visitedPlacesB[checkingAdjacent].getData()[1] + 1][visitedPlacesB[checkingAdjacent].getData()[0]] == 1)) {
                boolean allreadyChecked = false;
                for (int k = 0; k < totalVisitedB; k++) {
                    if ((visitedPlacesB[checkingAdjacent].getData()[0] == visitedPlacesB[k].getData()[0]) && (visitedPlacesB[checkingAdjacent].getData()[1] + 1 == visitedPlacesB[k].getData()[1])) {

                        allreadyChecked = true;
                        break;
                    }
                }
                if (!allreadyChecked) {


                    visitedPlacesB[totalVisitedB] = new Node < int[] > (new int[] {
                            visitedPlacesB[checkingAdjacent].getData()[0], visitedPlacesB[checkingAdjacent].getData()[1] + 1
                    }, visitedPlacesB[checkingAdjacent]);


                    currentReached[0] = visitedPlacesB[checkingAdjacent].getData()[0];
                    currentReached[1] = visitedPlacesB[checkingAdjacent].getData()[1] + 1;





                    for (int i = 0; i < totalVisitedA; i++) {

                        if ((currentReached[0] == visitedPlacesA[i].getData()[0]) && (currentReached[1] == visitedPlacesA[i].getData()[1])) {
                            foundCommonAdjacant = true;
                            break;
                        }

                    }
                    if (foundCommonAdjacant) {
                        break;
                    }
                    // else we should also check if there's common found empty places

                    totalVisitedB++;
                }
            }
            //check the left adjacent point
            if ((getMap().points[visitedPlacesB[checkingAdjacent].getData()[1]][visitedPlacesB[checkingAdjacent].getData()[0] - 1] == 1) || (getMap().points[visitedPlacesB[checkingAdjacent].getData()[1]][visitedPlacesB[checkingAdjacent].getData()[0] - 1] == 1)) {
                boolean allreadyChecked = false;
                for (int k = 0; k < totalVisitedB; k++) {
                    if ((visitedPlacesB[checkingAdjacent].getData()[0] - 1 == visitedPlacesB[k].getData()[0]) && (visitedPlacesB[checkingAdjacent].getData()[1] == visitedPlacesB[k].getData()[1])) {

                        allreadyChecked = true;
                        break;
                    }
                }
                if (!allreadyChecked) {

                    visitedPlacesB[totalVisitedB] = new Node < int[] > (new int[] {
                            visitedPlacesB[checkingAdjacent].getData()[0] - 1, visitedPlacesB[checkingAdjacent].getData()[1]
                    }, visitedPlacesB[checkingAdjacent]);


                    currentReached[0] = visitedPlacesB[checkingAdjacent].getData()[0] - 1;
                    currentReached[1] = visitedPlacesB[checkingAdjacent].getData()[1];



                    for (int i = 0; i < totalVisitedA; i++) {

                        if ((currentReached[0] == visitedPlacesA[i].getData()[0]) && (currentReached[1] == visitedPlacesA[i].getData()[1])) {
                            foundCommonAdjacant = true;
                            break;
                        }

                    }
                    if (foundCommonAdjacant) {
                        break;
                    }

                    totalVisitedB++;
                }
            }




            checkingAdjacent++;


        }


        //Getting the path from the Ghost to the middle point
        int commonSpot = 0;
        for (int i = 0; i < visitedPlacesA.length; i++) {
            if ((currentReached[0] == visitedPlacesA[i].getData()[0]) && (currentReached[1] == visitedPlacesA[i].getData()[1])) {

                commonSpot = i;
                break;
            }
        }
        Node < int[] > myNode = visitedPlacesA[commonSpot];
        int[][] myNodesArray = new int[400][2];
        int countMyNodes = 0;
        int countZeros = 0;

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


        setTotalSearched(totalVisitedA + totalVisitedB);









        if (!isEscape()) {

            this.setStepX(myNodesArray[1][0]);
            this.setStepY(myNodesArray[1][1]);
            this.setX((myNodesArray[1][0]) * 20);
            this.setY((myNodesArray[1][1]) * 20);
        } else {

            // Run Away

            this.setStepX(myNodesArray[0][0]);
            this.setStepY(myNodesArray[0][1]);
            if (myNodesArray[0][0] - myNodesArray[1][0] == 1) {

                if (getMap().points[getStepY()][getStepX() + 1] != 0) {
                    setX(getX() + 20);
                    setStepX(getStepX() + 1);
                    if ((this.getX() == 540) && (this.getY() == 300)) {
                        this.setX(520);
                        setStepX(26);
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
                        this.setX(20);
                        setStepX(1);
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

    }


    // Bidirectional search ends here

}