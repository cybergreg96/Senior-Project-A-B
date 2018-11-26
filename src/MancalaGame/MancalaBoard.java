package MancalaGame;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * citation: https://github.com/mitchell3/SWE443-A5
 * 
 * This class sets up the mancala board and handles the game mechanics
 */
public class MancalaBoard
{
	private int housesPerPlayer;
	private String gameTitle;
	private List<MancalaPlayer> mancalaPlayers = new LinkedList<MancalaPlayer>();
	private int pebblesPerHouse;
	private MancalaPlayer activePlayer;

	public List<MancalaPlayer> getPlayers() 
	{
		return this.mancalaPlayers;
	}

	//this class creates the mancala board and sets up the beginning of the game
	public static MancalaBoard createGame(int housesPerPlayer, int numPlayers, String gameTitle, int pebblesPerHouse)
	{
		MancalaBoard gameBoard = new MancalaBoard();
		gameBoard.setHousesPerPlayer(housesPerPlayer);
		gameBoard.setPebblesPerHouse(pebblesPerHouse);
		gameBoard.setGameTitle(gameTitle);

		//fills houses based on the number of players
		for(int i=0; i<numPlayers; i++)
		{
			MancalaPlayer mancalaPlayer = new MancalaPlayer();

			MancalaHouse firstHouse = new MancalaHouse();
			firstHouse.setPlayer(mancalaPlayer);
			firstHouse.setNumPebbles(pebblesPerHouse);
			mancalaPlayer.setFirst(firstHouse);

			MancalaHouse tempHouse = firstHouse;
			for(int j=1; j<housesPerPlayer; j++)
			{
				MancalaHouse nextHouse = new MancalaHouse();
				nextHouse.setNumPebbles(pebblesPerHouse);
				nextHouse.setPlayer(mancalaPlayer);
				tempHouse.setSuccessor(nextHouse);
				tempHouse = nextHouse;
			}

			MancalaHouse store = new MancalaHouse();
			store.setNumPebbles(0);
			store.setPlayer(mancalaPlayer);
			store.setIsStore(mancalaPlayer);
			tempHouse.setSuccessor(store);

			gameBoard.addPlayer(mancalaPlayer);
		}
		
		//sets each store to point to first house of next player as successor
		for (int i=0; i<numPlayers; i++)
		{
			MancalaHouse storei = gameBoard.mancalaPlayers.get(i).getStore();
			MancalaHouse nextFirst = gameBoard.mancalaPlayers.get((i+1)%numPlayers).getFirstHouse();
			storei.setSuccessor(nextFirst);
		}

		return gameBoard;
	}

	public String getGameTitle() 
	{
		return gameTitle;
	}

	public void setGameTitle(String gameTitle)
	{
		this.gameTitle = gameTitle;
	}

	public int getHousesPerPlayer()
	{
		return this.housesPerPlayer;
	}

	public void setHousesPerPlayer(int housesPerPlayer) 
	{
		if (housesPerPlayer <= 0) 
		{
			throw new IllegalArgumentException("Players must have at least one house each!");
		}
		this.housesPerPlayer = housesPerPlayer;
	}

	public void addPlayer(MancalaPlayer mancalaPlayer)
	{
		if(mancalaPlayer != null)
		{
			boolean present = mancalaPlayers.contains(mancalaPlayer);
			if(!present)
			{
				mancalaPlayers.add(mancalaPlayer);
			}
		}
	}

	public int getPebblesPerHouse()
	{
		return this.pebblesPerHouse;
	}

	public void setPebblesPerHouse(int pebblesPerHouse) 
	{
		if(pebblesPerHouse <= 0){
			throw new IllegalArgumentException("Houses must have at least one pebble each!");
		}
		this.pebblesPerHouse = pebblesPerHouse;
	}

	public void setActivePlayer(MancalaPlayer mancalaPlayer)
	{
		if (mancalaPlayer == null)
		{
			throw new IllegalArgumentException("Active player should not be null.");
		}
		else if (mancalaPlayers.contains(mancalaPlayer))
		{
			this.activePlayer = mancalaPlayer;
		}
		else
		{
			throw new IllegalArgumentException("Player must be added to the game before becoming active!");
		}
	}

	//alternates the active player so they take turns
	public void incrActivePlayer()
	{
		//move to next player in list. If last in line, return to first player.
		if (activePlayer == null)
		{
			//assume it's the start of the game. Attempt to set first player as active.
			if(mancalaPlayers.isEmpty())
			{
				throw new IllegalArgumentException("No players in game!");
			}
			else
			{
				setActivePlayer(mancalaPlayers.get(0));
			}
		}
		else
		{
			MancalaPlayer nextPlayer = mancalaPlayers.get(mancalaPlayers.indexOf(activePlayer)+1);
			if (nextPlayer == null)
			{ 
				nextPlayer = mancalaPlayers.get(0);
			}
			setActivePlayer(nextPlayer);
		}
	}
}