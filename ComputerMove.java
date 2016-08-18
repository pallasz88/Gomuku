import java.util.Random;
import java.math.BigInteger;
import java.awt.Color;

public class ComputerMove{


  public static void makeMove( Gui gui, GameBoard board, Color color ){
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
/*
  public static int callComputer( GameBoard board ){
    int alpha = -Integer.MAX_VALUE;
    int beta  =  Integer.MAX_VALUE;
    Master.negaMax( board, 3, alpha, beta, 1 );
  }
*/
}