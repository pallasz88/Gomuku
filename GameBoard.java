import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;


public class GameBoard{

  private int size;
  private int legalMovesNum;
  private boolean redPlayer        = true;
  private BigInteger redBoard      = BigInteger.ZERO;
  private BigInteger blueBoard     = BigInteger.ZERO;
  private static Computer computerPlayer;
  private BigInteger winHorizMask;
  private BigInteger winVertcMask;
  private BigInteger winDiagRMask;
  private BigInteger winDiagLMask;

  GameBoard( int     size,
             boolean isComputerPlaying,
             boolean isComputerFirst,
             int mode ){
    this.size = size;
    initLegalMovesNum();
    initMasks();
    if( mode == 1 )
      computerPlayer = new RandomPlayer( isComputerPlaying, isComputerFirst );
    else if( mode == 2 )
      computerPlayer = new SmartPlayer( isComputerPlaying, isComputerFirst );
  }

  public Computer getComputerPlayer() {
    return ( Computer )computerPlayer;
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

  public BigInteger getEmptySquares(){
    BigInteger base = new BigInteger( "2" );
    BigInteger allBoard = base.pow( size*size ).subtract( new BigInteger( "1" ) );
    return redBoard.or( blueBoard ).xor( allBoard );
  }

  public List< Integer > getEmptySquareList(){
    BigInteger emptyBitBoard = getEmptySquares();
    List<Integer> emptySquareArray = new ArrayList< Integer >();
    for( int sq=0; sq < size*size; sq++) {
      if( emptyBitBoard.testBit( sq ) )
        emptySquareArray.add( sq );
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
    if( isWin( gui ) ){
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
    BigInteger allBoard = base.pow( size*size ).subtract( BigInteger.ONE );
    if( redBoard.or( blueBoard ).equals( allBoard ) ){
      return true;
    }
    return false;
  }

  private boolean isWin( Gui gui ){

    if( checkWin( size, size-4, winHorizMask, gui ) ){
      return true;
    }

    if( checkWin( size-4, size, winVertcMask, gui ) ){
      return true;
    }

    if( checkWin( size-4, size-4, winDiagRMask, gui ) ){
      return true;
    }

    if( checkWin( size-4, size-4, winDiagLMask, gui ) ){
      return true;
    }
    return false;
  }

  private void showWinningPattern( Gui gui, BigInteger mask, int shift ){
    BigInteger pattern = mask;
    pattern = pattern.shiftLeft( shift );
    for( int i=0; i < size * size; i++ ){
      if( pattern.testBit( i ) ){
        gui.showOneWinningButton( i );
      }
    }
  }

  private boolean isWinningPattern( BigInteger playerBoard, BigInteger mask, int shift ){
    return playerBoard.shiftRight( shift ).and( mask ).equals( mask );
  }

  public int getShiftNum( int row, int col ){
    return row * size + col;
  }

  private boolean checkWin( int x, int y, BigInteger mask, Gui gui){
    for( int row = 0; row < x; row++ ){
      for( int col = 0; col < y; col++ ){
        int shift = getShiftNum( row, col );
        if( redPlayer && isWinningPattern( redBoard, mask, shift ) ){
          showWinningPattern( gui, mask, shift );
          return true;
        }
        else if( !redPlayer && isWinningPattern( blueBoard, mask, shift ) ){
          showWinningPattern( gui, mask, shift );
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
    assert getEmptySquareList().size() == legalMovesNum;
    computerPlayer.makeComputerFirstMoveIfNeeded( gui, this );
  }

  public static void main( String[] args ){
    int     forPassSize  = Integer.parseInt ( args[0] );
    boolean itPlays      = ( args[1].equals( "1" ) ) ? true : false;
    boolean plays1st     = ( args[2].equals( "1" ) ) ? true : false;
    int     opponent     = Integer.parseInt ( args[3] );
    assert ( forPassSize >= 5 && forPassSize <= 19 );
    assert ( args[1].equals( "1" ) || args[1].equals( "0" ) );
    assert ( args[2].equals( "1" ) || args[2].equals( "0" ) );
    assert ( args[3].equals( "1" ) || args[3].equals( "2" ) );
    GameBoard board  = new GameBoard( forPassSize, itPlays, plays1st, opponent );
    Gui gui          = new Gui( forPassSize, board );
    computerPlayer.makeComputerFirstMoveIfNeeded( gui, board );
  }

}
