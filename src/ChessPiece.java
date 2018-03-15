
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Translate;

//class declaration - abstract because we will not want to create a Piece object but we would
//like to specify the private fields that all pieces should have in addition to their behaviours
public abstract class ChessPiece extends Group{

	// Piece can be either white (1) or black (2)
	protected int type;
	// Position of the piece on the board
	protected int xPos;
	protected int yPos;
	// Name of the piece
	protected String name;
	// ImageView
	protected ImageView imageView = new ImageView();
	// Position 
	protected Translate pos;
	// GameLogic
	protected ChessGameLogic chessGameLogic = new ChessGameLogic();
	// Variable to know if the piece can move in a check situation
	protected boolean isASavior = false;
	
	public ChessPiece(int type, int xPos, int yPos) {
		this.type = type;
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	// Select method: When a piece is selected by a first click
	// Highlight all the available position where the piece can go
	public void SelectPiece(ChessBoard board) {
	}
	
	// Move method: When a piece is already selected and that the player click on a highlighted position
	// Change the position of the piece and update the board
	public void MovePiece(ChessBoard chessBoard, int x, int y) {
		chessBoard.setBoard(this.xPos, this.yPos, 0);
		chessBoard.setPiece(this.xPos, this.yPos, null);
		this.xPos = x;
		this.yPos = y;
		if (chessBoard.getPiece(x, y) != null)
			chessBoard.getPiece(x, y).capture(chessBoard);
		chessBoard.setBoard(x, y, this.type);
		chessBoard.setPiece(x, y, this);
		if (this.name == "Pawn" && ((this.type == 1 && this.yPos == 0) || (this.type == 2 && this.yPos == 5)))
		{
			chessBoard.createPromotePiece(this);
			if (this.type == 1)
				chessBoard.playerOnePawn--;
			else
				chessBoard.playerTwoPawn--;
		}
	}
	
	// Return the image of the piece
	public ImageView getImage() {
		return (imageView);
	}
	
	public void centerImage() {
        Image img = imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);

        }
    }
	
	// Capture method: When a piece is captured by another one
	public void capture(ChessBoard chessBoard) {
		if (this.type == 1)
		{
			if (this.name == "Rook")
				chessBoard.playerOneRook--;
			else if (this.name == "Queen")
				chessBoard.playerOneQueen--;
			else if (this.name == "Pawn")
				chessBoard.playerOnePawn--;
			else if (this.name == "Bishop" && (this.xPos + this.yPos) % 2 != 0)
				chessBoard.playerOneBishopDarkSquare--;
			else if (this.name == "Bishop" && (this.xPos + this.yPos) % 2 == 0)
				chessBoard.playerOneBishopLightSquare--;
		}
		else
		{
			if (this.name == "Rook")
				chessBoard.playerTwoRook--;
			else if (this.name == "Queen")
				chessBoard.playerTwoQueen--;
			else if (this.name == "Pawn")
				chessBoard.playerTwoPawn--;
			else if (this.name == "Bishop" && (this.xPos + this.yPos) % 2 == 0)
				chessBoard.playerTwoBishopLightSquare--;
			else if (this.name == "Bishop" && (this.xPos + this.yPos) % 2 != 0)
				chessBoard.playerTwoBishopDarkSquare--;
		}
		chessBoard.getChildren().remove(this.getImage());
	}

	public void resize(double width, double height) {
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);
	}

	// overridden version of the relocate method
	public void relocate(double x, double y) {
		imageView.setTranslateX(x);
		imageView.setTranslateY(y);	
		centerImage();
	}
	
	public void resetPiece()
	{
		this.isASavior = false;
	}

	
	public int getX(){
		return this.xPos;
	}
	
	public int getY(){
		return this.yPos;
	}
	

}
