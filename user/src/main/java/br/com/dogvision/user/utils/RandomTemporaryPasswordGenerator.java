package br.com.dogvision.user.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomTemporaryPasswordGenerator {
    
  
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%&*";
    private static final String ALL_CHARS = LOWERCASE + UPPERCASE + DIGITS + SPECIAL;


    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomPassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 8 caracteres.");
        }

        List<Character> passwordChars = new ArrayList<>();

        passwordChars.add(getRandomChar(LOWERCASE));
        passwordChars.add(getRandomChar(UPPERCASE));
        passwordChars.add(getRandomChar(DIGITS));
        passwordChars.add(getRandomChar(SPECIAL));

        for (int i = 4; i < length; i++) {
            passwordChars.add(getRandomChar(ALL_CHARS));
        }
        
        Collections.shuffle(passwordChars, RANDOM);
        StringBuilder sb = new StringBuilder();
        for (Character ch : passwordChars) {
            sb.append(ch);
        }
        return sb.toString();
    }
    private static char getRandomChar(String source) {
        int index = RANDOM.nextInt(source.length()); 
        return source.charAt(index);
    }
}
