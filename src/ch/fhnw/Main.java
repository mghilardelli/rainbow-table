package ch.fhnw;

import com.sun.istack.internal.NotNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {

    static int PASSWORD_COUNT = 2000;

    static String[] passwords = new String[PASSWORD_COUNT];

    public static void main(String[] args) {
        for (int i = 0; i < 2000; i++) {
            String password = "0000000";
            for (int j = 0; j < i; j++) {
                password = generatePassword(password, 6);
            }
            passwords[i] = password;
        }


        for (String s : passwords)
            System.out.println(s);
    }

    private static String reduction(String hash, int step) {
        StringBuilder clear_text = new StringBuilder("0000000");
        BigInteger H = new BigInteger(hash, 16);
        H = H.add(BigInteger.valueOf(step));

        int L = 7;
        char[] Z = new char[36];
        BigInteger Z_length =  BigInteger.valueOf(Z.length);

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

        System.out.println("Reduction: " + clear_text.toString());
        return clear_text.toString();
    }

    public static String md5 (String input){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            String hash = new BigInteger(1, messageDigest).toString(16);
            return hash;
        }catch (NoSuchAlgorithmException e){
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
}