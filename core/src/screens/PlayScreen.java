package screens;

import org.omg.CORBA.INITIALIZE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Tools.WorldContactListener;
import hercules.game.main;
import sprites.Bird;
import sprites.BrownMan;
import sprites.HealthBar;
import sprites.TileObjects;
import sprites.WoodenEnemy;
import sprites.herclus;
import sprites.herclus.State;

public class PlayScreen implements Screen{
	
	private main game;
	
	//map properties
	int levelWidth=0;
	int levelHeight=0;
	float startX;
	float startY;
	
	//to load fonts 
	public static FreeTypeFontGenerator fontGenerator; 
	
	//camera to move with hercules
	public OrthographicCamera gamecam;
	
	//to fit with all screens 
	public Viewport gameport; 
	
	//objects render
	public World world;
	private Box2DDebugRenderer b2dr;
	
	//SCREENS
	public Level1 level1;
	Level2 level2;
	public GameOver game_over_screen;
	
	//atlas
	private TextureAtlas atlas; //hercules
	private TextureAtlas dieAtlas;
	private TextureAtlas herc_punchAtlas;
	private TextureAtlas super_swordAtlas; //super sword
	private TextureAtlas herc_superPunchAtlas;
	
	//ENEMIES LEVEL 1 ATLAS
	private TextureAtlas atlas_E; //bird
	private TextureAtlas woodenEnemyAtlas; //wooden enemy
	private TextureAtlas brownManAtlas; //man enemy
	
	// HERCULES 
	public herclus player;
	
	//ENEMIES LEVEL 1
	public WoodenEnemy woodenEnemy1;
	public WoodenEnemy woodenEnemy2;
	public WoodenEnemy woodenEnemy3;
	private Bird bird1,bird2,bird3,bird4,bird5,bird6,bird7,bird8,bird9,bird10,bird11;
	public BrownMan brownMan;
		
	//levels done or not 
	public boolean level1_done = false;
	public boolean level2_done = false;
	public boolean initialize_level2 = false;
	int deleteLevel1BodiesNum = 0;
	
	//collision class for level 1
	WorldContactListener wcl;
	
	public PlayScreen(main game)
	{
		this.game = game;
		

		//to load fonts 
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		
	    //camera to move with hercules
		gamecam = new OrthographicCamera();
		
		//to fit with all screens
		gameport = new  StretchViewport(main.v_width / main.PPM, main.v_height / main.PPM,gamecam);
	
		//set camera to the start of our map
		gamecam.position.set(gameport.getWorldWidth()/2,gameport.getWorldHeight()/2,0);
		
		world = new World(new Vector2(0,-10),true);
		b2dr = new Box2DDebugRenderer();
		
		//HERCULES ATLAS
		atlas =new TextureAtlas(Gdx.files.internal("herc_walk.pack"));
		dieAtlas =new TextureAtlas("die2.pack");
		herc_punchAtlas =new TextureAtlas("Hercules with punch-edit.pack");
		super_swordAtlas =new TextureAtlas("Super-sword.pack");  //super sword
		herc_superPunchAtlas =new TextureAtlas("super punch.pack");
		
		//ENEMIES LEVEL 1 ATLAS
		atlas_E =new TextureAtlas("bird2.pack");
		woodenEnemyAtlas =new TextureAtlas("wooden-enemy.pack");
		brownManAtlas =new TextureAtlas("brown-man.pack");
		
		//HERCULES DEF
		player =new herclus(world,this);
		
		//ENEMIES LEVEL 1 DEF 
		bird1 =new Bird(this,0.2f,1.6f);
		bird2 =new Bird(this,3f,2f);
		bird3 =new Bird(this,9f,1.6f);
		bird4 =new Bird(this,12f,2f);
		bird5 =new Bird(this,15f,1.2f);
		bird6 =new Bird(this,18f,2f);
		bird7 =new Bird(this,21f,1);
		bird8 =new Bird(this,24f,1.6f);
		bird9 =new Bird(this,27f,2f);
		bird10 =new Bird(this,30f,1.6f);
		bird11 =new Bird(this,6f,1.6f);

		woodenEnemy1=new WoodenEnemy(this,23f,0.9f);
        woodenEnemy2=new WoodenEnemy(this,24.5f,0.7f);
        woodenEnemy3=new WoodenEnemy(this,28f,0.7f);
        brownMan=new BrownMan(this,35f,0.7f);
		
		//SCREENS default is level1
		level1 = new Level1(this);
		
		//collision class for level 1
		wcl = new WorldContactListener();
		world.setContactListener(wcl);
		wcl.sendScreenToListener(this); // send playscreen to world listener
		
	}
	
