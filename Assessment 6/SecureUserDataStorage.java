import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

public class SecureUserDataStorage {
    private static final String FILE_NAME = "users.dat", SECRET_KEY_FILE = "secret.key";

    public static void main(String[] args) throws Exception {
        SecretKey key = loadOrCreateKey();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            switch (sc.nextInt()) {
                case 1 -> register(sc, key);
                case 2 -> login(sc, key);
                case 3 -> System.exit(0);
            }
        }
    }

    private static SecretKey loadOrCreateKey() throws Exception {
        File f = new File(SECRET_KEY_FILE);
        if (f.exists()) return new SecretKeySpec(new FileInputStream(f).readAllBytes(), "AES");
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128, new SecureRandom());
        SecretKey key = kg.generateKey();
        try (FileOutputStream fos = new FileOutputStream(f)) { fos.write(key.getEncoded()); }
        return key;
    }

    private static void register(Scanner sc, SecretKey key) throws Exception {
        System.out.print("Username: ");
        String user = sc.next();
        System.out.print("Password: ");
        String pass = sc.next();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(encrypt(user + ":" + pass, key));
            bw.newLine();
        }
        System.out.println("Registration successful!");
    }

    private static void login(Scanner sc, SecretKey key) throws Exception {
        System.out.print("Username: ");
        String user = sc.next();
        System.out.print("Password: ");
        String pass = sc.next();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            for (String line; (line = br.readLine()) != null; ) {
                String[] creds = decrypt(line, key).split(":");
                if (creds[0].equals(user) && creds[1].equals(pass)) {
                    System.out.println("Login successful!");
                    return;
                }
            }
        }
        System.out.println("Invalid credentials.");
    }

    private static String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }

    private static String decrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
    }
}
