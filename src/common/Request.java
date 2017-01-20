package common;

import java.io.Serializable;
import java.util.ArrayList;

public class Request extends Message implements Serializable {
	
	private static final long serialVersionUID = -4116537866638364877L;
	private String userID;
	 // TIMELINE if request for timeline
	
	// Per me � meglio generalizzare al massimo la richiesta.
	// Invece di tanti parametri come usersToFollow, imgName... meglio creare una lista di oggetti
	// In base al tipo di richiesta, lato server sappiamo noi come gestire la lista di oggetti
	// Se la richiesta � FOLLOW, -> qui ci saranno una lista di nomi da seguire
	// Se la richiesta � SUBSCRIBE -> vuoto
	// Se la richiesta � IMAGE, -> ci sar� il nome dell'immagine
	// Se la richieta � TIMELINE, -> boh!
	
	public Request(String userID, MessageType type, int name) {
		super(type);
		this.userID = userID;
		getParam().add(name);
	}
	
	public Request(String userID, MessageType type, ArrayList<String> users) {
		super(type);
		this.userID = userID;
		getParam().addAll(users);
	}
	
	// Request used when i want to subscribe to the tweet service
	public Request(String userID, MessageType type) {
		super(type);
		this.userID = userID;
	}
	
	public String getUserID() {
		return userID;
	}

}
