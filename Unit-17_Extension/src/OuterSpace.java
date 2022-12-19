//(c) A+ Computer Science
//www.apluscompsci.com
//Name -

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import static java.lang.Character.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OuterSpace extends Canvas implements KeyListener, Runnable
{
	private Ship ship;
	private Bullets bullets;
	private AlienHorde horde;
	
	private Powerup pu;
	private boolean spawnedPU;

	private boolean press;
	
	private boolean[] keys;
	private BufferedImage back;

	public OuterSpace()
	{
		setBackground(Color.black);

		keys = new boolean[5];
		bullets = new Bullets();
		horde = new AlienHorde(8);
		pu = new Powerup();
		spawnedPU = false;
		press = true;       

		//instantiate other instance variables
		//Ship, Alien
		ship = new Ship(400, 450, 50, 50, 5);
		for(int i = 0, x=10; i < 8; i++) {
			horde.add(new Alien(x,10,50,50,2));
			x+=50;
		}

		this.addKeyListener(this);
		new Thread(this).start();

		setVisible(true);
	}

   public void update(Graphics window)
   {
	   paint(window);
   }

	public void paint( Graphics window )
	{
		//set up the double buffering to make the game animation nice and smooth
		Graphics2D twoDGraph = (Graphics2D)window;

		//take a snap shop of the current screen and same it as an image
		//that is the exact same width and height as the current screen
		if(back==null)
		   back = (BufferedImage)(createImage(getWidth(),getHeight()));

		//create a graphics reference to the back ground image
		//we will draw all changes on the background image
		Graphics graphToBack = back.createGraphics();

		graphToBack.setColor(Color.BLUE);
		graphToBack.drawString("StarFighter ", 25, 50 );
		graphToBack.setColor(Color.BLACK);
		graphToBack.fillRect(0,0,800,600);
		
		if(spawnedPU == false) {
			spawnedPU = true;
			pu.spawnPowerUp();
		}
		
		if(pu != null) {
			if(pu.isCollected()) {
				pu=null;
			}
			pu.draw(window);
			pu.shipCollect(ship);
		}

		//add code to move Ship, Alien, etc.
		if(keys[0] == true) {
			ship.move("left");
			ship.draw(window);
		}
		if(keys[1] == true) {
			ship.move("right");
			ship.draw(window);
		}
		if(keys[2] == true) {
			ship.move("up");
			ship.draw(window);
		}
		if(keys[3] == true) {
			ship.move("down");
			ship.draw(window);
		}
		if(keys[4] == true && press) {
			Ammo shot = new Ammo(ship.getX()+20,ship.getY(),3);
			bullets.add(shot);
			press = false;
		}
		
		bullets.drawEmAll(graphToBack);
		bullets.moveEmAll();
		bullets.cleanEmUp();

		//add in collision detection to see if Bullets hit the Aliens and if Bullets hit the Ship
		horde.drawEmAll(graphToBack);
		horde.moveEmAll();
		horde.removeDeadOnes(bullets.getList());
		ship.draw(graphToBack);
		twoDGraph.drawImage(back, null, 0, 0);
	}


	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			keys[0] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			keys[1] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP)
		{
			keys[2] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			keys[3] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			keys[4] = true;
		}
		repaint();
	}

	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			keys[0] = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			keys[1] = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP)
		{
			keys[2] = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			keys[3] = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			keys[4] = false;
			press = true;
		}
		repaint();
	}

	public void keyTyped(KeyEvent e)
	{
      //no code needed here
	}

   public void run()
   {
   	try
   	{
   		while(true)
   		{
   		   Thread.currentThread().sleep(5);
            repaint();
         }
      }catch(Exception e)
      {
      }
  	}
}

