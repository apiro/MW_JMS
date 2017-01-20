package common;

import java.io.Serializable;
import java.util.ArrayList;

public class Request extends Message implements Serializable {
	
	private static final long serialVersionUID = -4116537866638364877L;
	private String userID;
	 // TIMELINE if request for timeline
	
	// Per me è meglio generalizzare al massimo la richiesta.
	// Invece di tanti parametri come usersToFollow, imgName... meglio creare una lista di oggetti
	// In base al tipo di richiesta, lato server sappiamo noi come gestire la lista di oggetti
	// Se la richiesta è FOLLOW, -> qui ci saranno una lista di nomi da seguire
	// Se la richiesta è SUBSCRIBE -> vuoto
	// Se la richiesta è IMAGE, -> ci sarà il nome dell'immagine
	// Se la richieta è TIMELINE, -> boh!
	
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
