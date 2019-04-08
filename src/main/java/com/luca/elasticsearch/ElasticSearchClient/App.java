package com.luca.elasticsearch.ElasticSearchClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
public class App {
	static final public class UserData {
		public UserData(@JsonProperty("name") String name, @JsonProperty("surname") String surname, @JsonProperty("age") int age) {
			this.name = name;
			this.surname = surname;
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public String getSurname() {
			return surname;
		}

		public int getAge() {
			return age;
		}

		private final String name;
		private final String surname;
		private final int age;
	}

	public static void addData(Client client) throws IOException {
		List<App.UserData> input = new ArrayList<App.UserData>();
		input.add(new App.UserData("luca", "stoppa", 37));
		input.add(new App.UserData("saverio", "stoppa", 2));
		input.add(new App.UserData("lucja", "szpak-stoppa", 31));
		input.add(new App.UserData("fabrizio", "stoppa", 60));
		input.add(new App.UserData("teresa", "mesin", 58));
		input.add(new App.UserData("giulia", "stoppa", 30));
		input.add(new App.UserData("filippo", "trentini", 34));

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
		Settings settings = Settings.builder().put("client.transport.sniff", true).put("cluster.name", "lucacluster")
				.build();
		TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("localhost"), 9300);
		Client client = new PreBuiltTransportClient(settings).addTransportAddress(transportAddress);
		//addData(client);
		
		QueryBuilder all = QueryBuilders.rangeQuery("age").from(25).to(60);
		SearchResponse response = client.prepareSearch().setTypes()
				.setPostFilter(all).execute().actionGet();
		ObjectMapper mapper = new ObjectMapper();
		SearchHits hits = response.getHits();
		System.out.println("I got " + hits.getTotalHits() + " results");
		for (SearchHit hit : hits.getHits()) {
			System.out.println(hit.getSourceAsString());
			App.UserData data = mapper.readValue(hit.getSourceAsString(), App.UserData.class);
			System.out.println(data.getName() + "/" + data.getSurname() + "/" + data.getAge());
		}
		
		client.close();
	}
}
