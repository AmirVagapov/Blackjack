import model.deck.Card;
import model.deck.Deck;
import model.gameTacticsBehavior.AgressiveBehavior;
import model.gameTacticsBehavior.DealerBehavior;
import model.gameTacticsBehavior.NormalBehavior;
import model.gameTacticsBehavior.PassiveBehavior;
import model.players.Bot;
import model.players.Dealer;
import model.players.Player;
import view.View;

import java.util.*;

public class Main {

    private static final String START_GAME = "start";
    private static final String MORE = "more";
    private static final String STOP = "stop";
    private static final String EXIT = "exit";
    private static final String HELP = "help";
    private static final String NEXT = "next";
    private static final String DOUBLE = "double";
    private static final String PLAYER_NAME = "Player";
    private static final String DEALER_NAME = "Dealer";
    private static final String BOT_NAME = "Bot";
    private static final String INSURANCE = "insurance";
    private static ArrayList<Card> deck;
    private static int cardsCount = 52;
    private static int playersCount;
    private static ArrayList<Player> players = new ArrayList<>();
    private static int bank;
    private static final int RATE = 100;
    private static boolean nextRound;
    private static boolean firstGive;
    private static boolean isDoubleAvailable = true;
    private static ArrayList<Player> doubleList = new ArrayList<>();
    private static int doubleWinnersCount;
    private static boolean insurance;


    private static boolean startGame() {
        String startCommand;
        while (playersCount > 3 || playersCount < 1) {
            try {
                playersCount = View.enterCountPlayers();
            } catch (InputMismatchException exc) {
                View.errorCommand();
            }
        }
        View.showCountPlayers(playersCount);
        do {
            startCommand = View.startGame();
            if (startCommand.toLowerCase().equals(HELP)) {
                View.showHelp();
            } else if (startCommand.toLowerCase().equals(EXIT)) {
                return false;
            }
        } while (!startCommand.toLowerCase().equals(START_GAME));

        return true;
    }

    private static void createPlayers() {
        deck = Deck.getInstance();
        for (int i = 0; i < playersCount; i++) {
            players.add(new Player(PLAYER_NAME + i));
        }
        players.add(new Bot(BOT_NAME, new PassiveBehavior()));
        players.add(new Bot(BOT_NAME, new AgressiveBehavior()));
        players.add(new Bot(BOT_NAME, new NormalBehavior()));
        players.add(new Dealer(DEALER_NAME, new DealerBehavior()));

        preparationForGame();
    }

    private static void giveCardInfo() {
        for (Player somePlayer : players) {
            if (somePlayer.getClass().equals(Dealer.class)) {
                if (somePlayer.getCards().get(0).getCardValue().equals(Card.CardValue.Ace)) {
                    insurance = true;
                }
            }
            View.showCardsInfo(somePlayer.toString().toLowerCase());
        }
    }

    private static void giveCards() {
        for (Player somePlayer : players) {
            getCards(somePlayer, 2);
            if (somePlayer.getClass().equals(Dealer.class)) {
                ((Dealer) somePlayer).setShowAllCards(false);
            }
        }
    }

    private static boolean checkWin() {
        int cardAmount = 0;
        for (Player somePlayer : players) {
            if (somePlayer.getClass().equals(Dealer.class) && firstGive) {
                continue;
            }
            cardAmount = somePlayer.cardsValueToInteger();
            if (cardAmount == 21) {
                return true;
            }
        }
        return false;
    }

    private static void getCards(Player player, int i) {
        try {
            for (int j = 0; j < i; j++) {
                Card card = Deck.getRandomCard(cardsCount);
                deck.remove(card);
                cardsCount--;
                player.getCards().add(card);
            }
        } catch (IllegalArgumentException exc) {
            deck = Deck.newDeck();
            cardsCount = deck.size();
            View.newDeck();
            getCards(player, i);
        }
    }

