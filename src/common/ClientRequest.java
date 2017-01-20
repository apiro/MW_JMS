package common;

import java.io.Serializable;

public class ClientRequest implements Serializable {

	private static final long serialVersionUID = -4116537866638364877L;
	private String username;
	private MessageType type;
	
	 // TIMELINE if request for timeline
	
	// Per me � meglio generalizzare al massimo la richiesta.
	// Invece di tanti parametri come usersToFollow, imgName... meglio creare una lista di oggetti
	// In base al tipo di richiesta, lato server sappiamo noi come gestire la lista di oggetti
	// Se la richiesta � FOLLOW, -> qui ci saranno una lista di nomi da seguire
	// Se la richiesta � SUBSCRIBE -> vuoto
	// Se la richiesta � IMAGE, -> ci sar� il nome dell'immagine
	// Se la richieta � TIMELINE, -> boh!
	
//	public Request(String userID, MessageType type, int name) {
//		super(type);
//		this.userID = userID;
//		getParam().add(name);
//	}
//	
//	public Request(String userID, MessageType type, ArrayList<String> users) {
//		super(type);
//		this.userID = userID;
//		getParam().addAll(users);
//	}
	
	// Request used when i want to subscribe to the tweet service
	public ClientRequest(String username, MessageType type) {
		this.setUsername(username);
		this.setType(type);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ClientRequest [username=" + username + ", type=" + type + "]";
	}
	
}
