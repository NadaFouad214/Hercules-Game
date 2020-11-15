package sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import javax.swing.text.Position;

import com.badlogic.gdx.ai.steer.behaviors.Jump.GravityComponentHandler;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


import screens.PlayScreen;

public abstract class Enemy extends Sprite {

	protected World world;
	protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity ;
    
	public Enemy (PlayScreen screen , float x,float y)
	{
		this.world = screen.getWorld();
		this.screen = screen;
		setPosition(x,y);
		defineEnemy();
		velocity = new Vector2(0.5f,0);
	}
	
	protected abstract void defineEnemy();
	
	/* fun take 2 parameters which
	 * x: (number) x coordinate X of the linear velocity
	 * y : (number) y coordinate Y of the linear velocity*/
	public void reverseVelocity(boolean x , boolean y)
	{ 
		if (x)
			velocity.x = -velocity.x ;
		if(y)
			velocity.y = -velocity.y ;
	}
}
