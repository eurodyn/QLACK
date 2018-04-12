package com.eurodyn.qlack.fuse.search;

import com.eurodyn.qlack.fuse.search.dto.QFESESDocumentIdentifierDTO;
import com.eurodyn.qlack.fuse.search.dto.QFESIndexingDTO;
import com.eurodyn.qlack.fuse.search.exception.QFESSearchException;
import com.eurodyn.qlack.fuse.search.util.QFESESClient;
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
public class QFESIndexingService {

  private static final Logger LOGGER = Logger.getLogger(QFESIndexingService.class.getName());
  private static ObjectMapper mapper;
  // The ES client injected by blueprint.
  private QFESESClient esClient;

  @Autowired
  public QFESIndexingService(QFESESClient esClient) {
    this.esClient = esClient;
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  public void indexDocument(QFESIndexingDTO dto) {
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
      throw new QFESSearchException(
          MessageFormat.format("Could not index document with id: {0}", dto.getId()));
    }
  }

  public void unindexDocument(QFESESDocumentIdentifierDTO dto) {
    try {
      String endpoint = dto.getIndex() + "/" + dto.getType() + "/" + dto.getId();
      Map<String, String> params =
          dto.isRefresh() ? Collections.singletonMap("refresh", "wait_for") : new HashMap<>();

      // Execute indexing request.
      esClient.getClient().performRequest("DELETE", endpoint, params);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE,
          MessageFormat.format("Could not delete document with id: {0}", dto.getId()), e);
      throw new QFESSearchException(
          MessageFormat.format("Could not delete document with id: {0}", dto.getId()));
    }
  }
}
