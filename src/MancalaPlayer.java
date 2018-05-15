/**
 * citation: https://github.com/mitchell3/SWE443-A5
 * 
 * This class handles operations for a single Mancala player
 */

public class MancalaPlayer
{
	private boolean activePlayer;
	private MancalaHouse store;
	private MancalaHouse firstHouse;
	private boolean takeNextTurn;

	public MancalaHouse getFirstHouse() 
	{
		return firstHouse;
	}

	public boolean isActive() 
	{
		return activePlayer;
	}

	public void setActivePlayer(boolean b)
	{
		this.activePlayer = b;
	}

	public MancalaHouse getStore() 
	{
		return this.store;
	}

	public MancalaHouse setStore(MancalaHouse store)
	{
		MancalaHouse oldStore = this.store;
		this.store = store;
		if((oldStore != null) && (oldStore != store))
		{
			store.setPlayer(this);
		}
		return this.store;
	}

	public MancalaHouse setFirst(MancalaHouse first)
	{
		MancalaHouse oldFirst = this.firstHouse;
		this.firstHouse = first;
		if((oldFirst != null) && (oldFirst != first))
		{
			first.setIsFirst(this);
			oldFirst.setNotFirst();
		}
		return this.firstHouse;
	}

	public boolean setTakeNextTurn(boolean takeNextTurn)
	{
		boolean oldval = this.takeNextTurn;
		this.takeNextTurn = takeNextTurn;
		//return the old value
		return oldval;
	}

	//return value: true if active player takes next turn as well.
	//              false if next player should become active.
	public boolean selectHouse(MancalaHouse mancalaHouse)
	{
		if(mancalaHouse == null)
		{
			return false;
		}
		if(mancalaHouse.getIsStore())
		{
			throw new IllegalArgumentException("You cannot redistribute from your store!");
		}
		if(mancalaHouse.getNumPebbles()==0)
		{
			throw new IllegalArgumentException("You must choose a house with pebbles in it!");
		}
		this.takeNextTurn = false;
		mancalaHouse.redistributePebblesCounterclockwise();
		return this.takeNextTurn;
	}
}
