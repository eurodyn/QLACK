package com.eurodyn.qlack.fuse.crypto;

import static com.eurodyn.qlack.fuse.crypto.CryptoConstants.CN;

import com.eurodyn.qlack.fuse.crypto.dto.SignDTO;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;
import java.util.Date;

/**
 * Signing services.
 */
@Service
@Validated
public class CryptoSignService {

  /**
   * Signs a key, providing a certificate, with another key.
   * @param signDTO The details of the signing to take place.
   */
  public X509CertificateHolder signKey(SignDTO signDTO) throws OperatorCreationException {

    // Create a generator for the certificate including all certificate details.
    X509v1CertificateBuilder certGenerator = new X509v1CertificateBuilder(
        new X500Name(CN + "=" + StringUtils.defaultIfBlank(signDTO.getIssuerCN(), signDTO.getSubjectCN())),
        BigInteger.ONE,
        new Date(signDTO.getValidForm().toEpochMilli()),
        new Date(signDTO.getValidto().toEpochMilli()),
        signDTO.getLocale(),
        new X500Name(CN + "=" + signDTO.getSubjectCN()),
        SubjectPublicKeyInfo.getInstance(signDTO.getPublicKey().getEncoded())
    );

    // Generate the certificate.
    X509CertificateHolder certHolder;
    if (StringUtils.isNotEmpty(signDTO.getIssuerCN()) && signDTO.getPrivateKey() != null) {
      certHolder = certGenerator.build(
          new JcaContentSignerBuilder(signDTO.getSignatureAlgorithm())
              .setProvider(signDTO.getSignatureProvider())
              .build(signDTO.getPrivateKey()));
    } else {
      certHolder = certGenerator.build(
          new JcaContentSignerBuilder(signDTO.getSignatureAlgorithm())
              .setProvider(signDTO.getSignatureProvider())
              .build(signDTO.getPrivateKey()));
    }

    return certHolder;
  }
}
