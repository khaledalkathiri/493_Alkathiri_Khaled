package Game;

import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
//for using keys 
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import Objects.Assets;
import Objects.Dates;
import Objects.Farmer;
import Objects.Feather;
import Objects.Fire;
import Objects.Rock;
import Objects.Farmer.JUMP_STATE;
import Utilities.AudioManager;
import Utilities.CameraHelper;
import Utilities.Constants;

import com.badlogic.gdx.math.Rectangle;
import Screens.MenuScreen;
import Utilities.CameraHelper;
import Utilities.Constants;

public class WorldController  extends InputAdapter
{

	public CameraHelper cameraHelper;
	private float timeLeftGameOverDelay;
	
	ArrayList <Integer> scores = new ArrayList<Integer>();

	
	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	
	public float livesVisual;
	public float scoreVisual;
	

	private static final String TAG = WorldController.class.getName();

	public Level level;
	public int lives;
	public int score;
	private Game game;


	/**
	 * 
	 */
	private void initLevel () 
	{
		score = 0;
		scoreVisual = score;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.farmer);

	}

	
	/**
	 * 
	 */
	private void backToMenu () 
	{
		// switch to menu screen
		game.setScreen(new MenuScreen(game));
	}



	/**
	 * 
	 * @param game
	 */
	public WorldController(Game game)
	{
		this.game= game;
		init();
	}

	
	/**
	 * 
	 */
	private void init()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		livesVisual = lives;
		timeLeftGameOverDelay = 0;
		initLevel();
	}


	
	/**
	 * 
	 * @param deltaTime
	 */
	public void update (float deltaTime)
	{
		handleDebugInput(deltaTime);

		if (isGameOver()) 
		{
			timeLeftGameOverDelay -= deltaTime;
			if (timeLeftGameOverDelay < 0) 
				init();
		} else 
		{
			handleInputGame(deltaTime);
		}

		level.update(deltaTime);
		testCollisions();

		cameraHelper.update(deltaTime);
		if (!isGameOver() && isPlayerInWater()) 
		{
		    AudioManager.instance.play(Assets.instance.sounds.liveLost);
			lives--;
			if (isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
		
		level.mountains.updateScrollPosition (cameraHelper.getPosition());
		
		if (livesVisual> lives)
			livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);
		
		if (scoreVisual< score)
			   scoreVisual = Math.min(score, scoreVisual + 250 * deltaTime);
	}
	
	

	/**
	 * 
	 * @param deltaTime
	 */
	private void handleDebugInput(float deltaTime) 
	{
		if(Gdx.app.getType() != ApplicationType.Desktop)
			return;


		if (!cameraHelper.hasTarget(level.farmer)) 
		{
			//camera Contrls (move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor =5;

			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
				camMoveSpeed *= camMoveSpeedAccelerationFactor;

			if(Gdx.input.isKeyPressed(Keys.LEFT))
				moveCamera(- camMoveSpeed,0);

			if(Gdx.input.isKeyPressed(Keys.RIGHT))
				moveCamera(camMoveSpeed,0);

			if(Gdx.input.isKeyPressed(Keys.UP))
				moveCamera(0, camMoveSpeed);		

			if(Gdx.input.isKeyPressed(Keys.DOWN))
				moveCamera(0,-camMoveSpeed);

			if(Gdx.input.isKeyPressed(Keys.BACKSPACE))
				cameraHelper.setPosition(0,0);
		}


		//camera control (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;

		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			camZoomSpeed *= camZoomSpeedAccelerationFactor;

		if(Gdx.input.isKeyPressed(Keys.COMMA))
			cameraHelper.addZoom(camZoomSpeed);

		if(Gdx.input.isKeyPressed(Keys.PERIOD))
			cameraHelper.addZoom(-camZoomSpeed);

		if(Gdx.input.isKeyPressed(Keys.SLASH))
			cameraHelper.setZoom(1);


	}

	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	private void moveCamera(float x, float y)
	{
		x+= cameraHelper.getPosition().x;
		y+= cameraHelper.getPosition().y;
		cameraHelper.setPosition(x,y);
	}


/**
 * 
 */
	@Override
	public boolean keyUp (int keycode)
	{
		//Reset game World
		if (keycode == Keys.R)
		{
			init();
			Gdx.app.debug(TAG, "Game World Resetted");

		}

		//Toggle Camera follow
		else if (keycode == Keys.ENTER)
		{
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.farmer);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}

		// Back to Menu
		else if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
		{
			backToMenu();
		}
		return false;
	}




