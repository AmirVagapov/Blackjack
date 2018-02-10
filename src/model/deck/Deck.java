package model.deck;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

    private static Random random = new Random();
    private static ArrayList<Card> deck;

    private Deck() {
        createDeck();
    }

    public static ArrayList<Card> getInstance(){
        if(deck == null){
            createDeck();
        }
        return deck;
    }

    public static Card getRandomCard (int count) throws IllegalArgumentException{
        return deck.get(random.nextInt(count));
    }

    private static void createDeck() {
        deck = new ArrayList<>();
        Card.CardValue[] values = Card.CardValue.values();
        Card.CardSuit[] suits = Card.CardSuit.values();
        for (Card.CardValue value : values) {
            for (Card.CardSuit suit : suits) {
                deck.add(new Card(suit, value));
            }
        }
    }

    public static ArrayList<Card> newDeck(){
        createDeck();
        return deck;
    }

}
