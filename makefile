.PHONY: run

run: compile
	java cs380.othello.Test $(SIZE) $(P1) $(D1) $(S1) $(P2) $(D2) $(S2)

compile: cs380/othello/OthelloAlphabetaPlayerAWH.java cs380/othello/OthelloMove.java cs380/othello/OthelloRandomPlayer.java cs380/othello/Test.java cs380/othello/OthelloMinimaxPlayerAWH.java cs380/othello/OthelloPlayer.java cs380/othello/monteCarlo/MonteCarloNode.java cs380/othello/monteCarlo/OthelloMonteCarloPlayerAWH.java cs380/othello/OthelloScorer.java cs380/othello/OthelloScorerStandard.java cs380/othello/OthelloScorerImproved.java
	javac cs380/othello/*.java cs380/othello/monteCarlo/*.java

clean:
	-rm -f cs380/othello/*.class cs380/othello/*/*.class
