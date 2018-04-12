package com.eurodyn.qlack.fuse.search.mappers;

import com.eurodyn.qlack.fuse.search.mappers.request.QFESInternalCreateIndexRequest;
import com.eurodyn.qlack.fuse.search.mappers.request.QFESInternalUpdateMappingRequest;
import com.eurodyn.qlack.fuse.search.request.QFESCreateIndexRequest;
import com.eurodyn.qlack.fuse.search.request.QFESUpdateMappingRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.nio.entity.NStringEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.UnsupportedEncodingException;

@Mapper(componentModel = "spring")
public abstract class QFESCreateIndexRequestMapper {

  private ObjectMapper mapper = new ObjectMapper();

  @Mapping(source = "shards", target = "settings.index.numberOfShards")
  @Mapping(source = "replicas", target = "settings.index.numberOfReplicas")
  @Mapping(source = "indexMapping", target = "mappings")
  @Mapping(source = "stopwords", target = "settings.analysis.filter.myStop.stopwords")
  abstract QFESInternalCreateIndexRequest mapToInternal(QFESCreateIndexRequest request);

  public NStringEntity mapToNStringEntity(QFESCreateIndexRequest createIndexRequest)
      throws JsonProcessingException, UnsupportedEncodingException {

    return new NStringEntity(mapper.writeValueAsString(mapToInternal(createIndexRequest)));
  }

  public NStringEntity mapToNStringEntity(QFESUpdateMappingRequest updateMappingRequest)
      throws JsonProcessingException, UnsupportedEncodingException {

    QFESInternalUpdateMappingRequest request = new QFESInternalUpdateMappingRequest();
    request.setProperties(updateMappingRequest.getIndexMapping());

    return new NStringEntity(mapper.writeValueAsString(request));
  }
}
