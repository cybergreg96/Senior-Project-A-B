import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Card extends ImageView {

    private static final String emptyCard = "file:src/birds/unknown.png";

    private String url;
    private boolean flipped;

    public Card(String url) {
        super(new Image(emptyCard, 100, 100, true, true));
        this.url = url;
    }

    public void flip() {
        setImage(new Image(url, 100, 100, true, true));
        flipped = true;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void show() {
        setImage(new Image(url, 100, 100, true, true));
    }

    public void hide() {
        setImage(new Image(emptyCard, 100, 100, true, true));
    }

    public boolean equals(Object obj) {
        return obj instanceof Card
                && ((Card)obj).url.equals(url);
    }
}
