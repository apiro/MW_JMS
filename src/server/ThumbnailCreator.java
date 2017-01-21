package server;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import common.FinalTweet;
import common.Timeline;
import common.Tweet;
import common.User;

public class ThumbnailCreator extends Handler implements MessageListener, Runnable {

	public ThumbnailCreator(String identity) {
		super(identity);
	}

	private JMSContext jmsContext;
	
	@Override
	public void onMessage(Message msg) {
		
		print("onMessage");
		
		try{
			
			Tweet tweet = msg.getBody(Tweet.class);
			print(tweet.toString());
			byte[] thumbnail = null;
			
			InputStream in = new ByteArrayInputStream(tweet.getImage());
			BufferedImage img = ImageIO.read(in);
			BufferedImage scaled = scale(img, 0.5);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(scaled, "jpg", baos);
			thumbnail = baos.toByteArray();
			
			FinalTweet finalTweet = new FinalTweet(tweet.getUsername(), tweet.getText(), thumbnail, tweet.getImgName());
			User user = Resources.RS.getUserById(tweet.getUsername());
			ArrayList<User> followers = (ArrayList<User>)user.getFollowers();
			
			for(User u : followers){
				Timeline timeline = u.getMytimeline();
				timeline.addTweet(finalTweet);
			}
			
			System.out.println(msg);
			
		} catch(JMSException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedImage scale(BufferedImage source,double ratio) {
		  int w = (int) (source.getWidth() * ratio);
		  int h = (int) (source.getHeight() * ratio);
		  BufferedImage bi = getCompatibleImage(w, h);
		  Graphics2D g2d = bi.createGraphics();
		  double xScale = (double) w / source.getWidth();
		  double yScale = (double) h / source.getHeight();
		  AffineTransform at = AffineTransform.getScaleInstance(xScale,yScale);
		  g2d.drawRenderedImage(source, at);
		  g2d.dispose();
		  return bi;
		}
	
	private BufferedImage getCompatibleImage(int w, int h) {
		  GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		  GraphicsDevice gd = ge.getDefaultScreenDevice();
		  GraphicsConfiguration gc = gd.getDefaultConfiguration();
		  BufferedImage image = gc.createCompatibleImage(w, h);
		  return image;
		}
	
	@Override
	public void run() {
		Context initialContext;
		try {
			initialContext = getContext();
			jmsContext = ((ConnectionFactory) initialContext.lookup("java:comp/DefaultJMSConnectionFactory")).createContext();
			
			//lookup saveQueue
			String thumbnailQueueName ="thumbnailQueue";
			Queue thumbnailQueue = (Queue) initialContext.lookup(thumbnailQueueName);
			jmsContext.createConsumer(thumbnailQueue).setMessageListener(this);
			
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
