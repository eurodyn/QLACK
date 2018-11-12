package com.eurodyn.qlack.fuse.crypto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Symmetric encryption/decryption utility methods.
 */
@Service
@Validated
public class CryptoSymmetricService {
  // JUL reference.
  private static final Logger LOGGER = Logger.getLogger(CryptoSymmetricService.class.getName());

  /**
   * A convenience method to encrypt a message without providing an additional salt. Note that in this case the actual
   * password is also used as the salt, so at least make sure your password is a truly random.
   *
   * @param message The message to encrypt.
   * @param password The password/secret to use. Always kept secret.
   * @return Returns a hexed representation of the encrypted message.
   */
  public String encrypt(final String message, final String password) {
    return encrypt(message, password, password);
  }

  /**
   * Encrypts a message using 256-bit AES using PKCS #5's PBKDF2.
   *
   * @param message The message to encrypt.
   * @param password The password/secret to use. Always kept secret.
   * @param salt The salt to initialise the cipher with. Can be shared.
   * @return Returns a hexed representation of the encrypted message.
   */
  public String encrypt(final String message, final String password, String salt) {
    LOGGER.log(Level.FINEST, "Encrypting: {0}", message);
    if (StringUtils.isBlank(message)) {
      return message;
    }

    salt = new String(org.springframework.security.crypto.codec.Hex.encode(salt.getBytes(StandardCharsets.UTF_8)));
    final TextEncryptor textEncryptor = Encryptors.text(password, salt);

    return textEncryptor.encrypt(message);
  }

  /**
   * A convenience method to decrypt a message without providing an additional salt. Note that in this case the actual
   * password is also used as the salt, so at least make sure your password is a truly random.
   *
   * @param ciphertext The encrypted message as encrypted by `encrypt` method.
   * @param password The password/secret to use. Always kept secret.
   * @return Returns the decrypted version of the originally encrypted message.
   */
  public String decrypt(final String ciphertext, final String password) {
    return decrypt(ciphertext, password, password);
  }

  /**
   * Decrypts a message encrypted with the `encrypt` method.
   *
   * @param ciphertext The encrypted message.
   * @param password The password to decrypt with.
   * @param salt The salt used to initialise the cipher.
   * @return Returns the decrypted version of the originally encrypted message.
   */
  public String decrypt(final String ciphertext, final String password, String salt) {
    LOGGER.log(Level.FINEST, "Decrypting: {0}", ciphertext);
    if (StringUtils.isBlank(ciphertext)) {
      return ciphertext;
    }

    salt = new String(Hex.encode(salt.getBytes(StandardCharsets.UTF_8)));
    final TextEncryptor textEncryptor = Encryptors.text(password, salt);

    return textEncryptor.decrypt(ciphertext);
  }

}