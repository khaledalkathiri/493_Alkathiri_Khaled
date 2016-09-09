package com.alkathirigdx.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;


import com.badlogic.gdx.InputAdapter;



public class WorldController  extends InputAdapter
{
	public Sprite[] testSprites;
	public int selectedSprite;
	
	private static final String TAG = WorldController.class.getName();
	
	public WorldController()
	{
		init();
	}
	
	private void init()
	{
		Gdx.input.setInputProcessor(this);
		initTestObjects();
	}
	
	private void initTestObjects() 
	{
		//Create new array for 5 sprites
		testSprites = new Sprite[5];
		
		//create empty POT-sized Pixmap width 8 bit RGBA pixel data 
		int width = 32;
		int height = 32;
		Pixmap pixmap = createProceduralPixmap(width, height);
		
		//create a new texture form pixmap data
		Texture texture = new Texture(pixmap);
		
		//create new sprites using the just created textrue
		for (int i=0; i< testSprites.length; i++)
		{
			Sprite spr = new Sprite(texture);
			
			//Define sprite size to be 1m x 1m in game world
			spr.setSize(1, 1);
			
			//set orighin to sprite's center
			spr.setOrigin(spr.getWidth()/2.0f, spr.getHeight()/2.0f);
			
			//calculate random positon for sprite
			float randomX = MathUtils.random(-2.0f, 2.0f);
			float randomY = MathUtils.random(-2.0f, 2.0f);
			spr.setPosition(randomX, randomY);
			
			//put new sprite into array 
			testSprites[i] = spr;
			

		}
		
		//set first sprite as selected one
		selectedSprite = 0;
	}

	private Pixmap createProceduralPixmap(int width, int height) 
	{
		Pixmap pixmap = new Pixmap(width,height, Format.RGBA8888);
		
		//Fill suare with red color at 50% opacity
		pixmap.setColor(1,0,0,0.5f);
		pixmap.fill();
		
		//Draw a yellow-Colored x shape on squre 
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);
		
		//Draw a cyan-colored border around square 
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		
		
		
		return pixmap;
	}

	public void update (float deltaTime)
	{
		handleDebugInput(deltaTime);
		updateTestObjects(deltaTime);
	}

	private void handleDebugInput(float deltaTime) 
	{
		if(Gdx.app.getType() != ApplicationType.Desktop)
			return;
		
		//selected sprite controls
		float sprMoveSpeed = 5 * deltaTime;
		if ( Gdx.input.isKeyPressed(Keys.A))
			MoveSelectedSprites(-sprMoveSpeed, 0);
		
		if ( Gdx.input.isKeyPressed(Keys.D))
			MoveSelectedSprites(sprMoveSpeed, 0);
		
		if ( Gdx.input.isKeyPressed(Keys.W))
			MoveSelectedSprites(0, sprMoveSpeed);
		
		if ( Gdx.input.isKeyPressed(Keys.S))
			MoveSelectedSprites(0, -sprMoveSpeed);
	

	}

	private void MoveSelectedSprites(float x, float y) 
	{
		testSprites[selectedSprite].translate(x, y);
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
		
		//Select next sprite
		else if (keycode == Keys.SPACE)
		{
			selectedSprite = (selectedSprite + 1)% testSprites.length;
			Gdx.app.debug(TAG, "Sprite #" + selectedSprite + "selected");
			
		}
		return false;
	}
	
	
	
	private void updateTestObjects(float deltaTime)
	{
		//get current rotation form selected sprite
		float rotation = testSprites[selectedSprite].getRotation();
		
		//Rotate sprite by 90 degree per sec
		rotation += 90 * deltaTime;
		
		//Wrap around at 360 degrees 
		rotation %=360;
		
		//set new rotation value ot selected sprite 
		testSprites[selectedSprite].setRotation(rotation);
	}
}
