import java.awt.Color;

public class Computer{

  protected static boolean isComputerPlaying;
  protected static boolean isComputerFirst;

  Computer( boolean isPlaying, boolean isFirst ){
    isComputerPlaying = isPlaying;
    isComputerFirst   = isFirst;
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

  public void tryMove( GameBoard board, int color, int square ){
    if( color == 1 ){
      board.setRedBoard( square );
    }
    else
      board.setBlueBoard( square );
  }

  public void makeMove( Gui gui, GameBoard board, Color color ){

  }

  public void makeComputerFirstMoveIfNeeded( Gui gui, GameBoard board ){

  }

}