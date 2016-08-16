import java.util.Random;
import java.math.BigInteger;
import java.awt.Color;

public class ComputerMove{


  public static void makeMove( Board board, Color color ){
    BigInteger shiftMe = BigInteger.ONE;
    int square = pickMove( board );
    board.setButton( square, color );
    if( color == Color.RED )
      board.setRedBoard( board.getRedBoard().or( shiftMe.shiftLeft( square ) ) );
    else
      board.setBlueBoard( board.getBlueBoard().or( shiftMe.shiftLeft( square ) ) );
  }

  public static int pickMove( Board board ){
    BigInteger empties = board.getEmptySquares();
    Random rand = new Random();
    int row;
    int col;
    int square;
    do{
      row = rand.nextInt( board.getBoardSize() );
      col = rand.nextInt( board.getBoardSize() );
      square = row * board.getBoardSize() + col;
    }while( !empties.shiftRight( board.getShiftNum( row,col ) ).and( BigInteger.ONE ).equals(
                                                                     BigInteger. ONE) );
    return square;
  }

}