package cs380.othello.monteCarlo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs380.othello.OthelloMove;
import cs380.othello.OthelloState;

public class MonteCarloNode
{
	private OthelloState state_;
	private List<OthelloMove> moves_;
	private OthelloMove action_;
	private MonteCarloNode parent_;
	private List<MonteCarloNode> children_;
	private int visited_;
	private int total_score_;

	public MonteCarloNode(OthelloState state)
	{
		state_ = state;
		moves_ = state_.generateMoves();
		if (moves_.isEmpty())
		{
			moves_.add(null);
		}
		action_ = null;
		parent_ = null;
		children_ = new ArrayList<MonteCarloNode>();
		visited_ = 0;
		total_score_ = 0;
	}

	public MonteCarloNode(MonteCarloNode parent, OthelloMove move)
	{
		this(parent.state_.applyMoveCloning(move));
		parent_ = parent;
		action_ = move;
	}

	public OthelloMove getAction()
	{
		return action_;
	}

	public boolean noMoves()
	{
		return moves_.isEmpty();
	}

	public boolean addNextChild()
	{
		if (children_.size() < moves_.size())
		{
			children_.add(new MonteCarloNode(this, moves_.get(children_.size())));
			return true;
		}

		return false;
	}

	public MonteCarloNode lastChild()
	{
		return children_.get(children_.size() - 1);
	}

	public boolean gameOver()
	{
		return state_.gameOver();
	}

	public void backup(int score)
	{
		visited_++;
		total_score_ += score;
		if (parent_ != null)
		{
			parent_.backup(score);
		}
	}

	public MonteCarloNode bestChild()
	{
		int index = 0;
		if (state_.nextPlayer() == state_.PLAYER1)
		{
			double max = children_.get(0).getAverage();
			for (int i = 1; i < children_.size(); i++)
			{
				double next = children_.get(i).getAverage();
				if (next > max)
				{
					max = next;
					index = i;
				}
			}
		}
		else
		{
			double min = children_.get(0).getAverage();
			for (int i = 1; i < children_.size(); i++)
			{
				double next = children_.get(i).getAverage();
				if (next < min)
				{
					min = next;
					index = i;
				}
			}
		}

		return children_.get(index);
	}

	public MonteCarloNode randomChild(Random rand)
	{
		return children_.get(rand.nextInt(children_.size()));
	}

	public OthelloState stateClone()
	{
		return state_.clone();
	}

	private double getAverage()
	{
		return (double) total_score_ / visited_;
	}
}
