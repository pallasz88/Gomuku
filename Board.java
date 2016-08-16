import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.math.BigInteger;


public class Board extends JPanel implements ActionListener{

  private int size                         = 19;
  private JButton[] button                 = new JButton[ size*size ];
  private Insets inset                     = new Insets( 0, 0, 0, 0 );
  private static boolean redPlayer         = true;
  private static boolean isComputerPlaying = true;
  private static boolean isComputerFirst   = true;
  private BigInteger redBoard              = BigInteger.ZERO;
  private BigInteger blueBoard             = BigInteger.ZERO;
  private BigInteger winHorizMask;
  private BigInteger winVertcMask;
  private BigInteger winDiagRMask;
  private BigInteger winDiagLMask;

  Board(){
    initLayout();
    initMasks();
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

  public void setBlueBoard( BigInteger board ){
    blueBoard = board;
  }

  public BigInteger getEmptySquares(){
    BigInteger base = new BigInteger( "2" );
    BigInteger allBoard = base.pow( size*size ).subtract( new BigInteger( "1" ) );
    return redBoard.or( blueBoard ).xor( allBoard );
  }

  public void setButton( int square, Color color){
    button[ square ].setBackground(color);
  }

  private void initLayout() {
    setLayout( new GridBagLayout() );
    GridBagConstraints gbc = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
        GridBagConstraints.CENTER, GridBagConstraints.BOTH, inset, 0, 0 );

    for( int i=0; i < size*size; i++ ){
      gbc.gridx = i % size;
      gbc.gridy = i / size;
      button[i] = new JButton();
      button[i].addActionListener( this );
      button[i].setBackground ( Color.lightGray );
      add(button[i], gbc);
    }
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

  public void actionPerformed(ActionEvent e){
    JButton src = ( JButton ) e.getSource();

    for( int i=0; i < size*size; i++ ) {
      if( src == button[i] && src.getBackground() == Color.lightGray ){
        if( redPlayer ){
          handleClick( Color.RED, i );
        }
        else{
          handleClick( Color.BLUE, i );
        }
      }
    }

  }

  private boolean isNewGameNeeded(){
    if( isGameOver() ){
      initNewGame();
      return true;
    }
    return false;
  }

  public void handleClick( Color c, int clicked ){
    BigInteger shiftMe = BigInteger.ONE;
    button[clicked].setBackground(c);
    Color oppColor = (c == Color.RED) ? Color.BLUE : Color.RED;
    if( !isComputerPlaying ){
      if( redPlayer )
        redBoard = redBoard.or( shiftMe.shiftLeft( clicked ) );
      else
        blueBoard = blueBoard.or( shiftMe.shiftLeft( clicked ) );
      if( isNewGameNeeded() )
        return;
      redPlayer = !redPlayer;
    }
    else{
      if( isComputerFirst ){
        blueBoard = blueBoard.or( shiftMe.shiftLeft( clicked ) );
      }
      else{
        redBoard = redBoard.or( shiftMe.shiftLeft( clicked ) );
      }
      if( isNewGameNeeded() )
        return;
      else{
        ComputerMove.makeMove( this, oppColor );
        redPlayer = !redPlayer;
        if( isNewGameNeeded() )
          return;
        redPlayer = !redPlayer;
      }
    }
  }

  private boolean isGameOver(){
    String message = "";
    if( isWin() ){
      message = ( redPlayer ) ? "Nyert a piros j�t�kos!" : "Nyert a k�k j�t�kos!";
      JOptionPane.showMessageDialog( this, message );
      return true;
    }
    if( isDraw() ){
      message = "D�ntetlen";
      JOptionPane.showMessageDialog( this, message );
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
        int shift = getShiftNum( row, col);
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

  private void initNewGame(){
    redBoard  = BigInteger.ZERO;
    blueBoard = BigInteger.ZERO;
    redPlayer = true;

    for( int i=0; i<size*size; i++){
      button[i].setBackground ( Color.lightGray );
    }

    if( isComputerPlaying && isComputerFirst ){
      ComputerMove.makeMove( this, Color.RED );
      redPlayer = false;
    }
  }

  public static void main(String[] args){
    Board board = new Board();
    JFrame f    = new JFrame( "Am�ba" );
    f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    f.setSize( 800, 800);
    f.setLocationRelativeTo( null );
    f.setVisible( true );
    f.setResizable( false );
    f.add(board);

    if( isComputerPlaying && isComputerFirst ){
      ComputerMove.makeMove( board, Color.RED );
      redPlayer = !redPlayer;
    }

  }

}