	@Override
	public void render(float delta)
	{	
		// after delete level1 bodies still exist ,
		//so i destroy world bodies then initialize & reload map 2
		if(initialize_level2 == true && deleteLevel1BodiesNum == 0)
		{
			removeLevel1Objects();
			deleteLevel1BodiesNum = 1;
			player = new herclus(world, this);
			level2 = new Level2(this);
		}

		update(delta);
		
		//clear the game screen with black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//to check which level we will render it 
		currentLevel(delta);
	
		//b2dr.render(world, gamecam.combined);
		
		//to render only what camera see
		game.batch.setProjectionMatrix(gamecam.combined);
	
		//game batch
		game.batch.begin();
		player.draw(game.batch);
		
		if(level1_done == false) {
			
			bird1.draw(game.batch);
			bird2.draw(game.batch);
			bird2.draw(game.batch);
			bird4.draw(game.batch);
			bird5.draw(game.batch);
			bird6.draw(game.batch);
			bird7.draw(game.batch);
			bird8.draw(game.batch);
			bird9.draw(game.batch);
			bird10.draw(game.batch);
			bird11.draw(game.batch);

			if(woodenEnemy1.destroyed == false) woodenEnemy1.draw(game.batch);
			if(woodenEnemy2.destroyed == false) woodenEnemy2.draw(game.batch);
			if(woodenEnemy3.destroyed == false) woodenEnemy3.draw(game.batch);
			if(brownMan.destroyed==false) brownMan.draw(game.batch);
			
		}
		game.batch.end();		
		
		//hercules die-game over
		if(game_over_screen.die == true) {
			 player.currentState=State.die;
		     player.hercDie=true;
		     main.die_sound.play();
		     game.setScreen(new GameOver(game,this));
		}
		
		// get map properties
		MapProperties prop=level1.getMap().getProperties();
		levelWidth=prop.get("width", Integer.class);
		levelHeight=prop.get("height", Integer.class);
	}
	
	public void update(float delta)
	{	
		//calculate camera current position
		startX=gamecam.viewportWidth/2;
		startY=gamecam.viewportHeight/2;
				
		handleInput(delta);
		world.step(1/60f, 6, 2);
		
		
		if(level1.update_check_point1 == true) {
			player.b2body.setTransform(level1.tileObject.checkPointsArray.get(0).positionX, 30/main.PPM,0);
			level1.update_check_point1 = false;
		}
		else if(level1.update_check_point2 == true) {
			player.b2body.setTransform(level1.tileObject.checkPointsArray.get(1).positionX, 30/main.PPM,0);
			level1.update_check_point2 = false;
		}
		else if(level1.update_check_point3 == true) {
			player.b2body.setTransform(level1.tileObject.checkPointsArray.get(2).positionX, 30/main.PPM,0);
			level1.update_check_point3 = false;
		}
		
		
		player.update(delta);
		//System.out.println(player.getX());
		gamecam.position.x=player.b2body.getPosition().x;
		if(level1_done == false) {
			
			bird1.update(delta);
			bird2.update(delta);
			bird2.update(delta);
			bird4.update(delta);
			bird5.update(delta);
			bird6.update(delta);
			bird7.update(delta);
			bird8.update(delta);
			bird9.update(delta);
			bird10.update(delta);
			bird11.update(delta);

			if(woodenEnemy1.destroyed == false) woodenEnemy1.update(delta);
			if(woodenEnemy2.destroyed == false) woodenEnemy2.update(delta);
			if(woodenEnemy3.destroyed == false) woodenEnemy3.update(delta);
			brownMan.update(delta);
		}
		
		CameraBoundaries(gamecam,startX,startY,levelWidth*32-startX*2,levelHeight*32-startY*2);
		gamecam.update();
	}
	
/******************************************************************/

	// select the current level to render it and delete other levels
	public void currentLevel(float delta) {  //TODO level2 render handle
		if(level1_done == false)
		{
			level1.render(delta);
		}
		else if(level1_done == true)
		{
			if(initialize_level2 == false) {
				initialize_level2 = true;
				level2 = new Level2(this);
			}
			level2.render(delta);
		}
	}
	
	
	//destroy level1 objects 
	public void removeLevel1Objects() {
		world.getBodies(level1.tileObject.arrayOfBodies);
        for(Body bod: level1.tileObject.arrayOfBodies){
            world.destroyBody(bod);
        }
	}
	
	//to handle black spaces in our tiled map
    public void CameraBoundaries(Camera cam,float startX,float startY,float width,float height)
    {
    	Vector3 position=cam.position;
    	if(position.x < startX)
    		position.x = startX;
    	if(position.y < startY)
    		position.y = startY;
    	if(position.x > startX + width)
    		position.x = startX + width;
    	if(position.y > startY + height)
    		position.y = startY + height;
    	if(position.x>=width/209)
        	position.x=width/209;
    	cam.position.set(position);
    	cam.update();
    }
	
/********************************************************/
	

