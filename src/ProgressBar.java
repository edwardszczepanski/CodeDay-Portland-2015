import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class ProgressBar
{
	Image codeDay;
	Rectangle bar;
	int maxChars;
	int charsCompleted;
	
	public ProgressBar(String[] code, int windowHeight, int windowWidth)
	{
		try {
			codeDay = new Image("data/CodeDay.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		codeDay = codeDay.getScaledCopy(windowHeight/6, windowHeight/6);
		
		bar = new Rectangle(windowWidth/10, windowHeight * 3/20, windowWidth * 4/5, 5);
		
		maxChars = 0;
		for (int i = 0; i < code.length; i++)
			maxChars += code[i].length();
		charsCompleted = 0;
	}
	
	public void update(int update)
	{
		charsCompleted += update;
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.white);
		g.draw(bar);
		g.fill(new Rectangle(bar.getX(), bar.getY(), (bar.getWidth() * ((float) charsCompleted/ (float) maxChars)), bar.getHeight()));
		g.drawImage(codeDay, bar.getX() - (codeDay.getWidth()/2) + (bar.getWidth() * ((float) charsCompleted/ (float) maxChars)), bar.getY() + (bar.getHeight()/2) - (codeDay.getHeight()/2));
	}
}
