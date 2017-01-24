package com.dms.planb.core;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.dms.planb.support.ActionPerformer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;

class DmsVerticle extends AbstractVerticle {
	private HttpServer server;
	private HttpServerResponse response;
	
	private JSONObject requestObject;
	private JSONObject responseObject;
	/*
	 *  org.json.JSONObject
	 *  I created instance of io.vertx.core.json.JsonObject, but can't find a way about serialize
	 */

	/*
	 * (non-Javadoc)
	 * @see io.vertx.core.AbstractVerticle#start()
	 */
	@Override
	public void start() throws Exception {
		System.out.println("Server started");
		
		server = vertx.createHttpServer();
		server.requestHandler(request -> {
			System.out.println("Received request");
			
			Buffer totalBuffer = Buffer.buffer();
//			MultiMap params = request.params();
			// Get parameters from request
			
			if(request.method() == HttpMethod.POST) {
				// The server will only work if the Http method is POST.
				System.out.println("Received POST method");
				
				request.handler(buffer -> {
					totalBuffer.appendBuffer(buffer);
				});
				
				request.endHandler(v -> {
					 // The endHandler of the request is invoked when the entire request, including any body has been fully read.
					System.out.println("fully read");
					
					// 1-1. Get command from header.
					int command = Integer.parseInt(request.getHeader("command"));
					
					// 1-2. Get client's info
					try {
						JSONObject clientObject = new JSONObject(request.getHeader("User-Agent"));
//						clientObject.getString("Version");
//						clientObject.getString("UUID");
					} catch	(JSONException e) {
						e.printStackTrace();
					}
					
					// 1-3. Get request object from buffer.
					try {
						requestObject = new JSONObject(totalBuffer.toString());
//						requestObject = new JSONObject().put("testKey", "testValue");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					/*
					 *  2. Performs the operation.
					 *  Branch off the ActionPerformer class' perform method.
					 */
					try {
						responseObject = ActionPerformer.perform(command, requestObject);
					} catch (JSONException | SQLException e) {
						e.printStackTrace();
					}
					
					// 3. Response to client.
					response = request.response();
					response.putHeader("Content-type", "application/json; charset=utf-8");
					
					try {
						response.setStatusCode(responseObject.getInt("status"));
						responseObject.remove("status");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					// Success, Fail, etc..
					
					response.end(responseObject.toString());
					response.close();
				}); // endHandler
			} else {
				/*
				 *  If Http method is CONNECT, DELETE, GET, HEAD, OPTIONS, or other ...
				 *  Communication fail.
				 */
			}
		}).listen(10419);
	}
	
	public void stop(@SuppressWarnings("rawtypes") Future stopFuture) throws Exception {
		
	}
}
