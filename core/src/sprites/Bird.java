package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

import hercules.game.main;
import screens.PlayScreen;

public class Bird extends Enemy {

	private float stateTime;
	private Animation<TextureRegion> walkAnimation;
	private Array<TextureRegion> frames; 
	private boolean setToDestroy;
    private boolean destroyed;
    
	public Bird(PlayScreen screen, float x, float y) {
		
		super(screen, x, y);
		frames=new Array<TextureRegion>();
		for(int i=0;i<5;i++)
		{
			
			frames.add(new TextureRegion (screen.getBirdAtlas().findRegion("bird"),i*113,1,113,150));
			walkAnimation=new Animation(0.2f,frames);
			stateTime=0;
			setBounds(getX(),getY(), 80/main.PPM ,80/main.PPM);
			setToDestroy=false;
			destroyed=false;
		}
		
	}
	
	public void update (float dt)
	{
		stateTime+=dt;
		if(setToDestroy && ! destroyed) {
			world.destroyBody(b2body);
			destroyed = true ;
			setRegion(new TextureRegion (screen.getBirdAtlas().findRegion("Birdr1"),32,1,150,210));

			stateTime = 0 ;
		}
		
		else if (!destroyed) {
			b2body.setLinearVelocity(velocity);
			setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
			setRegion(walkAnimation.getKeyFrame(stateTime,true));
		
		}
	     
	}
   
	@Override
	protected void defineEnemy() {
		BodyDef bdef=new BodyDef ();
		bdef.position.set(getX(),getY());
		bdef.type=BodyDef.BodyType.KinematicBody;
	    b2body=world.createBody(bdef);
		
		FixtureDef fdef= new FixtureDef();
		CircleShape shape= new CircleShape();
		shape.setRadius(5/main.PPM);
		
		//for collision
		fdef.filter.categoryBits = main.BIRD_BIT;
		fdef.filter.maskBits = main.LEFT_WALL_BIT|main.HERCLUES_BIT|main.BIRD_BIT|main.RIGHT_WALL_BIT;
		
		fdef.shape=shape;
		b2body.createFixture(fdef); 
	}
	
}