package com.alkathirigdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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


	public AssetFonts fonts;

	
	
	public AssetFarmer farmer;
	public AssetRock rock;
	public AssetFeather feather;
	public AssetDates dates;
	public AssetLevelDecoration levelDecoration;
	//public AssetTree palmTree;
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
		

		
		// create game resource objects
	    fonts = new AssetFonts();

		farmer = new AssetFarmer(atlas);
		rock = new AssetRock(atlas);
		dates = new AssetDates(atlas);
		feather = new AssetFeather(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
		//palmTree = new AssetTree(atlas);
		snakes = new AssetSnake(atlas);
		house = new AssetHouse(atlas);
	}

	
	

	@Override
	public void dispose () 
	{
		assetManager.dispose();
		fonts.defaultSmall.dispose();
	    fonts.defaultNormal.dispose();
	    fonts.defaultBig.dispose();
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
		//public final AtlasRegion rightEdge;
		public final AtlasRegion middle;
		
		public AssetRock (TextureAtlas atlas) 
		{
			leftEdge = atlas.findRegion("leftEdge");
			//rightEdge = atlas.findRegion("rightEdge");
			middle = atlas.findRegion("middle");
		}
	}
	
	public class AssetDates 
	{
		public final AtlasRegion dates;
		public AssetDates (TextureAtlas atlas) 
		{
			dates = atlas.findRegion("dates");
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

//	public class AssetTree
//	{
//		public final AtlasRegion singleTree;
//		public final AtlasRegion twoTrees;
//		
//		public AssetTree(TextureAtlas atlas)
//		{
//			singleTree = atlas.findRegion("singleTree");
//			twoTrees = atlas.findRegion("PalmTree");
//		}
//	}


	public class AssetLevelDecoration 
	{
		public final AtlasRegion twoTrees;
		public final AtlasRegion cloud01;
		public final AtlasRegion cloud02;
		public final AtlasRegion cloud03;
		public final AtlasRegion mountainLeft;
		public final AtlasRegion mountainRight;
		public final AtlasRegion waterOverlay;

		public AssetLevelDecoration (TextureAtlas atlas) 
		{
			cloud01 = atlas.findRegion("cloud01");
			cloud02 = atlas.findRegion("cloud02");
			cloud03 = atlas.findRegion("cloud03");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");
			twoTrees = atlas.findRegion("PalmTree");
			waterOverlay = atlas.findRegion("water_overlay");

		}
	}

	
	public class AssetFonts 
	{
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		
		public AssetFonts ()
		{
			// create three fonts using Libgdx's 15px bitmap font
			defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			
			
			// set font sizes
			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(1.0f);
			defaultBig.getData().setScale(2.0f);
			
			
			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		} 
	}


}




