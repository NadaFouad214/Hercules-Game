package sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import hercules.game.main;
import screens.Level1;
import screens.PlayScreen;

public class WoodenEnemy extends Enemy {
	
	private float stateTime;
	private Animation<TextureRegion> walkAnimation;
	private Array<TextureRegion> frames;
	public boolean setToDestroy;
    public boolean destroyed;
    public int hit_num = 3;
    
	public WoodenEnemy (PlayScreen screen, float x, float y) {
		super(screen,x,y);
		frames=new Array<TextureRegion>();
		for(int i=0;i<4;i++)
		{
			
			frames.add(new TextureRegion (screen.getWoodenEnemyAtlas().findRegion("wooden monster"), i*48,-5,48,85));
			walkAnimation=new Animation(0.1f,frames);
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
			setRegion(new TextureRegion (screen.getWoodenEnemyAtlas().findRegion("wooden monster"),48,-5,48,85));
			stateTime = 0 ;
			destroyed = true ;
			main.woodenMove_sound.play();
		}
		else if (!destroyed) {
			b2body.setLinearVelocity(velocity);
			setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
	        setRegion(walkAnimation.getKeyFrame(stateTime,true));
	        if(hit_num <= 0) {
				setToDestroy = true;
			}
		}
		
		
	     
	}
	@Override
	public void defineEnemy(){
		
		BodyDef bdef=new BodyDef ();
		bdef.position.set(getX(),getY());
		bdef.type=BodyDef.BodyType.DynamicBody;
	    b2body=world.createBody(bdef);
		FixtureDef fdef= new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.3f, 0.2f);
		
		//for collision
		fdef.filter.categoryBits = main.WOODEN_ENEMY_BIT;
		fdef.filter.maskBits = main.GROUND_BIT|main.HERCLUES_BIT|main.WOODEN_ENEMY_BIT;
		
		fdef.shape=shape;
		b2body.createFixture(fdef).setUserData(this);
		
		
			
	}
	
}