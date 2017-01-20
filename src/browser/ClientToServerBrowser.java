package browser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
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

import common.ClientRequest;
import common.Tweet;

public class ClientToServerBrowser {
	
	public static void main(String[] args) throws IOException, JMSException, NamingException {

		String queueName = null;
		Queue queueToExamine = null;
		QueueBrowser browser = null;
		
		System.out.println("> Starting Browser ... ");
		Boolean closeBrowser = false;
		
		JMSContext context = ((ConnectionFactory) getContext().lookup("java:comp/DefaultJMSConnectionFactory")).createContext();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		while(Boolean.FALSE.equals(closeBrowser)) {

			queueName = br.readLine();
			queueToExamine = (Queue)getContext().lookup(queueName);
			browser = context.createBrowser(queueToExamine);
			
			Enumeration<?> enumeration = browser.getEnumeration(); 
			while(enumeration.hasMoreElements()) {
				
				System.out.println("Queue has elements ... ");
				
				Object obj = enumeration.nextElement();
				ClientRequest request = null;
				ObjectMessage objMsg = null;
				if (obj instanceof ObjectMessage) {
					
					objMsg = (ObjectMessage)obj;
					request = (Tweet)objMsg.getObject();
					System.out.println("Detected ClientRequest");
					System.out.println("Sender -> " + request.getUsername());
					System.out.println(" | Tweet -> " + request.toString());
				
				}
			}
			
			System.out.println("Do you want to close the browser?");
			String choice = br.readLine();
			if(choice.equals("Yes")) {
				closeBrowser = true;
			}
		}
		
	}

	private static Context getContext() throws NamingException {
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
		props.setProperty("java.naming.provider.url", "iiop://localhost:3700");
		return new InitialContext(props);
	}
	
}
