import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

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

public class Game extends BasicGameState
{
	String typed;
	TrueTypeFont wordFont;
	ReadFile reader;
	ProgressBar progressBar;
	String[] code;
	int currentLine;
	int cursorCounter;
	boolean displayCursor;
	
	int windowWidth;
	int windowHeight;
	RoundedRectangle typingBox;
	RoundedRectangle codeBox;
	Color typingBoxColor;
	GameTimer timer;
	GameAccuracy accuracy;
	GamePath filePath;
	
	public Game(GamePath path)
	{
		filePath = path;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException
	{
		Font font = new Font("Courier", Font.BOLD, 40);
		wordFont = new TrueTypeFont(font, true);
		
		windowWidth = gc.getWidth();
		windowHeight = gc.getHeight();
		codeBox = new RoundedRectangle(windowWidth/10, windowHeight * 3/10, windowWidth * 4/5, windowHeight * 2/5, 8);
		typingBox = new RoundedRectangle(windowWidth/10, windowHeight * 8/10 , windowWidth * 4/5, windowHeight * 1/10, 8);
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame state)
	{
		reader = new ReadFile(filePath.path);
		try {
			code = reader.OpenFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		accuracy = new GameAccuracy(code);
		
		typed = "";
		currentLine = 1;
		cursorCounter = 0;
		displayCursor = false;
		
		progressBar = new ProgressBar(code, windowHeight, windowWidth);
		
		timer = new GameTimer(System.nanoTime());
	}

	public void drawBackground(Graphics g)
	{
		g.setColor(Color.gray);
		
		g.fillRect(0, 0, windowWidth, windowHeight);
		
		g.setColor(new Color(22, 139, 168));
		g.setAntiAlias(true);
		
		g.fill(codeBox);
		
		g.setColor(typingBoxColor);
		g.fill(typingBox);
	}
	
	public void drawCursor(Graphics g)
	{
		float typedStartX = typingBox.getX() + 5;
		Rectangle cursor = new Rectangle(typedStartX + wordFont.getWidth(typed) + 5, typingBox.getY() + 10, 2, wordFont.getHeight(typed));
		
		if (cursorCounter % 50 == 0)
			displayCursor = !displayCursor;
		if (displayCursor)
		{
			g.setColor(Color.black);
			g.fill(cursor);
		}
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame state, Graphics g) throws SlickException
	{
		drawBackground(g);
		
		for (int i = currentLine; i < 5 + currentLine; i++)
		{
			if (i < code.length)
				wordFont.drawString(codeBox.getX() + 10, codeBox.getY() + 10 + ((i - currentLine) * wordFont.getHeight(typed)), code[i], Color.black);
		}
			
		wordFont.drawString(typingBox.getX() + 10, typingBox.getY() + typingBox.getHeight()/2 - wordFont.getHeight(typed)/2, typed, Color.black);
		
		progressBar.draw(g);
		
		drawCursor(g);
		
		accuracy.draw(g, wordFont, codeBox.getX(), codeBox.getY() + codeBox.getHeight() + (typingBox.getY() - (codeBox.getY() + codeBox.getHeight()))/2);
		
		timer.draw(g, gc, wordFont, codeBox.getX() + codeBox.getWidth(), codeBox.getY() + codeBox.getHeight() + (typingBox.getY() - (codeBox.getY() + codeBox.getHeight()))/2);
	}
	
	@Override
	public void keyPressed(int key, char c)
	{
		if (key == Input.KEY_BACK)
		{
			if (typed.length() > 0)
			{
				if (typed.length() < code[currentLine].length())
				{
					if (typed.equals(code[currentLine].substring(0, typed.length())))
						progressBar.update(-1);
				}
				
				typed = typed.substring(0, typed.length()-1);
			}
		}
		else if (key == Input.KEY_TAB)
		{
			typed += "    ";
			
			if (typed.length() < code[currentLine].length())
			{
				if (typed.equals(code[currentLine].substring(0, typed.length())))
				{
					progressBar.update(4);
					accuracy.update(4);
				}
			}
		}
		else if (key != Input.KEY_LSHIFT && key != Input.KEY_RSHIFT && key != Input.KEY_ENTER)
		{
			typed += Character.toString(c);
			if (typed.length() < code[currentLine].length())
			{
				if (typed.equals(code[currentLine].substring(0, typed.length())))
				{
					progressBar.update(1);
					accuracy.update(1);
				}
			}
		}
		
		if (typed.length() < code[currentLine].length())
		{
			if (!typed.equals(code[currentLine].substring(0, typed.length())))
			{
				accuracy.update(0);
			}
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame state, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_ENTER))
		{
			if (typed.equals(code[currentLine]))
			{
				typed = "";
				currentLine++;
				if (currentLine == code.length)
				{
					PrintWriter writer = null;
					try {
						writer = new PrintWriter("data/recent_highscore.txt", "UTF-8");
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					writer.println(filePath.path);
					writer.println(timer.timer);
					writer.println(accuracy.accuracy);
					writer.close();
					state.enterState(3);
				}
			}
		}
		else if (currentLine < code.length)
		{
			if (typed.equals(code[currentLine]))
				typingBoxColor = new Color(67, 181, 84);
			else if (typed.length() > code[currentLine].length())
				typingBoxColor = new Color(217, 46, 46);
			else if (!typed.equals(code[currentLine].substring(0, typed.length())))
				typingBoxColor = new Color(217, 46, 46);
			else
				typingBoxColor = new Color(22, 139, 168);
		}
		
		cursorCounter++;
		
		timer.updateTime(System.nanoTime());
	}

	@Override
	public int getID()
	{
		return 1;
	}

}
