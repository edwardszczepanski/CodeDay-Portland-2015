import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Menu extends BasicGameState
{
	TrueTypeFont wordFont;
	
	int windowWidth;
	int windowHeight;
	
	Image codeDay;
	
	RoundedRectangle[] menuBoxes;
	RoundedRectangle typedBox = new RoundedRectangle(windowWidth*1/2 - windowWidth * 1/10, windowHeight * 1/2 - windowHeight* 1/20, windowWidth * 1/5,windowHeight*1/10, 8);
	
	String[] menuBoxesText;
	String typed;
	
	Color[] menuBoxesColors;
	Color typedBoxColor;
	
	@Override
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		Font font = new Font("Courier", Font.BOLD, 40);
		wordFont = new TrueTypeFont(font, true);
		
		codeDay = new Image("data/codeday_logo.png");
		codeDay = codeDay.getScaledCopy(400, 82);
		
		windowWidth = gc.getWidth();
		windowHeight = gc.getHeight();
		
		typed = "";
		typedBox = new RoundedRectangle(windowWidth * 1/2 - windowWidth * 1/10, windowHeight * 1/2 - windowHeight * 1/20, windowWidth * 1/5, windowHeight * 1/10, 8);
		typedBoxColor = Color.white;
		
		menuBoxes = new RoundedRectangle[3];
		menuBoxes[0] = new RoundedRectangle(windowWidth * 8/10 - (windowWidth * 1/5)/2, windowHeight * 3/10, windowWidth * 1/5, windowHeight * 1/10, 8);
		menuBoxes[1] = new RoundedRectangle(windowWidth * 1/2 - windowWidth * 1/10, windowHeight * 17/20, windowWidth * 1/5,windowHeight * 1/10, 8);
		menuBoxes[2] = new RoundedRectangle(windowWidth * 1/10, windowHeight * 3/10, windowWidth * 1/5, windowHeight * 1/10, 8);
		
		menuBoxesText = new String[3];
		menuBoxesText[0] = "High Score";
		menuBoxesText[1] = "Exit";
		menuBoxesText[2] = "Play";
		
		menuBoxesColors = new Color[3];
		menuBoxesColors[0] = new Color(22, 139, 168);
		menuBoxesColors[1] = new Color(67, 181, 84);
		menuBoxesColors[2] = new Color(217, 46, 46);
	}
	
	public void enter(GameContainer gc, StateBasedGame state)
	{
		typed = "";
	}

	@Override
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setAntiAlias(true);
		
		g.setColor(new Color(193,194,193));
		g.fillRect(0, 0, windowWidth, windowHeight);
		
		g.drawImage(codeDay, windowWidth/2 - codeDay.getWidth()/2, windowHeight*1/10 - codeDay.getHeight()/2);
		
		int value = 420;
		g.setLineWidth(9001);
		g.setColor(menuBoxesColors[0]);
		g.drawArc(windowWidth/2 - value/2, windowHeight/2 - value/2, value, value, 275, 385);
		g.setColor(menuBoxesColors[1]);
		g.drawArc(windowWidth/2 - value/2, windowHeight/2 - value/2, value, value, 35, 145);
		g.setColor(menuBoxesColors[2]);
		g.drawArc(windowWidth/2 - value/2, windowHeight/2 - value/2, value, value, 155, 265);
		
		for (int i = 0; i < menuBoxes.length; i++)
		{
			g.setColor(menuBoxesColors[i]);
			g.fill(menuBoxes[i]);
			wordFont.drawString(menuBoxes[i].getX() + menuBoxes[i].getWidth()/2 - wordFont.getWidth(menuBoxesText[i])/2, menuBoxes[i].getY() + menuBoxes[i].getHeight()/2 - wordFont.getHeight(menuBoxesText[i])/2, menuBoxesText[i], Color.black);
		}
		
		g.setColor(typedBoxColor);
		g.fill(typedBox);
		wordFont.drawString(typedBox.getX() + 10, typedBox.getY() + typedBox.getHeight()/2 - wordFont.getHeight(typed)/2, typed, Color.black);
	}
	
	@Override
	public void keyPressed(int key, char c)
	{
		if (key == Input.KEY_BACK)
		{
			if (typed.length() > 0)
				typed = typed.substring(0, typed.length()-1);
		}
		else if (key == Input.KEY_TAB)
		{
			typed += "    ";
		}
		else if (key != Input.KEY_LSHIFT && key != Input.KEY_RSHIFT && key != Input.KEY_ENTER)
		{
			typed += Character.toString(c);
		}
		
		System.out.println(typed);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		for (int i = 0; i < menuBoxes.length; i++)
		{
			if (typed.equals(menuBoxesText[i]))
			{
				typedBoxColor = menuBoxesColors[i];
				
				break;
			}
			else
				typedBoxColor = Color.white;
		}
		
		if (input.isKeyPressed(Input.KEY_ENTER))
		{
			if (typed.equals(menuBoxesText[2]))
				state.enterState(2);
			else if (typed.equals(menuBoxesText[1]))
				gc.exit();
			else
				state.enterState(4);
		}
	}

	@Override
	public int getID() 
	{
		return 0;
	}
	
}
