package com.cisco.cmad;

import java.io.IOException;

import org.mongodb.morphia.Datastore;

import com.cisco.cmad.UserGet;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.core.http.HttpServer;

public class PubSub extends AbstractVerticle{
	
	public void start(Future<Void> startFuture) {
    	System.out.println("My verticle started!");
    	HttpServer();
    	startFuture.complete();
    }
    
    @Override
    public void stop(Future stopFuture) throws Exception{
    	System.out.println("My verticle started!");
    }
	
    public void HttpServer() {
    	HttpServer server = vertx.createHttpServer();
    	Router router = Router.router(vertx);
    	
    	//get API
    	getAPI(router);
    	
    	//serve static resources
    	serveStaticResources(router);
    	
    	//post API
    	postAPI(router);
    	
    	server.requestHandler(router::accept).listen(8084);

    }
    
    public void getAPI(Router router) {
    	router.get("/services/users/:id").handler(new UserGet());
    }
    
    public void serveStaticResources(Router router) {
    	router.route().handler(StaticHandler.create()::handle);
    }
    
    public void postAPI(Router router) {
    	router.route().handler(BodyHandler.create());
    	
    	router.post("/user").handler(rc -> {
    	String jsonStr = rc.getBodyAsString();
    	System.out.println(jsonStr);
    	
    	ObjectMapper mapper = new ObjectMapper();
    	
    	try {
    		UserDTO dto = null;
			try {
				dto = mapper.readValue(jsonStr, UserDTO.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
			User u = dto.toModel();
			Datastore dataStore = ServiceFactory.getMongoDB();
			dataStore.save(u);
			rc.response().setStatusCode(200).end("Data saved");
			
	    	// rc.response().end(node.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	});
    }
    
	public static void main(String[] args) {
		
		
    	VertxOptions options = new VertxOptions().setWorkerPoolSize(10);
    	Vertx vertx = Vertx.vertx(options);
    	
    	vertx.deployVerticle("com.cisco.cmad.PubSub", new Handler<AsyncResult<String>>() {

			@Override
			public void handle(AsyncResult<String> stringAsyncResult) {
				System.out.println("Verticle Deployment complete!!");
				
			}
    		
    	});
    	
    	vertx.eventBus().consumer("channel1", message -> {
    		System.out.println("message body : " + message.body());
    	});
    	
    	vertx.eventBus().publish("channel1", "message 2");
    	
    }
}
