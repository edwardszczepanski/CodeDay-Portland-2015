import java.awt.Font;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class HighScore extends BasicGameState
{
	TrueTypeFont wordFont;
	
	String typed;
	int currentLanguage;
	
	int windowWidth;
	int windowHeight;
	
	Polygon leftArrow = new Polygon();
	Polygon rightArrow = new Polygon();
	
	Color blue = new Color(22,139,168);
	Color green = new Color(67,181,84);
	Color red = new Color(217,46,46);
	
	RoundedRectangle titleBox;
	RoundedRectangle typedBox;
	RoundedRectangle[] scoreBoxes;
	
	Color leftArrowColor;
	Color rightArrowColor;
	Color typedBoxColor;
	Color scoreBoxColor;
	
	String[] highScores;
	String languageTitle;
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException
	{
		Font font = new Font("Courier", Font.BOLD, 40);
		wordFont = new TrueTypeFont(font, true);
		
		typed = "";
		currentLanguage = 1;
		windowWidth = gc.getWidth();
		windowHeight = gc.getHeight();
		
		titleBox = new RoundedRectangle(windowWidth * 1/2 - windowWidth * 2/10, windowHeight * 1/7, windowWidth * 2/5, windowHeight * 1/10, 8);
		
		scoreBoxes = new RoundedRectangle[3];
		for (int i = 0; i < scoreBoxes.length; i++)
			scoreBoxes[i] = new RoundedRectangle(windowWidth/2 - windowWidth/6, windowHeight * (i + 3)/10, windowWidth/3, windowHeight * 1/10, 8);
		scoreBoxColor = new Color(22,139,168);
		
		leftArrowColor = new Color(67,181,84);
		leftArrow.addPoint(windowWidth * 1/2 - windowWidth * 2/10 - 100, windowHeight * 1/7);
		leftArrow.addPoint(windowWidth * 1/2 - windowWidth * 2/10 - 100, windowHeight * 1/7 + windowHeight * 1/10);
		leftArrow.addPoint(windowWidth * 1/2 - windowWidth * 2/10 - 180, windowHeight * 1/7 + windowHeight * 1/20);
		
		rightArrowColor = new Color(217,46,46);
		rightArrow.addPoint(windowWidth * 1/2 + windowWidth * 2/10 + 100, windowHeight * 1/7);
		rightArrow.addPoint(windowWidth * 1/2 + windowWidth * 2/10 + 100, windowHeight * 1/7 + windowHeight * 1/10);
		rightArrow.addPoint(windowWidth * 1/2 + windowWidth * 2/10 + 180, windowHeight * 1/7 + windowHeight * 1/20);
		
		typedBox = new RoundedRectangle(windowWidth * 1/2 - windowWidth * 1/8, windowHeight * 5/7, windowWidth * 1/4, windowHeight * 1/10, 8);
		typedBoxColor = Color.white;
		
		ReadFile reader = new ReadFile("data/hello_worlds/" + currentLanguage + ".txt");
		try {
			languageTitle = reader.OpenFile()[0];
		} catch (IOException e) {
			e.printStackTrace();
		}
		ReadFile read = new ReadFile("data/High Scores/" + currentLanguage + ".txt");
		try {
			highScores = read.OpenFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < highScores.length; i++)
			highScores[i] = highScores[i].substring(1, highScores[i].indexOf(','));
	}
	
	public void enter(GameContainer gc, StateBasedGame state)
	{
		typed = "";
	}

	@Override
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setColor(new Color(193,194,193));
		g.fillRect(0, 0, windowWidth, windowHeight);
		
		g.setColor(Color.white);
		g.fill(titleBox);
		wordFont.drawString(titleBox.getX() + titleBox.getWidth()/2 - wordFont.getWidth(languageTitle)/2, titleBox.getY() + titleBox.getHeight()/2 - wordFont.getHeight(languageTitle)/2, languageTitle, Color.black);
		
		g.setColor(leftArrowColor);
		g.fill(leftArrow);
		
		g.setColor(rightArrowColor);
		g.fill(rightArrow);
		
		g.setColor(scoreBoxColor);
		for (int i = 0; i < scoreBoxes.length; i++)
		{
			g.fill(scoreBoxes[i]);
			wordFont.drawString(scoreBoxes[i].getX() + scoreBoxes[i].getWidth()/2 - wordFont.getWidth(highScores[i])/2, scoreBoxes[i].getY() + scoreBoxes[i].getHeight()/2 - wordFont.getHeight(highScores[i])/2, highScores[i], Color.black);
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

	@Override
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_ENTER))
		{
			if (typed.equals("Menu") || typed.equals("Exit"))
			{
				typedBoxColor = scoreBoxColor;
				state.enterState(0);
			}
			else if (typed.equals("Left") || typed.equals("Back"))
			{
				typedBoxColor = leftArrowColor;
				
				if (currentLanguage == 1)
					currentLanguage = 15;
				else
					currentLanguage--;
			}
			else if (typed.equals("Right") || typed.equals("Next"))
			{
				typedBoxColor = rightArrowColor;
				
				if (currentLanguage == 15)
					currentLanguage = 1;
				else
					currentLanguage++;
			}
			
			ReadFile reader = new ReadFile("data/hello_worlds/" + currentLanguage + ".txt");
			try {
				languageTitle = reader.OpenFile()[0];
			} catch (IOException e) {
				e.printStackTrace();
			}
			ReadFile read = new ReadFile("data/High Scores/" + currentLanguage + ".txt");
			try {
				highScores = read.OpenFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			for (int i = 0; i < highScores.length; i++)
				highScores[i] = highScores[i].substring(1, highScores[i].indexOf(','));
		}
		
		if (typed.equals("Left"))
		{
			typedBoxColor = leftArrowColor;
		}
		else if (typed.equals("Right"))
		{
			typedBoxColor = rightArrowColor;
		}
		else
			typedBoxColor = Color.white;
	}


	public int getID()
	{
		return 4;
	}
}