package model.players;

import model.deck.Card;
import model.gameTacticsBehavior.GameBehavior;

import java.util.Random;

public class Bot extends Player {

    private static int count = 1;

    GameBehavior behavior;

    public Bot(String name, GameBehavior behavior) {
        super(name);
        this.name = name + count;
        this.behavior = behavior;
        count++;
    }


    @Override
    public String toString() {
        StringBuilder cardsToString = new StringBuilder(name + ": ");
        for (Card card : cards) {
            cardsToString.append(card.getCardValue()).append(" ").append(card.getCardSuit()).append(", ");
        }
        return cardsToString.toString();
    }

    public boolean needMoreCard() {
        int value = cardsValueToInteger();
        return behavior.isCardNeed(value);
    }
}
