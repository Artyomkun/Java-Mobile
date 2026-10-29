package homework;

public class DZ_1 {
    public static void main(String[] args) {

        // ===== Задача 1. Переменные всех типов данных =====
        byte myByte = 100;
        short myShort = 1000;
        int myInt = 50000;
        long myLong = 100000L;
        float myFloat = 3.14f;
        double myDouble = 3.14159;
        char myChar = 'A';
        boolean myBoolean = true;
        String myString = "Привет, Java!";

        System.out.println("byte: " + myByte);
        System.out.println("int: " + myInt);
        System.out.println("float: " + myFloat);
        System.out.println("char: " + myChar);
        System.out.println("String: " + myString);

        // ===== Задача 2. Приветствие по имени =====
        greet("Иван");
        greet("Мария");

        // ===== Задача 3. Проверка на отрицательное число =====
        System.out.println("\n=== Задача 3 ===");
        System.out.println(isNegative(10));  // false

        // ===== Задача 4. Вывод знака числа =====
        System.out.println("\n=== Задача 4 ===");
        printSign(-7);
        printSign(42);
        printSign(0);

        // ===== Задача 5. Числа от 1000 до 0, кратные 3 =====
        System.out.println("\n=== Задача 5 ===");
        printMultiplesOfThree();
    }

    // Метод для задачи 2
    public static void greet(String name) {
        System.out.println("Привет, " + name + "!");
    }

    // Метод для задачи 3
    public static boolean isNegative(int number) {
        return number < 0;
    }

    // Метод для задачи 4
    public static void printSign(int number) {
        if (number < 0) {
            System.out.println("Число " + number + " отрицательное");
        } else {
            System.out.println("Число " + number + " положительное");
        }
    }

    // Метод для задачи 5
    public static void printMultiplesOfThree() {
        for (int i = 1000; i >= 0; i--) {
            if (i % 3 == 0) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }
}