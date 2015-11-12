package cs380.othello.monteCarlo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs380.othello.OthelloMove;
import cs380.othello.OthelloPlayer;
import cs380.othello.OthelloState;

public class OthelloMonteCarloPlayerAWH extends OthelloPlayer
{
	private int iterations_;
	private Random rand = new Random();	

	public OthelloMonteCarloPlayerAWH(int iterations)
	{
		iterations_ = iterations;
	}

	public OthelloMove getMove(OthelloState state)
	{
		MonteCarloNode root = new MonteCarloNode(state);
		if (root.noMoves())
		{
			return null;
		}

		for (int i = 0; i < iterations_; i++)
		{
			MonteCarloNode node = treePolicy(root);
			if (node != null)
			{
				OthelloState node2_state = defaultPolicy(node);
				int node_score = node2_state.score();
				node.backup(node_score);
			}
		}

		return root.bestChild().getAction();
	}	

	private MonteCarloNode treePolicy(MonteCarloNode node)
	{
		if (node.addNextChild())
		{
			return node.lastChild();
		}

		if (node.gameOver())
		{
			return node;
		}

		MonteCarloNode nodetmp;
		//generate number in [0, 9]; if less than 9 (i.e., in [1, 8], probability of .9), take the
		//best child. Otherwise, take a random one
		if (rand.nextInt(10) < 9)
		{
			nodetmp = node.bestChild();
		}
		else
		{
			nodetmp = node.randomChild(rand);
		}

		return treePolicy(nodetmp);
	}

	private OthelloState defaultPolicy(MonteCarloNode node)
	{
		OthelloState new_state = node.stateClone();
		while (!new_state.gameOver())
		{
			List<OthelloMove> moves = new_state.generateMoves();
			if (moves.size() > 0)
			{
				new_state.applyMove(moves.get(rand.nextInt(moves.size())));
			}
			else
			{
				new_state.applyMove(null);
			}
		}

		return new_state;
	}
}
