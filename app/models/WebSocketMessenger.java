package models;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Logger;
import play.libs.Json;
import play.mvc.WebSocket.Out;

public class WebSocketMessenger {

		public static void notifyAll(String username, String x, String y, Map<String, Out<JsonNode>> members) {
			ObjectNode event = Json.newObject();
			event.put("username", username);
			event.put("x", x);
			event.put("y", y);
			Logger.info(event.toString());
			
			for (Out<JsonNode> channel : members.values()) {
				channel.write(event);
			}
		}
}
