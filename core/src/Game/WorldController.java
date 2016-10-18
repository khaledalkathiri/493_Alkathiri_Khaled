/**
 * Game Development 
 * @author khaledalkathiri
 */

package Game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.Game;
//for using keys 
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import Objects.BunnyHead;
import Objects.Feather;
import Objects.GoldCoin;
import Objects.Rock;
import Objects.BunnyHead.JUMP_STATE;
import Screens.MenuScreen;
import Utilities.CameraHelper;
import Utilities.Constants;

import com.badlogic.gdx.math.Rectangle;



public class WorldController  extends InputAdapter
{
	//public Sprite[] testSprites;
	//public int selectedSprite;
	public CameraHelper cameraHelper;


	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();


	public float livesVisual;
	public float scoreVisual;
	
	public Level level;
	public int lives;
	public int score;
	private void initLevel () 
	{
		score = 0;
		scoreVisual = score;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.bunnyHead);
	}


	private Game game;
	private void backToMenu () 
	{
		// switch to menu screen
		game.setScreen(new MenuScreen(game));
	}
	


	private float timeLeftGameOverDelay;

	private static final String TAG = WorldController.class.getName();

	public WorldController(Game game)
	{
		this.game= game;
		init();
	}

	private void init()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		//initTestObjects();
		lives = Constants.LIVES_START;
		   livesVisual = lives;
		timeLeftGameOverDelay = 0;
		initLevel();
	}


	public void update (float deltaTime)
	{
		handleDebugInput(deltaTime);

		if (isGameOver()) 
		{
			timeLeftGameOverDelay -= deltaTime;

			if (timeLeftGameOverDelay < 0) 
				backToMenu();
		} else 
		{
			handleInputGame(deltaTime);
		}
		level.update(deltaTime);
		testCollisions();

		cameraHelper.update(deltaTime);
		if (!isGameOver() && isPlayerInWater()) 
		{
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

	private void handleDebugInput(float deltaTime) 
	{
		if(Gdx.app.getType() != ApplicationType.Desktop)
			return;



		if (!cameraHelper.hasTarget(level.bunnyHead)) 
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

	private void moveCamera(float x, float y)
	{
		x+= cameraHelper.getPosition().x;
		y+= cameraHelper.getPosition().y;
		cameraHelper.setPosition(x,y);
	}


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
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.bunnyHead);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		// Back to Menu
		else if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
		{
			backToMenu();
		}
		return false;
	}



	private void onCollisionBunnyHeadWithRock(Rock rock) 
	{
		BunnyHead bunnyHead = level.bunnyHead;

		float heightDifference = Math.abs(bunnyHead.position.y - (  rock.position.y + rock.bounds.height));
		if (heightDifference > 0.25f) 
		{
			boolean hitRightEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
			if (hitRightEdge)
			{
				bunnyHead.position.x = rock.position.x + rock.bounds.width;
			} else {
				bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
			}
			return; 
		}
		switch (bunnyHead.jumpState)
		{
		case GROUNDED:
			break;
		case FALLING:
		case JUMP_FALLING:
			bunnyHead.position.y = rock.position.y +bunnyHead.bounds.height  + bunnyHead.origin.y;
			bunnyHead.jumpState = JUMP_STATE.GROUNDED;
			break;
		case JUMP_RISING:
			bunnyHead.position.y = rock.position.y +bunnyHead.bounds.height + bunnyHead.origin.y;
			break; 
		}
	}

	private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin) 
	{
		goldcoin.collected = true;
		score += goldcoin.getScore();
		Gdx.app.log(TAG, "Gold coin collected");

	}

	private void onCollisionBunnyWithFeather(Feather feather) 
	{
		feather.collected = true;
		score += feather.getScore();
		level.bunnyHead.setFeatherPowerup(true);
		Gdx.app.log(TAG, "Feather collected");
	}

	private void testCollisions () 
	{
		r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y,level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);

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
		for (GoldCoin goldcoin : level.goldcoins)
		{
			if (goldcoin.collected) 
				continue;
			r2.set(goldcoin.position.x, goldcoin.position.y,goldcoin.bounds.width, goldcoin.bounds.height);

			if (!r1.overlaps(r2)) 
				continue;
			onCollisionBunnyWithGoldCoin(goldcoin);
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
	}


	private void handleInputGame (float deltaTime) 
	{
		if (cameraHelper.hasTarget(level.bunnyHead)) 
		{
			// Player Movement
			if (Gdx.input.isKeyPressed(Keys.LEFT))
			{
				level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
			} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) 
			{
				level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
			} else {

				// Execute auto-forward movement on non-desktop platform
				if (Gdx.app.getType() != ApplicationType.Desktop) 
				{
					level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
				}
			}

			// Bunny Jump
			if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) 
			{
				level.bunnyHead.setJumping(true);
			} else 
			{
				level.bunnyHead.setJumping(false);
			}
		} 
	}


	public boolean isGameOver () 
	{
		return lives < 0;
	}

	public boolean isPlayerInWater () 
	{
		return level.bunnyHead.position.y < -5;
	}


}
