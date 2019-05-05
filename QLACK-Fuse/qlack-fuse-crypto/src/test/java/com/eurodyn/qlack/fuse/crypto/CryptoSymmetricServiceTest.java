package com.eurodyn.qlack.fuse.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptoSymmetricServiceTest {

  private CryptoSymmetricService cryptoSymmetricService = new CryptoSymmetricService();

  @Test
  public void generateKey() throws NoSuchAlgorithmException {
    final String key = Base64.encodeBase64String(cryptoSymmetricService.generateKey(128, "AES"));
    assertNotNull(key);
  }

  @Test
  public void keyFromString() throws NoSuchAlgorithmException {
    final String key = Base64.encodeBase64String(cryptoSymmetricService.generateKey(128, "AES"));
    final SecretKey secretKey = cryptoSymmetricService.keyFromString(key, "AES");
    assertNotNull(key);
    assertNotNull(secretKey);
    assertEquals(key, Base64.encodeBase64String(secretKey.getEncoded()));
  }

  @Test
  public void generateIV() {
    final byte[] iv = cryptoSymmetricService.generateIV();
    assertNotNull(iv);
    assertEquals(iv.length, 16);
  }

  @Test
  public void ivFromString() {
    final byte[] iv = cryptoSymmetricService.generateIV();
    String ivStr = Base64.encodeBase64String(iv);
    assertEquals(ivStr, Base64.encodeBase64String(cryptoSymmetricService.ivFromString(ivStr)));
  }

  @Test
  public void encryptDecrypt()
  throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
         BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
    String plaintext = "Hello world!";
    final String aes = Base64.encodeBase64String(cryptoSymmetricService.generateKey(128, "AES"));
    final SecretKey aesKey = cryptoSymmetricService.keyFromString(aes, "AES");
    final byte[] iv = cryptoSymmetricService.generateIV();

    // No IV-prefix test.
    byte[] ciphertext = cryptoSymmetricService
      .encrypt(plaintext.getBytes(StandardCharsets.UTF_8), aesKey, iv, "AES/CBC/PKCS5Padding",
        "AES", false);
    assertNotNull(ciphertext);
    byte[] plaintextDecrypted = cryptoSymmetricService
      .decrypt(ciphertext, aesKey, iv, "AES/CBC/PKCS5Padding", "AES");
    assertEquals(plaintext, new String(plaintextDecrypted, StandardCharsets.UTF_8));

    // IV-prefix test.
    ciphertext = cryptoSymmetricService
      .encrypt(plaintext.getBytes(StandardCharsets.UTF_8), aesKey, iv, "AES/CBC/PKCS5Padding",
        "AES", true);
    assertNotNull(ciphertext);
    plaintextDecrypted = cryptoSymmetricService
      .decrypt(ciphertext, aesKey, "AES/CBC/PKCS5Padding", "AES");
    assertEquals(plaintext, new String(plaintextDecrypted, StandardCharsets.UTF_8));
  }
}
