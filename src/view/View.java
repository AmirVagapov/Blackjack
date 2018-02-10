package view;

import java.util.Scanner;

public class View {

    private  Scanner scanner;

    public View() {
        scanner = new Scanner(System.in);
    }

    public  void showHelp() {
        System.out.println("start - начать игру \n more - взять еще одну карту" +
                " \n stop - не брать карту \n help - получить информацию о командах.");
    }

    public  String startGame() {
        System.out.println("Введите \"start\" для начала игры Blackjack. Введите \"exit\" для выхода из игры " +
                "Для получения информации обо всех командах введите \"help\".");
        return scanner.next();
    }

    public int enterCountPlayers() {
        System.out.println("Укажите количество игроков(от 1 до 3): ");
        return scanner.nextInt();
    }

    public  void showCountPlayers(int count) {
        System.out.println(String.format("Количество игроков %s", count));
    }

    public void showGameInfo(int rate, int bank) {
        System.out.println(String.format("Игроки созданы. Размер ставки: %s. В банке %s", rate, bank));
    }

    public  String playerGo(String name) {
        System.out.println(String.format("Ход игрока: %s. Введите \"more\", " +
                "чтобы получить еще одну карты, \"double\" чтобы удвоить ставку" +
                " или \"stop\", чтобы передать ход следующему игроку.", name));
        return scanner.next();
    }


    public  void showWinner(String name, int win) {
        System.out.println(String.format("Победитель: %s. Выигрыш %s", name, win));
    }

    public  String getAnswer(){
        return scanner.next();
    }

    public  void errorCommand() {
        System.out.println("Неизвестная команда. Попробуйте еще раз.");
        scanner.next();
    }

    public  void newDeck() {
        System.out.println("Раздача новой колоды");
    }

    public  void endGame() {
        System.out.println("End Game");
        if(scanner != null){
            scanner.close();
        }
    }

    public  void showScores(String name, int score) {
        System.out.println(String.format("%s: %s очков", name, score));
    }

    public  void showCardsInfo(String cards) {
        System.out.println(cards);
    }

    public  void showLoser(String name) {
        System.out.println(String.format("%s перебор.", name));
    }

    public  void nobodyWin() {
        System.out.println("Никто не выиграл");
    }

    public  void playMore() {
        System.out.println("Игра окончена. Если хотите продолжить введите \"next\", " +
                "введите \"exit\" чтобы закончить игру");
    }

    public  void errorDouble() {
        System.out.println("Можно использовать только сразу после раздачи.");
    }

    public  void errorInsurance() {
        System.out.println("Страховка невозможна. У Дилера нет туза");
    }
}
