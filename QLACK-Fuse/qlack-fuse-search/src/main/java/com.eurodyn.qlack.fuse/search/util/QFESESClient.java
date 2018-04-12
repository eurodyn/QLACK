package com.eurodyn.qlack.fuse.search.util;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A client to communicate with ES. This client is using the {@link RestClient} implementation
 * of the ES Java client.
 *
 * //TODO support credentials as well as SSL: https://qbox.io/blog/rest-calls-new-java-elasticsearch-client-tutorial
 */
@Service
@Validated
public class QFESESClient {

  // Logger.
  private static final Logger LOGGER = Logger.getLogger(QFESESClient.class.getName());

  // Service references.
  private RestClient client;
  private QFESProperties qfesProperties;

  @Autowired
  public QFESESClient(QFESProperties qfesProperties) {
    this.qfesProperties = qfesProperties;
  }

  @PostConstruct
  public void init() {
    LOGGER.log(Level.CONFIG, "Initialising connection to ES: {0}", qfesProperties.getEsHosts());

    /** Process Http hosts for ES */
    final HttpHost[] httpHosts = Arrays.stream(qfesProperties.getEsHosts().split(",")).map(host ->
        new HttpHost(host.split(":")[1], Integer.parseInt(host.split(":")[2]), host.split(":")[0])
    ).collect(Collectors.toList())
        .toArray(new HttpHost[qfesProperties.getEsHosts().split(",").length]);

    client = RestClient
        .builder(httpHosts)
        .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {

          @Override
          public HttpAsyncClientBuilder customizeHttpClient(
              HttpAsyncClientBuilder httpClientBuilder) {
            if (StringUtils.isNotEmpty(qfesProperties.getEsUsername()) && StringUtils
                .isNotEmpty(qfesProperties.getEsPassword())) {
              final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
              credentialsProvider.setCredentials(AuthScope.ANY,
                  new UsernamePasswordCredentials(qfesProperties.getEsUsername(),
                      qfesProperties.getEsPassword()));

              httpClientBuilder = httpClientBuilder
                  .setDefaultCredentialsProvider(credentialsProvider);
            }

            if ("false".equals(qfesProperties.isVerifyHostname())) {
              httpClientBuilder = httpClientBuilder.setSSLHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                  return true;
                }
              });
            }

            return httpClientBuilder;
          }
        })
        .build();
  }

  /**
   * Default shutdown hook.
   *
   * @throws IOException If client can not be closed.
   */
  public void shutdown() throws IOException {
    LOGGER.log(Level.CONFIG, "Shutting down connection to ES.");
    client.close();
  }

  /**
   * Returns the client.
   *
   * @return A {@link RestClient} instance.
   */
  public RestClient getClient() {
    return client;
  }
}
