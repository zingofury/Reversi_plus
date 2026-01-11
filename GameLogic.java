import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic{

    private Disc[][] board; // 2d matrix of the discs on board. each coordination is a Position
    private Player player1; // the first player
    private Player player2; // the second player
    private boolean is_player1_turn; // indicate if it's the first player turn
    Stack<Move> moves=new Stack<>(); // stack of the moves taken to far
    public GameLogic(){
        this.is_player1_turn=true;
        int size=this.getBoardSize();
        this.board=new Disc[size][size];
    }


    @Override
    public boolean locate_disc(Position a, Disc disc) {
        if (this.isValidMove(a)) {
            Player disc_owner=disc.getOwner();
            String disc_type=disc.getType();
            if (disc_type.equals(BombDisc.Symbol)){ // dealing with the bomb discs counter and limit
                if (disc_owner.getNumber_of_bombs()>0) {
                    disc_owner.reduce_bomb();
                }else {
                    return false;
                }
            } else if (disc_type.equals(UnflippableDisc.Symbol)){ // dealing with the unflappable discs counter and limit
                if (disc_owner.getNumber_of_unflippedable()>0) {
                    disc_owner.reduce_unflippedable();
                }else {
                    return false;
                }
            }
            Move move = new Move(this, disc, a);
            moves.add(move);
            move.executeMove();
            this.changeTurn();
            if (this.isGameFinished()){
                this.printGameResultsAndIncreaseWin();
            }
            return true;
        } else {
            return  false;
        }
    }

    @Override
    public List<Position> ValidMoves() {
        List<Position> validMoves= new ArrayList<>();

        // iterate on all the positions on the board
        for (int row = 0; row <this.getBoardSize() ; row++) {
            for (int col = 0; col <this.getBoardSize() ; col++) {
                Position pos =new Position(row,col);
                if (this.isValidMove(pos)){ // if the placement of this position is valid then add the position to the list
                    validMoves.add(pos);
                }
            }
        }
        return validMoves;
    }

    @Override
    public int countFlips(Position a) {return this.getDisksToFlipPositions(a).size();}
    @Override
    public Disc getDiscAtPosition(Position position) {
        int size=this.getBoardSize();
        int col=position.col();
        int row=position.row();
        if (col<0 || row<0 || col>=size || row >=size){ // check that this position isn't out of bound
            return null;
        }else{
            return this.board[row][col];
        }
    }

    @Override
    public int getBoardSize() {return 8;}

    @Override
    public Player getFirstPlayer() {return this.player1;}

    @Override
    public Player getSecondPlayer() {return this.player2;}

    @Override
    public void setPlayers(Player player1, Player player2) {
        this.player1=player1;
        this.player2=player2;
    }

    @Override
    public boolean isFirstPlayerTurn() {return this.is_player1_turn;}

    @Override
    public boolean isGameFinished() {return this.ValidMoves().isEmpty();}

    @Override
    public void reset() {
        int size=this.getBoardSize();
        this.board=new Disc[size][size]; // clear the board

        //set the four initial discs art the center
        this.board[size/2][size/2]=new SimpleDisc(player1);
        this.board[size/2 -1][size/2]=new SimpleDisc(player2);
        this.board[size/2][size/2-1]=new SimpleDisc(player2);
        this.board[size/2-1][size/2-1]=new SimpleDisc(player1);

        this.moves= new Stack<>(); // clear the moves stack
        for (Player player : new Player[]{player1, player2}) { // reset the bombs and unflappable counter of each player
            player.reset_bombs_and_unflippedable();
        }
        this.is_player1_turn=true; // switch the turn back to player1
    }

    @Override
    public void undoLastMove() {
        if (!this.moves.isEmpty()) { // if no move taken yet - do nothing
            Move lastMove = this.moves.pop(); // get the last move done
            lastMove.undo();

            //increase back the bomb or unflappable counter if the disc type is one of them
            if (lastMove.disc().getType().equals(BombDisc.Symbol)){
                lastMove.disc().getOwner().increase_bomb();
            } else if (lastMove.disc().getType().equals(UnflippableDisc.Symbol)){
                lastMove.disc().getOwner().increase_unflippedable();
            }
            this.changeTurn();
        }else {
            System.out.println("\tNo previous move available to undo.");
        }
        System.out.println();
    }

    /**
     * get the positions of the discs that will be flipped if current player place disc at a position
     *
     * @param disc_placed_at the position  the disc to be placed at
     * @return list of all the position to be flipped
     */
    public List<Position> getDisksToFlipPositions(Position disc_placed_at){
        List<Position> otherPlayerDiscs=new ArrayList<>(); // the list of all the discs to be flipped - also used to prevent flipping more that once the same disc

        //iterate on all the 8 direction to go from this position
        for (int vec_y = -1; vec_y <=1 ; vec_y++) {
            for (int vec_x = -1; vec_x <=1 ; vec_x++) {
                boolean is_other_player_discs_between=false;
                if (vec_y==0 && vec_x==0){
                    continue; // because in this case it will not move in any direction
                }
                List<Position> otherPlayerInThisDirection=new ArrayList<>(); // the list of all the discs belong to the other player found in this direction

                //iterate on  position in this direction from the placing position
                for (int row_to_check=disc_placed_at.row()+vec_y, col_to_check=disc_placed_at.col()+vec_x;;
                     row_to_check+=vec_y,col_to_check+=vec_x){

                    Position pos_to_check=new Position(row_to_check,col_to_check);
                    Disc disc_to_check=this.getDiscAtPosition(pos_to_check);
                    if (disc_to_check==null){
                        //went out of bound or to empty before reaching current player disk-search in other directions
                        break;
                    }
                    if (isDiscOfTheCurrentPlayer(disc_to_check)) { // check if the disc belong to the current playing player
                        if (is_other_player_discs_between) {
                            //there is another disk of the current player in the other side and
                            //other player disks between the two disks - add disks to primary lists
                            // and check other directions
                            otherPlayerDiscs.addAll(otherPlayerInThisDirection); // the discs of the other player found in this direction should be flipped
                        }
                        break; // stop in this direction
                    }else{
                        //in this location there is other player disk
                        is_other_player_discs_between=true;
                        if (!disc_to_check.getType().equals(UnflippableDisc.Symbol)){
                            otherPlayerInThisDirection.add(pos_to_check);
                        }
                    }
                }
            }
        }

        //iterate on all the bomb disc found to be flipped till now and recursively all the discs to be flipped by them
        for (int i=0; i<otherPlayerDiscs.size();i++){
            Position pos= otherPlayerDiscs.get(i);
            if (this.getDiscAtPosition(pos).getType().equals(BombDisc.Symbol)){
                addDisksToFlipByBombToList(pos,otherPlayerDiscs);
            }
        }
        return otherPlayerDiscs;
    }

    /**
     * flip a disc at certain position
     *
     * @param pos_of_disc the pos of the disc to be flipped
     */
    public void flipDisc(Position pos_of_disc){
        Disc diskToFlip=this.getDiscAtPosition(pos_of_disc);
        if (diskToFlip!=null) //second test for bug prevention
        {
            if (diskToFlip.getOwner().isPlayerOne){
                diskToFlip.setOwner(this.player2);
            }else{
                diskToFlip.setOwner(this.player1);
            }
        }
    }


    /**
     * get a pointer to the 2d-matrix board of the game
     *
     * @return pointer to the 2d-matrix board of the game
     */
    public Disc[][] getBoardPtr(){
        return this.board;
    }

    /**
     * get the player which his turn now
     *
     * @return the player which his turn now
     */
    private Player getCurrentPlayer(){return this.is_player1_turn?player1:player2; }

    /**
     * get a list of position to be flipped and recursively add to it the positions to be flipped by a certain bomb and by bombs flipped by it
     *
     * @param  bomb_placed_at the position to place the bomb
     * @param listToAddTo the list to add the position flipped by the bomb to
     */
    private void addDisksToFlipByBombToList(Position bomb_placed_at,List<Position> listToAddTo){
        //iterate on all the 8 direction to go from this position
        for (int vec_y = -1; vec_y <=1 ; vec_y++) {
            for (int vec_x = -1; vec_x <=1 ; vec_x++) {
                if (vec_y==0 && vec_x==0){
                    continue; // the bomb itself position
                }
                Position pos_to_check=new Position(bomb_placed_at.row()+vec_y,bomb_placed_at.col()+vec_x);
                Disc disc_to_check=this.getDiscAtPosition(pos_to_check);
                if (disc_to_check==null){
                    //went out of bound or to empty
                    continue;
                }

                // adding the disc to the list only if is of the other player, isn't yet in the list(to avoid second flipping of it) and isn't unflappable
                if ((!isDiscOfTheCurrentPlayer(disc_to_check))
                        && !listToAddTo.contains(pos_to_check)
                         && (!disc_to_check.getType().equals(UnflippableDisc.Symbol))){
                    listToAddTo.add(pos_to_check);
                    //if this is a bomb disc then add recursively all the discs to be flipped by it
                    if (disc_to_check.getType().equals(BombDisc.Symbol)){ // add the
                        this.addDisksToFlipByBombToList(pos_to_check,listToAddTo);
                    }
                }

            }
        }
    }

    /**
     *get number of disc per player on the board
     *
     * @return 2 cells array where in the 0 index the number of discs of player1 and at 1 the number of discs of player2
     */
    private int[] getNumOfDiscsPerPlayer(){
        int player1_discs=0,player2_discs=0;
        for (int row = 0; row <this.getBoardSize() ; row++) {
            for (int col = 0; col <this.getBoardSize() ; col++) {
                Position pos =new Position(row,col);
                Disc disc = this.getDiscAtPosition(pos);
                if (disc!=null){
                    if (disc.getOwner().isPlayerOne()){
                        player1_discs++;
                    }else{
                        player2_discs++;
                    }
                }
            }
        }
        return new int[]{player1_discs, player2_discs};
    }

    /**
     * indicate if the current player can place a disc at certain position
     *
     * @param position the position to be placed at
     * @return true if disc can be placed there and false if not
     */
    private boolean isValidMove(Position position){
        if (this.getDiscAtPosition(position)!=null){ // if the square already has disc, new disc cant be placed there
            return false;
        }
        return countFlips(position)>0; // it is valid if the location is empty and the cause at least one flip
    }

    /**
     * change turn to the other player
     *
     */
    private void changeTurn(){
        this.is_player1_turn=!this.is_player1_turn;
    }


    /**
     * print text that write which player lead and how many discs there are of each player
     * and increase the win counter of the winner player
     *
     */
    private void printGameResultsAndIncreaseWin(){
        int []players_num_of_discs=this.getNumOfDiscsPerPlayer();
        int player1_discs=players_num_of_discs[0];
        int player2_discs=players_num_of_discs[1];

        if (player1_discs>player2_discs){
            System.out.printf("Player 1 wins with %d discs! Player 2 had %d discs.",player1_discs,player2_discs);
            this.player1.addWin();
        }else if(player1_discs<player2_discs){
            this.player2.addWin();
            System.out.printf("Player 2 wins with %d discs! Player 1 had %d discs.",player2_discs,player1_discs);
        }

    }

    /**
     * indicate if a disc belong to the current playing player
     *
     * @param disc // the disc to check
     * @return true if the disc belong to the current player  and false if not
     */
    private boolean isDiscOfTheCurrentPlayer(Disc disc){
        return disc.getOwner().isPlayerOne()==getCurrentPlayer().isPlayerOne();
    }

}
