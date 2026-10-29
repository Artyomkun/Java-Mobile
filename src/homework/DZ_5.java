package homework;

import java.util.Arrays;

public class DZ_5 {

    public static <T> void swap(T[] array, int index1, int index2) {
        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static String caesarCipher(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                char shifted = (char) ((c - base + shift) % 26 + base);
                result.append(shifted);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String[] words = {"Hello", "World", "Java"};
        System.out.println("Before swap: " + Arrays.toString(words));
        swap(words, 0, 2);
        System.out.println("After swap:  " + Arrays.toString(words));
        String original = "Hello, World!";
        String encrypted = caesarCipher(original, 3);
        System.out.println("Original:  " + original);
        System.out.println("Encrypted: " + encrypted);
    }
}