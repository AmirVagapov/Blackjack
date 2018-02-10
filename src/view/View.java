package view;

import java.util.Scanner;

public class View {

    private static Scanner scanner;

    public static void showHelp() {
        System.out.println("start - начать игру \n more - взять еще одну карту" +
                " \n stop - не брать карту \n help - получить информацию о командах.");
    }

    public static String startGame() {
        System.out.println("Введите \"start\" для начала игры Blackjack. Введите \"exit\" для выхода из игры " +
                "Для получения информации обо всех командах введите \"help\".");
        return scanner.next();
    }

    public static int enterCountPlayers() {
        scanner = new Scanner(System.in);
        System.out.println("Укажите количество игроков(от 1 до 3): ");
        return scanner.nextInt();
    }

    public static void showCountPlayers(int count) {
        System.out.println("Количество игроков " + count);
    }

    public static void showGameInfo(int rate, int bank) {
        System.out.println("Игроки созданы. Размер ставки: " + rate + ". В банке " + bank);
    }

    public static String playerGo(String name) {
        System.out.println("Ход игрока: " + name + ". Введите \"more\", " +
                "чтобы получить еще одну карты, \"double\" чтобы удвоить ставку" +
                " или \"stop\", чтобы передать ход следующему игроку. ");
        return scanner.next();
    }


    public static void showWinner(String name, int win) {
        System.out.println("Победитель: " + name + ". Выигрыш " + win);
    }

    public static String getAnswer(){
        return scanner.next();
    }

    public static void errorCommand() {
        System.out.println("Неизвестная команда. Попробуйте еще раз.");
        scanner.next();
    }

    public static void newDeck() {
        System.out.println("Раздача новой колоды");
    }

    public static void endGame() {
        System.out.println("End Game");
        if(scanner != null){
            scanner.close();
        }
    }

    public static void showScores(String name, int score) {
        System.out.println(name + " " + score + " очков");
    }

    public static void showCardsInfo(String cards) {
        System.out.println(cards);
    }

    public static void showLoser(String name) {
        System.out.println(name + " перебор.");
    }

    public static void nobodyWin() {
        System.out.println("Никто не выиграл");
    }

    public static void playMore() {
        System.out.println("Игра окончена. Если хотите продолжить введите \"next\", " +
                "введите \"exit\" чтобы закончить игру");
    }

    public static void errorDouble() {
        System.out.println("Можно использовать только сразу после раздачи.");
    }

    public static void errorInsurance() {
        System.out.println("Страховка невозможна. У Дилера нет туза");
    }
}
