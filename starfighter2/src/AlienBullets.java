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

public class AlienBullets
{
	private List<AlienAmmo> ammo;

	public AlienBullets()
	{
		ammo = new ArrayList<AlienAmmo>();
	}

	public void add(AlienAmmo al)
	{
		ammo.add(al);
	}

	//post - draw each Ammo
	public void drawEmAll( Graphics window )
	{
		for(AlienAmmo a: ammo) {
			a.draw(window);
		}
	}

	public void moveEmAll()
	{
		for(AlienAmmo a: ammo) {
			a.move(null);
		}
	}

	public void cleanEmUp()
	{
		for(int i = 0; i < ammo.size(); i++) {
			if(ammo.get(i).getY()>525) {
				ammo.remove(i);
				i--;
			}
		}
	}

	public List<AlienAmmo> getList()
	{
		return ammo;
	}

	public String toString()
	{
		return "";
	}
}
