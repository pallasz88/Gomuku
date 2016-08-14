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

  private int size                       = 19;
  private JButton[] button               = new JButton[ size*size ];
  private Insets inset                   = new Insets( 0, 0, 0, 0 );
  private boolean redPlayer              = true;
  private static BigInteger redBoard     = BigInteger.ZERO;
  private static BigInteger blueBoard    = BigInteger.ZERO;
  private static BigInteger winHorizMask;
  private static BigInteger winVertcMask;
  private static BigInteger winDiagRMask;
  private static BigInteger winDiagLMask;

  Board(){
    initLayout();
    initMasks();
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

    System.out.println( "Mask1: " + winHorizMask );
    System.out.println( "Mask2: " + winVertcMask );
    System.out.println( "Mask3: " + winDiagRMask );
    System.out.println( "Mask4: " + winDiagLMask );
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
  
  public void handleClick( Color c, int clicked ){
    BigInteger shiftMe = BigInteger.ONE;
    //playerBoard = playerBoard.or( shiftMe.shiftLeft( clicked ) );
    button[clicked].setBackground(c);
    if( redPlayer )
      redBoard = redBoard.or( shiftMe.shiftLeft( clicked ) );
    else
      blueBoard = blueBoard.or( shiftMe.shiftLeft( clicked ) );
    if( isGameOver() ){
      initNewGame();
    }
    redPlayer = !redPlayer;
  }
  
  private boolean isGameOver(){
    String message = "";
    if(redPlayer)
      message = "Nyert a piros játékos!";
    else
      message = "Nyert a kék játékos!";
    if( isWin() ){
      JOptionPane.showMessageDialog( this, message );
      return true;
    }
    if( isDraw() ){
      message = "Döntetlen";
      JOptionPane.showMessageDialog( this, message );
      return true;
    }
    return false;
  }
  
  private boolean isDraw(){
    BigInteger base = new BigInteger( "2" );
    if( redBoard.or( blueBoard ).equals( base.pow( size*size ).subtract( 
                        new BigInteger( "1" ) ) ) ){
      return true;
    }
    return false;
  }

  private boolean isWin(){

    System.out.println( redBoard.or( blueBoard ) );
    
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
  
  private boolean checkWin( int x, int y, BigInteger mask){
    for( int row = 0; row < x; row++ ){
      for( int col = 0; col < y; col++ ){
        BigInteger redBuf  = redBoard;
        BigInteger blueBuf = blueBoard;
        if( redPlayer ){
          if( (redBuf.shiftRight( row*size + col).and( mask ) ).equals( mask ) ){
            return true;
          }
        }
        else{
          if( (blueBuf.shiftRight( row*size + col).and( mask ) ).equals( mask ) ){
            return true;
          }
        }
      }
    }
    return false;
  }

  private void initNewGame(){
    redBoard  = BigInteger.ZERO;
    blueBoard = BigInteger.ZERO;
    redPlayer = false;

    for( int i=0; i<size*size; i++){
      button[i].setBackground ( Color.lightGray );
    }
  }

  public static void main(String[] args){
    Board t = new Board();
    JFrame f = new JFrame( "Amõba" );
    f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    f.setSize(800,800);
    f.setLocationRelativeTo( null );
    f.setVisible(true);
    f.add(t);
  }

}
