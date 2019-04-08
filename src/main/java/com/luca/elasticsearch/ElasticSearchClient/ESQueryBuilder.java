package com.luca.elasticsearch.ElasticSearchClient;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class ESQueryBuilder {
	public static SearchResponse getAgesBetween25And60(ESConfigurator conf) {
		QueryBuilder query = QueryBuilders.rangeQuery("age").from(25).to(60);
		return conf.getClient().prepareSearch().setTypes().setPostFilter(query).execute().actionGet();
	}
}
