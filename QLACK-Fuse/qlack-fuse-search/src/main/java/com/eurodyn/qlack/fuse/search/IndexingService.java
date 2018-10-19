package com.eurodyn.qlack.fuse.search;

import com.eurodyn.qlack.fuse.search.dto.ESDocumentIdentifierDTO;
import com.eurodyn.qlack.fuse.search.dto.IndexingDTO;
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Validated
public class IndexingService {

  private static final Logger LOGGER = Logger.getLogger(IndexingService.class.getName());
  private static ObjectMapper mapper;
  // The ES client injected by blueprint.
  private ESClient esClient;

  @Autowired
  public IndexingService(ESClient esClient) {
    this.esClient = esClient;
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  public void indexDocument(IndexingDTO dto) {
    try {
      String endpoint = dto.getIndex() + "/" + dto.getType() + "/" + dto.getId();
      Map<String, String> params =
          dto.isRefresh() ? Collections.singletonMap("refresh", "wait_for") : new HashMap<>();

      // Execute indexing request.
      ContentType contentType = ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8"));
      esClient.getClient().performRequest("PUT", endpoint, params,
          new NStringEntity(mapper.writeValueAsString(dto.getSourceObject()), contentType));
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE,
          MessageFormat.format("Could not index document with id: {0}", dto.getId()), e);
      throw new SearchException(
          MessageFormat.format("Could not index document with id: {0}", dto.getId()));
    }
  }

  public void unindexDocument(ESDocumentIdentifierDTO dto) {
    try {
      String endpoint = dto.getIndex() + "/" + dto.getType() + "/" + dto.getId();
      Map<String, String> params =
          dto.isRefresh() ? Collections.singletonMap("refresh", "wait_for") : new HashMap<>();

      // Execute indexing request.
      esClient.getClient().performRequest("DELETE", endpoint, params);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE,
          MessageFormat.format("Could not delete document with id: {0}", dto.getId()), e);
      throw new SearchException(
          MessageFormat.format("Could not delete document with id: {0}", dto.getId()));
    }
  }
}
