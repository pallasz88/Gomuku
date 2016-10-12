import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.math.BigInteger;

public class Master{

  public static int[] negaMax( GameBoard board, int depth, int alpha, int beta, int color ){
    try{
      List<Integer> emptySquareArray = board.getEmptySquareList();

      if( depth == 0 ){
        return new int[] { emptySquareArray.get( 0 ), color * eval( board ) };
      }

      int bestValue = Integer.MIN_VALUE;
      int bestMove  = emptySquareArray.get( 0 );

      while( emptySquareArray.size() > 0 ){
        GameBoard newBoard = new GameBoard( board );
        int tryMove = emptySquareArray.get( 0 ).intValue();
        newBoard.getComputerPlayer().tryMove( newBoard, color, tryMove );
        int tempScore = -negaMax( newBoard, depth-1, -beta, -alpha, -color )[1];
        if( bestValue < tempScore ){
          bestValue = tempScore;
          bestMove  = tryMove;
        }
        //System.out.println( "bestMove: " + bestMove + " Depth: " + depth );
        alpha = ( alpha < tempScore )? tempScore : alpha;
        if( alpha >= beta )
          break;
        emptySquareArray.remove( 0 );
      }
      //System.out.println( "Current position:" + bestValue );
      return new int [] { bestMove, bestValue };
    }
    catch(  Exception e ){
      System.out.println( "Error" );
      return new int [] { 0, 0 };
    }
  }

  public static int eval( GameBoard board ) {
    int point = 0;
    int size = board.getBoardSize();
    if( evalMasks( size, size-4, board.getWinHorizMask(), board ) )
      point += 100;
    if( evalMasks( size-4, size, board.getWinVertcMask(), board ) )
      point += 100;
    if( evalMasks( size-4, size-4, board.getWinDiagLMask(), board ) )
      point += 100;
    if( evalMasks( size-4, size-4, board.getWinDiagRMask(), board ) )
      point += 100;
    return point;
  }

  private static boolean evalMasks( int x, int y, BigInteger mask, GameBoard board ){
    for( int row = 0; row < x; row++ ){
      for( int col = 0; col < y; col++ ){
        int shift = board.getShiftNum( row, col );
        if( evalPattern( board.getRedBoard(), mask, shift ) ){
          return true;
        }
        else if( evalPattern( board.getBlueBoard(), mask, shift ) ){
          return true;
        }
      }
    }
    return false;
  }

  private static boolean evalPattern( BigInteger playerBoard, BigInteger mask, int shift ){
    return playerBoard.shiftRight( shift ).and( mask ).equals( mask );
  }
}