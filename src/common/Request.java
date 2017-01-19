package common;

import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable{
	
	private static final long serialVersionUID = -4116537866638364877L;
	private String userID;
	private MessageType reqType; // TIMELINE if request for timeline
	
	// Per me è meglio generalizzare al massimo la richiesta.
	// Invece di tanti parametri come usersToFollow, imgName... meglio creare una lista di oggetti
	// In base al tipo di richiesta, lato server sappiamo noi come gestire la lista di oggetti
	// Se la richiesta è FOLLOW, -> qui ci saranno una lista di nomi da seguire
	// Se la richiesta è SUBSCRIBE -> vuoto
	// Se la richiesta è IMAGE, -> ci sarà il nome dell'immagine
	// Se la richieta è TIMELINE, -> boh!
	private ArrayList<Object> requestParams;
	
	// Questi li lascio per ora. Ne riparliamo in uni
	private int imgName;
	private String userToFollow;
						 // <followed name> if requests to follow
	
	public Request(String userID, MessageType type, int name) {
		super();
		this.userID = userID;
		this.reqType = type;
		this.imgName = name;
	}
	
	public Request(String userID, MessageType type, String user) {
		super();
		this.userID = userID;
		this.reqType = type;
		this.userToFollow = user;
	}
	
	// Request used when i want to subscribe to the tweet service
	public Request(String userID, MessageType type) {
		super();
		this.userID = userID;
		this.reqType = type;
	}
	
	public String getUserID() {
		return userID;
	}
	public MessageType getType() {
		return reqType;
	}
	public String getUserToFollow() {
		return userToFollow;
	}
	public int getImgName() {
		return imgName;
	}

}
