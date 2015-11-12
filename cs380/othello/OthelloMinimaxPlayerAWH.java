package cs380.othello;

import java.util.List;

public class OthelloMinimaxPlayerAWH extends OthelloPlayer
{
	private boolean isMax_;
	private int depth_;
	OthelloScorer scorer_;

	public OthelloMinimaxPlayerAWH(boolean isMax, int depth)
	{
		this(isMax, depth, new OthelloScorerStandard());
	}

	public OthelloMinimaxPlayerAWH(boolean isMax, int depth, OthelloScorer scorer)
	{
		isMax_ = isMax;
		depth_ = depth;
		scorer_ = scorer;
	}

	public OthelloMove getMove(OthelloState state)
	{
		List<OthelloMove> moves = state.generateMoves();
		if (moves.isEmpty())
		{
			return null;
		}

		//treat the call to getMove as the "first call" to minimax, though here, find the bestMove
		//instead of just the best score
		//assume the first move is the best one first
		int depth = depth_ - 1;
		OthelloMove bestMove = moves.get(0);
		int bestScore = minimax(state.applyMoveCloning(bestMove), !isMax_, depth);
		for (int i = 1; i < moves.size(); i++)
		{
			OthelloMove currMove = moves.get(i);
			int nextScore = minimax(state.applyMoveCloning(currMove), !isMax_, depth);
			if ((isMax_ && nextScore > bestScore) || (!isMax_ && nextScore < bestScore))
			{
				bestMove = currMove;
				bestScore = nextScore;
			}
		}

		return bestMove;
	}

	private int minimax(OthelloState state, boolean maxPlayer, int depth)
	{
		//terminal node; return minimax value
		if (depth <= 0 || state.gameOver())
		{
			return scorer_.score(state);
		}

		depth--;
		List<OthelloMove> moves = state.generateMoves();

		//if no moves available, then only move is to pass, so return that as the minimax value
		if (moves.isEmpty())
		{
			return minimax(state.applyMoveCloning(null), !maxPlayer, depth);
		}

		//otherwise, check each possible move and determine the highest/lowest possible value
		int bestScore = maxPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		for (int i = 0; i < moves.size(); i++)
		{
			int nextScore = minimax(state.applyMoveCloning(moves.get(i)), !maxPlayer, depth);
			if ((maxPlayer && nextScore > bestScore) || (!maxPlayer && nextScore < bestScore))
			{
				bestScore = nextScore;
			}
		}
		
		return bestScore;
	}
}
