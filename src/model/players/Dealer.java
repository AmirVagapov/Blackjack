package model.players;


import model.deck.Card;
import model.gameTacticsBehavior.GameBehavior;

public class Dealer extends Bot {

    private boolean showAllCards;


    public Dealer(String name, GameBehavior behavior) {
        super(name, behavior);
        this.showAllCards = false;
    }


    public void setShowAllCards(boolean showAllCards) {
        this.showAllCards = showAllCards;
    }

    @Override
    public String toString() {

        StringBuilder cardsToString = new StringBuilder(name + ": ");
        int size = cards.size();
        String space = "";
        if (!showAllCards) {
            size = size - 1;
            space = "***";
        }
        for (int i = 0; i < size; i++) {
            cardsToString.append(cards.get(i).getCardValue())
                    .append(" ")
                    .append(cards.get(i).getCardSuit())
                    .append(", ")
                    .append(space);
        }
        return cardsToString.toString();
    }

    @Override
    public boolean needMoreCard() {
        int value = cardsValueToInteger();
        return behavior.isCardNeed(value);
    }

}
