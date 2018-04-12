package com.eurodyn.qlack.fuse.search;

import com.eurodyn.qlack.fuse.search.dto.QFESSearchHitDTO;
import com.eurodyn.qlack.fuse.search.dto.QFESSearchResultDTO;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQueryBoolean;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQueryBoolean.BooleanType;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQueryMatch;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQueryMultiMatch;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQueryRange;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQuerySort;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQuerySpec;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQueryString;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQueryStringSpecField;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQueryStringSpecFieldNested;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQueryTerm;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQueryTermNested;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQueryTerms;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQueryTermsNested;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQueryWildcard;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESQueryWildcardNested;
import com.eurodyn.qlack.fuse.search.dto.queries.QFESSimpleQueryString;
import com.eurodyn.qlack.fuse.search.exception.QFESSearchException;
import com.eurodyn.qlack.fuse.search.mappers.request.QFESInternalScollRequest;
import com.eurodyn.qlack.fuse.search.mappers.request.QFESInternalSearchRequest;
import com.eurodyn.qlack.fuse.search.mappers.response.QFESQueryResponse;
import com.eurodyn.qlack.fuse.search.mappers.response.QFESQueryResponse.Aggregations.Agg.Bucket;
import com.eurodyn.qlack.fuse.search.mappers.response.QFESQueryResponse.Hits.Hit;
import com.eurodyn.qlack.fuse.search.request.QFESScrollRequest;
import com.eurodyn.qlack.fuse.search.util.QFESESClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Validated
public class QFESSearchService {

  private static final ObjectMapper mapper = new ObjectMapper();
  private static final Logger LOGGER = Logger.getLogger(QFESIndexingService.class.getName());

  // The ES client injected by blueprint.
  private QFESESClient esClient;

  @Autowired
  public QFESSearchService(QFESESClient esClient) {
    this.esClient = esClient;
  }

  public QFESSearchResultDTO search(QFESQuerySpec dto) {
    StringBuilder endpointBuilder = new StringBuilder();

    // This is done to remove duplicates
    List<String> indeces = new ArrayList<>(new HashSet<>(dto.getIndices()));

    // If no indeces are defind then search them all
    if (indeces.isEmpty()) {
      endpointBuilder.append("_all");
    }

    // append indeces to the query
    for (String index : indeces) {
      if (indeces.indexOf(index) > 0) {
        endpointBuilder.append(",");
      }

      endpointBuilder.append(index);
    }

    // This is done to remove duplicates
    List<String> types = new ArrayList<>(new HashSet<>(dto.getTypes()));

    // if no types are defined then search them all
    if (!types.isEmpty()) {
      endpointBuilder.append("/");
    }

    // append types to the query
    for (String type : types) {
      if (types.indexOf(type) > 0) {
        endpointBuilder.append(",");
      }

      endpointBuilder.append(type);
    }

    if (dto.isCountOnly()) {
      endpointBuilder.append("/_count");
    } else {
      endpointBuilder.append("/_search");
    }

    Map<String, String> params = new HashMap<>();

    QFESQuerySort dtoSort = dto.getQuerySort();
    QFESInternalSearchRequest internalRequest = new QFESInternalSearchRequest();
    if (!dto.isCountOnly()) {
      internalRequest.setFrom(dto.getStartRecord());
      internalRequest.setSize(dto.getPageSize());
      internalRequest.setExplain(dto.isExplain());
      internalRequest.setSort(buildSort(dtoSort));

      if (dto.getScroll() != null) {
        params.put("scroll", dto.getScroll().toString() + "m");
      }

      if (dto.getAggregate() != null) {
        internalRequest.setSource(new ArrayList<>());
        internalRequest.getSource().add(dto.getAggregate());
        internalRequest.setAggs(buildAggregate(dto.getAggregate(), dto.getAggregateSize()));
      }
    }
    internalRequest.setQuery(buildQuery(dto));

    Response response;
    try {
      ContentType contentType = ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8"));
      response = esClient.getClient()
          .performRequest("GET", endpointBuilder.toString(), params,
              new NStringEntity(mapper.writeValueAsString(internalRequest), contentType));
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not execute query.", e);
      throw new QFESSearchException("Could not execute query.", e);
    }

    QFESQueryResponse queryResponse;
    try {
      queryResponse = mapper.readValue(response.getEntity().getContent(), QFESQueryResponse.class);
    } catch (UnsupportedOperationException | IOException e) {
      LOGGER.log(Level.SEVERE, "Could not deserialize response.", e);
      throw new QFESSearchException("Could not deserialize response.", e);
    }

    QFESSearchResultDTO result = buildResultFrom(queryResponse, dto.isCountOnly(),
        dto.isIncludeAllSource(),
        dto.isIncludeResults());

    if (!dto.isCountOnly()) {
      result.setHasMore(queryResponse.getHits().getTotal() > dto.getPageSize());
    }

    return result;
  }

