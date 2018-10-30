package com.eurodyn.qlack.fuse.search;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.eurodyn.qlack.fuse.search.dto.ESDocumentIdentifierDTO;
import com.eurodyn.qlack.fuse.search.dto.IndexingDTO;
import com.eurodyn.qlack.fuse.search.exception.SearchException;
import com.eurodyn.qlack.fuse.search.util.ESClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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

			// Execute indexing request.
			IndexRequest indexRequest = new IndexRequest(dto.getIndex(), dto.getType(), dto.getId())
					.source(mapper.writeValueAsString(dto.getSourceObject()), XContentType.JSON);

			
			  String endpoint = dto.getIndex() + "/" + dto.getType() + "/" + dto.getId();
		      Map<String, String> params =
		          dto.isRefresh() ? Collections.singletonMap("refresh", "wait_for") : new HashMap<>();

		      // Execute indexing request.
		      ContentType contentType = ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8"));
//			IndexResponse response = esClient.getClient().index(indexRequest, RequestOptions.DEFAULT);
	         IndexResponse response = esClient.getClient().index(indexRequest);

			
			
//		      Response response = esClient.getClient().performRequest("PUT", endpoint, params,
//		          new NStringEntity(mapper.writeValueAsString(dto.getSourceObject()), contentType));
//			
			
			LOGGER.log(Level.INFO, MessageFormat.format("Index document created with id: {0}, {1}", dto.getId(),response));

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, MessageFormat.format("Could not index document with id: {0}", dto.getId()), e);
			throw new SearchException(MessageFormat.format("Could not index document with id: {0}", dto.getId()));
		}
	}

	public void unindexDocument(ESDocumentIdentifierDTO dto) {
		try {

	      String endpoint = dto.getIndex() + "/" + dto.getType() + "/" + dto.getId();
	      Map<String, String> params =
	          dto.isRefresh() ? Collections.singletonMap("refresh", "wait_for") : new HashMap<>();

			DeleteRequest request = new DeleteRequest(dto.getIndex(),dto.getType(), dto.getId());
//			DeleteResponse response = esClient.getClient().delete(request, RequestOptions.DEFAULT);
	          
	          DeleteResponse response = esClient.getClient().delete(request);
//	          Response response = esClient.getClient().getLowLevelClient().performRequest("DELETE", endpoint, params);
			//.delete(request, RequestOptions.DEFAULT);
//		      esClient.getClient().performRequest("DELETE", endpoint, params);

			LOGGER.log(Level.INFO, MessageFormat.format("Index document deleted with id: {0}", dto.getId()),response );
			
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, MessageFormat.format("Could not delete document with id: {0}", dto.getId()), e);
			throw new SearchException(MessageFormat.format("Could not delete document with id: {0}", dto.getId()));
		}
	}
}
