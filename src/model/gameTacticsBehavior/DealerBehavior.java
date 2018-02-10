package model.gameTacticsBehavior;


public class DealerBehavior implements GameBehavior {
    @Override
    public boolean isCardNeed(int value) {
        return value < 17;
    }
}
