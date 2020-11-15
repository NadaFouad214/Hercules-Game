package sprites;

import java.awt.geom.RectangularShape;

import com.badlogic.gdx.Gdx;
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
import screens.Level2;
import screens.PlayScreen;

public class herclus extends Sprite{
	
public enum State {punching,jumping,standing ,runnig,super_punch,sword,die, super_sword};
public State currentState;
public State previousState; 
private float stateTimer; //time to do anything

private Animation hercRun;
private Animation hercpunch;
private Animation superpunch;
private Animation hercsword;
private Animation hercJump;
private Animation hercdie;
private Animation hercsupersword;
public static boolean runningRight;

//to calculate time of all frames for one action 
private float timeAnimation = 0;

//to check which action is applied now
public boolean herculesPunch = false; 
public boolean herculesSuperPunch = false; 
public boolean herculesSword = false; 
public boolean hercDie=false;
public boolean hercSuperSword=false;


public World world; 
public Body	b2body;
private	TextureRegion hercstand;

PlayScreen screen;


public herclus ( World world,PlayScreen screen)
{
	super(screen.getAtlas().findRegion("walk"));
	this.world=world;
	this.screen = screen;
	
	
	
	//intialize - at the start of the game => hercules is standing
	currentState = State.standing;
    previousState = State.standing;
    stateTimer=0;
    runningRight=true;
    
    //array of frames of current state
    Array<TextureRegion> frames = new Array<TextureRegion>();

    /***************sprites of walk****************/
    
     for(int j =0; j < 8; j++) {
    	  frames.add(new TextureRegion(screen.getAtlas().findRegion("walk"), j* 84,1, 85,101));     
      }
      hercRun = new Animation(0.1f, frames);
      frames.clear();
      
    
    /***************sprites of jump****************/
    
      for(int i = 0; i <8; i++)
          frames.add(new TextureRegion(screen.getAtlas().findRegion("jump"), i*84,1,79,99));
      hercJump = new Animation(0.1f, frames);
      frames.clear();
    
    /***************sprites of punch****************/
      for(int j =0; j < 7; j++) {
     	 frames.add(new TextureRegion(screen.getHerc_punchAtlas().findRegion("Hercules with punch-edit"), j* 78,0, 75,135));     
      }
      hercpunch = new Animation(0.2f, frames);
      frames.clear();
     
     /***************sprites of die****************/
     
     for(int j =0; j <3; j++) {
   	     frames.add(new TextureRegion(screen.getDieAtlas().findRegion("die2"), j*75,-40,77,110));     
     }
     hercdie = new Animation(1.2f, frames);
     frames.clear();
     
     /***************sprites of super-punch****************/
     
     for(int i = 0; i < 12; i++)
    	 frames.add(new TextureRegion(screen.getHerc_superPunchAtlas().findRegion("super punch"), i *75,-20,73,110));
     superpunch = new Animation(0.11f, frames);
     frames.clear();
     
     
     /***************sprites of sword****************/
     
     for(int j =0; j < 9; j++) {
   	  frames.add(new TextureRegion(screen.getAtlas().findRegion("Hercules with sword -edit"), j*101,1,88,98));     
     }
     hercsword = new Animation(0.4f, frames);
     frames.clear();
 
     /***************sprites of super sword****************/
     
     for(int j =0; j < 9; j++) {
       	frames.add(new TextureRegion(screen.getSuper_swordAtlas().findRegion("Hercules with super sword"), j*111,1,90,100));     
      }
      hercsupersword = new Animation(0.2f, frames);
      frames.clear();
     
    /****************/
    
	defineHerclus();
	
	//to define where should hercules stand at the start of the game 
	hercstand =new TextureRegion(getTexture(),1,193,88,101);
	setBounds(0, 0,65/main.PPM, 65/main.PPM);
	setRegion(hercstand);
	
}

/*************************************************************/

public void defineHerclus() {
	
	BodyDef bdefHerc=new BodyDef ();
	bdefHerc.position.set(65/main.PPM,65/main.PPM);
	bdefHerc.type=BodyDef.BodyType.DynamicBody;
    b2body=world.createBody(bdefHerc);
	
	FixtureDef fdefHerc= new FixtureDef();
	
	PolygonShape shape = new PolygonShape();
	shape.setAsBox(0.1f, 0.2f);
	
	//filter.categoryBits : to know who is herclues when collision happens
	fdefHerc.filter.categoryBits = main.HERCLUES_BIT;
	
	//filter.maskBits : to know which object herclues can collide with
	fdefHerc.filter.maskBits = main.COIN_BIT | main.GROUND_BIT | main.PUNCH_BAG_BIT | main.PILLAR_BIT 
								| main.BIG_STATUE_BIT | main.WOODEN_ENEMY_BIT | main.CHECK_POINT_BIT | main.FIRE_SWORD_BIT 
									| main.JUICE_BIT | main.SHIELD_BIT |main.BROWN_MAN_BIT | main.BIRD_BIT; 
	
	fdefHerc.shape=shape;
	b2body.createFixture(fdefHerc);
	
	//sensor for right side of hercules 
	EdgeShape rightSide = new EdgeShape();
	rightSide.set(new Vector2(14 /main.PPM, -20/main.PPM), new Vector2(14/main.PPM, 20/main.PPM));
	fdefHerc.shape = rightSide;
	fdefHerc.isSensor = true;
	b2body.createFixture(fdefHerc).setUserData("rightSide");
	
	//sensor for left side of hercules 
	EdgeShape leftSide = new EdgeShape();
	leftSide.set(new Vector2(-14 /main.PPM, -20/main.PPM), new Vector2(-14/main.PPM, 20/main.PPM));
	fdefHerc.shape = leftSide;
	fdefHerc.isSensor = true;
	b2body.createFixture(fdefHerc).setUserData("leftSide");
		
}

/******************************************************************/

public void update(float delta) {
	
	if(runningRight == true) {
		setPosition(b2body.getPosition().x-getWidth()/3, b2body.getPosition().y-getHeight()/3);
	}
	else {
		setPosition(b2body.getPosition().x-getWidth()/1.5f, b2body.getPosition().y-getHeight()/3);
	}
	setRegion(getFrame(delta)); //change frames
	
	if(screen.level1_done == false) {

		screen.level1.fightWoodenEnemy();
		screen.level1.fightpunchBag() ;
		screen.level1.fightpillar() ;
		screen.level1.CheckPointsPass();
		screen.level1.BrownManEnemy();
	//	System.out.println(b2body.getPosition().x);
	}
}

/******************************************************************/

public void jump(){
    if ( currentState != State.jumping ) {
        b2body.applyLinearImpulse(new Vector2(0, 3f), b2body.getWorldCenter(), true);
        currentState = State.jumping;
    }
}

/******************************************************************/

public TextureRegion getFrame(float delta)
{
	currentState=getState();
	TextureRegion region;
	switch(currentState)
	{ 
	
	case runnig:
		region=(TextureRegion) hercRun.getKeyFrame(stateTimer,true);
		break;
	case die:
		region=(TextureRegion) hercdie.getKeyFrame(stateTimer,true);
		break;
	case punching:
		 region = (TextureRegion) hercpunch.getKeyFrame(stateTimer,true);
        break;
	case jumping:
		region=(TextureRegion) hercJump.getKeyFrame(stateTimer,true);
		break;
	case super_punch:
		 region = (TextureRegion) superpunch.getKeyFrame(stateTimer,true);
       break;
	case sword:
		region =(TextureRegion) hercsword.getKeyFrame(stateTimer,true);
        break;
	case super_sword:	
		region =(TextureRegion) hercsupersword.getKeyFrame(stateTimer,true);
		break;
	case standing:
		default:
			region=hercstand;
			break;
	
	}
	
	if ((b2body.getLinearVelocity().x<0|| !runningRight)&& !region.isFlipX())
	{
		region.flip(true,false);
		runningRight=false;
	}
	else if((b2body.getLinearVelocity().x>0|| runningRight)&& region.isFlipX())
	{
		region.flip(true, false);
		runningRight=true;
	}
	stateTimer=currentState == previousState? stateTimer+delta:0;
	
	previousState =currentState;
	
	return region;
		
}

/*****************************************************************/

public State getState() {
	
	if(b2body.getLinearVelocity().x != 0)
		return State.runnig;
	else if(punch())
		return State.punching;
	else if((b2body.getLinearVelocity().y > 0 && currentState == State.jumping) || (b2body.getLinearVelocity().y < 0 && previousState == State.jumping))
		return State.jumping;
	else if(Sword())
  		return State.sword;
	else if(hercSuperSword())
		return State.super_sword;
	else if(Superpunch())
		return State.super_punch;
	else if(Die())
  		return State.die;
	else
		return State.standing;
}

/**********************function to check if frames are finished or not******************/

public boolean punch() {
    if (herculesPunch == true) {
        timeAnimation += Gdx.graphics.getDeltaTime();
        if (timeAnimation < 1.2) {
            return true;
        } else {
            timeAnimation = 0;
            herculesPunch = false;
            return false;
        }
    } else {
        return false;
    }
}

public boolean Superpunch() {
    if (herculesSuperPunch == true) {
        timeAnimation += Gdx.graphics.getDeltaTime();
        if (timeAnimation < 1.2) {
            return true;
        } else {
            timeAnimation = 0;
            herculesSuperPunch = false;
            return false;
        }
    } else {
        return false;
    }
}

public boolean Sword() {
    if (herculesSword == true) {
        timeAnimation += Gdx.graphics.getDeltaTime();
        if (timeAnimation < 0.9) {
            return true;
        } else {
            timeAnimation = 0;
            herculesSword = false;
            return false;
        }
    } else {
        return false;
    }
}

public boolean hercSuperSword()
{
	if(hercSuperSword==true)
	{
		timeAnimation += Gdx.graphics.getDeltaTime();
        if (timeAnimation < 1.4) 
        {
            return true;
        } 
        else {
            timeAnimation = 0;
            hercSuperSword = false;
            return false;
        }
	}
	return false;
}
public boolean Die()
{
	if(hercDie==true)
	{
		timeAnimation += Gdx.graphics.getDeltaTime();
        if (timeAnimation < 3.5) 
        {
            return true;
        } 
        else {
            timeAnimation = 0;
            hercDie = false;
			screen.level1.updateCheckPoints();
            return false;
        }
        
	}
	
	return false;
}

/**********************************************************/

}
