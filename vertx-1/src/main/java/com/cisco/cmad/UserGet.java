package com.cisco.cmad;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class UserGet implements Handler<RoutingContext> {

	String jsonResponse = "{\"Hello\": \"World\"}";
	
	String name = null;
	int age = 0;
	@Override
	public void handle(RoutingContext arg0) {
		String id = arg0.request().getParam("id");
		
		HttpServerResponse response = arg0.response();
		response.putHeader("Content-Type", "application/json");
		Datastore dataStore = ServiceFactory.getMongoDB();
		ObjectId oid = null;
		try {
			oid = new ObjectId(id);
		} catch (Exception e) {// Ignore format errors
		}
		List<User> users = dataStore.createQuery(User.class).field("id").equal(oid).asList();
		if (users.size() != 0) {
			UserDTO dto = new UserDTO().fillFromModel(users.get(0));
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.valueToTree(dto);
			response.end(node.toString());
		} else {
			response.setStatusCode(404).end("not found");
		}
		response.end(jsonResponse);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setAge(int age) {
		this.age = age;
	} 
	
	public String getName() {
		return "User's name is " + this.name;
	}
	
	public int getAge() {
		return this.age;
	}
	
	public String toString(){
	      return "User { name: "+ this.name +", age: "+ this.age+ " }";
	   }

}
