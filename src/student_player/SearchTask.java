package student_player;

import java.util.ArrayList;

import bohnenspiel.BohnenspielBoardState;
import bohnenspiel.BohnenspielMove;

public class SearchTask implements Runnable {
	int depthLimit = 8;
	Result searchResult;
	BohnenspielBoardState board_state;
	int player_id;
	int opponent_id;

	public SearchTask(BohnenspielBoardState board_state, int player_id, int opponent_id, Result r) {
		this.board_state = board_state;
		this.player_id = player_id;
		this.opponent_id = opponent_id;
		this.searchResult = r;
	}

	@Override
	public void run() {
//		try {
			while (!Thread.currentThread().isInterrupted()){
	    		alphaBetaSearch(board_state, -1000, 1000, 1);
    			System.err.println("Done");
    			depthLimit ++;
    		}
//			System.err.println("exiting//------------------------------");
//		}

//		catch (Exception e) {

//			System.out.println(e);

//		}

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
    				
    				if (depth == 1) searchResult.bestMove = move;	// root
    				
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
    				
    				if (depth == 1) searchResult.bestMove = move;	// root
    				
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


    

}
