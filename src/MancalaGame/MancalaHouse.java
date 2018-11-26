package MancalaGame;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

/**
 * citation: https://github.com/mitchell3/SWE443-A5
 * 
 * This class handles all operations for a single Mancala House
 */
public class MancalaHouse implements Iterable
{
	private int houseNumber;
	private MancalaPlayer mancalaPlayer;
	private int numPebbles;
	private boolean isStore;
	private boolean isFirst;
	private MancalaHouse next;
	private List<MancalaHouse> opposite = new LinkedList<MancalaHouse>();

	public int getHouseNumber() 
	{
		return houseNumber;
	}

	public void setHouseNumber(int houseNumber) 
	{
		if(houseNumber == 0)
		{
			throw new IllegalArgumentException("House number must not be zero!");
		}
		else if(houseNumber < 0)
		{
			throw new IllegalArgumentException("House number must be positive!");
		}
		this.houseNumber = houseNumber;
	}

	public boolean isFirst()
	{
		return this.isFirst;
	}

	public void setIsFirst(MancalaPlayer mancalaPlayer)
	{
		this.setPlayer(mancalaPlayer);
		boolean wasFirst = this.isFirst;
		this.isFirst = true;
		if(! wasFirst)
		{
			mancalaPlayer.setFirst(this);
		}
	}

	public void setNotFirst()
	{
		this.isFirst = false;
	}

	public MancalaPlayer getPlayer()
	{
		return this.mancalaPlayer;
	}

	public void setPlayer(MancalaPlayer mancalaPlayer)
	{
		MancalaPlayer oldPlayer = this.mancalaPlayer;
		this.mancalaPlayer = mancalaPlayer;
		if((oldPlayer != null) && (oldPlayer != mancalaPlayer))
		{
			if(this.isStore)
			{
				oldPlayer.setStore(null);
			}
			if(this.isFirst)
			{
				oldPlayer.setFirst(this.next);
			}
		}
	}

	public int getNumPebbles() 
	{
		return numPebbles;
	}

	public void setNumPebbles(int numPebbles)
	{
		this.numPebbles = numPebbles;
	}

	public boolean getIsStore()
	{
		return isStore;
	}

	public void setIsStore(MancalaPlayer mancalaPlayer)
	{
		this.setPlayer(mancalaPlayer);
		boolean wasStore = this.isStore;
		this.isStore = true;
		if(!wasStore)
		{
			mancalaPlayer.setStore(this);
		}
	}

	public MancalaHouse successor()
	{
		return this.next;
	}

	public void setSuccessor(MancalaHouse next)
	{
		this.next = next;
	}

	public List<MancalaHouse> getOpposite() 
	{
		return Collections.unmodifiableList(opposite);
	}

	public boolean addOpposite(MancalaHouse mancalaHouse)
	{
		boolean skipAdd = opposite.contains(mancalaHouse);
		if (skipAdd)
		{
			return false;
		}
		else 
		{
			opposite.add(mancalaHouse);
			mancalaHouse.addOpposite(this);
			return true;
		}
	}

	public boolean removeOpposite(MancalaHouse mancalaHouse)
	{
		boolean changed = opposite.remove(mancalaHouse);
		if(changed && (mancalaHouse != null))
		{
			mancalaHouse.removeOpposite(this);
		}
		return changed;
	}

	public void takeOppositePebbles()
	{
		Iterator<MancalaHouse> itr = opposite.listIterator(0);
		int pebblesTally = 0;
		while (itr.hasNext())
		{
			MancalaHouse opp = itr.next();
			if(opp.getNumPebbles() > 0)
			{
				pebblesTally = pebblesTally + opp.getNumPebbles();
				opp.setNumPebbles(0);
			}
		}
		if(this.mancalaPlayer != null) 
		{
			int prevScore = this.mancalaPlayer.getStore().getNumPebbles();
			this.mancalaPlayer.getStore().setNumPebbles(prevScore + pebblesTally);
		}
	}

	public MancalaHouse redistributePebblesCounterclockwise()
	{
		if(this.numPebbles == 0)
		{ 
			return null; 
		}
		int pebblesHeld = this.numPebbles;
		MancalaHouse tempHouse = this;
		while (pebblesHeld > 0)
		{
			tempHouse = tempHouse.successor();
			tempHouse.setNumPebbles(tempHouse.getNumPebbles()+1);
			pebblesHeld --;
		}
		if(tempHouse.mancalaPlayer == this.mancalaPlayer)
		{
			if(tempHouse.isStore && (this.mancalaPlayer.getStore() == tempHouse))
			{
				//player takes extra turn
				this.mancalaPlayer.setTakeNextTurn(true);
			}
			if(tempHouse.getNumPebbles()==1) 
			{
				//know that last pebble was placed in an empty house
				tempHouse.takeOppositePebbles();
			}
		}
		this.numPebbles = 0;
		return tempHouse;
	}

	public Iterator iterator() 
	{
		return null;
	}
}
