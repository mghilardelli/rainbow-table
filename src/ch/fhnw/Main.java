package ch.fhnw;

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