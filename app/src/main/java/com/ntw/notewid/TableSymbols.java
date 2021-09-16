package com.ntw.notewid;

import java.util.Arrays;
import java.util.List;

public class TableSymbols {
    private static List<Character> chars_rus = Arrays.asList('й', 'ц', 'у', 'к', 'е', 'н', 'г', 'ш', 'щ', 'з', 'х', 'ъ', 'ф', 'ы', 'в', 'а', 'п', 'р', 'о', 'л', 'д', 'ж', 'э', 'я', 'ч', 'с', 'м', 'и', 'т', 'ь', 'б', 'ю', 'ё', 'Й', 'Ц', 'У', 'К', 'Е', 'Н', 'Г', 'Ш', 'Щ', 'З', 'Х', 'Ъ', 'Ф', 'Ы', 'В', 'А', 'П', 'Р', 'О', 'Л', 'Д', 'Ж', 'Э', 'Я', 'Ч', 'С', 'М', 'И', 'Т', 'Ь', 'Б', 'Ю', 'Ё');
    private static List<Character> chars_en = Arrays.asList('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', ';', '\'', 'z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '`', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', '{', '}', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', ':', '\"', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '<', '>', '~');

    public static String convertLang(String input_str) {
        char[] input_char = input_str.toCharArray();
        for (int i = 0; i < input_char.length; i++) {
            if (Character.isLetter(input_char[i])) {
                if (chars_rus.contains(input_char[i])) {
                    input_char[i] = chars_en.get(chars_rus.indexOf(input_char[i]));
                } else if (chars_en.contains(input_char[i])) {
                    input_char[i] = chars_rus.get(chars_en.indexOf(input_char[i]));
                }
            }
        }
        return String.valueOf(input_char);
    }
}
