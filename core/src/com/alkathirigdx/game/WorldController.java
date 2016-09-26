package com.alkathirigdx.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

//for using keys 
import com.badlogic.gdx.InputAdapter;

import com.alkathirigdx.game.Rock;
import com.alkathirigdx.game.Constants;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class WorldController  extends InputAdapter
{

	public CameraHelper cameraHelper;

	
	private static final String TAG = WorldController.class.getName();
	
	public Level level;
	public int lives;
	public int score;
	private void initLevel () 
	{
		score = 0;
		level = new Level(Constants.LEVEL_01);
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
		initLevel();
	}


	public void update (float deltaTime)
	{
		handleDebugInput(deltaTime);
		cameraHelper.update(deltaTime);
	}

	private void handleDebugInput(float deltaTime) 
	{
		if(Gdx.app.getType() != ApplicationType.Desktop)
			return;
		
		
		
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
		
		return false;
	}
	
}
