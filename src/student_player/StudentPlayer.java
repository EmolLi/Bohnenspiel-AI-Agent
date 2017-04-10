package student_player;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import bohnenspiel.BohnenspielBoardState;
import bohnenspiel.BohnenspielMove;
import bohnenspiel.BohnenspielPlayer;
import bohnenspiel.BohnenspielMove.MoveType;
//import student_player.mytools.SearchTask;

/** A Hus player submitted by a student. */
public class StudentPlayer extends BohnenspielPlayer {
	int turn = 1;
	int turnTime = 700;
	int firstMoveTime = 700;
	int depthLimit = 10;
	Result searchResult;
	
//    private final ExecutorService threadPool = Executors.newSingleThreadExecutor();;

    /** You must modify this constructor to return your student number.
     * This is important, because this is what the code that runs the
     * competition uses to associate you with your agent.
     * The constructor should do nothing else. */
    public StudentPlayer() { 
    	super("260683698");
    }

    /** This is the primary method that you need to implement.
     * The ``board_state`` object contains the current state of the game,
     * which your agent can use to make decisions. See the class
bohnenspiel.RandomPlayer
     * for another example agent. */
    public BohnenspielMove chooseMove(BohnenspielBoardState board_state)
    {
    	// initialize result
    	searchResult = new Result(board_state);
    	// if only one move, no need to search
    	if (searchResult.validMoves.size() == 1){
    		return searchResult.bestMove;
    	}
    	
    	MoveState rootState;
    	try{
    		rootState = new MoveState(board_state, player_id, opponent_id);
    	}
    	
    	catch (IllegalArgumentException e){
    		return searchResult.bestMove;
    	}
    	
    	ExecutorService threadPool = Executors.newSingleThreadExecutor();
    	try{
        	Future<?> search = threadPool.submit(new SearchTask(rootState, player_id, opponent_id, searchResult));
        	search.get(400, TimeUnit.MILLISECONDS);
        }
        catch (TimeoutException e){
        	return searchResult.bestMove;
        }
        catch (Exception e){
        	return searchResult.bestMove;
//        	e.printStackTrace();
        }
        finally{
        	//shut down the executor service now
        	threadPool.shutdownNow();
        	turn ++;
        }
    	
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
//    	setUp(board_state);
    	// alphaBetaSearch(board_state, -1000, 1000, 1);
//    	setUpDefaultDecisionBeforeSearch(board_state);
//    	alphaBetaSearch(board_state, -1000, 1000, 1);
    	
    	
        return searchResult.bestMove;
    }
    

  



}
