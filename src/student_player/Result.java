package student_player;

import java.util.ArrayList;

import bohnenspiel.BohnenspielBoardState;
import bohnenspiel.BohnenspielMove;

public class Result {
	int bestScore;
	BohnenspielMove bestMove;
	BohnenspielBoardState board_state;
	ArrayList<BohnenspielMove> validMoves;
	boolean oneSearchFinished;
	
	public Result(BohnenspielBoardState board_state){
		this.board_state = board_state;
		this.validMoves = board_state.getLegalMoves();
		this.bestMove = validMoves.get(0);
		this.oneSearchFinished = false;
	}
	public void setBestMove(BohnenspielMove move){
		bestMove = move;
//		if (validMoves.contains(move)) bestMove = move;
//		else{
//			System.err.println("invalid---");
//		}
	}
}
