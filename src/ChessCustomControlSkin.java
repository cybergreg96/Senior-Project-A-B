/*
 * This class references the project at this link: https://github.com/GuiBon/ChessGame
 */


//basic implementation of a Skin

//imports 
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;

//class definition 
class ChessCustomControlSkin extends SkinBase<ChessCustomControl> implements Skin<ChessCustomControl> {
	public ChessCustomControlSkin(ChessCustomControl cc) {
		//call the super class constructor
		super(cc);
	}
}