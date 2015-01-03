package controllers;

import models.WebSocketActor;

import com.fasterxml.jackson.databind.JsonNode;

import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result draggable(String username) {
    	session("username", username);
        return ok(draggable.render("WebSocket Sample", username));
    }

    public static WebSocket<JsonNode> ws() {
    	final String username = session("username");
    	return new WebSocket<JsonNode>() {
    		@Override
    		public void onReady(final In<JsonNode> in, final Out<JsonNode> out) {
    			try {
    				WebSocketActor.join(username, in, out);
    			} catch (Exception e) {
    				Logger.error("Can't connect WebSocket");
    				e.printStackTrace();
    			}
    		}
    	};
    }
}
