import java.math.BigInteger;


public class GameBoard{

  private static int size;
  private static int legalMovesNum;
  private static boolean redPlayer         = true;
  private static boolean isComputerPlaying = true;
  private static boolean isComputerFirst   = true;
  private BigInteger redBoard              = BigInteger.ZERO;
  private BigInteger blueBoard             = BigInteger.ZERO;
  private BigInteger winHorizMask;
  private BigInteger winVertcMask;
  private BigInteger winDiagRMask;
  private BigInteger winDiagLMask;

  GameBoard( int size ){
    this.size = size;
    initLegalMovesNum();
    initMasks();
  }

  public void setBoardSize( int newSize ){
    size = newSize;
  }

  public int getBoardSize(){
    return size;
  }

  public BigInteger getRedBoard(){
    return redBoard;
  }

  public BigInteger getBlueBoard(){
    return blueBoard;
  }

  public void setRedBoard( BigInteger board ){
    redBoard = board;
  }

  public void setRedBoard( int clicked ){
    redBoard = redBoard.setBit( clicked );
  }

  public void setBlueBoard( BigInteger board ){
    blueBoard = board;
  }

  public void setBlueBoard( int clicked ){
    blueBoard = blueBoard.setBit( clicked );
  }

  public void decrementLegalMovesNum(){
    legalMovesNum--;
  }

  public int getLegalMovesNum(){
    return legalMovesNum;
  }

  public void initLegalMovesNum(){
    legalMovesNum = size * size;
  }

  public void initRedPlayer(){
    redPlayer = true;
  }

  public void switchRedPlayer(){
    redPlayer = !redPlayer;
  }

  public boolean getRedPlayer(){
    return redPlayer;
  }

  public static boolean getIsComputerPlaying(){
    return isComputerPlaying;
  }

  public static boolean getIsComputerFirst(){
    return isComputerFirst;
  }

  public void setIsComputerPlaying( boolean plays ){
    isComputerPlaying = plays;
  }

  public void setIsComputerFirst( boolean first ){
    isComputerFirst = first;
  }

  public BigInteger getEmptySquares(){
    BigInteger base = new BigInteger( "2" );
    BigInteger allBoard = base.pow( size*size ).subtract( new BigInteger( "1" ) );
    return redBoard.or( blueBoard ).xor( allBoard );
  }

  public int[] getEmptySquareArray(){
    BigInteger emptyBitBoard = getEmptySquares();
    int[] emptySquareArray = new int[ size * size ];
    int i=0;
    for( int sq=0; sq < size*size; sq++) {
      if( emptyBitBoard.testBit( sq ) )
        emptySquareArray[ i ] = sq;
      else
        emptySquareArray[ i ] = -1;
      i++;
    }
    return emptySquareArray;
  }

  private void initMasks(){
    BigInteger base = new BigInteger( "2" );
    winHorizMask = BigInteger.ONE.add( base.pow( 1 ) )
                                 .add( base.pow( 2 ) )
                                 .add( base.pow( 3 ) )
                                 .add( base.pow( 4 ) );

    winVertcMask = BigInteger.ONE.add( base.pow( 1 * size ) )
                                 .add( base.pow( 2 * size ) )
                                 .add( base.pow( 3 * size ) )
                                 .add( base.pow( 4 * size ) );

    winDiagRMask = BigInteger.ONE.add( base.pow( 1 * ( size + 1 ) ) )
                                 .add( base.pow( 2 * ( size + 1 ) ) )
                                 .add( base.pow( 3 * ( size + 1 ) ) )
                                 .add( base.pow( 4 * ( size + 1 ) ) );

    winDiagLMask =  base.pow( 4 ).add( base.pow( 1 * ( size - 1 ) + 4 ) )
                                 .add( base.pow( 2 * ( size - 1 ) + 4 ) )
                                 .add( base.pow( 3 * ( size - 1 ) + 4 ) )
                                 .add( base.pow( 4 * ( size - 1 ) + 4 ) );
  }

  public boolean isNewGameNeeded( Gui gui ){
    if( isGameOver( gui ) ){
      initNewGame( gui );
      return true;
    }
    return false;
  }

  private boolean isGameOver( Gui gui ){
    String message = "";
    if( isWin() ){
      message = ( redPlayer ) ? "Nyert a piros játékos!" : "Nyert a kék játékos!";
      gui.showMessage( message );
      return true;
    }
    if( isDraw() ){
      message = "Döntetlen";
      gui.showMessage( message );
      return true;
    }
    return false;
  }

  private boolean isDraw(){
    BigInteger base = new BigInteger( "2" );
    BigInteger allBoard = base.pow( size*size ).subtract( new BigInteger( "1" ) );
    if( redBoard.or( blueBoard ).equals( allBoard ) ){
      return true;
    }
    return false;
  }

  private boolean isWin(){

    if( checkWin( size, size-4, winHorizMask ) ){
      return true;
    }

    if( checkWin( size-4, size, winVertcMask ) ){
      return true;
    }

    if( checkWin( size-4, size-4, winDiagRMask ) ){
      return true;
    }

    if( checkWin( size-4, size-4, winDiagLMask ) ){
      return true;
    }
    return false;
  }

  private boolean isWinningPattern( BigInteger playerBoard, BigInteger mask, int shift ){
    return playerBoard.shiftRight( shift ).and( mask ).equals( mask );
  }

  public int getShiftNum( int row, int col ){
    return row * size + col;
  }

  private boolean checkWin( int x, int y, BigInteger mask){
    for( int row = 0; row < x; row++ ){
      for( int col = 0; col < y; col++ ){
        int shift = getShiftNum( row, col );
        if( redPlayer && isWinningPattern( redBoard, mask, shift ) ){
            return true;
        }
        else if( !redPlayer && isWinningPattern( blueBoard, mask, shift ) ){
            return true;
        }
      }
    }
    return false;
  }

  public void initNewGame( Gui gui ){
    setRedBoard( BigInteger.ZERO );
    setBlueBoard( BigInteger.ZERO );
    initRedPlayer();

    gui.initButtons();

    initLegalMovesNum();

    gui.makeComputerFirstMoveIfNeeded();
  }

  public static void main( String[] args ){
    int size = 19;
    GameBoard board = new GameBoard( size );
    Gui gui         = new Gui( size, board );
    gui.makeComputerFirstMoveIfNeeded();
  }

}
