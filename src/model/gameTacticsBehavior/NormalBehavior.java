package model.gameTacticsBehavior;

import java.util.Random;

public class NormalBehavior implements GameBehavior {
    @Override
    public boolean isCardNeed(int value) {
        if(value < 21) {
            if (value < 12) {
                return true;
            } else if (value <= 17) {
                new Random().nextBoolean();
            }
        }
        return false;
    }
}
