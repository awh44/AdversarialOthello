package cs380.othello;

public class OthelloScorerStandard implements OthelloScorer
{
	@Override
	public int score(OthelloState state)
	{
		return state.score();
	}
}
