package ch.fhnw;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Main {
    //iwant to push this
    static int PASSWORD_COUNT = 2000;
    static String HASH = "1d56a37fb6b08aa709fe90e12ca59e12";
    static String password;
    static String[][] passwords = new String[2][PASSWORD_COUNT];
    static int counter = 0;

    public static void main(String[] args) {
        for (int i = 0; i < 2000; i++) {
            String password = "0000000";
            for (int j = 0; j < i; j++) {
                password = generatePassword(password, 6);
            }
            passwords[0][i] = password;

            passwords[1][i] = md5(passwords[0][i]);
            passwords[1][i] = reduction(passwords[1][i], 0);
            for (int j = 1; j < 2000; j++) {
                passwords[1][i] = md5(passwords[1][i]);
                passwords[1][i] = reduction(passwords[1][i], j);

            }
        }

        String foundPassword = tryToFindPw(HASH);


        System.out.print("The Password is: " + foundPassword);

    }


    private static String reduction(String hash, int step) {
        StringBuilder clear_text = new StringBuilder("0000000");
        BigInteger H = new BigInteger(hash, 16);
        H = H.add(BigInteger.valueOf(step));

        int L = 7;
        char[] Z = new char[36];
        BigInteger Z_length = BigInteger.valueOf(Z.length);

        for (int i = 0; i < 10; i++) {
            Z[i] = (char) (i + 48);
        }

        for (int i = 10; i < 36; i++) {
            Z[i] = (char) (i + 87);
        }

        for (int i = L - 1; i >= 0; i--) {
            char value = Z[H.mod(Z_length).intValue()];
            clear_text.setCharAt(i, value);
            H = H.divide(Z_length);
        }
        return clear_text.toString();
    }


    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            String hash = new BigInteger(1, messageDigest).toString(16);
            while (hash.length() < 32) {
                hash = "0" + hash;
            }
            return hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generatePassword(String s, int pos) {
        StringBuilder stringBuilder = new StringBuilder(s);
        int i = (int) stringBuilder.charAt(pos);

        if (i >= 48 && i <= 56) {
            stringBuilder.setCharAt(pos, (char) (i + 1));
        } else if (i == 57) {
            stringBuilder.setCharAt(pos, 'a');
        } else if (i >= 97 && i <= 121) {
            stringBuilder.setCharAt(pos, (char) (i + 1));
        } else if (i == 122) {
            String received = generatePassword(stringBuilder.toString(), (pos - 1));
            stringBuilder.delete(0, 7).append(received);
            stringBuilder.setCharAt(pos, '0');
        }

        return stringBuilder.toString();
    }
//fixed this
    //http://royvanrijn.com/blog/2011/01/rainbow-tables/ Find Hash
    public static String tryToFindPw(String searching) {
        for (int i = passwords[0].length - 1; i > -1; i--) {
            String momentum = searching;
            int step = i;

            while (step < 1999) {
                momentum = reduction(momentum, step);
                momentum = md5(momentum);
                step++;


            }
            momentum = reduction(momentum, 1999);

            if (Arrays.asList(passwords[1]).contains(momentum)) {
                int index = Arrays.asList(passwords[1]).indexOf(momentum);
                String temp = passwords[0][index];
                for (int p = 0; p < i; p++) {
                    temp = md5(temp);
                    temp = reduction(temp, p);

                }
                return temp;
            }

        }

        return "not found";

    }
}