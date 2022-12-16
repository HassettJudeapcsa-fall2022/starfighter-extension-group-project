//(c) A+ Computer Science
//www.apluscompsci.com
//Name -

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class AlienHorde
{
	private List<Alien> aliens;

	public AlienHorde(int size)
	{
		aliens = new ArrayList<Alien>(size);
	}

	public void add(Alien al)
	{
		aliens.add(al);
	}

	public void drawEmAll( Graphics window )
	{
		for(Alien a: aliens) {
			a.draw(window);
		}
	}

	public void moveEmAll()
	{
		for(Alien a: aliens) {
			a.move(null);
		}
	}

	public void removeDeadOnes(List<Ammo> shots)
	{
		for(Ammo a: shots) {
			for(Alien al: aliens) {
				if(al.getX()+50>=a.getX() && al.getX()-50<=a.getX() && al.getY()+50>=a.getY() && al.getY()- 50 <= a.getY()) {
					shots.remove(a);
					aliens.remove(al);
				}
			}
		}
	}

	public String toString()
	{
		return "";
	}
}
