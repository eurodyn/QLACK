package com.eurodyn.qlack.fuse.search;

import com.eurodyn.qlack.fuse.search.exception.QFESSearchException;
import com.eurodyn.qlack.fuse.search.mappers.QFESCreateIndexRequestMapper;
import com.eurodyn.qlack.fuse.search.request.QFESCreateIndexRequest;
import com.eurodyn.qlack.fuse.search.request.QFESUpdateMappingRequest;
import com.eurodyn.qlack.fuse.search.util.QFESESClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Validated
public class QFESAdminService {

  // Logger reference.
  private static final Logger LOGGER = Logger.getLogger(QFESAdminService.class.getName());

  // Service references.
  private QFESESClient esClient;
  private QFESCreateIndexRequestMapper qfesCreateIndexRequestMapper;

  @Autowired
  public QFESAdminService(QFESESClient esClient,
      QFESCreateIndexRequestMapper qfesCreateIndexRequestMapper) {
    this.esClient = esClient;
    this.qfesCreateIndexRequestMapper = qfesCreateIndexRequestMapper;
  }

  public boolean createIndex(QFESCreateIndexRequest createIndexRequest) {
    boolean retVal = false;
    /** If the index already exists return without doing anything. */
    if (indexExists(createIndexRequest.getName())) {
      LOGGER.log(Level.WARNING, "Index already exists: {0}.", createIndexRequest.getName());
    } else {
      /**
       * If an indexMapping is provided create the index using this
       * mapping, otherwise create the index with no specific mapping (ES
       * will automatically map fields according to the underlying data
       * types, see 'Field datatypes' on
       * https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping.html).
       */
      try {
        esClient.getClient().performRequest("PUT", createIndexRequest.getName(), new HashMap<>(),
            qfesCreateIndexRequestMapper.mapToNStringEntity(createIndexRequest));

        retVal = true;
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE,
            MessageFormat.format("Could not create index {0}.", createIndexRequest.getName()), e);
        throw new QFESSearchException(
            MessageFormat.format("Could not create index {0}.", createIndexRequest.getName()), e);
      }
    }

    return retVal;
  }

  public boolean deleteIndex(String indexName) {
    // If the index does not exist return without doing anything.
    if (!indexExists(indexName)) {
      LOGGER.log(Level.WARNING, "Index does not exist: {0}.", indexName);
      return false;
    }

    try {
      // Delete the index.
      esClient.getClient().performRequest("DELETE", indexName);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, MessageFormat.format("Could not delete index {0}.", indexName), e);
      throw new QFESSearchException(MessageFormat.format("Could not delete index {0}.", indexName),
          e);
    }

    return true;
  }

  public boolean indexExists(String indexName) {
    Response response;
    try {
      response = esClient.getClient().performRequest("HEAD", indexName);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not check if index exists.", e);
      throw new QFESSearchException("Could not check if index exists.", e);
    }

    return response != null ? (response.getStatusLine().getStatusCode() == 200) : false;
  }

  public boolean updateTypeMapping(QFESUpdateMappingRequest request) {
    // If the index does not exist return without doing anything.
    if (!indexExists(request.getIndexName())) {
      LOGGER.log(Level.WARNING, "Index does not exist: {0}.", request.getIndexName());
      return false;
    }

    try {
      String endpoint = request.getIndexName() + "/_mapping/" + request.getTypeName();

      esClient.getClient().performRequest("PUT", endpoint, new HashMap<>(),
          qfesCreateIndexRequestMapper.mapToNStringEntity(request));
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could update index mapping.", e);
      throw new QFESSearchException("Could update index mapping.", e);
    }

    return true;
  }

  public boolean closeIndex(String indexName) {
    // If the index does not exist return without doing anything.
    if (!indexExists(indexName)) {
      LOGGER.log(Level.WARNING, "Index does not exist: {0}.", indexName);
      return false;
    }

    String endpoint = indexName + "/_close";
    try {
      esClient.getClient().performRequest("POST", endpoint);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not close index.", e);
      throw new QFESSearchException("Could not close index.");
    }
    return true;
  }

  public boolean openIndex(String indexName) {
    // If the index does not exist return without doing anything.
    if (!indexExists(indexName)) {
      LOGGER.log(Level.WARNING, "Index does not exist: {0}.", indexName);
      return false;
    }

    String endpoint = indexName + "/_open";
    try {
      esClient.getClient().performRequest("POST", endpoint);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not open index.", e);
      throw new QFESSearchException("Could not open index.", e);
    }
    return true;
  }

  public boolean updateIndexSettings(String indexName, Map<String, String> settings,
      boolean preserveExisting) {
    // If the index does not exist return without doing anything.
    if (!indexExists(indexName)) {
      LOGGER.log(Level.WARNING, "Index does not exist: {0}.", indexName);
      return false;
    }

    String endpoint = indexName + "/_settings";
    if (preserveExisting) {
      endpoint += "?preserve_existing=true";
    }

    try {
      closeIndex(indexName);

      esClient.getClient().performRequest("PUT", endpoint, new HashMap<>(),
          new NStringEntity(new ObjectMapper().writeValueAsString(settings)));

      openIndex(indexName);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not change index settings.", e);
      throw new QFESSearchException("Could not change index settings.", e);
    }
    return true;
  }

  public boolean checkIsUp() {
    try {
      Response response = esClient.getClient().performRequest("GET", "_cluster/health");
      return response.getStatusLine().getStatusCode() == 200;
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not check cluster health", e);
      return false;
    }
  }
}