  private String buildQuery(QFESQuerySpec dto) {
    StringBuilder builder = new StringBuilder("{");

    if (dto instanceof QFESQueryBoolean) {
      QFESQueryBoolean query = (QFESQueryBoolean) dto;

      builder.append("\"bool\" : {");

      Map<BooleanType, List<QFESQuerySpec>> queriesMap = new HashMap<>();
      for (Entry<QFESQuerySpec, BooleanType> entry : query.getTerms().entrySet()) {
        if (entry.getValue() != null) {
          queriesMap.putIfAbsent(entry.getValue(), new ArrayList<>());
          queriesMap.get(entry.getValue()).add(entry.getKey());
        }
      }

      boolean appendComa = false;
      for (Entry<BooleanType, List<QFESQuerySpec>> entry : queriesMap.entrySet()) {
        if (appendComa) {
          builder.append(",");
        }

        if (BooleanType.MUSTNOT.equals(entry.getKey())) {
          builder.append("\"must_not\" : [");
        } else if (BooleanType.SHOULD.equals(entry.getKey())) {
          builder.append("\"should\" : [");
        } else {
          builder.append("\"must\" : [");
        }

        for (QFESQuerySpec querySpec : entry.getValue()) {
          if (entry.getValue().indexOf(querySpec) > 0) {
            builder.append(",");
          }

          builder.append(buildQuery(querySpec));
        }

        builder.append("]");
        appendComa = true;
      }

      builder.append("}");
    } else if (dto instanceof QFESQueryMatch) {
      QFESQueryMatch query = (QFESQueryMatch) dto;

      builder.append("\"match\" : { \"")
          .append(query.getField())
          .append("\" : \"")
          .append(query.getValue())
          .append("\" }");
    } else if (dto instanceof QFESQueryMultiMatch) {
      QFESQueryMultiMatch query = (QFESQueryMultiMatch) dto;

      builder.append("\"multi_match\" : { \"query\" : \"")
          .append(query.getValue())
          .append("\", \"fields\" : [");

      for (int i = 0; i < query.getFields().length; i++) {
        if (i > 0) {
          builder.append(", ");
        }

        builder.append("\"")
            .append(query.getFields()[i])
            .append("\"");
      }

      builder.append("]}");
    } else if (dto instanceof QFESQueryString) {
      QFESQueryString query = (QFESQueryString) dto;

      builder.append("\"query_string\" : { \"query\" : \"")
          .append(query.getQueryString())
          .append("\"}");
    } else if (dto instanceof QFESQueryTerm) {
      QFESQueryTerm query = (QFESQueryTerm) dto;

      builder.append("\"term\" : { \"")
          .append(query.getField())
          .append("\" : \"")
          .append(query.getValue())
          .append("\" }");
    } else if (dto instanceof QFESQueryTermNested) {
      QFESQueryTermNested query = (QFESQueryTermNested) dto;
      builder.append("\"nested\" : { ")
          .append("\"path\": \"")
          .append(query.getPath())
          .append("\", \"query\": { ")
          .append("\"term\" : { \"")
          .append(query.getField())
          .append("\" : \"")
          .append(query.getValue())
          .append("\" }")
          .append(" } , \"inner_hits\": {")
          .append("\"_source\" : false, ")
          .append("\"docvalue_fields\" : [ \"")
          .append(query.getDocvalueFields())
          .append("\"]")
          .append("}}");
    } else if (dto instanceof QFESQueryWildcard) {
      QFESQueryWildcard query = (QFESQueryWildcard) dto;

      builder.append("\"wildcard\" : { \"")
          .append(query.getField())
          .append("\" : \"")
          .append(query.getWildcard())
          .append("\" }");
    } else if (dto instanceof QFESQueryWildcardNested) {
      QFESQueryWildcardNested query = (QFESQueryWildcardNested) dto;

      builder.append("\"nested\" : { ")
          .append("\"path\": \"")
          .append(query.getPath())
          .append("\", \"query\": { ")
          .append("\"wildcard\" : { \"")
          .append(query.getField())
          .append("\" : \"")
          .append(query.getWildcard())
          .append("\" }")
          .append(" } , \"inner_hits\": {")
          .append("\"_source\" : false, ")
          .append("\"docvalue_fields\" : [ \"")
          .append(query.getDocvalueFields())
          .append("\"]")
          .append("}}");
    } else if (dto instanceof QFESQueryTerms) {
      QFESQueryTerms query = (QFESQueryTerms) dto;
      builder.append("\"terms\" : { \"")
          .append(query.getField())
          .append("\" : [ ")
          .append(query.getValues())
          .append(" ] }");
    } else if (dto instanceof QFESQueryTermsNested) {
      QFESQueryTermsNested query = (QFESQueryTermsNested) dto;
      builder.append("\"nested\" : { ")
          .append("\"path\": \"")
          .append(query.getPath())
          .append("\", \"query\": { ")
          .append("\"terms\" : { \"")
          .append(query.getField())
          .append("\" : [ ")
          .append(query.getValues())
          .append(" ] }")
          .append(" } , \"inner_hits\": {")
          .append("\"_source\" : false, ")
          .append("\"docvalue_fields\" : [ \"")
          .append(query.getDocvalueFields())
          .append("\"]")
          .append("}}");
    } else if (dto instanceof QFESQueryRange) {
      QFESQueryRange query = (QFESQueryRange) dto;
      builder.append("\"range\" : { \"")
          .append(query.getField())
          .append("\" : { \"gte\" : \"")
          .append(query.getFromValue())
          .append("\" , \"lte\" : \"")
          .append(query.getToValue())
          .append("\" } }");
    } else if (dto instanceof QFESQueryStringSpecField) {
      QFESQueryStringSpecField query = (QFESQueryStringSpecField) dto;
      builder.append("\"query_string\" : { \"fields\" : [\"")
          .append(query.getField())
          .append("\"] , \"query\" : \"")
          .append(query.getValue())
          .append("\" , \"default_operator\" : \"")
          .append(query.getOperator())
          .append("\" }");
    } else if (dto instanceof QFESQueryStringSpecFieldNested) {
      QFESQueryStringSpecFieldNested query = (QFESQueryStringSpecFieldNested) dto;
      builder.append("\"nested\" : { ")
          .append("\"path\": \"")
          .append(query.getPath())
          .append("\", \"query\": { ")
          .append("\"query_string\" : { \"fields\" : [\"")
          .append(query.getField())
          .append("\"] , \"query\" : \"")
          .append(query.getValue())
          .append("\" , \"default_operator\" : \"")
          .append(query.getOperator())
          .append("\" }")
          .append(" } , \"inner_hits\": {")
          .append("\"_source\" : false, ")
          .append("\"docvalue_fields\" : [ \"")
          .append(query.getDocvalueFields())
          .append("\"]")
          .append("}}");
    } else if (dto instanceof QFESSimpleQueryString) {
      QFESSimpleQueryString query = (QFESSimpleQueryString) dto;

      builder.append("\"simple_query_string\" : { \"fields\" : [\"")
          .append(query.getField())
          .append("\"] , \"query\" : \"")
          .append(query.getValue())
          .append("\" , \"default_operator\" : \"")
          .append(query.getOperator())
          .append("\" }");
    }
    return builder.append("}")
        .toString().replace("\"null\"", "null");
  }

