package cs380.othello;

public class OthelloScorerImproved implements OthelloScorer
{
	private static final int CORNER_SCORE = 4;
	private static final int SIDE_SCORE = 1;
	private static final int SIDE_ADJACENT_CORNER = 2;
	private static final int INNER_CORNER = 3;

	/**
	  * This function calculates the heuristic based on the following parameters (adding to the
	  * total score for the O player and subtracting for the X player, so that O's desirable scores
	  * are positive and X's negative):
	  * 	control of the corners - controlling a corner adds 4 extra points in that player's
	  * 	    direction
	  *		control of the sides - controlling positions on the side of the board is worth one
	  *		    extra point, with a caveat
	  *     control of a position adjacent to a corner - these apply only when the corner is empty:
	  *		    if the player controls a side position next to that corner, two points are taken
	  *		    away, and if he controls a position diagonally inward towards the middle of the
	  *		    board, three points are taken away.
	  * These heuristics are justified as follow:
	  *		The corners: controlling the corners is extremely advantageous, in that they are
	  *		    "stable" (in the game's terminology) and can never be retaken, and therefore
	  *		    positions where they are controlled should be considered desirable.
	  *		The sides: being as they only expose three sides of the position, the side positions are
	  *		    relatively stable as well. Therefore, controlling the sides should be considered
	  *		    desirable, but not nearly as desirable as the corners.
	  *     The positions adjacent to corners: controlling these positions when there is not a piece
	  *         in the corner makes the corner highly susceptible to being taken by the opponent,
	  *         and because the corner is highly advantangeous, these positions should be considered
	  *         bad posititions.
	  */
	@Override
	public int score(OthelloState state)
	{
		int board[][] = state.board;
		int totalScore = 0;

		totalScore += considerCorner(board, 0, 0);
		totalScore += considerCorner(board, 0, board.length - 1);
		totalScore += considerCorner(board, board.length - 1, 0);
		totalScore += considerCorner(board, board.length - 1, board.length - 1);

		totalScore += considerRow(board, 0);
		totalScore += considerRow(board, board.length - 1);
		totalScore += considerCol(board, 0);
		totalScore += considerCol(board, board.length- 1);

		totalScore += considerInnerCorner(board, 0, 0, 1, 1);
		totalScore += considerInnerCorner(board, 0, board.length - 1, 1, board.length - 2);
		totalScore += considerInnerCorner(board, board.length - 1, 0, board.length - 2, 1);
		totalScore += considerInnerCorner(board, board.length - 1, board.length - 1, board.length - 2, board.length - 2);

		return totalScore;
	}

	private int considerCorner(int board[][], int row, int col)
	{
		//add extra points for holding a corner, because these pieces are stable and can never be
		//captured
		if (board[row][col] == OthelloState.PLAYER1)
		{
			return CORNER_SCORE;
		}

		if (board[row][col] == OthelloState.PLAYER2)
		{
			return -CORNER_SCORE;
		}

		return 0;
	}

	private int considerRow(int board[][], int row)
	{
		int localScore = 0;
		int start = 1, end = board.length - 1;

		//if there is nothing in the corner yet, it is very bad to have a piece in one of the
		//adjacent spots. Therefore, subtract points if this is the case
		if (board[row][0] == OthelloState.NOTHING)
		{
			start++;
			if (board[row][1] == OthelloState.PLAYER1)
			{
				localScore += -SIDE_ADJACENT_CORNER;
			}
			else if (board[row][1] == OthelloState.PLAYER2)
			{
				localScore += SIDE_ADJACENT_CORNER;
			}
		}

		if (board[row][board.length - 1] == OthelloState.NOTHING)
		{
			end--;
			if (board[row][board.length - 2] == OthelloState.PLAYER1)
			{
				localScore += -SIDE_ADJACENT_CORNER;
			}
			else if (board[row][board.length - 2] == OthelloState.PLAYER2)
			{
				localScore += SIDE_ADJACENT_CORNER;
			}
		}

		//finally, after handling above cases, grant additional points for holding the sides,
		//because these positions are relatively stable
		for (int i = start; i < end; i++)
		{
			if (board[row][i] == OthelloState.PLAYER1)
			{
				localScore += SIDE_SCORE;
			}
			else if (board[row][i] == OthelloState.PLAYER2)
			{
				localScore += -SIDE_SCORE;
			}
		}

		return localScore;
	}

	private int considerCol(int board[][], int col)
	{
		int localScore = 0;
		int start = 1, end = board.length - 1;
		//if there is nothing in the corner yet, it is very bad to have a piece in one of the
		//adjacent spots. Therefore, subtract points if this is the case
		if (board[0][col] == OthelloState.NOTHING)
		{
			start++;
			if (board[1][col] == OthelloState.PLAYER1)
			{
				localScore += -SIDE_ADJACENT_CORNER;
			}
			else if (board[1][col] == OthelloState.PLAYER2)
			{
				localScore += SIDE_ADJACENT_CORNER;
			}
		}

		if (board[board.length - 1][col] == OthelloState.NOTHING)
		{
			end--;
			if (board[board.length - 2][col] == OthelloState.PLAYER1)
			{
				localScore += -SIDE_ADJACENT_CORNER;
			}
			else if (board[board.length - 2][col] == OthelloState.PLAYER2)
			{
				localScore += SIDE_ADJACENT_CORNER;
			}
		}

		//finally, after handling above cases, grant additional points for holding the sides,
		//because these positions are relatively stable
		for (int i = start; i < end; i++)
		{
			if (board[i][col] == OthelloState.PLAYER1)
			{
				localScore += SIDE_SCORE;
			}
			else if (board[i][col] == OthelloState.PLAYER2)
			{
				localScore += -SIDE_SCORE;
			}
		}

		return localScore;
	}

	private int considerInnerCorner(int board[][], int corner_row, int corner_col, int row, int col)
	{
		if (board[corner_row][corner_col] == OthelloState.NOTHING)
		{
			if (board[row][col] == OthelloState.PLAYER1)
			{
				return -INNER_CORNER;
			}
			
			if (board[row][col] == OthelloState.PLAYER2)
			{
				return INNER_CORNER;
			}
		}

		return 0;
	}
}
