package com.senla.security.rsa;

import lombok.Getter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Getter
public class CardEncoder {

    private static PublicKey publicKey;
    private static PrivateKey privateKey;

    public static void generateKeyPair() {

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public static String encode(String card, PublicKey publicKey) {

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cardBytes = card.getBytes(StandardCharsets.UTF_8);
            byte[] encrypted = cipher.doFinal(cardBytes);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchAlgorithmException |
                 NoSuchPaddingException |
                 InvalidKeyException |
                 IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decode(String encryptedCard) {
        try {
            byte[] encrypted = Base64.getDecoder().decode(encryptedCard);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (NoSuchPaddingException |
                 InvalidKeyException |
                 BadPaddingException |
                 NoSuchAlgorithmException |
                 IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }
}
