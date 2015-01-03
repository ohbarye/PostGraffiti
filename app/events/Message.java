package events;

import java.io.Serializable;

import lombok.Data;

import com.fasterxml.jackson.databind.JsonNode;

import play.mvc.WebSocket.Out;

@Data
public class Message implements Event, Serializable{
	
	private static final long serialVersionUID = 4784843769982929333L;
	final String username;
	final String x;
	final String y;
	final WebSocketEvent event;
	final Out<JsonNode> channel;
	
	@Override
	public WebSocketEvent getEventType() {
		return event;
	}
}
