package ch.fhnw;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) {

    }

    private static String reduction(String hash, int step) {
        StringBuilder result = new StringBuilder("0000000");
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
            result.setCharAt(i, value);
            H = H.divide(Z_length);
        }

        System.out.println("Reduction: " + result.toString());
        return result.toString();
    }

    public static String md5 (String hash){
        String hashtext = "";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(hash.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            System.out.println(hashtext);
            return hashtext;

        }catch (NoSuchAlgorithmException e){
            System.out.println(e);
        }
        return hashtext;
    }
}
