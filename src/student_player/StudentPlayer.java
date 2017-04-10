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
import student_player.mytools.MoveState;
import student_player.mytools.Result;
import student_player.mytools.SearchTask;

public class StudentPlayer extends BohnenspielPlayer {
	int turn = 1;
	int turnTime = 600;
	int firstMoveTime = 29900;
	int depthLimit = 10;
	Result searchResult;
	

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
    	int timeToSearch = (turn == 1)? firstMoveTime : turnTime;
    	try{
        	Future<?> search = threadPool.submit(new SearchTask(rootState, player_id, opponent_id, searchResult));
        	search.get(turnTime - 100, TimeUnit.MILLISECONDS);
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
    	
        return searchResult.bestMove;
    }
    

  



}
