import java.util.*;

public class GreedyAI extends AIPlayer{
    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    public Move makeMove(PlayableLogic gameStatus) {
        List<Position> moves = gameStatus.ValidMoves();
        List<Position> greedyMoves= new ArrayList<>(); //a list of all the greedy moves
        int mostFlips = 0;

        Comparator<Position> comparatorByPosition= new Comparator<>() {
            /**
             * compare the two positions by location
             *
             * @param o1 the first position to be compared.
             * @param o2 the second position to be compared.
             * @return (if both at the same col return 1 if o1 is below, - 1 if o2 below, or else 0) else if o1 is righter return 1 and -1 if o2 is righter
             */
            @Override
            public int compare(Position o1, Position o2) {
                if (o1.col() > o2.col()) { // o1 is righter
                    return 1; // for o1
                } else if (o1.col() < o2.col()) { // o2 is righter
                    return -1; // for o2
                } else { //same col
                    return Integer.compare(o1.row(), o2.row()); // return 1 if o1 below, -1 if o2 below and 0 if the same
                }
            }
        };
        for (Position currentPos : moves) {
            int numOfFlips = gameStatus.countFlips(currentPos);
            if (numOfFlips > mostFlips) {
                greedyMoves.clear(); //reset the greedy moves
                greedyMoves.add(currentPos);
                mostFlips = numOfFlips;
            }
            if (numOfFlips == mostFlips) {
                greedyMoves.add(currentPos);
            }
        }
        SimpleDisc disc = new SimpleDisc(this);
        Position greedyPos = Collections.max(greedyMoves,comparatorByPosition);
        return new Move((GameLogic) gameStatus,disc,greedyPos);
    }
}
