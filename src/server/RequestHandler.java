package server;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class RequestHandler implements MessageListener, Runnable{

	private JMSContext jmsContext;
	
	@Override
	public void onMessage(Message msg) {
		
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
