import java.util.List;


public class Move {
    private final Disc _disc; // disc placed in the move
    private final Position _position;
    private final List<Position> flippedDisksPositions; // the positions of the disc flipped by this move
    private final Disc[][] ptr_board; // pointer to the board for placing discs and flipping discs from here
    private final GameLogic game_logic; // the game logic object - for using methods of it
    public Move(GameLogic game_logic,Disc _disc, Position _position){
        this.game_logic=game_logic;
        this.ptr_board=game_logic.getBoardPtr();
        this._disc=_disc;
        this._position=_position;
        this.flippedDisksPositions=game_logic.getDisksToFlipPositions(_position);
    }

    /**
     * placing the specific disc at the specific position defined in this move and flipping all the discs that should be flipped by it
     * and printing info of the disc placement the all the flips
     *
     */
    public void executeMove(){
        int player_num=(this._disc.getOwner().isPlayerOne?1:2);
        this.ptr_board[_position.row()][_position.col()]=_disc;
        System.out.printf("Player %d placed a %s in %s\n",player_num,this._disc.getType(),this._position.toString());
        for (Position pos : this.flippedDisksPositions){
            this.game_logic.flipDisc(pos);
            System.out.printf("Player %d flipped the %s in %s\n",player_num,game_logic.getDiscAtPosition(pos).getType(),pos.toString());
        }
        System.out.println();
    }

    /**
     * get the disc placed in this move
     *
     * @return the disc placed in this move
     */
    public Disc disc(){
        return this._disc;
    }

    /**
     * get the position the disc placed in this move
     *
     * @return the position the disc placed in this move
     */
    public Position position(){
        return this._position;
    }

    /**
     * undoing this specific move - remove the disc placed at it and flip back all the discs flipped at it
     * and printing info of the disc removed the all the discs flipped back
     */
    public void undo(){
        System.out.print("Undoing last move :\n");
        this.ptr_board[_position.row()][_position.col()]=null;
        System.out.printf("\tUndo: removing %s from %s\n",this._disc.getType(),this._position.toString());
        for (Position pos : this.flippedDisksPositions){
            this.game_logic.flipDisc(pos);
            System.out.printf("\tUndo: flipping back %s in %s\n",game_logic.getDiscAtPosition(pos).getType(),pos.toString());
        }
    }
}
