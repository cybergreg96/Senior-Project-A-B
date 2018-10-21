import java.io.Serializable;

public class PacmanScore implements Comparable<PacmanScore>, Serializable
{
	private int score;
	private String playerName;
	
	public PacmanScore()
	{
		score = 0;
		playerName = "aaa";
	}
	
	public PacmanScore(int s, String n)
	{
		score = s;
		playerName = n;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void setScore(int s)
	{
		score = s;
	}
	
	public String getPlayerName()
	{
		return playerName;
	}
	
	public void setPlayerName(String n)
	{
		playerName = n;
	}

	@Override
	public int compareTo(PacmanScore o) {
		if(this.score > o.getScore())
		{
			return 1;
		}
		else if(this.score < o.getScore())
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}

	
	
}
