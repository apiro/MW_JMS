package browser;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import server.Handler;
import server.ImageStorer;
import server.RequestHandler;
import server.ThumbnailCreator;
import server.TweetHandler;

public class Browser extends Thread{
	
	public boolean control = true;
	
	public void run() {
		HashMap<String, Queue> queues= new HashMap<String, Queue>();
		QueueBrowser[] browsers= new QueueBrowser[4];
		List<ArrayList<Handler>> handlers = new ArrayList<ArrayList<Handler>>(4);
		
		try {
			JMSContext context = ((ConnectionFactory) getContext().lookup("java:comp/DefaultJMSConnectionFactory")).createContext();
			
			queues.put("tweetQueue", (Queue)getContext().lookup("tweetQueue"));
			queues.put("saveQueue", (Queue)getContext().lookup("saveQueue"));
			queues.put("thumbnailQueue", (Queue)getContext().lookup("thumbnailQueue"));
			queues.put("requestQueue", (Queue)getContext().lookup("requestQueue"));
			
			browsers[0]=(context.createBrowser(queues.get(("tweetQueue"))));
			browsers[1]=(context.createBrowser(queues.get(("saveQueue"))));
			browsers[2]=(context.createBrowser(queues.get(("thumbnailQueue"))));
			browsers[3]=(context.createBrowser(queues.get(("requestQueue"))));
			
			handlers.add(0,new ArrayList<Handler>());//("TweetHandler_B");
			handlers.add(1,new ArrayList<Handler>());//("ImageStorer_B");
			handlers.add(2,new ArrayList<Handler>());//("ThumbnailCreator_B");
			handlers.add(3,new ArrayList<Handler>());//("RequestHandler_B");
		
		} catch (NamingException e1) {
			return;
		}
		
		print("starting");
		
		while(control) {
			try {
				Thread.sleep(5000);	//check every 10 seconds
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			} 
			
			//Checks the for queues
			for(int i=0;i<4;i++){
				QueueBrowser qb = browsers[i];
				Enumeration<?> enumeration;
				try {
					enumeration = qb.getEnumeration();
					int count = 0;
					while(enumeration.hasMoreElements()) {
						enumeration.nextElement();
						count++;
					}
					//If there are more than 10 messages in the queue, it will run a new thread 
					//	with the handler for that queue
					if(count < 4 && !handlers.get(i).isEmpty()){
						//print("Stopping by browser");
						handlers.get(i).remove(0).stopListening();
					} else if (count > 10){
						switch (i){
						case 0: 
							TweetHandler th = new TweetHandler("TweetHandler_B");
							handlers.get(i).add(th);
							th.print("Starting by browser");
							Thread t0 = new Thread(((Runnable) th));
							t0.start();
							break;
						case 1: 
							ImageStorer is = new ImageStorer("ImageStorer_B");
							handlers.get(i).add(is);
							is.print("Starting by browser");
							Thread t1 = new Thread(((Runnable) is));
							t1.start();
							break;
						case 2:
							ThumbnailCreator tc = new ThumbnailCreator("ThumbnailCreator_B");
							handlers.get(i).add(tc);
							tc.print("Starting by browser");
							Thread t2 = new Thread(((Runnable) tc));
							t2.start();
							break;
						case 3:
							RequestHandler rh = new RequestHandler("RequestHandler_B");
							handlers.get(i).add(rh);
							rh.print("Starting by browser");
							Thread t3 = new Thread(((Runnable) rh));
							t3.start();
							break;
						default:
							break;
					}
					} 
				} catch (JMSException e) {
					e.printStackTrace();
				} 
			}
		}
		
		for(ArrayList<Handler> a : handlers){
			for(Handler h : a)
				h.stopListening();
		}
	}

	private Context getContext() throws NamingException {
		
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
		props.setProperty("java.naming.provider.url", "iiop://localhost:3700");
		
		return new InitialContext(props);
	}
	
	public void print(String s) {
		System.out.println("> Browser > " + s);
	}
}
