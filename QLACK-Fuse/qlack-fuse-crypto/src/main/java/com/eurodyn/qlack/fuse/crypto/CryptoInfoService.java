package com.eurodyn.qlack.fuse.crypto;

import com.eurodyn.qlack.fuse.crypto.dto.SecurityProvider;
import com.eurodyn.qlack.fuse.crypto.dto.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.security.Security;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A provider of information regarding the security algorithms and services available in the runtime.
 */
@Service
@Validated
public class CryptoInfoService {

  /**
   * Returns the security providers available in the runtime.
   */
  public List<SecurityProvider> getSecurityProviders() {
    return Arrays.stream(Security.getProviders())
        .flatMap(o -> Arrays.asList(new SecurityProvider(o.getName(), o.getVersion(), o.getInfo()))
            .stream()
        ).collect(Collectors.toList());
  }

  /**
   * Returns the security services provided by a specific provider available in the runtime.
   * @param providerName The provider to inquiry for available services.
   */
  public List<SecurityService> getSecurityServices(String providerName) {
    return getSecurityProviders().stream()
        .filter(o -> o.getName().equals(providerName))
        .flatMap(o -> Arrays.asList(Security.getProvider(o.getName()).getServices()).stream())
        .flatMap(o -> o.stream().flatMap(
            x -> Arrays.asList(new SecurityService(providerName, x.getAlgorithm(), x.getType()))
                .stream()))
        .collect(Collectors.toList());
  }

  /**
   * Returns the security services providing a specific security algorithm.
   * @param algorithmType The algorithm type to find services providing it.
   */
  public List<SecurityService> getSecurityServicesForAlgorithmType(String algorithmType) {
    return getSecurityProviders().stream()
        .flatMap(o -> Arrays.asList(o.getName()).stream())
        .flatMap(o -> getSecurityServices(o).stream())
        .filter(o -> o.getType().equals(algorithmType))
        .collect(Collectors.toList());
  }

  /**
   * Returns all available security algorithms available in the runtime.
   */
  public List<String> getAlgorithmTypes() {
    return getSecurityProviders().stream()
        .flatMap(o -> getSecurityServices(o.getName()).stream())
        .flatMap(o -> Arrays.asList(o.getType()).stream())
        .distinct()
        .sorted()
        .collect(Collectors.toList());
  }

}
