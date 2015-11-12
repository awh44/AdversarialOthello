package cs380.othello;
import cs380.othello.monteCarlo.*;
/**
 *
 * @author santi
 */
public class Test {    
    public static void main(String args[]) {
		if (args.length < 7)
		{
			System.out.println("Board size and player configuration not included.");
			return;
		}

		int boardSize, depth1, depth2;
		try
		{
			boardSize = Integer.parseInt(args[0]);
			depth1 = Integer.parseInt(args[2]);
			depth2 = Integer.parseInt(args[5]);
		}
		catch (NumberFormatException e)
		{
			System.out.println("Non-integer depth used.");
			return;
		}

		OthelloScorer scorer1, scorer2;
		if (args[3].equals("s"))
		{
			scorer1 = new OthelloScorerStandard();
		}
		else if (args[3].equals("i"))
		{
			scorer1 = new OthelloScorerImproved();
		}
		else
		{
			System.out.println("Uncrecognized scorer type for player 1: " + args[3]);
			return;
		}

		if (args[6].equals("s"))
		{
			scorer2 = new OthelloScorerStandard();
		}
		else if (args[6].equals("i"))
		{
			scorer2 = new OthelloScorerImproved();
		}
		else
		{
			System.out.println("Unrecognized scorer type for player 2: " + args[6]);
			return;
		}

		OthelloPlayer player1, player2;
		if (args[1].equals("m"))
		{
			player1 = new OthelloMinimaxPlayerAWH(true, depth1, scorer1);
		}
		else if (args[1].equals("a"))
		{
			player1 = new OthelloAlphabetaPlayerAWH(true, depth1, scorer1);
		}
		else if (args[1].equals("c"))
		{
			player1 = new OthelloMonteCarloPlayerAWH(depth1);
		}
		else if (args[1].equals("r"))
		{
			player1 = new OthelloRandomPlayer();
		}
		else
		{
			System.out.println("Unrecognized player type for player 1: " + args[1]);
			return;
		}

		if (args[4].equals("m"))
		{
			player2 = new OthelloMinimaxPlayerAWH(false, depth2, scorer2);
		}
		else if (args[4].equals("a"))
		{
			player2 = new OthelloAlphabetaPlayerAWH(false, depth2, scorer2);
		}
		else if (args[4].equals("c"))
		{
			player2 = new OthelloMonteCarloPlayerAWH(depth2);
		}
		else if (args[4].equals("r"))
		{
			player2 = new OthelloRandomPlayer();
		}
		else
		{
			System.out.println("Unrecognized player type for player 2: " + args[3]);
			return;
		}

		System.out.println("Player 1 is: " + player1.getClass().getName());
		System.out.println("Player 2 is: " + player2.getClass().getName());

        // Create the game state with the initial position for an 8x8 board:
        OthelloState state = new OthelloState/*Improved*/(boardSize);
		OthelloPlayer players[] = { player1, player2 };
        
        do
		{
            // Display the current state in the console:
            System.out.println("\nCurrent state, " + OthelloState.PLAYER_NAMES[state.nextPlayerToMove] + " to move:");
            System.out.print(state);
            
            // Get the move from the player:
            OthelloMove move = players[state.nextPlayerToMove].getMove(state);            
            System.out.println(move);
            state = state.applyMoveCloning(move);            
        } while(!state.gameOver());

        // Show the result of the game:
        System.out.println("\nFinal state with score: " + state.score());
        System.out.println(state);
    }    
    
}
