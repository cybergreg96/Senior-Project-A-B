


//imports 
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;

/*
 * This class references the project at this link: https://github.com/GuiBon/ChessGame
 * 
 * This class is a basic implementation of a Skin and assists with the formatting of the chess game
 */
class ChessCustomControlSkin extends SkinBase<ChessCustomControl> implements Skin<ChessCustomControl> {
	public ChessCustomControlSkin(ChessCustomControl cc) {
		//call the super class constructor
		super(cc);
	}
}