package Game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import Objects.Assets;
import Screens.MenuScreen;
import Utilities.AudioManager;
import Utilities.GamePreferences;

//for testing the assets
import com.badlogic.gdx.assets.AssetManager;


public class CanyonBunnyMain extends Game 
{
	@Override
	public void create () 
	{
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		
		// Load preferences for audio settings and start playing music
	     GamePreferences.instance.load();
	     AudioManager.instance.play(Assets.instance.music.song01);
	     
		// Start game at menu screen
		setScreen(new MenuScreen(this));
	}
}