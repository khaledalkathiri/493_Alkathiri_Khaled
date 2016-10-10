package Objects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Game.AbstractGameObject;;

public class Trees extends AbstractGameObject
{

	private TextureRegion regPalmTree;
	private float length;
	
	
	public Trees (float length) 
	{
		this.length = length;
		init();
	}
	
	
	private void init () 
	{
		dimension.set(1.5f, 1.5f);

		//dimension.set(length * 10, 3);
		regPalmTree = Assets.instance.levelDecoration.twoTrees;
		origin.x = -dimension.x /2;
		//origin.x = -dimension.x / 2;
	}
	
	
	
	@Override
	public void render (SpriteBatch batch) 
	{
		TextureRegion reg = null;
		reg = regPalmTree;
		
		batch.draw(reg.getTexture(), position.x + origin.x, position.y
				+ origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,
				scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(), false, false);
	} 

}
