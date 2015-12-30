import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class GameTimer
{
	boolean running;
	long startingTime;
	int minutes;
	int seconds;
	int milliseconds;
	String timer;
	
	public GameTimer(long start)
	{
		running = true;
		startingTime = start;
		timer = "00:00:000";
	}
	
	public void updateTime(long currentTime)
	{
		if (running)
		{
			long diff = currentTime - startingTime;
			milliseconds = (int) (diff / Math.pow(10, 6));
			if (milliseconds > 1000)
			{
				seconds = (int) (milliseconds / 1000);
				milliseconds = milliseconds % 1000;
				if (seconds > 60)
				{
					minutes = (int) (seconds / 60);
					seconds = seconds % 60;
				}
			}
			else 
			{
				seconds = 0;
				minutes = 0;
			}
			
			timer = "0" + minutes + ":";
			if (seconds < 10)
			{
				 timer += "0";
			}
			timer += seconds + ":";
			if (milliseconds < 10)
			{
				timer += "00";
			}
			else if (milliseconds < 100)
			{
				timer += "0";
			}
			timer += milliseconds;
		}
	}
	
	public void stop()
	{
		running = false;
	}
	
	public void draw(Graphics g, GameContainer gc, TrueTypeFont wordFont, float x, float y)
	{
		wordFont.drawString(x - wordFont.getWidth(timer), y - wordFont.getHeight(timer)/2, timer, Color.black);
	}
}
