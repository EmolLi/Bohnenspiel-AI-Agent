package student_player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import bohnenspiel.BohnenspielBoardState;
import bohnenspiel.BohnenspielMove;
import bohnenspiel.BohnenspielMove.MoveType;

public class MoveState {
	BohnenspielMove move;
	BohnenspielBoardState nextState;
	BohnenspielBoardState currentState;
	int player_id;
	int opponent_id;
	int eval;

	public MoveState(BohnenspielMove move, BohnenspielBoardState currentState, int player_id, int opponent_id) throws IllegalArgumentException{
		this.player_id = player_id;
		this.opponent_id = opponent_id;
		this.move = move;
		this.currentState = currentState;
		this.nextState = getNextState(move, currentState);
		if (this.nextState == null) throw new IllegalArgumentException("InfiniteMove");
		this.eval = eval(nextState, player_id, opponent_id);	// we dont need to store player id and opponent id 
	}
	
	// moveState at root
	public MoveState(BohnenspielBoardState nextState, int player_id, int opponent_id){
		// move and currentState at rootState is null
		this.nextState = nextState;
		this.player_id = player_id;
		this.opponent_id = opponent_id;
	}
	

	
    private BohnenspielBoardState getNextState(BohnenspielMove move, BohnenspielBoardState currentState){
    	BohnenspielBoardState cloned_board_state = (BohnenspielBoardState) currentState.clone();
        boolean notInfinite = cloned_board_state.move(move);
        return notInfinite? cloned_board_state : null;	// if infinite move, return null
    }
    
    
    // TODO: eval function evaluation function, use score
    private int eval(BohnenspielBoardState board_state, int player_id, int opponent_id) {
    	return board_state.getScore(player_id) - board_state.getScore(opponent_id);
    }
    
    /**
     * 
     * @param compType:
     * 			1 - MIN_TURN
     * 			2 - MAX_TURN
     * 			3 - ROOT_AGAIN
     * @return
     */
    public PriorityQueue<MoveState> getNextMoveStates(int turnType, Result r){
    	ArrayList<BohnenspielMove> moves = nextState.getLegalMoves();
    	if (nextState.gameOver() || moves.size() == 0){
    		return null;
    	}
    	
    	Comparator<MoveState> selectedComp = selectComparator(turnType, r); 
    	PriorityQueue<MoveState> searchQueue = new PriorityQueue<>(moves.size(), selectedComp);
    	
    	for (BohnenspielMove move : moves){
    		try{
    			MoveState nextMoveState = new MoveState(move, nextState, player_id, opponent_id);
    			searchQueue.add(nextMoveState);
    		} catch (IllegalArgumentException e){
    			// move leads to infinite loop --> ignore
    			continue;
    		}
    	}
    	return (searchQueue.size() == 0)? null : searchQueue;
    }
    
    private Comparator<MoveState> selectComparator(int turnType, Result r){
    	switch (turnType) {
		case 1:
			return new  MIN_TURN();
		case 2:
			return new MAX_TURN();
		case 3:
			return new ROOT_AGAIN(r);
		default:
			return null;
		}
    }
    
}



//-------------------Comparators-------------------------------
//this comparators are used in priority queue that help to search with priority (based on heuristic)
class MIN_TURN implements Comparator<MoveState>{

	@Override
	public int compare(MoveState o1, MoveState o2) {
		// TODO Auto-generated method stub
		return o1.eval - o2.eval;
	}
	
}

class MAX_TURN implements Comparator<MoveState>{
	@Override
	public int compare(MoveState o1, MoveState o2) {
		// TODO Auto-generated method stub
		return o2.eval - o1.eval;
	}
}

//we use Root_again comparator when we have already finish one iteration of search, in the second iteration, we search the branch that gives best value first
class ROOT_AGAIN implements Comparator<MoveState>{
	Result result;
	
	public ROOT_AGAIN(Result r){
		result = r;
	}
	
	private boolean sameMoveAsBestMove(BohnenspielMove move){
		return ( (move.getPit() == result.bestMove.getPit()) && (move.getMoveType() == result.bestMove.getMoveType()));
	}
	
	@Override
	// at root, it's also max turn
	public int compare(MoveState o1, MoveState o2) {
		 if (sameMoveAsBestMove(o1.move)) return -1000;	// search me first!
		 if (sameMoveAsBestMove(o2.move)) return 1000;	// search the one behind me first! (search me later)
         return o2.eval - o1.eval;	// max turn at root
	}
}

