import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer{
    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    //Getters
    public int getNumOfBombs(){
        return this.number_of_bombs;
    }
    public int getNumOfUnflippedable(){
        return this.number_of_unflippedable;
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        List<Position> moves = gameStatus.ValidMoves();
        if (moves.size()>0) {
            int numOfMoves = moves.size();  //range to pick a random move from
            Random rand = new Random();     //creating an instance of Random
            int randomMoveIndex = rand.nextInt(numOfMoves);    //picking the random move index
            Position randomPosition = moves.get(randomMoveIndex); //picks the random move

            List<String> availableDiscs = new ArrayList<>(); //creating a list of available bombs
            availableDiscs.add("simple"); //adds a simple disc to available discs
            if (getNumOfBombs()>0){ availableDiscs.add("bomb");} //adds a bomb to available discs
            if (getNumOfUnflippedable()>0){availableDiscs.add("unflippedable");} //adds an unflippedable disc to the list
            int randomDiscIndex = rand.nextInt(availableDiscs.size()); //picking a random index from the list
            switch (availableDiscs.get(randomDiscIndex)) {
                case "simple" -> {
                    SimpleDisc disc = new SimpleDisc(this);
                    return new Move((GameLogic) gameStatus,disc,randomPosition);
                }
                case "bomb" -> {
                    BombDisc disc = new BombDisc(this);
                    return new Move((GameLogic) gameStatus,disc,randomPosition);
                }
                case "unflippedable" -> {
                    UnflippableDisc disc = new UnflippableDisc(this);
                    return new Move((GameLogic) gameStatus,disc,randomPosition);
                }
            }
        }
        return null;
    }
}