    private static void playersTurn(Player player) {
        nextRound = false;
        String startCommand = View.playerGo(player.getName());
        switch (startCommand.toLowerCase()) {
            case HELP:
                View.showHelp();
                playersTurn(player);
                break;
            case MORE:
                oneMoreCard(player);
                if (checkLose(player)) {
                    View.showLoser(player.getName());
                    return;
                }
                isDoubleAvailable = false;
                playersTurn(player);
                break;
            case STOP:
                isDoubleAvailable = true;
                break;
            case DOUBLE:
                if (isDoubleAvailable) {
                    doubleList.add(player);
                    oneMoreCard(player);
                    player.setDouble(true);
                    if (checkLose(player)) {
                        View.showLoser(player.getName());
                        return;
                    }
                } else {
                    View.errorDouble();
                    playersTurn(player);
                }
                break;
            case INSURANCE:
                if (insurance) {
                    player.setInsurance(true);
                } else {
                    View.errorInsurance();
                }
            default:
                View.errorCommand();
                playersTurn(player);
                break;
        }
    }

    private static boolean checkLose(Player player) {
        int cardAmount = player.cardsValueToInteger();
        return cardAmount > 21;
    }

    private static void oneMoreCard(Player player) {
        getCards(player, 1);
        View.showCardsInfo(player.toString());
    }

    private static void AITurn() {
        for (Player aiPlayer : players) {
            if (aiPlayer.getClass().equals(Player.class)) {
                continue;
            }
            if (aiPlayer.getClass().equals(Dealer.class)) {
                ((Dealer) aiPlayer).setShowAllCards(true);
            }
            while (((Bot) aiPlayer).needMoreCard()) {
                getCards(aiPlayer, 1);
            }
        }
    }

    private static void closeGame() {
        ArrayList<Player> winnersList = getWinners();
        if (winnersList.isEmpty()) {
            View.nobodyWin();
            View.playMore();
            oneMoreRound();
        }
        int win = bank;
        if (winnersList.size() > 1) {
            win = bank / winnersList.size();
        }
        int doubleBank = RATE + (doubleList.size() * RATE);
        for (Player winner : winnersList) {
            if (winner.isDouble()) {
                View.showWinner(winner.getName(), (win + (doubleBank / doubleWinnersCount)));
            } else {
                View.showWinner(winner.getName(), win);
            }
        }

        insurance = false;
        View.playMore();
        oneMoreRound();
    }

    private static ArrayList<Player> getWinners() {
        doubleWinnersCount = 1;
        int max = 0;
        ArrayList<Player> winners = new ArrayList<>();
        for (Player somePlayer : players) {
            if (firstGive && somePlayer.getClass().equals(Dealer.class)) {
                continue;
            }
            if (somePlayer.getClass().equals(Dealer.class) && !doubleList.isEmpty()) {
                somePlayer.setDouble(true);
            }
            int score = somePlayer.cardsValueToInteger();
            if (score == max && score <= 21) {
                winners.add(somePlayer);
                if (somePlayer.isDouble()) {
                    doubleWinnersCount++;
                }
            } else if (score > max && score <= 21) {
                doubleWinnersCount = 0;
                if (somePlayer.isDouble()) {
                    doubleWinnersCount = 1;
                }
                max = score;
                winners.clear();
                winners.add(somePlayer);
            }
            View.showScores(somePlayer.getName(), score);
        }
        return winners;
    }

    private static void oneMoreRound() {
        firstGive = false;
        String isRoundNeeded = View.getAnswer();
        switch (isRoundNeeded.toLowerCase()) {
            case NEXT:
                nextRound = true;
                break;
            case EXIT:
                nextRound = false;
                break;
            default:
                View.errorCommand();
                oneMoreRound();
                break;
        }
    }

    private static void newRound() {
        doubleWinnersCount = 0;
        doubleList.clear();
        if (deck.size() < (52 / 3)) {
            deck = Deck.newDeck();
            cardsCount = deck.size();
            View.newDeck();
        }
        for (Player somePlayer : players) {
            somePlayer.dropCards();
        }
        preparationForGame();
        if (checkWin()) {
            firstGive = true;
        }
    }

    private static void preparationForGame() {
        bank = players.size() * RATE;
        View.showGameInfo(RATE, bank);
        giveCards();
        giveCardInfo();
    }

    public static void main(String[] args) {
        if (startGame()) {
            createPlayers();
        } else {
            View.endGame();
            return;
        }
        if (checkWin()) {
            firstGive = true;
        }
        do {
            if (!firstGive) {
                for (int i = 0; i < playersCount; i++) {
                    if (players.get(i).getClass().equals(Player.class)) {
                        Player player = players.get(i);
                        playersTurn(player);
                    }
                }
                AITurn();
            }
            closeGame();
            if (nextRound) {
                newRound();
            }
        } while (nextRound);
        View.endGame();
    }

}
