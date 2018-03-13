

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