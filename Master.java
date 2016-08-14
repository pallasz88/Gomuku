import java.util.Scanner;


public class Master{
  
  public static int negaMax( int depth, int alpha, int beta, int color ){
    
    if( depth == 0 ){
      return color * eval();
    }
    
    Scanner sc = new Scanner(System.in);
    int bestValue = Integer.MIN_VALUE;
    int moves;
    System.out.println("How many moves are there?");
    moves = sc.nextInt();
    
    for(int i=0; i<moves; i++){
      int v = -negaMax(depth-1, -beta, -alpha, -color);
      bestValue = ( bestValue < v )? v : bestValue;
      alpha = ( alpha < v )? v : alpha;
      if( alpha >= beta )
        break;
    }
    
    return bestValue;
  }
  
  public static int eval(){
    int points;
    Scanner sc = new Scanner(System.in);
    System.out.println("Evaluation?");
    points = sc.nextInt();
    return points;
  }
  
  public static void main(String[] args){
    int alpha = -Integer.MAX_VALUE;
    int beta  =  Integer.MAX_VALUE;
    System.out.println("Position: " + Integer.toString( negaMax(3, alpha, beta, 1) ) );
  }
  
}