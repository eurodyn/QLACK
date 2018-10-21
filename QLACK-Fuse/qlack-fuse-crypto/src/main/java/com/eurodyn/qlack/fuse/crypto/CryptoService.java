package com.eurodyn.qlack.fuse.crypto;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Encrypt/Decrypt utility methods.
 */
@Service
@Validated
public class CryptoService {

  /**
   * Encrypt a message using a specific security algorithm and key.
   *
   * @param algorithm The security algorithm to use for encryption. The first security provider providing this algorithm
   * will be chosen automatically.
   * @param key The key to use for encryption.
   * @param message The message to encrypt.
   */
  public byte[] encrypt(String algorithm, Key key, byte[] message)
      throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
      BadPaddingException, NoSuchProviderException, InvalidKeyException {
    return encrypt(null, algorithm, key, message);
  }

  /**
   * Encrypt a message using a specific security algorithm and key.
   *
   * @param algorithm The security algorithm to use for encryption.
   * @param key The key to use for encryption.
   * @param message The message to encrypt.
   * @param provider The security provider to use for encryption.
   */
  public byte[] encrypt(String provider, String algorithm, Key key, byte[] message)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
      NoSuchProviderException, BadPaddingException, IllegalBlockSizeException {
    Cipher cipher;
    if (StringUtils.isBlank(provider)) {
      cipher = Cipher.getInstance(algorithm);
    } else {
      cipher = Cipher.getInstance(algorithm, provider);
    }
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return cipher.doFinal(message);
  }

  public byte[] decrypt(String algorithm, Key key, byte[] encryptedMessage)
      throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
      BadPaddingException, NoSuchProviderException, InvalidKeyException {
    return decrypt(null, algorithm, key, encryptedMessage);
  }

  /**
   * Decrypts a encrypted messages.
   * @param provider The security provider with which the message was encrypted.
   * @param algorithm The security algorithm with which the message was encrypted.
   * @param key The key to be used during decryption.
   * @param encryptedMessage The message to decrypt.
   */
  public byte[] decrypt(String provider, String algorithm, Key key, byte[] encryptedMessage)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
      NoSuchProviderException, BadPaddingException, IllegalBlockSizeException {
    Cipher cipher;
    if (StringUtils.isBlank(provider)) {
      cipher = Cipher.getInstance(algorithm);
    } else {
      cipher = Cipher.getInstance(algorithm, provider);
    }
    cipher.init(Cipher.DECRYPT_MODE, key);
    return cipher.doFinal(encryptedMessage);
  }

}