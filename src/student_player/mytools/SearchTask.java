package student_player.mytools;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import bohnenspiel.BohnenspielBoardState;
import bohnenspiel.BohnenspielMove;

public class SearchTask implements Runnable {
	// =============comparators for queue info =============
	public static final int MIN_TURN = 1;
	public static final int MAX_TURN = 2;
	public static final int ROOT_AGAIN = 3;
	
	
	int depthLimit = 8;
	Result searchResult;
	MoveState moveState;
	int player_id;
	int opponent_id;

	public SearchTask(MoveState moveState, int player_id, int opponent_id, Result r) {
		this.moveState = moveState;
		this.player_id = player_id;
		this.opponent_id = opponent_id;
		this.searchResult = r;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()){
	    	alphaBetaSearch(moveState, -1000, 1000, 1);
	    	searchResult.oneSearchFinished = true;
    		depthLimit ++;
    	}

	}
	


    
//    """ 
//    Returns best score for the player associated with the given node.
//    Also sets the variable bestMove to the move associated with the
//    best score at the root node.  
//    """
//    ref: https://www.cs.swarthmore.edu/~meeden/cs63/f05/minimax.html
    private int alphaBetaSearch(MoveState moveState, int alpha, int beta, int depth){

    	// check if at search bound
    	if (depth == depthLimit){
    		return moveState.eval;
    	}
    	
    	
    	BohnenspielBoardState currentState = moveState.nextState;
    	int turnType = getTurnType(depth);
    	PriorityQueue<MoveState> statesToSearch = moveState.getNextMoveStates(turnType, searchResult);
    	
    	// if game over, we dont use eval --> eval only used when the current node is not leaf
    	if (currentState.gameOver()){
    		return (currentState.getWinner() == player_id) ? 1000 : -1000;
    	}
    	
    	// no state to search
    	if (statesToSearch == null) return moveState.eval;

    	
    	// max turn -> odd depth
    	if (turnType == MAX_TURN || turnType == ROOT_AGAIN){
    		while (!statesToSearch.isEmpty()){
    			MoveState move = statesToSearch.remove();
    			int resultScore = this.alphaBetaSearch(move, alpha, beta, depth + 1);
    			// interpret result for max
    			if (resultScore > alpha){
    				alpha = resultScore;
    				
    				if (depth == 1) searchResult.bestMove = move.move;	// root
    				
    				if (alpha >= beta) return alpha;	// prune
    			}
    		}
    		return alpha;
    	}
    	
    	// min turn -> even depth
    	else{
    		while (!statesToSearch.isEmpty()){
    			MoveState move = statesToSearch.remove();
    			int resultScore = this.alphaBetaSearch(move, alpha, beta, depth + 1);
    			
    			// interpret result for max
    			if (resultScore < beta){
    				beta = resultScore;
    				
    				if (depth == 1) searchResult.bestMove = move.move;	// root
    				
    				if (beta <= alpha) return beta;	// prune
    			}
    		}
    		return beta;
    	}

    }
    
    
    private int getTurnType(int depth){
    	if (depth == 1 && searchResult.oneSearchFinished){
    		return ROOT_AGAIN;
    	}
    	// max turn -> odd depth
    	if (depth % 2 == 1){
    		return MAX_TURN;
    	}
    	return MIN_TURN;
    }
}



