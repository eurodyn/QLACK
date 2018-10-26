package com.eurodyn.qlack.fuse.search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.eurodyn.qlack.fuse.search.dto.SearchResultDTO;

public interface SearchRepository extends ElasticsearchRepository<SearchResultDTO, String> {

}