	public void handleInput(float delta)
	{
		//level 2
	    if (Gdx.input.isKeyJustPressed(Input.Keys.W)) { //TODO level2 button
	        level1_done = true;
	       }
	    
		/***************hercules functions*******************/
		// jump
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.b2body.getPosition().y <= 1.4f) {
				main.jump_sound.play();
				player.jump();
		}
		
		//walk right
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
			player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
				
		//walk left
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
		    player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
		
		//punch
	    if (Gdx.input.isKeyJustPressed(Input.Keys.X) && player.b2body.getLinearVelocity().x <=0) {
		        player.currentState= State.punching;
		        player.herculesPunch =true;
		        main.punch_sound.play();
		        
		        if(level1_done == false) {
		        	fight_enemies_level1_punchBags("P");
		        	destroy_level1_pillar("p");
		        }
	       }
	    
	    //super punch
	    if (Gdx.input.isKeyJustPressed(Input.Keys.C) && player.b2body.getLinearVelocity().x <=0) {
		        player.currentState=State.super_punch;
		        player.herculesSuperPunch =true;
		        main.punch_sound.play();
		        
		        if(level1_done == false) {
		        	fight_enemies_level1_punchBags("S");
		        	destroy_level1_pillar("S");
		        }
	       }
	    
	    //sword
	    if (Gdx.input.isKeyJustPressed(Input.Keys.S) && player.b2body.getLinearVelocity().x <=0) {
		        player.currentState= State.sword;
		        player.herculesSword = true;
		        main.sword_sound.play();
		        
		        if(level1_done == false) {
		        	fight_enemies_level1_woodenEnemy();
		        	fight_enemies_level1_punchBags("P");
		        	fight_enemies_level1_BrownMan();
		        }
	       }
	    
