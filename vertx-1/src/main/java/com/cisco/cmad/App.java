package com.cisco.cmad;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * Hello world!
 *
 */
public class App extends AbstractVerticle
{
    @Override
    public void start(Future<Void> startFuture) {
    	System.out.println("My verticle started!");
    }
    
    @Override
    public void stop(Future stopFuture) throws Exception{
    	System.out.println("My verticle started!");
    }
    
    public static void main(String[] args) {
    	VertxOptions options = new VertxOptions().setWorkerPoolSize(10);
    	Vertx vertx = Vertx.vertx(options);
    	vertx.deployVerticle("com.cisco.cmad.App");
    }
}
