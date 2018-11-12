package com.eurodyn.qlack.fuse.crypto;

import com.eurodyn.qlack.fuse.crypto.dto.CPPPemHolderDTO;
import com.eurodyn.qlack.fuse.crypto.dto.CreateCADTO;
import com.eurodyn.qlack.fuse.crypto.dto.SignDTO;
import com.eurodyn.qlack.fuse.crypto.dto.SignDTO.SignDTOBuilder;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

/**
 * Certificate Authority management.
 */
@Service
@Validated
public class CryptoCAService {

  private final CryptoKeyService cryptoKeyService;
  private final CryptoSignService cryptoSignService;
  private final CryptoConversionService cryptoConversionService;

  public CryptoCAService(CryptoKeyService cryptoKeyService,
      CryptoSignService cryptoSignService,
      CryptoConversionService cryptoConversionService) {
    this.cryptoKeyService = cryptoKeyService;
    this.cryptoSignService = cryptoSignService;
    this.cryptoConversionService = cryptoConversionService;
  }

  /**
   * Create a new Certificate Authority. This method also supports creating a sub-CA by providing the issuer's
   * information.
   *
   * @param createCADTO The details of the CA to be created.
   */
  public CPPPemHolderDTO createCA(CreateCADTO createCADTO)
      throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, OperatorCreationException, IOException {
    // Create a keypair for this CA.
    KeyPair keyPair = cryptoKeyService.createKeyPair(createCADTO.getCreateKeyPairRequestDTO());

    // Get hold of the public key.
    byte[] publicKey = keyPair.getPublic().getEncoded();
    SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey);

    // Prepare signing.
    final SignDTOBuilder signDTOBuilder = SignDTO.builder()
        .validForm(createCADTO.getValidFrom())
        .validto(createCADTO.getValidTo())
        .locale(createCADTO.getLocale())
        .publicKey(keyPair.getPublic())
        .signatureAlgorithm(createCADTO.getSignatureAlgorithm())
        .signatureProvider(createCADTO.getSignatureProvider())
        .subjectCN(createCADTO.getSubjectCN());

    // Choose which private key to use. If no parent key is found then this is a self-signed certificate and the
    // private key created for the keypair will be used.
    if (StringUtils.isNotEmpty(createCADTO.getIssuerCN()) && StringUtils
        .isNotEmpty(createCADTO.getIssuerPrivateKey())) {
      signDTOBuilder
          .privateKey(cryptoConversionService.pemToPrivateKey(
              createCADTO.getIssuerPrivateKey(),
              createCADTO.getIssuerPrivateKeyProvider(),
              createCADTO.getIssuerPrivateKeyAlgorithm()))
          .issuerCN(createCADTO.getIssuerCN());
    } else {
      signDTOBuilder
          .privateKey(keyPair.getPrivate())
          .issuerCN(createCADTO.getSubjectCN());
    }

    X509CertificateHolder certHolder = cryptoSignService.signKey(signDTOBuilder.build());

    // Prepare reply.
    CPPPemHolderDTO cppPemKey = new CPPPemHolderDTO();
    cppPemKey.setPublicKey(cryptoConversionService.publicKeyToPEM(keyPair));
    cppPemKey.setPrivateKey(cryptoConversionService.privateKeyToPEM(keyPair));
    cppPemKey.setCertificate(cryptoConversionService.certificateToPEM(certHolder));

    return cppPemKey;
  }
}