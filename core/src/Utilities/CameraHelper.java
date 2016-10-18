/**
 * Game Development 
 * @author khaledalkathiri
 */

package Utilities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import Game.AbstractGameObject;

import com.badlogic.gdx.math.MathUtils;

public class CameraHelper 
{

	private static final String TAG = CameraHelper.class.getName();
	private final float MAX_ZOOM_IN = 0.25f;
	private final float MAX_ZOOM_OUT = 10.0f;
	private Vector2 position;
	private float zoom;

	private AbstractGameObject target;

	private final float FOLLOW_SPEED = 4.0f;


	public CameraHelper()
	{
		position = new Vector2();
		zoom = 1.0f;
	}



	public void update(float deltaTime) 
	{
		if (!hasTarget())
			return;

		//position.x = target.getX()+ target.getOriginX();
		//position.y = target.getY()+ target.getOriginY();

		position.x = target.position.x + target.origin.x;
		position.y = target.position.y + target.origin.y;

		position.lerp(target.position, FOLLOW_SPEED * deltaTime);
		   
		// Prevent camera from moving down too far
		position.y = Math.max(-1f, position.y);

	}

	public void setPosition(float x, float y) 
	{
		this.position.set(x,y);
	}

	public Vector2 getPosition()
	{
		return position;
	}


	public void addZoom(float amount) 
	{
		setZoom(zoom + amount);
	}

	public void setZoom(float zoom)
	{
		this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);

	}

	public float getZoom()
	{
		return zoom;
	}

	public void setTarget(AbstractGameObject target) 
	{
		this.target = target;
	}

	public AbstractGameObject getTarget()
	{
		return target;
	}


	//Page 185
	public boolean hasTarget(AbstractGameObject target) //(Sprite target) 
	{
		return hasTarget() && this.target.equals(target);

	}

	public boolean hasTarget()
	{
		return target !=null;
	}


	public void applyTo(OrthographicCamera camera)
	{
		camera.position.x = position.x;
		camera.position.y = position.y;
		camera.zoom = zoom;
		camera.update();
	}


}
