package cs380.othello;

import java.util.List;

public class OthelloAlphabetaPlayerAWH extends OthelloPlayer
{
	private boolean isMax_;
	private int depth_;
	private OthelloScorer scorer_;

	public OthelloAlphabetaPlayerAWH(boolean isMax, int depth)
	{
		this(isMax, depth, new OthelloScorerStandard());
	}

	public OthelloAlphabetaPlayerAWH(boolean isMax, int depth, OthelloScorer scorer)
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

		//treat the call to getMove as the "first call" to alphabeta, though here, find the bestMove
		//instead of just the best score
		int depth = depth_ - 1;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
	
		//assume the first move is the best one first
		OthelloMove bestMove = moves.get(0);
		int bestScore = alphabeta(state.applyMoveCloning(bestMove), !isMax_, depth, alpha, beta);
		if (isMax_)
		{
			//update alpha based on the bestScore for the first move
			alpha = max(alpha, bestScore);
			//note that loop is exited if beta ever becomes <= alpha
			for (int i = 1; i < moves.size() && beta > alpha; i++)
			{
				OthelloMove currMove = moves.get(i);
				int nextScore = alphabeta(state.applyMoveCloning(currMove), false, depth, alpha, beta);
				if (nextScore > bestScore)
				{
					bestScore = nextScore;
					bestMove = currMove;
				}
				alpha = max(alpha, bestScore);
			}
		}
		else
		{
			//update best based on the bestScore for the first move
			beta = min(beta, bestScore);
			//note that loop is exited if beta ever becomes <= than alpha
			for (int i = 1; i < moves.size() && beta > alpha; i++)
			{
				OthelloMove currMove = moves.get(i);
				int nextScore = alphabeta(state.applyMoveCloning(currMove), true, depth, alpha, beta);
				if (nextScore < bestScore)
				{
					bestScore = nextScore;
					bestMove = currMove;
				}
				beta = min(beta, bestScore);
			}
		}

		return bestMove;
	}

	private int alphabeta(OthelloState state, boolean maxPlayer, int depth, int alpha, int beta)
	{
		//terminal node - return minimax value
		if (depth <= 0 || state.gameOver())
		{
			return scorer_.score(state);
		}

		depth--;
		List<OthelloMove> moves = state.generateMoves();

		//if no moves available, then only move is to pass, so return that as the alphabeta value
		if (moves.isEmpty())
		{
			return alphabeta(state.applyMoveCloning(null), !maxPlayer, depth, alpha, beta);
		}

		//otherwise, check each possible move and determine the highest/lowest possible value
		int bestScore = maxPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		//note that loop is exited if beta ever becomes <= alpha
		for (int i = 0; i < moves.size() && beta > alpha; i++)
		{
			int nextScore = alphabeta(state.applyMoveCloning(moves.get(i)), !maxPlayer, depth, alpha, beta);
			if (maxPlayer)
			{
				bestScore = max(bestScore, nextScore);
				alpha = max(alpha, bestScore);
			}
			else
			{
				bestScore = min(bestScore, nextScore);
				beta = min(beta, bestScore);
			}
		}
		
		return bestScore;
	}

	private int min(int a, int b)
	{
		return a < b ? a : b;
	}

	private int max(int a, int b)
	{
		return a > b ? a : b;
	}
}
