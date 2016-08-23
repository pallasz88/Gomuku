import java.util.Random;
import java.math.BigInteger;
import java.awt.Color;

public class RandomPlayer extends Computer{

  RandomPlayer( boolean isPlaying, boolean isFirst ){
    super( isPlaying, isFirst );
  }

  @Override
  public void makeMove( Gui gui, GameBoard board, Color color ){
    int square = pickMove( board );
    gui.setButton( square, color );
    if( color == Color.RED ){
      board.setRedBoard( square );
    }
    else
      board.setBlueBoard( square );
  }

  public static int pickMove( GameBoard board ){
    BigInteger empties = board.getEmptySquares();
    Random rand        = new Random();
    int boardSize      = board.getBoardSize();
    int row;
    int col;
    int square;
    do{
      row    = rand.nextInt( boardSize );
      col    = rand.nextInt( boardSize );
      square = board.getShiftNum( row, col );
    }while( !empties.testBit( square ) );
    return square;
  }

  @Override
  public void makeComputerFirstMoveIfNeeded( Gui gui, GameBoard board ){
    if( isComputerPlaying && isComputerFirst ){
      makeMove( gui, board, Color.RED );
      board.decrementLegalMovesNum();
      board.initRedPlayer();
      board.switchRedPlayer();
    }
  }

}