package ch.fhnw;

import com.sun.istack.internal.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Main {

    static int PASSWORD_COUNT = 2000;
    static String HASH = "1d56a37fb6b08aa709fe90e12ca59e12";
    static String password;
    static String[][] passwords = new String[2][PASSWORD_COUNT];
    static boolean isInIf = false;
    static int counter;

    public static void main(String[] args) {
        for (int i = 0; i < 2000; i++) {
            String password = "0000000";
            for (int j = 0; j < i; j++) {
                password = generatePassword(password, 6);
            }
            passwords[0][i] = password;

            for (int j = 1; j < 2000; j++) {
                passwords[1][i] = md5(passwords[0][i]);
                passwords[1][i] = reduction(passwords[1][i], i);

            }
        }


        //    for (int i = 0; i < PASSWORD_COUNT; i++)
//            System.out.println(passwords[0][i] + " " + passwords[1][i]);

        String p = reduction(HASH, 1999);

        for()
        for (int j = 0 ;j < passwords[0].length; j++) {
            if(passwords[1][j] == p){}
                //found hash



            }
        }

        /*for (int i = 0; i < passwords[0].length; i++) {
            String hashedPW = md5(password);
            String pw;
            if (hashedPW.equals(HASH)) {
                pw = password;

                break;
            }
            password = reduction(hashedPW, i);
        }*/

        System.out.println(searchForPW(HASH));

    }

    private static String searchForPW(String pw) {
        String password = reduction(pw, 1);
        for (int i = 0; i < PASSWORD_COUNT; i++) {
            if (Arrays.asList(passwords[1]).contains(password)) {
                isInIf = true;
                counter = i;
                return password;

            } else {
                isInIf = false;
                password = algoHash1Step(password);
            }

        }
        return password;
    }
//findet nicht
/*    private static String searchForPW(String pw){
        String password = reduction(pw,1);
        while (!isInIf && counter < PASSWORD_COUNT) {
            if (passwords[1][passwords[1].length - 1].equals(password)) {
                isInIf = true;

                password = passwords[1][passwords[1].length-1];
            } else {
                isInIf = false;
                counter++;
                password = algoHash1Step(password);
            }

        }*/

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

    public static String algoHash1Step(String pw) {
        for (int i = 0; i < 1; i++) {
            String password = pw;
            for (int j = 0; j < i; j++) {
                password = generatePassword(password, 6);
            }
            passwords[0][i] = password;


            passwords[1][i] = md5(passwords[0][i]);
            passwords[1][i] = reduction(passwords[1][i], i);


        }
        return passwords[1][0];
    }
}
