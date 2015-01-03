package events;

public enum WebSocketEvent {
	JOIN	{ public String event() {return "JOIN";		} },
	MESSAGE	{ public String event() {return "MESSAGE";	} },
	QUIT	{ public String event() {return "QUIT";		} };
	
	abstract public String event();
}
