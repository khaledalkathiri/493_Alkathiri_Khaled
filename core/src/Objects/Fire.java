package Objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import Game.AbstractGameObject;

import com.badlogic.gdx.math.MathUtils;



public class Fire extends AbstractGameObject
{
	private TextureRegion regFire;
	public boolean touched;
	
	public Fire()
	{
		init();
	}
	
	private void init () 
	{
		dimension.set(0.8f, 0.8f);
		

	     setAnimation(Assets.instance.fire.animFire);
	     stateTime = MathUtils.random(0.0f, 1.0f);
	     
		
	    //regGoldCoin = Assets.instance.goldCoin.goldCoin;
		// Set bounding box for collision detection
		bounds.set(0, 0, dimension.x, dimension.y);
		touched = false;
	}
	
	
	@Override
	public void render (SpriteBatch batch) 
	{
		TextureRegion reg = null;
		

		reg = animation.getKeyFrame(stateTime, true);
		batch.draw(reg.getTexture(),position.x, position.y , origin.x, origin.y,  dimension.x, dimension.y,
				scale.x, scale.y, rotation,reg.getRegionX(), reg.getRegionY(),
				reg.getRegionWidth(), reg.getRegionHeight(),false, false);
	}
	
	
	public int getScore()
	{
		return 250;
	}
}
