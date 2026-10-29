package homework;

import java.util.*;

public class DZ_4 {
    public static void main(String[] args) {
        // Массив слов (10-20 слов, с повторяющимися)
        String[] wordsArray = {
                "кот", "собака", "кот", "мышь", "собака",
                "утка", "кот", "гусь", "мышь", "утка",
                "собака", "лиса", "волк", "лиса", "кот",
                "заяц", "мышь", "утка", "собака", "волк"
        };

        System.out.println("Исходный массив слов:");
        System.out.println(Arrays.toString(wordsArray));
        System.out.println();

        // 1. Найти и вывести список уникальных слов, из которых состоит массив
        LinkedHashSet<String> uniqueWords = new LinkedHashSet<>(Arrays.asList(wordsArray));

        System.out.println("Список уникальных слов:");
        for (String word : uniqueWords) {
            System.out.println("- " + word);
        }
        System.out.println();

        // 2. Посчитать, сколько раз встречается каждое слово
        HashMap<String, Integer> wordCountMap = new HashMap<>();

        for (String word : wordsArray) {
            if (wordCountMap.containsKey(word)) {
                wordCountMap.put(word, wordCountMap.get(word) + 1);
            } else {
                wordCountMap.put(word, 1);
            }
        }

        System.out.println("Сколько раз встречается каждое слово:");
        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            System.out.println(entry.getKey() + " — " + entry.getValue() + " раз(а)");
        }
    }
}