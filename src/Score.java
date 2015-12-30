import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Score extends BasicGameState
{
	TrueTypeFont wordFont;
	
	int windowHeight;
	int windowWidth;
	
	String typed;
	String[] topScores;
	String highScore;
	String[] topScoreLabels;
	
	RoundedRectangle[] topScoreBoxes;
	RoundedRectangle scoreBox;
	RoundedRectangle typedBox;
	RoundedRectangle menuBox;
	
	Color typedBoxColor;
	Color menuBoxColor;
	
	String file;
	String accuracy;
	String time;
	int fileNumber;
	int minutes;
	int seconds;
	int milliseconds;
	
	@Override
	public void init(GameContainer gc, StateBasedGame state) throws SlickException
	{
		Font font = new Font("Courier", Font.BOLD, 40);
		wordFont = new TrueTypeFont(font, true);
		
		windowHeight = gc.getHeight();
		windowWidth = gc.getWidth();
		
		highScore = "Score";
	}

	public void enter(GameContainer gc, StateBasedGame state)
	{
		typed = "";
		typedBoxColor = Color.white;
		
		String[] holder = null;
		ReadFile reader = new ReadFile("data/recent_highscore.txt");
		try {
			holder = reader.OpenFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		file = holder[0];
		fileNumber = Integer.parseInt(file.substring(file.indexOf('/', 5) + 1, file.indexOf('.')));
		
		accuracy = holder[2].substring(0, holder[2].length()-1);
		
		time = holder[1];
		minutes = Integer.parseInt(time.substring(0, time.indexOf(':')));
		seconds = Integer.parseInt(time.substring(3, 5));
		milliseconds = Integer.parseInt(time.substring(6));
		
		topScores = null;
		ReadFile highReader = new ReadFile("data/High Scores/" + fileNumber + ".txt");
		try {
			topScores = highReader.OpenFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < topScores.length; i++)
			topScores[i] = topScores[i].substring(1, topScores[i].indexOf(','));
		for (int i = 0; i < topScores.length; i++)
		{
			if (seconds < Integer.parseInt(topScores[i].substring(0, 2)) || Integer.parseInt(topScores[i].substring(0, 2)) == 0)
			{
				highScore = "HIGH SCORES";
				for (int j = 1 - i; j >= 0; j--)
					topScores[j + 1] = topScores[j];
				topScores[i] = "";
				if (seconds < 10)
					topScores[i] += "0";
				topScores[i] += seconds + "." + milliseconds;
				if (milliseconds % 10 == 0)
					topScores[i] += "0";
				break;
			}
			else if (milliseconds < Integer.parseInt(topScores[i].substring(3, 6)) && seconds == Integer.parseInt(topScores[i].substring(0, 2)))
			{
				highScore = "HIGH SCORES";
				for (int j = 1 - i; j >= 0; j--)
					topScores[j + 1] = topScores[j];
				topScores[i] = "";
				if (seconds < 10)
					topScores[i] += "0";
				topScores[i] += seconds + "." + milliseconds;
				if (milliseconds % 10 == 0)
					topScores[i] += "0";
				break;
			}
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("data/High Scores/" + fileNumber + ".txt", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < topScores.length; i++)
		{
			writer.println("(" + topScores[i] + "," + accuracy + ")");
		}
		writer.close();

		topScoreLabels = new String[3];
		topScoreLabels[0] = "1";
		topScoreLabels[1] = "2";
		topScoreLabels[2] = "3";
		
		topScoreBoxes = new RoundedRectangle[3];
		topScoreBoxes[0] = new RoundedRectangle(windowWidth * 1/10, windowHeight * 3/10, windowWidth * 1/5, windowHeight * 1/10, 8);
		topScoreBoxes[1] = new RoundedRectangle(windowWidth * 4/10, windowHeight * 3/10, windowWidth * 1/5, windowHeight * 1/10, 8);
		topScoreBoxes[2] = new RoundedRectangle(windowWidth * 7/10, windowHeight * 3/10, windowWidth * 1/5, windowHeight * 1/10, 8);
	
		scoreBox = new RoundedRectangle(windowWidth * 4/10, windowHeight * 1/10, windowWidth * 1/5, windowHeight * 1/10, 8);
		
		typedBox = new RoundedRectangle(windowWidth * 4/10, windowHeight * 6/10, windowWidth * 1/5, windowHeight * 1/10, 8);
	
		menuBox = new RoundedRectangle(windowWidth * 4/10, windowHeight * 8/10, windowWidth * 1/5, windowHeight * 1/10, 8);
		menuBoxColor = new Color(67, 181, 84);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		g.setColor(new Color(193,194,193));
		g.fillRect(0, 0, windowWidth, windowHeight);
		
		g.setColor(new Color(22, 139, 168));
		
		for (int i = 0; i < topScoreBoxes.length; i++)
		{
			g.fill(topScoreBoxes[i]);
			wordFont.drawString(topScoreBoxes[i].getX() + topScoreBoxes[i].getWidth()/2 - wordFont.getWidth(topScores[i])/2, topScoreBoxes[i].getY() + topScoreBoxes[i].getHeight()/2 - wordFont.getHeight(topScores[i])/2, topScores[i], Color.black);
			wordFont.drawString(topScoreBoxes[i].getX() + topScoreBoxes[i].getWidth()/2 - wordFont.getWidth(topScoreLabels[i])/2, topScoreBoxes[i].getY() - topScoreBoxes[i].getHeight()/2 - wordFont.getHeight(topScoreLabels[i])/2, topScoreLabels[i], Color.black);
		}
		
		g.setColor(new Color(217, 46, 46));
		
		g.fill(scoreBox);
		wordFont.drawString(scoreBox.getX() + scoreBox.getWidth()/2 - wordFont.getWidth(seconds + "." + milliseconds)/2, scoreBox.getY() + scoreBox.getHeight()/2 - wordFont.getHeight(highScore)/2, seconds + "." + milliseconds, Color.black);
		wordFont.drawString(scoreBox.getX() + scoreBox.getWidth()/2 - wordFont.getWidth(highScore)/2, scoreBox.getY() - scoreBox.getHeight()/2 - wordFont.getHeight(highScore)/2, highScore, Color.black);
		
		g.setColor(typedBoxColor);
		g.fill(typedBox);
		wordFont.drawString(typedBox.getX() + typedBox.getWidth()/2 - wordFont.getWidth(typed)/2, typedBox.getY() + typedBox.getHeight()/2 - wordFont.getHeight(typed)/2, typed, Color.black);
		
		g.setColor(menuBoxColor);
		g.fill(menuBox);
		wordFont.drawString(menuBox.getX() + menuBox.getWidth()/2 - wordFont.getWidth("Menu")/2, menuBox.getY() + menuBox.getHeight()/2 - wordFont.getHeight("Menu")/2, "Menu", Color.black);
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
		
		if (typed.equals("Menu"))
		{
			typedBoxColor = menuBoxColor;
			if (input.isKeyPressed(Input.KEY_ENTER))
			{
				state.enterState(0);
			}
		}
	}

	@Override
	public int getID()
	{
		return 3;
	}
	
}
