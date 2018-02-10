package model.controler;

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

import java.util.ArrayList;
import java.util.InputMismatchException;

public class Game {

    private final String START_GAME = "start";
    private final String MORE = "more";
    private final String STOP = "stop";
    private final String EXIT = "exit";
    private final String HELP = "help";
    private final String NEXT = "next";
    private final String DOUBLE = "double";
    private final String PLAYER_NAME = "Player";
    private final String DEALER_NAME = "Dealer";
    private final String BOT_NAME = "Bot";
    private final String INSURANCE = "insurance";
    private ArrayList<Card> deck;
    private int cardsCount = 52;
    private int playersCount;
    private ArrayList<Player> players;
    private int bank;
    private final int RATE = 100;
    private boolean nextRound;
    private boolean firstGive;
    private boolean isDoubleAvailable = true;
    private ArrayList<Player> doubleList;
    private int doubleWinnersCount;
    private boolean insurance;

    private View view;

    public Game(View view) {
        this.view = view;
        players = new ArrayList<>();
        doubleList = new ArrayList<>();
        initialGame();
    }

    private boolean startGame() {
        String startCommand;
        while (playersCount > 3 || playersCount < 1) {
            try {
                playersCount = view.enterCountPlayers();
            } catch (InputMismatchException exc) {
                view.errorCommand();
            }
        }
        view.showCountPlayers(playersCount);
        do {
            startCommand = view.startGame();
            if (startCommand.toLowerCase().equals(HELP)) {
                view.showHelp();
            } else if (startCommand.toLowerCase().equals(EXIT)) {
                return false;
            }
        } while (!startCommand.toLowerCase().equals(START_GAME));

        return true;
    }

    private void createPlayers() {
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

    private void giveCardInfo() {
        for (Player somePlayer : players) {
            if (somePlayer.getClass().equals(Dealer.class)) {
                if (somePlayer.getCards().get(0).getCardValue().equals(Card.CardValue.Ace)) {
                    insurance = true;
                }
            }
            view.showCardsInfo(somePlayer.toString().toLowerCase());
        }
    }

    private void giveCards() {
        for (Player somePlayer : players) {
            getCards(somePlayer, 2);
            if (somePlayer.getClass().equals(Dealer.class)) {
                ((Dealer) somePlayer).setShowAllCards(false);
            }
        }
    }

    private boolean checkWin() {
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

    private void getCards(Player player, int i) {
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
            view.newDeck();
            getCards(player, i);
        }
    }

    private void playersTurn(Player player) {
        nextRound = false;
        String startCommand = view.playerGo(player.getName());
        switch (startCommand.toLowerCase()) {
            case HELP:
                view.showHelp();
                playersTurn(player);
                break;
            case MORE:
                oneMoreCard(player);
                if (checkLose(player)) {
                    view.showLoser(player.getName());
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
                        view.showLoser(player.getName());
                        return;
                    }
                } else {
                    view.errorDouble();
                    playersTurn(player);
                }
                break;
            case INSURANCE:
                if (insurance) {
                    player.setInsurance(true);
                } else {
                    view.errorInsurance();
                }
            default:
                view.errorCommand();
                playersTurn(player);
                break;
        }
    }

    private boolean checkLose(Player player) {
        int cardAmount = player.cardsValueToInteger();
        return cardAmount > 21;
    }

    private void oneMoreCard(Player player) {
        getCards(player, 1);
        view.showCardsInfo(player.toString());
    }

    private void AITurn() {
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

    private void closeGame() {
        ArrayList<Player> winnersList = getWinners();
        if (winnersList.isEmpty()) {
            view.nobodyWin();
            view.playMore();
            oneMoreRound();
        }
        int win = bank;
        if (winnersList.size() > 1) {
            win = bank / winnersList.size();
        }
        int doubleBank = RATE + (doubleList.size() * RATE);
        for (Player winner : winnersList) {
            if (winner.isDouble()) {
                view.showWinner(winner.getName(), (win + (doubleBank / doubleWinnersCount)));
            } else {
                view.showWinner(winner.getName(), win);
            }
        }

        insurance = false;
        view.playMore();
        oneMoreRound();
    }

    private ArrayList<Player> getWinners() {
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
            view.showScores(somePlayer.getName(), score);
        }
        return winners;
    }

    private void oneMoreRound() {
        firstGive = false;
        String isRoundNeeded = view.getAnswer();
        switch (isRoundNeeded.toLowerCase()) {
            case NEXT:
                nextRound = true;
                break;
            case EXIT:
                nextRound = false;
                break;
            default:
                view.errorCommand();
                oneMoreRound();
                break;
        }
    }

    private void newRound() {
        doubleWinnersCount = 0;
        doubleList.clear();
        if (deck.size() < (52 / 3)) {
            deck = Deck.newDeck();
            cardsCount = deck.size();
            view.newDeck();
        }
        for (Player somePlayer : players) {
            somePlayer.dropCards();
        }
        preparationForGame();
        if (checkWin()) {
            firstGive = true;
        }
    }

    private void preparationForGame() {
        bank = players.size() * RATE;
        view.showGameInfo(RATE, bank);
        giveCards();
        giveCardInfo();
    }


    private void initialGame() {
        if (startGame()) {
            createPlayers();
        } else {
            view.endGame();
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
        view.endGame();
    }
}
