package io.github.travisdeshotels.poker.terminal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IoUtil {
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void out(String text){
        System.out.println(text);
    }

    public static String in(){
        try {
            return br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String prompt(String text){
        out(text);
        return in();
    }
}
