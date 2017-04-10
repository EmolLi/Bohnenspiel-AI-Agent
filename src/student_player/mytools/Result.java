package student_player.mytools;

import java.util.ArrayList;

import bohnenspiel.BohnenspielBoardState;
import bohnenspiel.BohnenspielMove;

public class Result {
	int bestScore;
	public BohnenspielMove bestMove;
	BohnenspielBoardState board_state;
	public ArrayList<BohnenspielMove> validMoves;
	boolean oneSearchFinished;
	
	public Result(BohnenspielBoardState board_state){
		this.board_state = board_state;
		this.validMoves = board_state.getLegalMoves();
		this.bestMove = validMoves.get(0);
		this.oneSearchFinished = false;
	}
	public void setBestMove(BohnenspielMove move){
		bestMove = move;
	}
}
