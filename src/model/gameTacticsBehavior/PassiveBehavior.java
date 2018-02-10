package model.gameTacticsBehavior;

import java.util.Random;

public class PassiveBehavior implements GameBehavior{
    @Override
    public boolean isCardNeed(int value) {
        if(value < 21) {
            return value < 10 || new Random().nextInt(10) < 3;
        }
        return false;
    }
}
