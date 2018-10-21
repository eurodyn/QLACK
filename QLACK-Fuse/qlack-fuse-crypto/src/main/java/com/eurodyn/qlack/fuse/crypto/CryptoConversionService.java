package com.eurodyn.qlack.fuse.crypto;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.security.Key;
import java.util.Base64;

/**
 * Format conversion services for security elements.
 */
@Service
@Validated
public class CryptoConversionService {

  /**
   * Converts a {@link Key} to its primary encoding format as Base64 string.
   *
   * @param key The key to convert.
   */
  public String keyToString(Key key) {
    return Base64.getEncoder().encodeToString(key.getEncoded());
  }

  /**
   * Converts a key in Base64 format to a {@link Key}.
   *
   * @param key The key to convert.
   * @param keyAlgorithm The algorithm with which this key was created.
   */
  public Key stringToKey(String key, String keyAlgorithm) {
    byte[] decodedKey = Base64.getDecoder().decode(key);

    return new SecretKeySpec(decodedKey, 0, decodedKey.length, keyAlgorithm);
  }
}
