package com.eurodyn.qlack.fuse.crypto;

import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Format conversion services for security elements.
 */
@Service
@Validated
public class CryptoConversionService {
  private final static String RSA_PUBLIC_KEY = "RSA PUBLIC KEY";
  private final static String RSA_PRIVATE_KEY = "RSA PRIVATE KEY";
  private final static String CERTIFICATE = "CERTIFICATE";
  private final static String BC = "BC";
  private final static String RSA = "RSA";
  private final static String PEM_BEGIN = "-----BEGIN.*-----";
  private final static String PEM_END = "-----END.*-----";

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

  private String convertKeyToPEM(KeyPair keyPair, String keyType) throws IOException {
    try (StringWriter pemStrWriter = new StringWriter()) {
      try (JcaPEMWriter pemWriter = new JcaPEMWriter(pemStrWriter)) {
        pemWriter.writeObject(new PemObject(keyType, keyPair.getPrivate().getEncoded()));
        pemWriter.flush();
        return pemStrWriter.toString();
      }
    }
  }

  /**
   * Converts a public key to string in PEM format.
   *
   * @param keyPair The keypair containing the public key to convert.
   */
  public String publicKeyToPEM(KeyPair keyPair) throws IOException {
    return convertKeyToPEM(keyPair, RSA_PUBLIC_KEY);
  }

  /**
   * Converts a private key to string in PEM format.
   *
   * @param keyPair The keypair containing the private key to convert.
   */
  public String privateKeyToPEM(KeyPair keyPair) throws IOException {
    return convertKeyToPEM(keyPair, RSA_PRIVATE_KEY);
  }

  public String certificateToPEM(X509CertificateHolder certificateHolder) throws IOException {
    try (StringWriter pemStrWriter = new StringWriter()) {
      try (PemWriter writer = new PemWriter(pemStrWriter)) {
        writer.writeObject(new PemObject(CERTIFICATE, certificateHolder.toASN1Structure().getEncoded()));
        writer.flush();
        return pemStrWriter.toString();
      }
    }
  }

  /**
   * Converts a text-based public key (in PEM format) to {@link PublicKey}.
   *
   * @param publicKey The public key in PEM format to convert.
   */
  private PublicKey pemToPublicKey(String publicKey)
      throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, IOException {
    return pemToPublicKey(publicKey, BC, RSA);
  }

  /**
   * Converts a text-based public key (in PEM format) to {@link PublicKey}.
   *
   * @param publicKey The public key in PEM format to convert.
   * @param provider The security provider with which this key was generated.
   * @param algorithm The security algorithm with which this key was generated.
   */
  private PublicKey pemToPublicKey(String publicKey, String provider, String algorithm)
      throws IOException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
    PublicKey key = null;

    // Cleanup the PEM from unwanted text.
    publicKey = publicKey.replaceAll(PEM_BEGIN, "")
        .replaceAll(PEM_END, "")
        .replaceAll("[\n\r]", "")
        .trim();

    // Read the cleaned up PEM and generate the public key.
    byte[] encoded = Base64.getDecoder().decode(publicKey);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
    KeyFactory factory = KeyFactory.getInstance(algorithm, provider);
    key = factory.generatePublic(keySpec);

    return key;
  }

  /**
   * Converts a text-based private key (in PEM format) to {@link PrivateKey}.
   *
   * @param privateKey The private key in PEM format to convert.
   * @param provider The security provider with which this key was generated.
   * @param algorithm The security algorithm with which this key was generated.
   */
  public PrivateKey pemToPrivateKey(String privateKey, String provider, String algorithm)
      throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
    PrivateKey key = null;

    // Cleanup the PEM from unwanted text.
    privateKey = privateKey.replaceAll(PEM_BEGIN, "")
        .replaceAll(PEM_END, "")
        .replaceAll("[\n\r]", "")
        .trim();

    // Read the cleaned up PEM and generate the public key.
    byte[] encoded = Base64.getDecoder().decode(privateKey);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
    KeyFactory factory = KeyFactory.getInstance(algorithm, provider);
    key = factory.generatePrivate(keySpec);

    return key;
  }
}
