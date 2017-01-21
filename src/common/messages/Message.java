package common.messages;

import java.io.Serializable;
import java.util.ArrayList;

//this class is useless
public class Message implements Serializable{

	private static final long serialVersionUID = -1751638466505235363L;
	private final MessageType reqType;
	private ArrayList<Object> param;


	public Message(MessageType reqType) {
		this.reqType = reqType;
		param = new ArrayList<Object>();
	}
	
	public MessageType getType() {
		return reqType;
	}

	public ArrayList<Object> getParam() {
		return param;
	}

	@Override
	public String toString() {
		
		String temp = "param = ( ";
		
		for(Object p : param) {
			temp.concat(p.toString().concat(" "));
		}
		
		temp = temp.concat(")");
		
		return "Message [reqType=" + reqType + ", " + temp;
	}
	
	
	
}
