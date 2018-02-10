package model.deck;


public class Card {

    public enum CardSuit {
        Diamonds, Clubs, Hearts, Spades
    }

    public enum CardValue{
        Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Ace
    }

    private CardSuit cardSuit;
    private CardValue cardValue;

    Card(CardSuit cardSuit, CardValue cardValue) {
        this.cardSuit = cardSuit;
        this.cardValue = cardValue;

    }

    public CardSuit getCardSuit() {
        return cardSuit;
    }

    public CardValue getCardValue() {
        return cardValue;
    }

}
