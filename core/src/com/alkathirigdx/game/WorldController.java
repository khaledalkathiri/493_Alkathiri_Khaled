package com.alkathirigdx.game;

import com.badlogic.gdx.Application.ApplicationType;
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

import com.alkathirigdx.game.Rock;
//import com.alkathirigdx.game.Farmer.JUMP_STATE;

import com.alkathirigdx.game.Constants;
import com.alkathirigdx.game.Farmer.JUMP_STATE;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;


public class WorldController  extends InputAdapter
{

	public CameraHelper cameraHelper;
	private float timeLeftGameOverDelay;


	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();

	private static final String TAG = WorldController.class.getName();

	public Level level;
	public int lives;
	public int score;


	private void initLevel () 
	{
		score = 0;
		level = new Level(Constants.LEVEL_01);
		cameraHelper.setTarget(level.farmer);

	}



	public WorldController()
	{
		init();
	}

	private void init()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START; 
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
			lives--;
			if (isGameOver())
				timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
			else
				initLevel();
		}
	}

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
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.farmer);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		return false;

	}





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

	private void onCollisionBunnyWithGoldCoin(Dates dates) 
	{
		dates.collected = true;
		score += dates.getScore();
		Gdx.app.log(TAG, "dates collected");

	}

	private void onCollisionBunnyWithFeather(Feather feather) 
	{
		feather.collected = true;
		score += feather.getScore();
		level.farmer.setFeatherPowerup(true);
		Gdx.app.log(TAG, "Feather collected");
	}

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
	}


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


	public boolean isGameOver () 
	{
		return lives < 0;
	}

	public boolean isPlayerInWater () 
	{
		return level.farmer.position.y < -5;
	}


}
