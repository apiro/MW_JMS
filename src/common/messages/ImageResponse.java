package common.messages;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageResponse extends ServerResponse {

	private static final long serialVersionUID = 1L;
	private byte[] image = null;

	public ImageResponse(MessageType type, int code, byte[] image) {
		super(type, code);
		this.setImage(image);
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
	
	@Override
	public String render() {
		
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(image);
			Image img = ImageIO.read(bais);
			JFrame frame = new JFrame();
			frame.setSize(img.getWidth(null), img.getHeight(null));
			JLabel label = new JLabel(new ImageIcon(img));
			frame.add(label);
			frame.setVisible(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return super.render();
	}

}