	    //super sword //TODO replace it
	    if (Gdx.input.isKeyJustPressed(Input.Keys.Z) && player.b2body.getLinearVelocity().x <=0) {
		        player.currentState=State.super_sword; 
		        player.hercSuperSword =true;
		        main.super_sword_sound.play();
		        
		        if(level1_done == false) {
		        	fight_enemies_level1_woodenEnemy();
		        	fight_enemies_level1_punchBags("S");
		        	fight_enemies_level1_BrownMan();
		        }
	       }
	    /**********************************/
	    
	}
	public void fight_enemies_level1_BrownMan() {
    	//brown man enemy
    	if(level1.herc_fight_BrownMan == true)brownMan.hit_num--;
    	System.out.println(brownMan.hit_num);
	}
	public void fight_enemies_level1_woodenEnemy() {

    	//wooden enemy
    	if(level1.herc_fight_woodenEnemy1 == true) woodenEnemy1.hit_num--;
        else if(level1.herc_fight_woodenEnemy2 == true) woodenEnemy2.hit_num--;
        else if(level1.herc_fight_woodenEnemy3 == true) woodenEnemy3.hit_num--;
    	
	}
	public void destroy_level1_pillar(String key) {
		int num_hit=0;
		if(key == "S") num_hit = 2;
		else num_hit = 1;
    	
    	if(level1.herc_fight_pillar1 == true) {
    		
    		if(level1.tileObject.pillarArray.get(0).destroy == true) {
    			level1.PillarToRemove.add(level1.tileObject.pillarArray.get(0).body);
    			main.Pillar_Crash_sound.play();
    		}
    		else level1.tileObject.pillarArray.get(0).update_hit_object(num_hit);
    	}
    	
    	if(level1.herc_fight_pillar2 == true) {
    		
    		if(level1.tileObject.pillarArray.get(1).destroy == true) {
    			level1.PillarToRemove.add(level1.tileObject.pillarArray.get(1).body);
    			main.Pillar_Crash_sound.play();
    		}
    		else level1.tileObject.pillarArray.get(1).update_hit_object(num_hit);
    	}
    	
    	if(level1.herc_fight_pillar3 == true) {
    		
    		if(level1.tileObject.pillarArray.get(2).destroy == true) {
    			level1.PillarToRemove.add(level1.tileObject.pillarArray.get(2).body);
    			main.Pillar_Crash_sound.play();
    		}
    		else level1.tileObject.pillarArray.get(2).update_hit_object(num_hit);
    	}
    	
	}
	
	public void fight_enemies_level1_punchBags(String key) {
		int num_hit = 0;
		if(key == "S") num_hit = 2;
		else num_hit = 1;
		
		//punch bags
    	if(level1.herc_fight_punchBag1 == true) {
    		
    		if(level1.tileObject.punchBagsArray.get(0).destroy == true) {
    			level1.punchBagsToRemove.add(level1.tileObject.punchBagsArray.get(0).body);
    			main.punch_bag_sound.play();
    		}
    		else level1.tileObject.punchBagsArray.get(0).update_hit_object(num_hit);
    	}
    	
    	if(level1.herc_fight_punchBag2 == true) {
    		
    		if(level1.tileObject.punchBagsArray.get(1).destroy == true) {
    			level1.punchBagsToRemove.add(level1.tileObject.punchBagsArray.get(1).body);
    			main.punch_bag_sound.play();
    		}
    		else level1.tileObject.punchBagsArray.get(1).update_hit_object(num_hit);
    	}
    	
    	if(level1.herc_fight_punchBag3 == true) {
    		
    		if(level1.tileObject.punchBagsArray.get(2).destroy == true) {
    			level1.punchBagsToRemove.add(level1.tileObject.punchBagsArray.get(2).body);
    			main.punch_bag_sound.play();
    		}
    		else level1.tileObject.punchBagsArray.get(2).update_hit_object(num_hit);
    	}
    	
    	if(level1.herc_fight_punchBag4 == true) {
    		
    		if(level1.tileObject.punchBagsArray.get(3).destroy == true) {
    			level1.punchBagsToRemove.add(level1.tileObject.punchBagsArray.get(3).body);
    			main.punch_bag_sound.play();
    		}
    		else level1.tileObject.punchBagsArray.get(3).update_hit_object(num_hit);
    	}
    	
    	if(level1.herc_fight_punchBag5 == true) {
    		
    		if(level1.tileObject.punchBagsArray.get(4).destroy == true) {
    			level1.punchBagsToRemove.add(level1.tileObject.punchBagsArray.get(4).body);
    			main.punch_bag_sound.play();
    		}
    		else level1.tileObject.punchBagsArray.get(4).update_hit_object(num_hit);
    	}
    	
    	if(level1.herc_fight_punchBag6 == true) {
    		
    		if(level1.tileObject.punchBagsArray.get(5).destroy == true) {
    			level1.punchBagsToRemove.add(level1.tileObject.punchBagsArray.get(5).body);
    			main.punch_bag_sound.play();
    		}
    		else level1.tileObject.punchBagsArray.get(5).update_hit_object(num_hit);
    	}
    	
    	if(level1.herc_fight_punchBag7 == true) {
    		
    		if(level1.tileObject.punchBagsArray.get(6).destroy == true) {
    			level1.punchBagsToRemove.add(level1.tileObject.punchBagsArray.get(6).body);
    			main.punch_bag_sound.play();
    		}
    		else level1.tileObject.punchBagsArray.get(6).update_hit_object(num_hit);
    	}
    	
    	if(level1.herc_fight_punchBag8 == true) {
    		
    		if(level1.tileObject.punchBagsArray.get(7).destroy == true) {
    			level1.punchBagsToRemove.add(level1.tileObject.punchBagsArray.get(7).body);
    			main.punch_bag_sound.play();
    		}
    		else level1.tileObject.punchBagsArray.get(7).update_hit_object(num_hit);
    	}
	}
	
	@Override
	public void resize(int width, int height)
	{
		gameport.update(width, height);
	}
	
	public World getWorld()
	{
	    return world;
	}
	
    public TextureAtlas getAtlas() {
    	
    	return atlas;
    }
    public TextureAtlas getDieAtlas()
    {
    	return dieAtlas;
    }
    public TextureAtlas getHerc_punchAtlas()
    {
    	return herc_punchAtlas;
    }
    public TextureAtlas getSuper_swordAtlas()
    {
    	return super_swordAtlas;
    }
	public TextureAtlas getHerc_superPunchAtlas() 
	{
    	return herc_superPunchAtlas;
	}
	public TextureAtlas getBirdAtlas() {
		return atlas_E;
	}
	public  TextureAtlas getWoodenEnemyAtlas() {
		
    	return woodenEnemyAtlas;
    }
	public TextureAtlas getBrownManAtlas() {
		return brownManAtlas;
	}
	@Override
	public void dispose()
	{
		if(level1_done == false) {
			level1.dispose();
		}else
		{
			level2.dispose();
		}
	     world.dispose();
	     b2dr.dispose();
	}


	@Override
	public void show()
	{
	
		
	}

	@Override
	public void pause() {
		
		
	}

	@Override
	public void resume() {
	
	}

	@Override
	public void hide() {
		
	}



	
	

}