/**
 * 
 * @param rock
 */
	private void onCollisionBunnyHeadWithRock(Rock rock) 
	{
		Farmer farmer = level.farmer;

		float heightDifference = Math.abs(farmer.position.y - (  rock.position.y + rock.bounds.height));
		if (heightDifference > 0.25f) 
		{
			boolean hitRightEdge = farmer.position.x > (rock.position.x + rock.bounds.width / 2.0f);
			if (hitRightEdge)
			{
				farmer.position.x = rock.position.x + rock.bounds.width;
			} else {
				farmer.position.x = rock.position.x - farmer.bounds.width;
			}
			return; 
		}
		switch (farmer.jumpState)
		{
		case GROUNDED:
			break;
		case FALLING:
		case JUMP_FALLING:
			farmer.position.y = rock.position.y +farmer.bounds.height  + farmer.origin.y;
			farmer.jumpState = JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			farmer.position.y = rock.position.y +farmer.bounds.height + farmer.origin.y;
			break; 
		}
	}

	
	/**
	 * 
	 * @param dates
	 */
	private void onCollisionBunnyWithGoldCoin(Dates dates) 
	{
		dates.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupCoin);
		score += dates.getScore();
		Gdx.app.log(TAG, "dates collected");

	}

	
	/**
	 * 
	 * @param feather
	 */
	private void onCollisionBunnyWithFeather(Feather feather) 
	{
		feather.collected = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
		score += feather.getScore();
		level.farmer.setFeatherPowerup(true);
		Gdx.app.log(TAG, "Feather collected");
	}
	
	
	/**
	 * 
	 * @param fire
	 */
	private void onCollisionBunnyWithFire(Fire fire) 
	{
		fire.touched = true;
		AudioManager.instance.play(Assets.instance.sounds.pickupFeather);
		score -= fire.getScore();
		Gdx.app.log(TAG, "Fire touched");
	}

	/**
	 * 
	 */
	private void testCollisions () 
	{
		r1.set(level.farmer.position.x, level.farmer.position.y,level.farmer.bounds.width, level.farmer.bounds.height);

		// Test collision: Bunny Head <-> Rocks
		for (Rock rock : level.rocks) 
		{
			r2.set(rock.position.x, rock.position.y, rock.bounds.width,rock.bounds.height);
			if (!r1.overlaps(r2)) 
				continue;
			onCollisionBunnyHeadWithRock(rock);
			// IMPORTANT: must do all collisions for valid
			// edge testing on rocks.
		}


		// Test collision: Bunny Head <-> Gold Coins
		for (Dates dates : level.dates)
		{
			if (dates.collected) 
				continue;
			r2.set(dates.position.x, dates.position.y,dates.bounds.width, dates.bounds.height);

			if (!r1.overlaps(r2)) 
				continue;
			onCollisionBunnyWithGoldCoin(dates);
			break;
		}


		// Test collision: Bunny Head <-> Feathers
		for (Feather feather : level.feathers)
		{
			if (feather.collected) 
				continue;
			r2.set(feather.position.x, feather.position.y,feather.bounds.width, feather.bounds.height);
			if (!r1.overlaps(r2)) 
				continue;
			onCollisionBunnyWithFeather(feather);
			break;
		}
		
		//test collision: Farmer <-> Fire
		for(Fire fire: level.fires)
		{
//			if(fire.touched)
//				continue;
			r2.set(fire.position.x, fire.position.y,fire.bounds.width, fire.bounds.height);
			if (!r1.overlaps(r2)) 
				continue;
			onCollisionBunnyWithFire(fire);
			break;

		}
	}


	
	/**
	 * 
	 * @param deltaTime
	 */
	private void handleInputGame (float deltaTime) 
	{
		if (cameraHelper.hasTarget(level.farmer)) 
		{
			// Player Movement
			if (Gdx.input.isKeyPressed(Keys.LEFT))
			{
				level.farmer.velocity.x = -level.farmer.terminalVelocity.x;
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) 
			{
				level.farmer.velocity.x = level.farmer.terminalVelocity.x;
			} else {

				// Execute auto-forward movement on non-desktop platform
				if (Gdx.app.getType() != ApplicationType.Desktop) 
				{
					level.farmer.velocity.x = level.farmer.terminalVelocity.x;
				}
			}

			// Bunny Jump
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) 
			{
				level.farmer.setJumping(true);
			} else 
			{
				level.farmer.setJumping(false);
			}
		} 
	}


	/**
	 * 
	 * @return
	 */
	public boolean isGameOver () 
	{
		return lives < 0;
	}

	
	/**
	 * 
	 * @return
	 */
	public boolean isPlayerInWater () 
	{
		return level.farmer.position.y < -5;
	}
	
	/**
	 * 
	 */



}
