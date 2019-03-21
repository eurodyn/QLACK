package com.eurodyn.qlack.fuse.search.service;

import com.eurodyn.qlack.fuse.search.dto.ESDocumentIdentifierDTO;
import com.eurodyn.qlack.fuse.search.dto.IndexingDTO;
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
@Service
@Validated
public class IndexingService {

	private static final Logger LOGGER = Logger.getLogger(IndexingService.class.getName());
	private static ObjectMapper mapper;
	// The ES client injected by blueprint.
	private ESClient esClient;

	@Autowired
	private ElasticsearchOperations operations;

	@Autowired
	public IndexingService(ESClient esClient, ElasticsearchOperations elasticsearchOperations) {
		this.esClient = esClient;
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		this.operations = elasticsearchOperations;
	}

	public void indexDocument(IndexingDTO dto) {
		try {

			IndexRequest indexRequest = new IndexRequest(dto.getIndex(), dto.getType(), dto.getId())
					.source(mapper.writeValueAsString(dto.getSourceObject()), XContentType.JSON);

			IndexResponse response = esClient.getClient().index(indexRequest, RequestOptions.DEFAULT);

			LOGGER.log(Level.INFO, MessageFormat.format("Index document created with id: {0}, {1}", dto.getId(),response));

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, MessageFormat.format("Could not index document with id: {0}", dto.getId()), e);
			throw new SearchException(MessageFormat.format("Could not index document with id: {0}", dto.getId()));
		}
	}

  public void unindexDocument(ESDocumentIdentifierDTO dto) {
    try {

      DeleteRequest request = new DeleteRequest(dto.getIndex(), dto.getType(), dto.getId());
      DeleteResponse response = esClient.getClient().delete(request, RequestOptions.DEFAULT);

      LOGGER.log(Level.INFO, MessageFormat.format("Index document deleted with id: {0}", dto.getId()), response);

    } catch (IOException e) {
			LOGGER.log(Level.SEVERE, MessageFormat.format("Could not delete document with id: {0}", dto.getId()), e);
			throw new SearchException(MessageFormat.format("Could not delete document with id: {0}", dto.getId()));
    }
  }
}