import java.util.Random;
import java.math.BigInteger;
import java.awt.Color;

public class ComputerMove{
  
  
  public static void makeMove( Board board, Color color ){
    BigInteger shiftMe = BigInteger.ONE;
    System.out.println("It's computer turn");
    int square = pickMove( board );
    System.out.println( "Square: " + square );
    board.setButton( square, color);
    if( color == Color.BLUE )
      board.setBlueBoard( board.getBlueBoard().or( shiftMe.shiftLeft( square ) ) );
    else
      board.setRedBoard( board.getRedBoard().or( shiftMe.shiftLeft( square ) ) );
  }
  
  public static int pickMove( Board board ){
    BigInteger empties = board.getEmptySquares();
    System.out.println( "Empty: " + empties );
    BigInteger shiftMe = BigInteger.ONE;
    Random rand = new Random();
    int row;
    int col;
    int square;
    do{
      row = rand.nextInt( board.getBoardSize() );
      col = rand.nextInt( board.getBoardSize() );
      square = row * board.getBoardSize() + col;
    }while( !empties.shiftRight( square ).and( BigInteger.ONE ).equals( BigInteger. ONE) );
    return square;
  }
  
}