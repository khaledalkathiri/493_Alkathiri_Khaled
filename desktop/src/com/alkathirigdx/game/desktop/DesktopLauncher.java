/**
 * Game Development 
 * @author khaledalkathiri
 */

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
	
	//set it to true to rebuild a new atlas
    private static boolean rebuildAtlas = false;
    
    //when you change it make sure you rebuild a new atlas
    private static boolean drawDebugOutline = false;
    
    
	public static void main (String[] arg) 
	{
		if (rebuildAtlas) 
		{
            Settings settings = new Settings();
            settings.maxWidth = 1024;
            settings.maxHeight = 1024;
            settings.duplicatePadding = false;
            settings.debug = drawDebugOutline;
            TexturePacker.process(settings, "assets-raw/images", "../core/assets/images",
  "canyonbunny.pack");
}

		
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new CanyonBunnyMain(), config);
	}
}
