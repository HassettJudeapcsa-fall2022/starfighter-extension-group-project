
import java.io.File;
import java.net.URL;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;

public class Powerup extends MovingThing {
	private Image image;
	private boolean collected;
	
	//empty constructor
	public Powerup() {
		this(0,0,50,50);
	}
	//x and y constructor
	public Powerup( int x, int y) {
		this(x,y,50,50);
	}
	//x,y,width,height constructor
	public Powerup(int x, int y, int w, int h) {
		super(x,y,w,h,0);
		collected = false;
		//grabs image from url
		try
		{
			URL url = getClass().getResource("pu.png");
			image = ImageIO.read(url);
		}
		catch(Exception e)
		{
			//feel free to do something here
		}
	}
	
	public void move(String direction) {
		//obsolete because powerup should not move
	}
	
	//code for when the ship collides with the powerup
	public void shipCollect(Ship ship) {
		//collision between ship and powerup values
		if(this.getX()+50 > ship.getX() && this.getX()-50 < ship.getX() && this.getY()+50 > ship.getY() && this.getY()-50 < ship.getY()) {
			ship.collectPowerUp();
			collected = true;
		}
	}
	
	public void spawnPowerUp() {
		this.setPos((int)(Math.random()*750), (int)(Math.random()*262)+262);
	}
	
	public boolean isCollected() {
		return collected;
	}
	
	//draws the powerup on the screen
	public void draw(Graphics window) {
		window.drawImage(image, getX(), getY(), getWidth(), getHeight(), null);
	}
}
