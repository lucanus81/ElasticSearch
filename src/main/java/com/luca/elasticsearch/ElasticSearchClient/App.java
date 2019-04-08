package com.luca.elasticsearch.ElasticSearchClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

/**
 * Hello world!
 *
 */
public class App {

	public static void addDataToElasticsearch(Client client) throws IOException {
		List<UserData> input = new ArrayList<UserData>();
		input.add(new UserData("luca", "stoppa", 37));
		input.add(new UserData("saverio", "stoppa", 2));
		input.add(new UserData("lucja", "szpak-stoppa", 31));
		input.add(new UserData("fabrizio", "stoppa", 60));
		input.add(new UserData("teresa", "mesin", 58));
		input.add(new UserData("giulia", "stoppa", 30));
		input.add(new UserData("filippo", "trentini", 34));

		input.forEach((o) -> {
			try {
				XContentBuilder builder = XContentFactory.jsonBuilder().startObject().field("name", o.getName())
						.field("surname", o.getSurname()).field("age", o.getAge()).endObject();
				IndexResponse response = client.prepareIndex("userdata", "entity").setSource(builder).get();
				if (response.getResult() != Result.CREATED)
					throw new RuntimeException("Cannot create a new data");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	public static void main(String[] args) throws IOException {
		ESConfigurator conf = new ESConfigurator();

		try {
			SearchHits hits = ESQueryBuilder.getAgesBetween25And60(conf).getHits();			
			for (SearchHit hit : hits.getHits()) {
				System.out.println(hit.getSourceAsString());
				UserData data = conf.getMapper().readValue(hit.getSourceAsString(), UserData.class);
				System.out.println(data.getName() + "/" + data.getSurname() + "/" + data.getAge());
			}
		} finally {
			if (conf.getClient() != null)
				conf.getClient().close();
		}
	}
}
