package model.players;

import model.deck.Card;

import java.util.ArrayList;

public class Player {
    ArrayList<Card> cards;
    String name;
    private boolean isDouble;
    private boolean isInsurance;

    public Player(String name) {
        cards = new ArrayList<>();
        this.name = name;
    }

    public boolean isDouble() {
        return isDouble;
    }

    public void setDouble(boolean aDouble) {
        isDouble = aDouble;
    }

    public boolean isInsurance() {
        return isInsurance;
    }


    public void setInsurance(boolean insurance) {
        isInsurance = insurance;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void dropCards() {
        cards.clear();
        isDouble = false;
        isInsurance = false;
    }

    @Override
    public String toString() {
        StringBuilder cardsToString = new StringBuilder(name).append(": ");
        for (Card card : cards) {
            cardsToString.append(card.getCardValue()).append(" ").append(card.getCardSuit()).append(", ");
        }
        return cardsToString.toString();
    }

    public int cardsValueToInteger() {
        int sum = 0;
        for (Card card : cards) {
            switch (card.getCardValue()) {
                case Two:
                    sum += 2;
                    break;
                case Three:
                    sum += 3;
                    break;
                case Four:
                    sum += 4;
                    break;
                case Five:
                    sum += 5;
                    break;
                case Six:
                    sum += 6;
                    break;
                case Seven:
                    sum += 7;
                    break;
                case Eight:
                    sum += 8;
                    break;
                case Nine:
                    sum += 9;
                    break;
                case Ace:
                    sum += 11;
                    break;
                default:
                    sum += 10;
                    break;
            }
        }
        if(sum > 21) {
            for (Card card : cards) {
                if(card.getCardValue().equals(Card.CardValue.Ace)){
                    sum -= 10;
                }
            }
        }
        return sum;
    }

}
