package models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import events.EventUtil;
import events.Message;
import events.WebSocketEvent;
import play.libs.Akka;
import play.libs.F.Callback0;
import play.libs.F.Option;
import play.libs.Json;
import play.libs.F.Callback;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import static java.util.concurrent.TimeUnit.*;
import static akka.pattern.Patterns.ask;

public class WebSocketActor extends UntypedActor {
	
	private final static ActorRef ref = Akka.system().actorOf(new Props(WebSocketActor.class));
	
	Map<String, Out<JsonNode>> members = new HashMap<>();
	
	public static void join(final String username, In<JsonNode> in, Out<JsonNode> out) throws Exception {
		Boolean result = (Boolean) Await.result(
				ask(ref, new Message(username, "", "", WebSocketEvent.JOIN, out), 1000)
				,Duration.create(1, SECONDS));
		if (result) {
			in.onMessage(new Callback<JsonNode>() {
				public void invoke(JsonNode event) {
					ref.tell(new Message(
								username, event.get("x").asText(), event.get("y").asText(), WebSocketEvent.MESSAGE, null)
					, ref);
				}
			});
			
			in.onClose(new Callback0() {
				public void invoke() {
					ref.tell(new Message(username, "", "", WebSocketEvent.MESSAGE, null)
					, ref);
				}
			});
			
		} else {
			ObjectNode error = Json.newObject();
			error.put("error", result);
			out.write(error);
		}
	}
	
	@Override
	public void onReceive(Object message) {
		Option<Message> event = EventUtil.getEvent(message);
		
		if (event.isDefined()) {
			Message m = event.get();
			switch (m.getEventType()) {
			case JOIN:
				members.put(m.getUsername(), m.getChannel());
				getSender().tell(true, ref);				
				break;
			case MESSAGE:
				WebSocketMessenger.notifyAll(m.getUsername(), m.getX(), m.getY(), members);
				break;
			case QUIT:
				members.remove(m.getUsername());
				break;
			default:
				unhandled(message);
				break;
			}
		}
	}
}
