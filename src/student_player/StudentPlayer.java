package student_player;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bohnenspiel.BohnenspielBoardState;
import bohnenspiel.BohnenspielMove;
import bohnenspiel.BohnenspielPlayer;
import bohnenspiel.BohnenspielMove.MoveType;
import student_player.mytools.SearchTask;

/** A Hus player submitted by a student. */
public class StudentPlayer extends BohnenspielPlayer {
	int depthLimit = 7;
	int bestScore;
	BohnenspielMove bestMove;
//    private final ExecutorService threadPool;

    /** You must modify this constructor to return your student number.
     * This is important, because this is what the code that runs the
     * competition uses to associate you with your agent.
     * The constructor should do nothing else. */
    public StudentPlayer() { super("260683698"); }

    /** This is the primary method that you need to implement.
     * The ``board_state`` object contains the current state of the game,
     * which your agent can use to make decisions. See the class
bohnenspiel.RandomPlayer
     * for another example agent. */
    public BohnenspielMove chooseMove(BohnenspielBoardState board_state)
    {
        // Get the contents of the pits so we can use it to make decisions.
//        int[][] pits = board_state.getPits();

        // Use ``player_id`` and ``opponent_id`` to get my pits and opponent pits.
//        int[] my_pits = pits[player_id];
//        int[] op_pits = pits[opponent_id];

        // Use code stored in ``mytools`` package.
//        MyTools.getSomething();

        // Get the legal moves for the current board state.
//        ArrayList<BohnenspielMove> moves = board_state.getLegalMoves();
//        BohnenspielMove move = moves.get(0);
  
     
        // We can see the effects of a move like this...
//        BohnenspielBoardState cloned_board_state = (BohnenspielBoardState) board_state.clone();
//        cloned_board_state.move(move);


        // But since this is a placeholder algorithm, we won't act on that information.
    	setUpDefaultDecisionBeforeSearch(board_state);
    	alphaBetaSearch(board_state, -1000, 1000, 1);
    	
        return bestMove;
    }
    
    
    // TODO: eval function evaluation function, use score
    private int eval(BohnenspielBoardState board_state) {
    	if (board_state.gameOver()){
    		// if game over, we dont use heuristic
    		// if we win, the score is 1000, else -1000
    		return (board_state.getWinner() == player_id) ? 1000 : -1000;
    	}
    	return board_state.getScore(player_id) - board_state.getScore(opponent_id);
    }
    
    /**
     * we run this at the very beginning, so at least we have a decision
     * we then do search to find better decision
     * TODO: use open book to optimize the first decision
     * @param currentState
     */
    private void setUpDefaultDecisionBeforeSearch(BohnenspielBoardState currentState){
    	ArrayList<BohnenspielMove> moves = currentState.getLegalMoves();
    	this.bestMove = moves.get(0);
    	BohnenspielBoardState cloned_board_state = getNextState(bestMove, currentState);
    	this.bestScore = eval(cloned_board_state);
    }
    
//    """ 
//    Returns best score for the player associated with the given node.
//    Also sets the variable bestMove to the move associated with the
//    best score at the root node.  
//    """
//    ref: https://www.cs.swarthmore.edu/~meeden/cs63/f05/minimax.html
    private int alphaBetaSearch(BohnenspielBoardState currentState, int alpha, int beta, int depth){
    	// TODO:

//         check if at search bound or game over
    	if ((depth == depthLimit) || currentState.gameOver()){
    		return eval(currentState);
    	}
    	
    	ArrayList<BohnenspielMove> moves = currentState.getLegalMoves();
    	// we dont really need this, but just in case
    	if (moves.size() == 0){
    		return eval(currentState);
    	}
    	
    	// max turn -> odd depth
    	if (depth % 2 == 1){
    		for (BohnenspielMove move : moves){
    			int resultScore = this.alphaBetaSearch(getNextState(move, currentState), alpha, beta, depth + 1);
    			
    			// interpret result for max
    			if (resultScore > alpha){
    				alpha = resultScore;
    				
    				if (depth == 1) bestMove = move;	// root
    				
    				if (alpha >= beta) return alpha;	// prune
    			}
    		}
    		return alpha;
    	}
    	
    	// min turn -> even depth
    	else{
    		for (BohnenspielMove move : moves){
    			int resultScore = this.alphaBetaSearch(getNextState(move, currentState), alpha, beta, depth + 1);
    			
    			// interpret result for max
    			if (resultScore < beta){
    				beta = resultScore;
    				
    				if (depth == 1) bestMove = move;	// root
    				
    				if (beta <= alpha) return beta;	// prune
    			}
    		}
    		return beta;
    	}

}

    private BohnenspielBoardState getNextState(BohnenspielMove move, BohnenspielBoardState currentState){
    	BohnenspielBoardState cloned_board_state = (BohnenspielBoardState) currentState.clone();
        cloned_board_state.move(move);
        return cloned_board_state;
    }



/**

    // at the begining of the game, we run to set up the thread for search, so the main thread will take care of time
    // management as we dont want to run out of time
    public void setUp(String args[]){

        threadPool = Executors.newSingleThreadExecutor();
        Future<Object> threadPool.submit(new SearchTask());
        //create a list to hold the Future object associated with Callable
        List<Future<String>> list = new ArrayList<Future<String>>();
        //Create MyCallable instance
        Callable<String> callable = new MyCallable();
        for(int i=0; i< 100; i++){
            //submit Callable tasks to be executed by thread pool
            Future<String> future = executor.submit(callable);
            //add Future to the list, we can get return value using Future
            list.add(future);
        }
        for(Future<String> fut : list){
            try {
                //print the return value of Future, notice the output delay in console
                // because Future.get() waits for task to get completed
                System.out.println(new Date()+ "::"+fut.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        //shut down the executor service now
        executor.shutdown();
    }
    **/
    


}