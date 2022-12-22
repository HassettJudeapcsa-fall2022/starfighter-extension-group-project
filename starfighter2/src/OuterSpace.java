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
import java.net.URL;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Image;
import java.io.File;


import javax.imageio.ImageIO;

public class OuterSpace extends Canvas implements KeyListener, Runnable
{
	private Ship ship;
	private Bullets bullets;
	private AlienHorde horde;
	
	private Powerup pu;
	private Timer puPlaceTimer;
	private TimerTask puPlacement;
	private Timer shipHasPU;
	private TimerTask puDuration;
	private boolean spawnedPU;
	private boolean hasPU;
	private boolean isDead;

	private AlienBullets alienBullets;
	private Timer shootTimer;
	private TimerTask shoot;
	private boolean press;
	
	private boolean[] keys;
	private BufferedImage back;
	
	private Image gameOver;

	public OuterSpace()
	{
		try
		{
			URL url = getClass().getResource("gameover.jpg");
			gameOver = ImageIO.read(url);
		}
		catch(Exception e)
		{
			//feel free to do something here
		}
		
		isDead = false;
		
		setBackground(Color.black);

		keys = new boolean[5];
		bullets = new Bullets();
		horde = new AlienHorde(14);
		pu = new Powerup();
		//initializes the powerup as well as the timer required for the powerup to spawn
		puPlaceTimer = new Timer();
		puPlacement = new TimerTask() {
			public void run() {
				if(pu != null) {
					if(!spawnedPU) {
						pu.spawnPowerUp();
						spawnedPU = true;
					}
				}
			}
		};
		
		//initializes the timer for the duration of the powerup
		shipHasPU = new Timer();
		puDuration = new TimerTask() {
			public void run() {
				ship.removePowerUp();
				hasPU = false;
			}
		};
		
		//initializes the booleans for the powerup logic
		spawnedPU = false;
		hasPU = false;
		press = true;
		
		shootTimer = new Timer();
		alienBullets = new AlienBullets();
		
		shoot = new TimerTask() {
			public void run() {
				Alien guy = horde.getList().get((int)(Math.random()*(horde.getList().size())));
				AlienAmmo shot = new AlienAmmo(guy.getX(), guy.getY());
				alienBullets.add(shot);
			}
		};
		
		shootTimer.scheduleAtFixedRate(shoot, 1000, 200);

		//instantiate other instance variables
		//Ship, Alien
		ship = new Ship(400, 450, 50, 50, 2);
		for(int i = 0, x=10; i < 14; i++) {
			horde.add(new Alien(x,10,50,50,1));
			x+=50;
		}
		
		//set timer for when powerup is placed
		puPlaceTimer.schedule(puPlacement, 1000);
		
		
		this.addKeyListener(this);
		new Thread(this).start();

		setVisible(true);
	}

   public void update(Graphics window)
   {
	   if(!isDead) {
		   paint(window);
	   }
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
		
		//code to implement once powerup actually spawns
		if(pu != null) {
			pu.shipCollect(ship);
			if(pu.isCollected()) {
				hasPU = true;
				spawnedPU = false;
				pu=null;
			}
			if(spawnedPU == true) {
				pu.draw(graphToBack);
			}
			
			//determines the duration of the powerup while hasPU is true
			if(hasPU) {
				shipHasPU.schedule(puDuration, 3000);
			}
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
		
		alienBullets.drawEmAll(graphToBack);
		alienBullets.moveEmAll();
		alienBullets.cleanEmUp();

		//add in collision detection to see if Bullets hit the Aliens and if Bullets hit the Ship
		horde.drawEmAll(graphToBack);
		horde.moveEmAll();
		horde.removeDeadOnes(bullets.getList());
		ship.draw(graphToBack);
		twoDGraph.drawImage(back, null, 0, 0);
		
		 if(ship.isCollide(alienBullets.getList()) && hasPU == false){
				window.drawImage(gameOver,0,0,800,600,null);
			 	isDead = true;
		 }
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

