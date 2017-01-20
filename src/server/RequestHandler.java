package server;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import common.MessageType;
import common.Request;
import common.Response;
import common.Timeline;
import common.User;

public class RequestHandler implements MessageListener, Runnable{

	private JMSContext jmsContext;
	
	@Override
	public void onMessage(Message msg) {
		
		try {
			Queue replyToQueue = (Queue) msg.getJMSReplyTo();
			Request request = msg.getBody(Request.class);
			switch(request.getType()){
				case SUBSCRIBE:
					// TODO - Creare una risposta sensata per i vari tipi di richieste. Meglio se un tipo per tutte le richieste
					if(createSubscription(request)){
						System.out.println("Registrazione Utente OKAY");
						jmsContext.createProducer().send(replyToQueue, new Response(MessageType.SUBSCRIBE, "OK"));	
					}else{
						System.out.println("Registrazione Utente KO");
						jmsContext.createProducer().send(replyToQueue, new Response(MessageType.SUBSCRIBE, "KO"));	
					}
					break;
				case FOLLOW:
					if(follow(request)){
						System.out.println("Follow OKAY");
						jmsContext.createProducer().send(replyToQueue, new Response(MessageType.SUBSCRIBE, "OK"));	
					}else{
						System.out.println("Follow KO");
						jmsContext.createProducer().send(replyToQueue, new Response(MessageType.SUBSCRIBE, "KO"));	
					}
					break;
				case IMAGE:
					System.out.println("getImage message");
					jmsContext.createProducer().send(replyToQueue, new Response(MessageType.SUBSCRIBE, "null if failed", getImage(request)));
					break;
				case TIMELINE:
					System.out.println("getTimeline message");
					jmsContext.createProducer().send(replyToQueue, new Response(MessageType.SUBSCRIBE, "null if failed", getTimeline(request)));
					break;
				default:
					System.out.println("Spero di non entrare qui");
					break;
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}		
	}
	
	/** Function that handles the subscription of a user, 
	 * adding it to the Resources users
	 */
	private boolean createSubscription(Request request) {
		User newUser = new User(request.getUserID());
		return Resources.RS.addUser(newUser);	
	}
	
	/** Function that handles the follow operation 
	 * adding user to the followers of toFollow user
	 * return false if a user(sender or toFollow) doesn't exists
	 */   //for the moment follow just one user at a time
	private boolean follow(Request request){  
		User user = Resources.RS.getUserById(request.getUserID());
		String toFollow = (String)request.getParam().get(0);
		if(user != null && toFollow != null){
			User utf = Resources.RS.getUserById(toFollow);
			if(utf != null){
				utf.addFollower(user);
				return true;
			}
		}
		return false;
	}
	
	/** Function that handles the request to get a Timenline 
	 *  return a null if user doesn't exists - Lo so che non è elegante... si può cambiare
	 */ 
	private Timeline getTimeline(Request request){
		User user = Resources.RS.getUserById(request.getUserID());
		if(user != null){
			return user.getMytimeline();
		}
		return null;
	}

	/** Function that handles the request to get an image
	 *  return a null if the image doesn't exist - Lo so che non è elegante... si può cambiare
	 */ 
	private byte[] getImage(Request request){
		User user = Resources.RS.getUserById(request.getUserID());
		int name = (int)request.getParam().get(0);
		if(user != null){
			byte[] img = Resources.RS.getImage(Integer.toString(name));
			return img;
		}
		return null;
	}

	@Override
	public void run() {
		Context initialContext;
		try {
			initialContext = getContext();
			jmsContext = ((ConnectionFactory) initialContext.lookup("java:comp/DefaultJMSConnectionFactory")).createContext();
			
			//lookup tweetQueue and set messageListener
			String requestQueueName ="requestQueue";
			Queue requestQueue = (Queue) initialContext.lookup(requestQueueName);
			jmsContext.createConsumer(requestQueue).setMessageListener(this);
			
		} catch (NamingException e) {
			e.printStackTrace();
		}

		System.out.println("> Request queue started");
	}
	
	private Context getContext() throws NamingException {
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
		props.setProperty("java.naming.provider.url", "iiop://localhost:3700");
		return new InitialContext(props);
	}	
}
