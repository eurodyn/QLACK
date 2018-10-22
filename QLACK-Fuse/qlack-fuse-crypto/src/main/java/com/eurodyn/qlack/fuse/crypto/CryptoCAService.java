package com.eurodyn.qlack.fuse.crypto;

import com.eurodyn.qlack.fuse.crypto.dto.CPPPemHolderDTO;
import com.eurodyn.qlack.fuse.crypto.dto.CreateCADTO;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

/**
 * Certificate Authority management.
 */
@Service
@Validated
public class CryptoCAService {

  private static final String CN = "CN";

  private final CryptoKeyService cryptoKeyService;
  private final CryptoConversionService cryptoConversionService;

  public CryptoCAService(CryptoKeyService cryptoKeyService,
      CryptoConversionService cryptoConversionService) {
    this.cryptoKeyService = cryptoKeyService;
    this.cryptoConversionService = cryptoConversionService;
  }

  /**
   * Create a new Certificate Authority. This method also supports creating a sub-CA by providing the issuer's information.
   * @param createCADTO The details of the CA to be created.
   */
  public CPPPemHolderDTO createCA(CreateCADTO createCADTO)
      throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, OperatorCreationException, IOException {
    // Create a keypair for this CA.
    KeyPair keyPair = cryptoKeyService.createKeyPair(createCADTO.getCreateKeyPairRequestDTO());

    // Get hold of the private key.
    byte[] publicKey = keyPair.getPublic().getEncoded();
    SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey);

    // Create a generator for the certificate including all certificate details.
    X509v1CertificateBuilder certGenerator = new X509v1CertificateBuilder(
        new X500Name(CN + "=" + createCADTO.getIssuerCN()), createCADTO.getSerial(),
        new Date(createCADTO.getValidFrom().toEpochMilli()),
        new Date(createCADTO.getValidTo().toEpochMilli()),
        createCADTO.getLocale(),
        new X500Name(CN + "=" + createCADTO.getSubjectCN()),
        publicKeyInfo
    );

    // Generate the certificate.
    X509CertificateHolder certHolder;
    if (StringUtils.isNotEmpty(createCADTO.getIssuerCN()) && StringUtils
        .isNotEmpty(createCADTO.getIssuerPrivateKey())) {
      certHolder = certGenerator.build(
          new JcaContentSignerBuilder(createCADTO.getSignatureAlgorithm())
              .setProvider(createCADTO.getSignatureProvider())
              .build(cryptoConversionService.pemToPrivateKey(
                  createCADTO.getIssuerPrivateKey(),
                  createCADTO.getSignatureProvider(),
                  createCADTO.getSignatureAlgorithm())));
    } else {
      certHolder = certGenerator.build(
          new JcaContentSignerBuilder(createCADTO.getSignatureAlgorithm())
              .build(keyPair.getPrivate()));
    }

    CPPPemHolderDTO cppPemKey = new CPPPemHolderDTO();
    cppPemKey.setPublicKey(cryptoConversionService.publicKeyToPEM(keyPair));
    cppPemKey.setPrivateKey(cryptoConversionService.privateKeyToPEM(keyPair));
    cppPemKey.setCertificate(cryptoConversionService.certificateToPEM(certHolder));

    return cppPemKey;
  }
}
