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
//import java.math.*;


public class Board extends JPanel implements ActionListener{

  private int size                       = 8;
  private JButton[] button               = new JButton[ size*size ];
  private Insets inset                   = new Insets( 0, 0, 0, 0 );
  private boolean redPlayer              = true;
  private static long redBoard           = 0L;
  private static long blueBoard          = 0L;
  private static final long winHorizMask = 31L;
  private static final long winVertcMask = 4311810305L;
  private static final long winDiagvMask = 4328785936L;
  private static final long winDiaghMask = 68853957121L;

  Board(){
    initLayout();
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

  public void actionPerformed(ActionEvent e){
    JButton src = ( JButton ) e.getSource();
    long shiftMe = 1L;

    for( int i=0; i < size*size; i++ ) {
      if( src == button[i] && src.getBackground() == Color.lightGray ){
        if( redPlayer ){
          handleClick( redBoard, Color.RED, i );
        }
        else{
          handleClick( blueBoard, Color.BLUE, i );
        }
      }
    }

  }
  
  public void handleClick( long playerBoard, Color c, int clicked ){
    long shiftMe = 1L;
    playerBoard |= (shiftMe << clicked);
    button[clicked].setBackground(c);
    if( redPlayer )
      redBoard = playerBoard;
    else
      blueBoard = playerBoard;
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
    if( (redBoard | blueBoard) == Long.MAX_VALUE * 2 + 1 ){
      return true;
    }
    return false;
  }

  private boolean isWin(){

    System.out.println( redBoard | blueBoard );
    
    if( checkWin( size, size-4, winHorizMask ) ){
      return true;
    }
    
    if( checkWin( size-4, size, winVertcMask ) ){
      return true;
    }
    
    if( checkWin( size-4, size-4, winDiagvMask ) ){
      return true;
    }
    
    if( checkWin( size-4, size-4, winDiaghMask ) ){
      return true;
    }
    return false;
  }
  
  private boolean checkWin( int x, int y, long mask){
    for( int row = 0; row < x; row++ ){
      for( int col = 0; col < y; col++ ){
        long redBuf  = redBoard;
        long blueBuf = blueBoard;
        if( redPlayer ){
          if( mask == (( redBuf >> row*size + col) & mask ) ){
            return true;
          }
        }
        else{
          if( mask == (( blueBuf >> row*size + col) & mask ) ){
            return true;
          }
        }
      }
    }
    return false;
  }

  private void initNewGame(){
    redBoard  = 0L;
    blueBoard = 0L;
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
