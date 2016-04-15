package com.cisco.cmad;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class User implements Handler<RoutingContext> {

	String jsonResponse = "{\"Hello\": \"World\"}";
	
	String name = null;
	int age = 0;
	@Override
	public void handle(RoutingContext arg0) {
		String id = arg0.request().getParam("id");
		
		HttpServerResponse response = arg0.response();
		response.putHeader("Content-Type", "application/json");
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
