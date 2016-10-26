/**
 * Game Development 
 * @author khaledalkathiri
 */

package Game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import Objects.Assets;
import Screens.MenuScreen;
import Utilities.AudioManager;
import Utilities.GamePreferences;

//for testing the assets
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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


//
//
//public class CanyonBunnyMain implements ApplicationListener
//{
//	private static final String TAG = CanyonBunnyMain.class.getName();
//	
//	private WorldController worldController;
//	private WorldRenderer worldRenderer;
//	
//	private boolean paused;
//
//
//	@Override
//	public void create()
//	{
//		
//		//set Libgdx log level to DEBUG
//		Gdx.app.setLogLevel(Application.LOG_DEBUG);
//		
//		// Load assets
//		Assets.instance.init(new AssetManager());
//		
//		//initialize controller and renderer
//		worldController = new WorldController();
//		worldRenderer = new WorldRenderer(worldController);
//
//		//game world is active on start
//		paused = false;
//	}
//
//	@Override
//	public void resize(int width, int height) 
//	{
//		
//		worldRenderer.resize(width, height);
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void render()
//	{
//		//Do not update game woeld when paused.
//		if(!paused)
//		{
//			
//		
//			//update game world by the time that has passed since last rendered frame
//			worldController.update(Gdx.graphics.getDeltaTime());
//		}
//		
//		//sets the clear screen color to: Cornflower Blue
//		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
//		
//		//clears the screen
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		
//		//Render game world to screen
//		worldRenderer.render();
//		
//		
//		
//		
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void pause() 
//	{
//		paused = true;
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void resume() 
//	{
//		paused = false;
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void dispose() 
//	{
//		worldRenderer.dispose();
//	    Assets.instance.dispose();
//	}
//
//}
