package com.eurodyn.qlack.fuse.search.service;

import com.eurodyn.qlack.fuse.search.dto.ESDocumentIdentifierDTO;
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.request.UpdateMappingRequest;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import java.io.IOException;
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
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AdminService {

  // Logger reference.
  private static final Logger LOGGER = Logger.getLogger(AdminService.class.getName());

  // Service references.
  private ESClient esClient;

  @Autowired
  private ElasticsearchOperations elasticsearchOperations;

  @Autowired
  public AdminService(ESClient esClient, ElasticsearchOperations elasticsearchOperations) {
    this.esClient = esClient;
    this.elasticsearchOperations = elasticsearchOperations;
  }

  public boolean createIndex(CreateIndexRequest createIndexRequest) {

    /** If the index already exists return without doing anything. */
    if (indexExists(createIndexRequest.getName())) {
      LOGGER.log(Level.WARNING, "Index already exists: {0}.", createIndexRequest.getName());
      return false;
    }

    boolean createdIndex = elasticsearchOperations.createIndex(createIndexRequest.getName());
    return createIndexRequest.getIndexMapping() == null ? createdIndex :
        elasticsearchOperations
            .putMapping(createIndexRequest.getName(), createIndexRequest.getType(), createIndexRequest.getIndexMapping());

  }

  public boolean createIndex(Class clazz) {
    if (indexExists(clazz)) {
      LOGGER.log(Level.WARNING, "Index for class {0} already exists.", clazz.getName());
      return false;
    }
    return !isClassValid(clazz) || (elasticsearchOperations.createIndex(clazz) && elasticsearchOperations.putMapping(clazz));
  }

  public boolean deleteIndex(String indexName) {
    return !canPerformOperation(indexName) || elasticsearchOperations.deleteIndex(indexName);
  }

  public boolean deleteIndex(Class clazz) {
    if (!indexExists(clazz)) {
      LOGGER.log(Level.WARNING, "Index for class {0} does not exist.", clazz.getName());
      return false;
    }
    return !isClassValid(clazz) || elasticsearchOperations.deleteIndex(clazz);
  }

  public boolean indexExists(String indexName) {
    return elasticsearchOperations.indexExists(indexName);
  }

  public boolean indexExists(Class clazz) {
    return !isClassValid(clazz) || elasticsearchOperations.indexExists(clazz);
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

  public boolean updateTypeMapping(UpdateMappingRequest updateRequest) {
    // If the index does not exist return without doing anything.
    if (!indexExists(updateRequest.getIndexName())) {
      LOGGER.log(Level.WARNING, "Index does not exist: {0}.", updateRequest.getIndexName());
      return false;
    }

    return elasticsearchOperations.putMapping(updateRequest.getIndexName(), updateRequest.getTypeName(), updateRequest.getIndexMapping());
  }

  public boolean updateIndexSettings(String indexName, Map<String, String> settings, boolean preserveExisting) {
    if (!canPerformOperation(indexName)) {
      return false;
    }

    String endpoint = indexName + "/_settings";
    if (preserveExisting) {
      endpoint += "?preserve_existing=true";
    }
    closeIndex(indexName);
    NStringEntity entity = new NStringEntity(new JSONObject(settings).toString(), ContentType.APPLICATION_JSON);
    boolean changedIndexSettings = commonIndexingOperationWithEntity("PUT", endpoint, "Could not change index settings.", entity);
    openIndex(indexName);
    return changedIndexSettings;
  }

  public boolean closeIndex(String indexName) {
    String endpoint = indexName + "/_close";
    return !canPerformOperation(indexName) || commonIndexingOperation("POST", endpoint, "Could not close index.");
  }

  public boolean openIndex(String indexName) {
    String endpoint = indexName + "/_open";
    return !canPerformOperation(indexName) || commonIndexingOperation("POST", endpoint, "Could not open index.");
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

  private boolean isClassValid(Class clazz) {
    if (!clazz.isAnnotationPresent(Document.class)) {
      LOGGER.log(Level.SEVERE, "Unable to identify index name. " + clazz.getSimpleName() +
          " is not a Document. Make sure the document class is annotated with @Document(indexName=\"foo\")");
      return false;
    }
    return true;
  }

  private boolean canPerformOperation(String indexName) {
    if (!indexExists(indexName)) {
      LOGGER.log(Level.WARNING, "Index does not exist: {0}.", indexName);
      return false;
    }
    return true;
  }

  private boolean commonIndexingOperation(String method, String indexName, String errorMsg) {
    return commonIndexingOperationWithEntity(method, indexName, errorMsg, null);
  }

  private boolean commonIndexingOperationWithEntity(String method, String endpoint, String errorMsg, NStringEntity entity) {
    try {
      Request request = new Request(method, endpoint);
      if (entity != null) {
        request.setEntity(entity);
      }
      Response response = esClient.getClient().getLowLevelClient().performRequest(request);
      return response.getStatusLine().getStatusCode() == 200;
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, errorMsg, e);
      throw new SearchException(errorMsg, e);
    }
  }
}