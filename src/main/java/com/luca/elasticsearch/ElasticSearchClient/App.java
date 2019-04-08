package com.luca.elasticsearch.ElasticSearchClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ClientInfoStatus;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws UnknownHostException {
		Settings settings = Settings.builder().put("client.transport.sniff", true).put("cluster.name", "lucacluster")
				.build();
		TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("localhost"), 9300);
		Client client = new PreBuiltTransportClient(settings).addTransportAddress(transportAddress);
		String jsonObject = "{\"age\":10,\"dateOfBirth\":1471466076564," + "\"fullName\":\"John Doe\"}";
		IndexResponse response = client.prepareIndex("people", "Doe").setSource(jsonObject, XContentType.JSON).get();

		String id = response.getId();
		String index = response.getIndex();
		String type = response.getType();
		long version = response.getVersion();
		
		System.out.println("Response: " + id + ", " + index + ", " + type + ", " + version);
		client.close();
	}
}
