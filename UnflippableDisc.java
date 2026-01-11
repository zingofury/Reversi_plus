public class UnflippableDisc implements Disc{
    private Player owner; //the player the disc belong to
    public static final String Symbol="⭕"; // saving the symbol in one place for consistency
    public UnflippableDisc(Player player){
        this.owner=player;
    }

    /**
     * get the owner of the disc
     *
     * @return Player of which the disc belong to
     */
    @Override
    public Player getOwner() {
        return this.owner;
    }

    /**
     * set the owner of the disc
     *
     * @param player the player to be assigned to the disc
     */
    @Override
    public void setOwner(Player player) {
        this.owner=player;
    }

    /**
     * get the type of the disc
     *
     * @return the string symbol of an unflappable disc
     */
    @Override
    public String getType() {
        return UnflippableDisc.Symbol;
    }
}
