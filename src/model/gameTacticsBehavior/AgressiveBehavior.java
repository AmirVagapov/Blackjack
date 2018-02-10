package model.gameTacticsBehavior;


import java.util.Random;

public class AgressiveBehavior implements GameBehavior {
    @Override
    public boolean isCardNeed(int value) {
        if(value <= 21) {
            if (value < 15) {
                return true;
            } else if (value < 19) {
                return new Random().nextBoolean();
            } else {
                return new Random().nextInt(5) < 4;
            }
        }
        return false;
    }
}
