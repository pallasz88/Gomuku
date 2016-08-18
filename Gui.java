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


public class Gui extends JPanel implements ActionListener{

  private static int size;
  private JButton[] button;
  private Insets inset     = new Insets( 0, 0, 0, 0 );
  private GameBoard board;

  Gui( int size, GameBoard b ){
    JFrame f  = new JFrame( "Amõba" );
    button    = new JButton[ size*size ];
    this.size = size;
    board     = b;
    initLayout();
    f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    f.setSize( 800, 800 );
    f.setLocationRelativeTo( null );
    f.setVisible( true );
    f.setResizable( false );
    f.add( this );
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

  public void actionPerformed(ActionEvent e){
    JButton src = ( JButton ) e.getSource();

    for( int i=0; i < size*size; i++ ) {
      if( src == button[i] && src.getBackground() == Color.lightGray ){
        if( board.getRedPlayer() ){
          handleClick( Color.RED, i );
        }
        else{
          handleClick( Color.BLUE, i );
        }
      }
    }
  }

  public void handleClick( Color c, int clicked ){
    Color oppColor = (c == Color.RED) ? Color.BLUE : Color.RED;
    button[clicked].setBackground(c);
    if( !board.getIsComputerPlaying() ){
      if( board.getRedPlayer() )
        board.setRedBoard( clicked );
      else
        board.setBlueBoard( clicked );
      if( board.isNewGameNeeded( this ) )
        return;
      board.decrementLegalMovesNum();
      board.switchRedPlayer();
    }
    else{
      if( board.getIsComputerFirst() ){
        board.setBlueBoard( clicked );
      }
      else{
        board.setRedBoard( clicked );
      }
      board.decrementLegalMovesNum();
      if( board.isNewGameNeeded( this ) )
        return;
      else{
        ComputerMove.makeMove( this, board, oppColor );
        board.decrementLegalMovesNum();
        board.switchRedPlayer();
        if( board.isNewGameNeeded( this ) )
          return;
        board.switchRedPlayer();
      }
    }
  }

  public void showMessage( String message ){
    JOptionPane.showMessageDialog( this, message );
  }

  public void initButtons(){
    for( int i=0; i < size*size; i++ ){
      button[ i ].setBackground ( Color.lightGray );
    }
  }

  public void showOneWinningButton( int square ){
    button[ square ].setBackground ( Color.YELLOW );
  }

  public void makeComputerFirstMoveIfNeeded(){
    if( board.getIsComputerPlaying() &&
        board.getIsComputerFirst() ){
      ComputerMove.makeMove( this, board, Color.RED );
      board.decrementLegalMovesNum();
      board.initRedPlayer();
      board.switchRedPlayer();
    }
  }

}
