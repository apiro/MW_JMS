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

import common.Request;
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
						jmsContext.createProducer().send(replyToQueue, "OK");	
					}else{
						System.out.println("Registrazione Utente KO");
						jmsContext.createProducer().send(replyToQueue, "KO");	
					}
					break;
				case FOLLOW:
					break;
				case IMAGE:
					break;
				case TIMELINE:
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
