package ConcentrationGame;
import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/*
 * This class references the project at this link: https://github.com/EvanTich/Memory
 * 
 * This class assigns an image to a concentration card and handles the flipping of the card
 */
public class ConcentrationCard extends ImageView 
{

	private static final String emptyCard = "file:src/birds/unknown.png";
	private String url;
	private boolean flipped;

	private Boolean isMatched;

	public ConcentrationCard(String url) 
	{
		super(new Image(emptyCard, 100, 100, true, true));
		this.url = url;

		this.isMatched = false;
		this.flipped = false;

	}

	public void setMatched(Boolean trueOrFalse) 
	{
		isMatched = trueOrFalse;
	}

	public Boolean isMatched() 
	{
		return isMatched;
	}

	/**
	 * flip to the opposite side of the card
	 * If face-down, color is grey
	 * If face-up, color is myColor
	 */
	public void flipIt() 
	{
		this.flipped = !flipped;
		if (flipped)
		{
			setImage(new Image(url, 100, 100, true, true));
		} 
		else
		{
			setImage(new Image(emptyCard, 100, 100, true, true));
		}
	}

	public void reveal() 
	{
		setImage(new Image(url, 100, 100, true, true));
	}


	public boolean isFlipped() 
	{
		return flipped;
	}

	public boolean equals(Object obj) 
	{
		return obj instanceof ConcentrationCard
				&& ((ConcentrationCard)obj).url.equals(url);
	}
}
