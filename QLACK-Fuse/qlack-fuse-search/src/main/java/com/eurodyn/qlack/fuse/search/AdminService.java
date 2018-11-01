package com.eurodyn.qlack.fuse.search;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.eurodyn.qlack.fuse.search.dto.ESDocumentIdentifierDTO;
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.request.UpdateMappingRequest;
import com.eurodyn.qlack.fuse.search.util.ESClient;

@Service
@Validated
public class AdminService {

  // Logger reference.
  private static final Logger LOGGER = Logger.getLogger(AdminService.class.getName());

  // Service references.
  private ESClient esClient;

  @Autowired
  public AdminService(ESClient esClient) {
    this.esClient = esClient;
  }

  public boolean createIndex(CreateIndexRequest createIndexRequest) {
    boolean retVal = false;
    /** If the index already exists return without doing anything. */
    if (indexExists(createIndexRequest.getName())) {
      LOGGER.log(Level.WARNING, "Index already exists: {0}.", createIndexRequest.getName());
    } else {

      /**
       * If an indexMapping is provided create the index using this mapping, otherwise create the
       * index with no specific mapping (ES will automatically map fields according to the
       * underlying data types, see 'Field datatypes' on
       * https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping.html).
       */

      Response response;
      try {

        Request request = new Request("PUT", createIndexRequest.getName());
        response = esClient.getClient().getLowLevelClient().performRequest(request);

        retVal = (response.getStatusLine().getStatusCode() == 200);
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

    Response response;

    try {
      // Delete the index.
      Request request = new Request("DELETE", indexName);

      response = esClient.getClient().getLowLevelClient().performRequest(request);

    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, MessageFormat.format("Could not delete index {0}.", indexName), e);
      throw new SearchException(MessageFormat.format("Could not delete index {0}.", indexName), e);
    }
    return (response.getStatusLine().getStatusCode() == 200);
  }

  public boolean indexExists(String indexName) {

    Response response;
    int statusCode;
    try {

      Request request = new Request("HEAD", indexName);
      response = esClient.getClient().getLowLevelClient().performRequest(request);

      // response
      statusCode = response.getStatusLine().getStatusCode();
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not check if index exists.", e);
      throw new SearchException("Could not check if index exists.", e);
    }
    return response != null ? (statusCode == 200) : false;
  }

  public boolean indexExists(ESDocumentIdentifierDTO dto) {
    boolean exists;
    try {

      GetRequest getRequest = new GetRequest(dto.getIndex(), dto.getType(), dto.getId());
      exists = esClient.getClient().exists(getRequest, RequestOptions.DEFAULT);

    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not check if index exists.", e);
      throw new SearchException("Could not check if index exists.", e);
    }
    return exists;
  }

  /**
   * @param updateRequest
   * @return
   */
  public boolean updateTypeMapping(UpdateMappingRequest updateRequest) {
    // If the index does not exist return without doing anything.
    if (!indexExists(updateRequest.getIndexName())) {
      LOGGER.log(Level.WARNING, "Index does not exist: {0}.", updateRequest.getIndexName());
      return false;
    }
    Response response;

    try {
      String endpoint = updateRequest.getIndexName() + "/_mapping/" + updateRequest.getTypeName();

      Request request = new Request("PUT", endpoint);
      request
          .setEntity(new NStringEntity(new JSONObject(updateRequest.getIndexMapping()).toString(),
              ContentType.APPLICATION_JSON));

      response = esClient.getClient().getLowLevelClient().performRequest(request);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could update index mapping.", e);
      throw new SearchException("Could update index mapping.", e);
    } catch (JSONException e) {
      LOGGER.log(Level.SEVERE, "Could update index mapping.", e);
      throw new SearchException("Could update index mapping.", e);
    }
    return response.getStatusLine().getStatusCode() == 200;
  }

  public boolean closeIndex(String indexName) {
    // If the index does not exist return without doing anything.
    if (!indexExists(indexName)) {
      LOGGER.log(Level.WARNING, "Index does not exist: {0}.", indexName);
      return false;
    }

    String endpoint = indexName + "/_close";
    Response response;

    try {
      Request request = new Request("POST", endpoint);
      response = esClient.getClient().getLowLevelClient().performRequest(request);

    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not close index.", e);
      throw new SearchException("Could not close index.");
    }
    return (response.getStatusLine().getStatusCode() == 200);
  }

  public boolean openIndex(String indexName) {
    // If the index does not exist return without doing anything.
    if (!indexExists(indexName)) {
      LOGGER.log(Level.WARNING, "Index does not exist: {0}.", indexName);
      return false;
    }
    Response response;
    String endpoint = indexName + "/_open";
    try {
      Request request = new Request("POST", endpoint);
      response = esClient.getClient().getLowLevelClient().performRequest(request);

    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not open index.", e);
      throw new SearchException("Could not open index.", e);
    }
    return (response.getStatusLine().getStatusCode() == 200);
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
    Response response;
    try {
      closeIndex(indexName);

      Request request = new Request("PUT", endpoint);
      request.setEntity(
          new NStringEntity(new JSONObject(settings).toString(), ContentType.APPLICATION_JSON));

      response = esClient.getClient().getLowLevelClient().performRequest(request);

      openIndex(indexName);

    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not change index settings.", e);
      throw new SearchException("Could not change index settings.", e);
    }
    return (response.getStatusLine().getStatusCode() == 200);
  }


  public boolean checkIsUp() {
    Response response;
    try {
      Request request = new Request("GET", "_cluster/health");
      response = esClient.getClient().getLowLevelClient().performRequest(request);

      return response.getStatusLine().getStatusCode() == 200;
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not check cluster health", e);
      return false;
    }
  }
}
