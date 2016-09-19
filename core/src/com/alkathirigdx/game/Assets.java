package com.alkathirigdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.alkathirigdx.game.Constants;


//to import atlasRegion
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;


public class Assets implements Disposable, AssetErrorListener
{
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	private AssetManager assetManager;


//	public AssetBunny bunny;
//	public AssetRock rock;
//	public AssetGoldCoin goldCoin;
//	public AssetFeather feather;
//	public AssetLevelDecoration levelDecoration;
	
	
	public AssetFarmer farmer;
	public AssetRock rock;
	public AssetFeather feather;
	public AssetLevelDecoration levelDecoration;
	public AssetTree palmTree;
	public AssetSnake snakes;
	public AssetHouse house;

	// singleton: prevent instantiation from other classes
	private Assets () 
	{

	}

	

	public void init (AssetManager assetManager) 
	{
		this.assetManager = assetManager;

		// set asset manager error handler
		assetManager.setErrorListener(this);

		// load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);

		// start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);

		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);
		
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		
		// enable texture filtering for pixel smoothing
		for (Texture t : atlas.getTextures()) 
		{
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
//		// create game resource objects
//		bunny = new AssetBunny(atlas);
//		rock = new AssetRock(atlas);
//		goldCoin = new AssetGoldCoin(atlas);
//		feather = new AssetFeather(atlas);
//		levelDecoration = new AssetLevelDecoration(atlas);
		
		// create game resource objects
		farmer = new AssetFarmer(atlas);
		rock = new AssetRock(atlas);
		feather = new AssetFeather(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
		palmTree = new AssetTree(atlas);
		snakes = new AssetSnake(atlas);
		house = new AssetHouse(atlas);
	}

	
	

	@Override
	public void dispose () 
	{
		assetManager.dispose();
	}



	@Override
	public void error(AssetDescriptor asset, Throwable throwable) 
	{
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
	}



	public class AssetFarmer
	{
		public final AtlasRegion farmer;
		public AssetFarmer (TextureAtlas atlas) 
		{
			farmer = atlas.findRegion("farmer");
		}
	}

	public class AssetRock 
	{
		public final AtlasRegion leftEdge;
		public final AtlasRegion rightEdge;
		public final AtlasRegion middle;
		
		public AssetRock (TextureAtlas atlas) 
		{
			leftEdge = atlas.findRegion("leftEdge");
			rightEdge = atlas.findRegion("rightEdge");
			middle = atlas.findRegion("rock_middle");
		}
	}
	
	public class AssetTree
	{
		public final AtlasRegion singleTree;
		public final AtlasRegion twoTrees;
		
		public AssetTree(TextureAtlas atlas)
		{
			singleTree = atlas.findRegion("singleTree");
			twoTrees = atlas.findRegion("PalmTree");
		}
	}

	public class AssetSnake 
	{
		public final AtlasRegion snake;
		public AssetSnake (TextureAtlas atlas) 
		{
			snake = atlas.findRegion("snake");
		}
	}

	public class AssetFeather 
	{
		public final AtlasRegion feather;
		
		public AssetFeather (TextureAtlas atlas)
		{
			feather = atlas.findRegion("feather");
		}
	}
	
	public class AssetHouse
	{
		public final AtlasRegion house;
		
		public AssetHouse(TextureAtlas atlas)
		{
			house = atlas.findRegion("house");
		}
	}


	public class AssetLevelDecoration 
	{
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public AssetLevelDecoration (TextureAtlas atlas) 
		{
			cloud01 = atlas.findRegion("cloud01");
			cloud02 = atlas.findRegion("cloud02");
			cloud03 = atlas.findRegion("cloud03");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");
		}
	}



}




