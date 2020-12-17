package com.sic.util;

import java.security.SecureRandom;
import java.util.Random;

public class PasswordGenerator {

    private final static Random rand = new SecureRandom();
    private final static StringBuilder password = new StringBuilder();
    private final static int PASSWORD_SIZE = 6;
    private final static int BOUND = 10;

    public static String getPassword() {
    	password.setLength(0);

        for (int i = 0; i < PASSWORD_SIZE; i++) {
            int randomNumber = rand.nextInt(BOUND);
            password.append(randomNumber);
        }

        return password.toString();
    }
}
