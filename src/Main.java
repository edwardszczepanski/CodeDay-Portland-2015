import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame
{
	GamePath filePath = new GamePath();
	
	public Main(String title)
	{
        super(title);
    }
	
    public static void main(String[] args) throws SlickException
    {
    	AppGameContainer app = new AppGameContainer(new Main("Coder Typer"));
        
        app.setDisplayMode(1280, 720, false);
        app.setTitle("Full Circle");
        app.setTargetFrameRate(60);
        app.setShowFPS(false);
        app.setVSync(true);
        app.start();
    }
 
    @Override
    public void initStatesList(GameContainer container) throws SlickException
    {
    	this.addState(new Menu());
    	this.addState(new Selection(filePath));
    	this.addState(new Game(filePath));
    	this.addState(new Score());
    	this.addState(new HighScore());
    }
}
