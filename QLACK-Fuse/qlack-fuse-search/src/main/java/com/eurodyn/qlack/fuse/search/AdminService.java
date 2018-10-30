package com.eurodyn.qlack.fuse.search;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.mappers.CreateIndexRequestMapper;
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.request.UpdateMappingRequest;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Validated
public class AdminService {

  // Logger reference.
  private static final Logger LOGGER = Logger.getLogger(AdminService.class.getName());

  // Service references.
  private ESClient esClient;
  private CreateIndexRequestMapper createIndexRequestMapper;

  @Autowired
  public AdminService(ESClient esClient,
      CreateIndexRequestMapper createIndexRequestMapper) {
    this.esClient = esClient;
    this.createIndexRequestMapper = createIndexRequestMapper;
  }

  public boolean createIndex(CreateIndexRequest createIndexRequest) {
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
        
        // Execute indexing request.
        
        esClient.getRestClient().performRequest("PUT", createIndexRequest.getName(), new HashMap<>(),
            createIndexRequestMapper.mapToNStringEntity(createIndexRequest));
        
        retVal = true;
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE,
            MessageFormat.format("Could not create index {0}.", createIndexRequest.getName()), e);
        throw new SearchException(
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
      DeleteRequest deleteRequest = new DeleteRequest(indexName);
      esClient.getClient().delete(deleteRequest);
      
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, MessageFormat.format("Could not delete index {0}.", indexName), e);
      throw new SearchException(MessageFormat.format("Could not delete index {0}.", indexName),
          e);
    }

    return true;
  }

  public boolean indexExists(String indexName) {
    Response response;
    try {
      response = esClient.getRestClient().performRequest("HEAD", indexName);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not check if index exists.", e);
      throw new SearchException("Could not check if index exists.", e);
    }

    return response != null ? (response.getStatusLine().getStatusCode() == 200) : false;
  }

  public boolean updateTypeMapping(UpdateMappingRequest request) {
    // If the index does not exist return without doing anything.
    if (!indexExists(request.getIndexName())) {
      LOGGER.log(Level.WARNING, "Index does not exist: {0}.", request.getIndexName());
      return false;
    }

    try {
      String endpoint = request.getIndexName() + "/_mapping/" + request.getTypeName();

      esClient.getRestClient().performRequest("PUT", endpoint, new HashMap<>(),
          createIndexRequestMapper.mapToNStringEntity(request));
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could update index mapping.", e);
      throw new SearchException("Could update index mapping.", e);
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
      esClient.getRestClient().performRequest("POST", endpoint);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not close index.", e);
      throw new SearchException("Could not close index.");
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
      esClient.getRestClient().performRequest("POST", endpoint);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not open index.", e);
      throw new SearchException("Could not open index.", e);
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

      esClient.getRestClient().performRequest("PUT", endpoint, new HashMap<>(),
          new NStringEntity(new ObjectMapper().writeValueAsString(settings)));

      openIndex(indexName);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not change index settings.", e);
      throw new SearchException("Could not change index settings.", e);
    }
    return true;
  }

  public boolean checkIsUp() {
    try {
      Response response = esClient.getRestClient().performRequest("GET", "_cluster/health");
      return response.getStatusLine().getStatusCode() == 200;
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not check cluster health", e);
      return false;
    }
  }
}
