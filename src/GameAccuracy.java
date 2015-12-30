import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class GameAccuracy
{
	String[] code;
	int charCount;
	float percent;
	int maxChars;
	String accuracy;
	
	public GameAccuracy(String[] c)
	{
		percent = 1;
		charCount = 0;
		code = c;
		accuracy = (percent * 100) + "%";
	}
	
	public void update(int charValues)
	{
		if (maxChars == 4)
			maxChars += 4;
		else
			maxChars++;
		charCount += charValues;
		
		percent = (float) charCount / (float) maxChars;
		accuracy = (percent * 100) + "%";
	}
	
	public void draw(Graphics g, TrueTypeFont wordFont, float x, float y)
	{
		wordFont.drawString(x, y - wordFont.getHeight(accuracy)/2, accuracy, Color.black);
	}
}
