package server;

import java.util.ArrayList;
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

import common.AckResponse;
import common.ClientRequest;
import common.FollowRequest;
import common.GenericErrorResponse;
import common.ImageRequest;
import common.ImageResponse;
import common.MessageType;
import common.Timeline;
import common.TimelineResponse;
import common.User;

public class RequestHandler extends Handler implements MessageListener, Runnable {

	public RequestHandler(String identity) {
		super(identity);
	}

	private JMSContext jmsContext;
	
	@Override
	public void onMessage(Message msg) {
		
		print("onMessage");
		
		try {
			Queue replyToQueue = (Queue) msg.getJMSReplyTo();
			ClientRequest request = msg.getBody(ClientRequest.class);
		
			switch(request.getType()){
				case SUBSCRIBE:
					print("subscribe");
					
					if(createSubscription(request)){
						print("new user registered [ " + request.getUsername() + " ]");
						jmsContext.createProducer().send(replyToQueue, new AckResponse(MessageType.SUBSCRIBE, 1));	
					}else{
						print("user registration error");
						jmsContext.createProducer().send(replyToQueue, new AckResponse(MessageType.SUBSCRIBE, 0));	
					}
					break;
				case FOLLOW:
					print("follow");
					
					if(follow(request)){
						print("updated following users by user [ " + request.getUsername() + " ]");
						jmsContext.createProducer().send(replyToQueue, new AckResponse(MessageType.FOLLOW, 1));	
					}else{
						print("following registration error");
						jmsContext.createProducer().send(replyToQueue, new AckResponse(MessageType.FOLLOW, 0));	
					}
					break;
				case IMAGE:
					print("image");
					byte[] replyImage = getImage(request);
					
					if(getImage(request) != null){
						print("loaded image requestes by user [ " + request.getUsername() + " ]");
						jmsContext.createProducer().send(replyToQueue, new ImageResponse(MessageType.IMAGE, 1, replyImage));
					}else{
						print("load image error");
						jmsContext.createProducer().send(replyToQueue, new ImageResponse(MessageType.IMAGE, 0, replyImage));
					}
					break;
				case TIMELINE:
					print("timeline");
					Timeline replyTimeline = getTimeline(request);
					
					if(getTimeline(request) != null){
						print("loaded image requestes by user [ " + request.getUsername() + " ]");
						jmsContext.createProducer().send(replyToQueue, new TimelineResponse(MessageType.TIMELINE, 1, replyTimeline));
					}else{
						print("load image error");
						jmsContext.createProducer().send(replyToQueue, new TimelineResponse(MessageType.TIMELINE, 0, replyTimeline));
					}
					break;
				default:
					print("the system can't detect the type of the ClientRequest, generic error is sent back");
					
					jmsContext.createProducer().send(replyToQueue, new GenericErrorResponse());
					break;
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}		
	}
	
	/** Function that handles the subscription of a user, 
	 * adding it to the Resources users
	 */
	private boolean createSubscription(ClientRequest request) {
		
		print("createSubscription");
		
		User newUser = new User(request.getUsername());
		
		return Resources.RS.addUser(newUser);	
		
	}
	
	/** Function that handles the follow operation 
	 * adding user to the followers of toFollow user
	 * return false if a user(sender or toFollow) doesn't exists
	 */  
	private boolean follow(ClientRequest request){  
		
		print("follow");
		
		User user = Resources.RS.getUserById(request.getUsername());
		ArrayList<String> usersToAdd = ((FollowRequest)request).getUsers();
		if(user != null && !usersToAdd.isEmpty()){
			
			for(String toAdd: usersToAdd) {
				User userToAdd = Resources.RS.getUserById(toAdd);
				userToAdd.addFollower(user);
			}
			
			return true;
			
		} else {
			
			return false;
			
		}
		
	}
	
	/** Function that handles the request to get a Timenline 
	 *  return a null if user doesn't exists - Lo so che non � elegante... si pu� cambiare
	 */ 
	private Timeline getTimeline(ClientRequest request){
		
		print("getTimeline");
		
		User user = Resources.RS.getUserById(request.getUsername());
		if(user != null){
			return user.getMytimeline();
		}
		return null;
	}

	/** Function that handles the request to get an image
	 *  return a null if the image doesn't exist - Lo so che non � elegante... si pu� cambiare
	 */ 
	private byte[] getImage(ClientRequest request){
		
		print("getImage");
		
		User user = Resources.RS.getUserById(request.getUsername());
		String name = ((ImageRequest)request).getImageID();
		
		if(user != null){
			byte[] img = Resources.RS.getImage(name);
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

		print("started");
	}
	
	private Context getContext() throws NamingException {
		
		print("getContext");
		
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming");
		props.setProperty("java.naming.provider.url", "iiop://localhost:3700");
		
		return new InitialContext(props);
	}	
}
