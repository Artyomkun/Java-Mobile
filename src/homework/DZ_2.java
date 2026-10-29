package homework;

import java.util.Random;
import java.util.Scanner;

public class DZ_2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int playAgain = 1;

        while (playAgain == 1) {
            // Генерируем число от 0 до 10 включительно
            int secretNumber = random.nextInt(11);
            int attempts = 3;
            boolean guessed = false;

            System.out.println("=== Игра «Угадай число» ===");
            System.out.println("Я загадал число от 0 до 10. У тебя 3 попытки.");

            for (int i = 1; i <= attempts; i++) {
                System.out.print("Попытка " + i + ". Введи число: ");
                int userNumber = scanner.nextInt();

                if (userNumber == secretNumber) {
                    System.out.println("🎉 Поздравляю! Ты угадал число " + secretNumber + "!");
                    guessed = true;
                    break;
                } else if (userNumber < secretNumber) {
                    System.out.println("Загаданное число БОЛЬШЕ.");
                } else {
                    System.out.println("Загаданное число МЕНЬШЕ.");
                }
            }

            if (!guessed) {
                System.out.println("😢 Ты проиграл. Было загадано число: " + secretNumber);
            }

            // Запрос на повтор
            System.out.print("\nПовторить игру еще раз? 1 - да / 0 - нет: ");
            playAgain = scanner.nextInt();

            System.out.println();
        }

        System.out.println("Спасибо за игру!");
        scanner.close();
    }
}