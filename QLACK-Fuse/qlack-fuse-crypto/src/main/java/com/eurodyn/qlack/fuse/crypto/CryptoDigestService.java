package com.eurodyn.qlack.fuse.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Message digest algorithms.
 */
@Service
@Validated
public class CryptoDigestService {

  /**
   * HMAC with SHA256.
   * @param secret
   * @param message
   * @return
   * @throws NoSuchAlgorithmException
   * @throws InvalidKeyException
   */
  public String hmacSha256(String secret, String message)
      throws NoSuchAlgorithmException, InvalidKeyException {
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    sha256_HMAC.init(secret_key);

    return Hex.encodeHexString(sha256_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8)));
  }

  /**
   * Calculates the MD5 of the given string,
   * @param message
   * @return
   */
  public String md5(String message) {
    return DigestUtils.md5Hex(message);
  }

}
