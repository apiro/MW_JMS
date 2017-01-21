package browser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import common.Tweet;
import common.messages.ClientRequest;

public class ClientToServerBrowser {
	
	public static void main(String[] args) throws IOException, JMSException, NamingException {

		String queueName = null;
		Queue queueToExamine = null;
		QueueBrowser browser = null;
		Boolean control = true;
		HashMap<String, String> queueNames= new HashMap<String, String>();
		initNames(queueNames);
		
		print("starting");
		
		JMSContext context = ((ConnectionFactory) getContext().lookup("java:comp/DefaultJMSConnectionFactory")).createContext();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while(control) {

			print("Which queue do you want to inspect?");
			System.out.println("1 - tweetQueue\n2 - saveQueue\n3 - thumbnailQueue\n4 - requestQueue");
			queueName = queueNames.get(br.readLine());
			queueToExamine = (Queue)getContext().lookup(queueName);
			browser = context.createBrowser(queueToExamine);
			
			Enumeration<?> enumeration = browser.getEnumeration(); 
			while(enumeration.hasMoreElements()) {
				
				print("queue has elements ");
				
				Object obj = enumeration.nextElement();
				ClientRequest request = null;
				ObjectMessage objMsg = null;
				if (obj instanceof ObjectMessage) {
					
					objMsg = (ObjectMessage)obj;
					request = (Tweet)objMsg.getObject();
					print("Detected ClientRequest");
					print("Sender -> " + request.getUsername());
					print(" | Request -> " + request.toString());
				}
			}
			
			print("Do you want to close the browser?");
			String choice = br.readLine();
			if(choice.equals("Yes")) {
				control = false;
			}
		}
		
	}

	private static Context getContext() throws NamingException {
		
		print("getContext");
		
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
		props.setProperty("java.naming.provider.url", "iiop://localhost:3700");
		
		return new InitialContext(props);
	}
	
	public static void print(String s) {
		System.out.println("> Browser > " + s);
	}
	
	private static void initNames(HashMap<String, String> queueNames){
		queueNames.put("1", "tweetQueue");
		queueNames.put("2", "saveQueue");
		queueNames.put("3", "thumbnailQueue");
		queueNames.put("4", "requestQueue");
	}
}
