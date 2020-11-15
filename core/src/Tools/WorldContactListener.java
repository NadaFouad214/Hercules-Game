package Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

import hercules.game.main;
import screens.Level1;
import screens.PlayScreen;
import sprites.Enemy;
import sprites.TileObjects;
import sprites.herclus;
import sprites.herclus.State;

public class WorldContactListener implements ContactListener{

	PlayScreen playScreen;
	public WorldContactListener() {
		super();
	}
	
	@Override
	public void beginContact(Contact contact) {
		
		
		//to know which 2 objects collided with each other
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();
		Fixture object1,object2; 
		
		/************************************/
		//COLLISION HERCULES WITH COINS 
		if((fixA.getFilterData().categoryBits == main.COIN_BIT && fixB.getFilterData().categoryBits == main.HERCLUES_BIT )
				|| (fixA.getFilterData().categoryBits == main.HERCLUES_BIT && fixB.getFilterData().categoryBits == main.COIN_BIT)) {
			
			if(fixA.getFilterData().categoryBits == main.COIN_BIT) object1 = fixA;
			else object1 = fixB;
			
			playScreen.level1.coinsToRemove.add(object1.getBody()); //if hercules collid with coin add them to array to delete
			playScreen.level1.level1Score += 2;
			main.coin_sound.play();
		}
		/************************************/
		//COLLISION WOODEN ENEMY WITH GROUND 
		if((fixA.getFilterData().categoryBits == main.WOODEN_ENEMY_BIT && fixB.getFilterData().categoryBits == main.GROUND_BIT )
				|| (fixA.getFilterData().categoryBits == main.GROUND_BIT && fixB.getFilterData().categoryBits == main.WOODEN_ENEMY_BIT)) {
			
			if(fixA.getFilterData().categoryBits == main.WOODEN_ENEMY_BIT) object1 = fixA;
			else object1 = fixB;
			
			 ((Enemy)object1.getUserData()).reverseVelocity(true, false); //reverse its velocity 
		}
		/************************************/
		//COLLISION WOODEN ENEMY WITH HERCULES 
		if((fixA.getFilterData().categoryBits == main.WOODEN_ENEMY_BIT && fixB.getFilterData().categoryBits == main.HERCLUES_BIT )
				|| (fixA.getFilterData().categoryBits == main.HERCLUES_BIT && fixB.getFilterData().categoryBits == main.WOODEN_ENEMY_BIT)) {
			
			playScreen.level1.health_bar.updateHealthBar();
		}
		/************************************/
		//COLLISION BROWN-MAN ENEMY WITH GROUND AND WALL 
		if((fixA.getFilterData().categoryBits == main.BROWN_MAN_BIT && fixB.getFilterData().categoryBits == main.GROUND_BIT )
				|| (fixA.getFilterData().categoryBits == main.GROUND_BIT && fixB.getFilterData().categoryBits == main.BROWN_MAN_BIT)
				|| (fixA.getFilterData().categoryBits == main.BROWN_MAN_BIT && fixB.getFilterData().categoryBits == main.RIGHT_WALL_BIT)
				|| (fixA.getFilterData().categoryBits == main.RIGHT_WALL_BIT && fixB.getFilterData().categoryBits == main.BROWN_MAN_BIT)) {
			
			if(fixA.getFilterData().categoryBits == main.BROWN_MAN_BIT) object1 = fixA;
			else object1 = fixB;
			
			((Enemy)object1.getUserData()).reverseVelocity(true, false);
			
		}
		/************************************/
		//COLLISION MAN ENEMY WITH HERCULES 
		if((fixA.getFilterData().categoryBits == main.HERCLUES_BIT && fixB.getFilterData().categoryBits == main.BROWN_MAN_BIT )
				|| (fixA.getFilterData().categoryBits == main.BROWN_MAN_BIT && fixB.getFilterData().categoryBits == main.HERCLUES_BIT)) {
			
			playScreen.level1.health_bar.updateHealthBar();
		}
		/*********************check point***************/
		if((fixA.getFilterData().categoryBits == main.JUICE_BIT && fixB.getFilterData().categoryBits == main.HERCLUES_BIT )
				|| (fixA.getFilterData().categoryBits == main.HERCLUES_BIT && fixB.getFilterData().categoryBits == main.JUICE_BIT)) {
			
			if(fixA.getFilterData().categoryBits == main.JUICE_BIT) object1 = fixA;
			else object1 = fixB;
			
			playScreen.level1.health_bar.drinkJuice();
			playScreen.level1.health_bar.drink_juice = true;
		}
		/***************************************************/
		//COLLISION WOODEN ENEMY WITH WOODEN ENEMY 
				if((fixA.getFilterData().categoryBits == main.WOODEN_ENEMY_BIT && fixB.getFilterData().categoryBits == main.WOODEN_ENEMY_BIT )
						|| (fixA.getFilterData().categoryBits == main.WOODEN_ENEMY_BIT && fixB.getFilterData().categoryBits == main.WOODEN_ENEMY_BIT)) {
					
					if(fixA.getFilterData().categoryBits == main.WOODEN_ENEMY_BIT) 
						{
						  object1 = fixA;
						  object2=fixB;
						}
					else 
						{
						  object1 = fixB;
						  object2=fixA;
						}
					
					 ((Enemy)object1.getUserData()).reverseVelocity(true, false); //reverse its velocity 
					 ((Enemy)object2.getUserData()).reverseVelocity(true, false); //reverse its velocity 
				}
				/************************************/
				//COLLISION BIRD WITH HERCULES 
				if((fixA.getFilterData().categoryBits == main.BIRD_BIT && fixB.getFilterData().categoryBits == main.HERCLUES_BIT )
						|| (fixA.getFilterData().categoryBits == main.HERCLUES_BIT && fixB.getFilterData().categoryBits == main.BIRD_BIT)) {
					
					playScreen.level1.health_bar.updateHealthBar();
				}


	}

	public void sendScreenToListener(PlayScreen s) {
		this.playScreen = s;
	}
	
	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
				
	}

}
