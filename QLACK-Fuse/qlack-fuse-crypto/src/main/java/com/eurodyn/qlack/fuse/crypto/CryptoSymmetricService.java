package com.eurodyn.qlack.fuse.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Symmetric encryption/decryption utility methods.
 */
@Service
@Validated
public class CryptoSymmetricService {

  /**
   * Trim an IV to 128 bits.
   * @param iv The IV to trim.
   */
  private byte[] trimIV(byte[] iv) {
    return iv.length > 16 ? ArrayUtils.subarray(iv, 0, 16) : iv;
  }

  /**
   * Generates a symmetric key.
   *
   * @param keyLength The length of the key.
   * @param algorithm The algorithm to use, e.g. AES.
   */
  public byte[] generateKey(final int keyLength, final String algorithm)
  throws NoSuchAlgorithmException {
    final KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
    keyGen.init(keyLength, SecureRandom.getInstanceStrong());

    return keyGen.generateKey().getEncoded();
  }

  /**
   * Generates a {@link SecretKey} from a Base64 encoded symmetric key.
   *
   * @param key The Base64 encoded version of the key.
   * @param algorithm The algorithm to use, e.g. AES.
   */
  public SecretKey keyFromString(final String key, final String algorithm) {
    return new SecretKeySpec(Base64.decodeBase64(key), algorithm);
  }

  /**
   * Generates a random IV of 16 bytes.
   */
  public byte[] generateIV() {
    final byte[] iv = new byte[16];
    new SecureRandom().nextBytes(iv);

    return iv;
  }

  /**
   * Generates a random IV of specific length.
   *
   * @param length The length of the IV.
   */
  public byte[] generateIV(int length) {
    final byte[] iv = new byte[length];
    new SecureRandom().nextBytes(iv);

    return iv;
  }


  /**
   * Generates the original IV from a Base64 encoded IV.
   *
   * @param iv The IV to decode.
   */
  public byte[] ivFromString(String iv) {
    return Base64.decodeBase64(iv);
  }

  /**
   * Encrypts a plaintext with a given IV.
   *
   * @param plaintext The plaintext to encrypt.
   * @param key The key to use for encryption.
   * @param iv The encryption IV.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   * @param prefixIv Whether to prefix the IV on the return value or not.
   *
   * @return The ciphertext optionally prefixed with the IV.
   */
  public byte[] encrypt(final byte[] plaintext, final SecretKey key, byte[] iv,
    final String cipherInstance, final String keyAlgorithm, final boolean prefixIv)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    final Cipher cipher = Cipher.getInstance(cipherInstance);
    final SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), keyAlgorithm);
    iv = trimIV(iv);
    final IvParameterSpec ivSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

    final byte[] ciphertext = cipher.doFinal(plaintext);

    if (prefixIv) {
      return ArrayUtils.addAll(iv, ciphertext);
    } else {
      return ciphertext;
    }
  }

  /**
   * Encrypts a plaintext with an internally generated IV.
   *
   * @param plaintext The plaintext to encrypt.
   * @param key The key to use for encryption.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   *
   * @return The ciphertext prefixed with the IV.
   */
  public byte[] encrypt(final byte[] plaintext, final SecretKey key, final String cipherInstance,
    final String keyAlgorithm)
  throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException,
         IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
    return encrypt(plaintext,  key, generateIV(), cipherInstance, keyAlgorithm, true);
  }

  /**
   * Decrypts an encrypted message prefixed with a 16 bytes IV.
   *
   * @param ciphertext The encrypted message to decrypt.
   * @param key The key to decrypt with.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   */
  public byte[] decrypt(final byte[] ciphertext, final SecretKey key,
    final String cipherInstance, final String keyAlgorithm)
  throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException,
         IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
    return decrypt(ArrayUtils.subarray(ciphertext, 16, ciphertext.length), key,
      ArrayUtils.subarray(ciphertext, 0, 16), cipherInstance, keyAlgorithm);
  }

  /**
   * Decrypts an encrypted message.
   *
   * @param ciphertext The encrypted message to decrypt.
   * @param key The key to decrypt with.
   * @param cipherInstance The cipher instance to use, e.g. "AES/CBC/PKCS5Padding".
   * @param keyAlgorithm The algorithm for the secret key, e.g. "AES".
   * @param iv The IV to use.
   */
  public byte[] decrypt(final byte[] ciphertext, final SecretKey key, byte[] iv,
    final String cipherInstance, final String keyAlgorithm)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
         InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    final Cipher cipher = Cipher.getInstance(cipherInstance);
    final SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), keyAlgorithm);
    iv = trimIV(iv);
    final IvParameterSpec ivSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

    return cipher.doFinal(ciphertext);
  }
}
