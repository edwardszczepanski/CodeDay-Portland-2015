import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Selection extends BasicGameState
{
	TrueTypeFont wordFont;
	
	int windowWidth;
	int windowHeight;
	
	String typed;
	
	RoundedRectangle[] selectionBoxes;
	RoundedRectangle typedBox;
	
	Polygon upArrowTriangle = new Polygon();
	Polygon downArrowTriangle = new Polygon();
	
	Rectangle upArrowBox;
	Rectangle downArrowBox;
	
	RoundedRectangle upBox;
	RoundedRectangle downBox;
	
	Color typedBoxColor;
	Color[] selectionBoxesColors;
	Color downArrowColor;
	Color upArrowColor;
	
	String[] selectionBoxesStrings;
	String[] selectionBoxesHighScores;
	String upArrowString;
	String downArrowString;
	
	ArrayList<String[]> files;
	ArrayList<String> languages;
	
	GamePath gamePath;
	
	int upperLimit;
	
	Selection(GamePath filePath)
	{
		gamePath = filePath;
	}
	
	public void init(GameContainer gc, StateBasedGame game) throws SlickException
	{
		Font font = new Font("Courier", Font.BOLD, 40);
		wordFont = new TrueTypeFont(font, true);
		
		windowWidth = gc.getWidth();
		windowHeight = gc.getHeight();
		
		typed = "";
		typedBox = new RoundedRectangle(windowWidth - windowWidth * 1/4 - windowHeight/10, windowHeight/2 - windowHeight * 1/20, windowWidth * 3/11, windowHeight * 1/10, 8);
		typedBoxColor = Color.white;
		
		upArrowTriangle.addPoint(windowHeight/20 + windowWidth/2 + 80, windowHeight/10);
		upArrowTriangle.addPoint(windowHeight/20 + windowWidth/2 + 40, 140);
		upArrowTriangle.addPoint(windowHeight/20 + windowWidth/2 + 120, 140);
		downArrowTriangle.addPoint(windowHeight/20 + windowWidth/2 + 80, windowHeight * 9/10);
		downArrowTriangle.addPoint(windowHeight/20 + windowWidth/2 + 40, windowHeight * 9/10 - 140 + windowHeight/10);
		downArrowTriangle.addPoint(windowHeight/20 + windowWidth/2 + 120, windowHeight * 9/10 - 140 + windowHeight/10);
		
		upArrowBox = new Rectangle(windowHeight/20 + windowWidth/2 + 65, windowHeight/5 - 10, 30, 50);
		downArrowBox = new Rectangle(windowHeight/20 + windowWidth/2 + 65, windowHeight * 9/10 - 140 + windowHeight/10 - 50, 30, 50);
		
		upBox = new RoundedRectangle(windowWidth * 16/25, windowHeight/10, windowWidth/9, windowHeight/10, 8);
		downBox = new RoundedRectangle(windowWidth * 16/25, windowHeight * 8/10, windowWidth/9, windowHeight/10, 8);
		
		selectionBoxesColors = new Color[8];
		for (int i = 0; i < selectionBoxesColors.length; i++)
			selectionBoxesColors[i] = Color.white;
		downArrowColor = new Color(22,139,168);
		upArrowColor = new Color(217,46,46);
		
		upArrowString = "Up";
		downArrowString = "Down";
		
		files = new ArrayList<String[]>();
		for (int i = 1; i <= 15; i++)
		{
			ReadFile reader = new ReadFile("data/hello_worlds/" + i + ".txt");
			try {
				files.add(reader.OpenFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		languages = new ArrayList<String>();
		for (int i = 0; i < files.size(); i++)
			languages.add(files.get(i)[0]);
		
		selectionBoxes = new RoundedRectangle[8];
		for (int i = 1; i <= selectionBoxes.length; i++)
			selectionBoxes[i-1] = new RoundedRectangle(windowHeight/20, windowHeight * i/10, windowWidth/2, windowHeight* 1/10, 8);
		
		selectionBoxesHighScores = new String[15];
		for (int i = 1; i <= 15; i++)
		{
			ReadFile reader = new ReadFile("data/High Scores/" + i + ".txt");
			try {
				selectionBoxesHighScores[i-1] = reader.OpenFile()[0].substring(1, 7);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		upperLimit = 0;
	}

	public void enter(GameContainer gc, StateBasedGame state)
	{
		typed = "";
	}

	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setColor(new Color(193,194,193));
		g.fillRect(0, 0, windowWidth, windowHeight);
		
		g.setColor(Color.white);
		
		g.setColor(upArrowColor);
		g.fill(upArrowTriangle);
		g.fill(upArrowBox);
		g.fill(upBox);
		wordFont.drawString(upBox.getX() + upBox.getWidth()/2 - wordFont.getWidth(upArrowString)/2, upBox.getY() + upBox.getHeight()/2 - wordFont.getHeight(upArrowString)/2, upArrowString, Color.black);
		
		g.setColor(downArrowColor);
		g.fill(downArrowTriangle);
		g.fill(downArrowBox);
		g.fill(downBox);
		wordFont.drawString(downBox.getX() + downBox.getWidth()/2 - wordFont.getWidth(downArrowString)/2, downBox.getY() + downBox.getHeight()/2 - wordFont.getHeight(downArrowString)/2, downArrowString, Color.black);
		
		
		for (int i = upperLimit; i < upperLimit + 8; i++)
		{
			g.setColor(selectionBoxesColors[i - upperLimit]);
			g.fill(selectionBoxes[i - upperLimit]);
			wordFont.drawString(selectionBoxes[i - upperLimit].getX() + 10, selectionBoxes[i - upperLimit].getY() + selectionBoxes[i - upperLimit].getHeight()/2 - wordFont.getHeight(languages.get(i))/2, languages.get(i), Color.black);
			wordFont.drawString(selectionBoxes[i - upperLimit].getX() + selectionBoxes[i - upperLimit].getWidth() - wordFont.getWidth(selectionBoxesHighScores[i - upperLimit]) - 10, selectionBoxes[i - upperLimit].getY() + selectionBoxes[i - upperLimit].getHeight()/2 - wordFont.getHeight(selectionBoxesHighScores[i])/2, selectionBoxesHighScores[i], Color.black);
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
	}

	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		for (int i = 0; i < languages.size(); i++)
		{
			if (typed.equals(languages.get(i)))
			{
				if (i < 7)
				{
					upperLimit = i;
					selectionBoxesColors[0] = new Color(67, 181, 84);
				}
				else
				{
					upperLimit = 7;
					selectionBoxesColors[i - 7] = new Color(67, 181, 84);
				}
					
				typedBoxColor = new Color(67, 181, 84);
				
				if (input.isKeyPressed(Input.KEY_ENTER))
				{
					gamePath.setGamePath("data/hello_worlds/" + (i + 1) + ".txt");
					state.enterState(1);
				}
				
				break;
			}
			else
			{
				typedBoxColor = Color.white;
				if (i < 8)
					selectionBoxesColors[i] = Color.white;
			}
			
		}
		
		if (typed.equals(upArrowString))
		{
			if (input.isKeyPressed(Input.KEY_ENTER) && upperLimit != 0)
			{
				upperLimit--;
			}
			typedBoxColor = upArrowColor;
		}
		else if (typed.equals(downArrowString))
		{
			typedBoxColor = downArrowColor;
			if (input.isKeyPressed(Input.KEY_ENTER) && upperLimit != 7)
			{
				upperLimit++;
			}
		}
	}


	public int getID()
	{
		return 2;
	}
}
