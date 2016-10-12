import java.util.Random;
import java.math.BigInteger;
import java.awt.Color;

public class SmartPlayer extends Computer{

  SmartPlayer( boolean isPlaying, boolean isFirst ){
    super( isPlaying, isFirst );
  }

  @Override
  public void makeMove( Gui gui, GameBoard board, Color color ){
    int square = pickMove( board, color );
    gui.setButton( square, color );
    if( color == Color.RED ){
      board.setRedBoard( square );
    }
    else
      board.setBlueBoard( square );
  }

  public static int pickMove( GameBoard board, Color color ){
    int thinkColor = ( color == Color.RED ) ? 1 : -1;
    int alpha = -Integer.MAX_VALUE;
    int beta  =  Integer.MAX_VALUE;
    return Master.negaMax( board, 2, alpha, beta, thinkColor )[ 0 ];
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