package com.alkathirigdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;



//import com.alkathirigdx.game.AlkathiriGdxGame;
import com.alkathirigdx.game.CanyonBunnyMain;


//to import the packer and the settings
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;



public class DesktopLauncher 
{
    private static boolean rebuildAtlas = true;
    private static boolean drawDebugOutline = true;
    
    
	public static void main (String[] arg) 
	{
		if (rebuildAtlas) 
		{
            Settings settings = new Settings();
            settings.maxWidth = 1024;
            settings.maxHeight = 1024;
            settings.duplicatePadding = false;
            settings.debug = drawDebugOutline;
            TexturePacker.process(settings, "assets-raw/images", "../CanyonBunny-core/assets/images",
  "canyonbunny.pack");
}

		
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new CanyonBunnyMain(), config);
	}
}
