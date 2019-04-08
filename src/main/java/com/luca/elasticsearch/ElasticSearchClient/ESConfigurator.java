package com.luca.elasticsearch.ElasticSearchClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ESConfigurator {
	private final Client client;
	private final ObjectMapper mapper;
	
	@SuppressWarnings("resource")
	public ESConfigurator() throws UnknownHostException {
		Settings settings = Settings.builder().put("client.transport.sniff", true).put("cluster.name", "lucacluster")
				.build();
		TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("localhost"), 9300);
		client = new PreBuiltTransportClient(settings).addTransportAddress(transportAddress);
		mapper = new ObjectMapper();
	}
	
	Client getClient() {
		return client;
	}
	
	ObjectMapper getMapper() {
		return mapper;
	}
}