  private String buildAggregate(String aggregate, int aggregateSize) {
    return new StringBuilder("{")
        .append("\"agg\" : {\"terms\" : {\"field\" : \"")
        .append(aggregate)
        .append("\", \"size\" : ")
        .append(aggregateSize)
        .append(",\"order\" : {\"_term\" : \"desc\"}")
        .append("}}}")
        .toString();
  }

  private String buildSort(QFESQuerySort dto) {
    StringBuilder builder = new StringBuilder("[");

    if (dto instanceof QFESQuerySort) {
      QFESQuerySort sort = dto;

      for (Entry<String, String> entry : sort.getSortMap().entrySet()) {
        if (builder.length() > 1) {
          builder.append(',');
        }

        builder.append("{")
            .append("\"")
            .append(entry.getKey())
            .append("\"")
            .append(" : {")
            .append("\"order\"").append(" : ").append("\"").append(entry.getValue()).append("\"")
            .append("}")
            .append("}");
      }
    }

    builder.append("]");

    return builder.toString();

  }

  public boolean exists(String indexName, String typeName, String id) {
    String endpoint = indexName + "/" + typeName + "/" + id;
    try {
      Response response = esClient.getClient().performRequest("HEAD", endpoint);
      return response.getStatusLine().getStatusCode() == 200;
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE,
          MessageFormat.format("Could not check if document with id: {0} exists", id), e);
      throw new QFESSearchException(
          MessageFormat.format("Could not check if document with id: {0} exists", id));
    }
  }

  public QFESSearchHitDTO findById(String indexName, String typeName, String id) {
    String endpoint = indexName + "/" + typeName + "/" + id;
    try {
      Response response = esClient.getClient().performRequest("GET", endpoint);
      if (response.getStatusLine().getStatusCode() == 200) {
        Hit hit = mapper.readValue(response.getEntity().getContent(), Hit.class);
        return map(hit);
      } else {
        return null;
      }
    } catch (IOException e) {
      return null;
    }
  }

  public QFESSearchResultDTO scroll(QFESScrollRequest request) {
    QFESInternalScollRequest internalRequest = new QFESInternalScollRequest();
    internalRequest.setScroll(request.getScroll().toString() + "m");
    internalRequest.setScrollId(request.getScrollId());

    Response response;
    try {
      ContentType contentType = ContentType.APPLICATION_JSON.withCharset(Charset.forName("UTF-8"));
      response = esClient.getClient().performRequest("GET", "_search/scroll", new HashMap<>(),
          new NStringEntity(mapper.writeValueAsString(internalRequest), contentType));
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Could not execute scroll query.", e);
      throw new QFESSearchException("Could not execute scroll query.", e);
    }

    QFESQueryResponse queryResponse;
    try {
      queryResponse = mapper.readValue(response.getEntity().getContent(), QFESQueryResponse.class);
    } catch (UnsupportedOperationException | IOException e) {
      LOGGER.log(Level.SEVERE, "Could not deserialize response.", e);
      throw new QFESSearchException("Could not deserialize response.", e);
    }

    QFESSearchResultDTO result = buildResultFrom(queryResponse, false, true, true);
    result.setHasMore(!result.getHits().isEmpty());

    return result;
  }

  private QFESSearchResultDTO buildResultFrom(QFESQueryResponse queryResponse,
      boolean countOnly, boolean includeAllSource, boolean includeResults) {

    QFESSearchResultDTO result = new QFESSearchResultDTO();
    if (!countOnly) {
      result.setBestScore(queryResponse.getHits().getMaxScore());
      result.setExecutionTime(queryResponse.getTook());
      result.setTimedOut(queryResponse.isTimeOut());
      result.setTotalHits(queryResponse.getHits().getTotal());
      result.setScrollId(queryResponse.getScrollId());
    } else {
      result.setTotalHits(queryResponse.getCount());
    }
    result.setShardsFailed(queryResponse.getShards().getFailed());
    result.setShardsSuccessful(queryResponse.getShards().getSuccessful());
    result.setShardsTotal(queryResponse.getShards().getTotal());

    if (!countOnly && includeAllSource) {
      try {
        result.setSource(mapper.writeValueAsString(queryResponse));
      } catch (JsonProcessingException e) {
        LOGGER.log(Level.SEVERE, "Could not serialize response.", e);
        throw new QFESSearchException("Could not serialize response.", e);
      }
    }

    if (!countOnly && includeResults) {
      for (Hit hit : queryResponse.getHits().getHits()) {
        result.getHits().add(map(hit));
      }
    }

    if (queryResponse.getAggregations() != null
        && queryResponse.getAggregations().getAgg() != null) {
      for (Bucket bucket : queryResponse.getAggregations().getAgg().getBuckets()) {
        result.getAggregations().put(bucket.getKey_as_string(), bucket.getDoc_count());
      }
    }

    return result;
  }

  private QFESSearchHitDTO map(Hit hit) {
    QFESSearchHitDTO sh = new QFESSearchHitDTO();
    sh.setScore(hit.getScore());
    sh.setType(hit.getType());
    sh.setSource(hit.getSource());
    sh.setId(hit.getId());
    sh.setInnerHits(hit.getInnerHits());
    return sh;
  }
}
