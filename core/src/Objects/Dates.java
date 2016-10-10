package Objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Game.AbstractGameObject;


public class Dates extends AbstractGameObject
{

	private TextureRegion regDates;
	public boolean collected;
	public Dates () 
	{
		init();
	}
	
	
	private void init () 
	{
		dimension.set(0.5f, 0.5f);
		regDates = Assets.instance.dates.dates;
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		collected = false;
	}
	
	@Override
	public void render (SpriteBatch batch) 
	{
		if (collected) return;
		TextureRegion reg = null;
		reg = regDates;
		batch.draw(reg.getTexture(), position.x, position.y,
				origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y,
				rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	}
	
	
	public int getScore() 
	{
		return 100;
	}
}