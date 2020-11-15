package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import hercules.game.*;
import sprites.*;
import sprites.herclus.State;


public class Level1 implements Screen
{
	//to load map 
	private TmxMapLoader maploader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	
	//objects render
	public TileObjects tileObject;
	
	//PlayScreen
	public PlayScreen screen;
	
	//hercules score 
	public Integer level1Score ;
	public Texture coin_texture;
	public SpriteBatch coin_batch;

	//hercules number of hearts
	public Integer HeartsNum ;

	//SCORE BAR
	private SpriteBatch score_batch;
	Texture score_bar_texture=new Texture(Gdx.files.internal("score.png"));
	BitmapFont scoreText ;
	FreeTypeFontGenerator.FreeTypeFontParameter fontParameter ; //to load font
	
	public static int CollectedCoins = 0;

	//health bar
	public HealthBar health_bar;

	
	public Array<Body>coinsToRemove; //coins to remove after hercules collide with them
	public Array<Body>punchBagsToRemove; //coins to remove after hercules destroy them
	public Array<Body>PillarToRemove; //pillar to remove after hercules destroy them

	//level 1 collision items
	public boolean herc_fight_woodenEnemy1;
	public boolean herc_fight_woodenEnemy2;
	public boolean herc_fight_woodenEnemy3;
	public boolean herc_fight_BrownMan;
	
	public boolean herc_fight_punchBag1;
	public boolean herc_fight_punchBag2;
	public boolean herc_fight_punchBag3;
	public boolean herc_fight_punchBag4;
	public boolean herc_fight_punchBag5;
	public boolean herc_fight_punchBag6;
	public boolean herc_fight_punchBag7;
	public boolean herc_fight_punchBag8;
	
	public boolean herc_fight_pillar1;
	public boolean herc_fight_pillar2;
	public boolean herc_fight_pillar3;
	
	public boolean check_point1;
	public boolean check_point2;
	public boolean check_point3;
	public boolean destroy_check_point1;
	public boolean destroy_check_point2;
	public boolean destroy_check_point3;
	public boolean update_check_point1;
	public boolean update_check_point2;
	public boolean update_check_point3;
	
	public Level1(PlayScreen screen)
	{
		this.screen = screen;
		
		//Load our map
		maploader = new TmxMapLoader();
		map = maploader.load("level1-map.tmx");
		renderer = new OrthogonalTiledMapRenderer(map,1/main.PPM);
		this.screen = screen;
		tileObject = new TileObjects(this,screen);
		
		//HERCUES VARIABLES 
		level1Score = 0;
		HeartsNum = 3;
		
		//SCORE BAR
		score_batch = new SpriteBatch();
		scoreText = new BitmapFont();
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 40;
        fontParameter.color = Color.GOLD;
        //Assigning the font generator to the bitmap font
        scoreText = screen.fontGenerator.generateFont(fontParameter);

		//health bar
		health_bar= new HealthBar(screen);

		coinsToRemove = new Array<Body>();
		punchBagsToRemove = new Array<Body>();
		PillarToRemove=new Array<Body>();
		
		//level 1 collision items
		herc_fight_woodenEnemy1 = false;
		herc_fight_woodenEnemy2 = false;
		herc_fight_woodenEnemy3 = false;
		herc_fight_BrownMan=false;

		
		herc_fight_punchBag1 = false ;
		herc_fight_punchBag2 = false ;
		herc_fight_punchBag3 = false ;
		herc_fight_punchBag4 = false ;
		herc_fight_punchBag5 = false ;
		herc_fight_punchBag6 = false ;
		herc_fight_punchBag7 = false ;
		herc_fight_punchBag8 = false ;
		
		herc_fight_pillar1 = false ;
		herc_fight_pillar2 = false ;
		herc_fight_pillar3 = false ;
		
		check_point1 = true ;
		check_point2 = false ;
		check_point3 = false ;
		
		destroy_check_point1 = false ;
		destroy_check_point2 = false ;
		destroy_check_point3 = false ;
		
		update_check_point1 = false;
		update_check_point2 = false;
		update_check_point3 = false;
	}
	
	
	
	@Override
	public void render(float delta)
	{
		update(delta);
		
		//to render map
		renderer.render();
		
		//SCORE BAR
		score_batch.begin();
		
		score_batch.draw(score_bar_texture, 980, 485, 180, 120);
		if(level1Score >= 10 && level1Score < 100) scoreText.draw(score_batch,level1Score.toString(), 1055, 560);
		else if(level1Score >= 100) scoreText.draw(score_batch,level1Score.toString(), 1050, 560);
		else scoreText.draw(score_batch,level1Score.toString(), 1062, 560);
		
		score_batch.end();
		
		//HEALTH BAR
		health_bar.drawHealthBar();
	}
	
	public void update(float delta)
	{
		renderer.setView(screen.gamecam);
		collectLevel1Coins(); //update coins 
		destroyLevel1PunchBags(); //update punch bags
		destroyLevel1Pillar();
	
		if(health_bar.drink_juice == true) {
			this.map.getLayers().get(78).setOpacity(0);
			screen.world.destroyBody(tileObject.juiceBody);
			health_bar.drink_juice = false;
		}
		DestroyCheckPointsBodies();
	}
	
	/*****************checkPointsUpdate***********************/
	public void updateCheckPoints() { //caled in hercules class in die function

		if(check_point1 == true && check_point2 == false && check_point3 == false) {
			update_check_point1 = true;
		}
		else if(check_point1 == true && check_point2 == true && check_point3 == false) {
			update_check_point2 = true;
		}
		else if(check_point1 == true && check_point2 == true && check_point3 == true) {
			update_check_point3 = true;
		}
	}
	
	public void CheckPointsPass() { //caled in hercules class in die function

		if(screen.player.getX() > 14) {
			check_point2 = true;
		}
		if(screen.player.getX() > 46) {
			check_point3 = true;
		}
	}
	
	public void DestroyCheckPointsBodies() {
		
		if(check_point1 == true && check_point2 == false && check_point3 == false) {
			if(destroy_check_point1 == false) {
				screen.world.destroyBody(tileObject.checkPointsArray.get(0).body);
				destroy_check_point1 = true;
			}
			
		}
		if(check_point1 == true && check_point2 == true && check_point3 == false) {
			if(destroy_check_point2 == false) {
				screen.world.destroyBody(tileObject.checkPointsArray.get(1).body);
				destroy_check_point2 = true;
			}
			
		}
		if(check_point1 == true && check_point2 == true && check_point3 == true) {
			if(destroy_check_point3 == false) {
				screen.world.destroyBody(tileObject.checkPointsArray.get(2).body);
				destroy_check_point3 = true;
			}
			
		}
	}
	
	/****************************************/

	//destroy level1 coins when hercules collect it
	public void collectLevel1Coins() {
		Array<Body>Bodies = coinsToRemove;
		if(Bodies.size != 0) { // check if array not empty
			for(int i=0 ;i<Bodies.size ;i++) {
				Body b = Bodies.get(i);
				int n = 0;         //get the number of coins layer which are collected by hercules to make it not visible
				for(int y = 0;y<tileObject.coinArray.size;y++) {
					if(tileObject.coinArray.get(y).positionX == Bodies.get(i).getPosition().x &&
							tileObject.coinArray.get(y).positionY == Bodies.get(i).getPosition().y) {
						 n = tileObject.coinArray.get(y).num;
					}
				}
				this.map.getLayers().get(n).setOpacity(0);
				screen.world.destroyBody(b);
			}
			Bodies.clear();
		}
	}
	/****************************************/
	
	//destroy level1 punch bags 
		public void destroyLevel1PunchBags() {
			Array<Body>Bodies = punchBagsToRemove;
			if(Bodies.size != 0) { // check if array not empty
				for(int i=0 ;i<Bodies.size ;i++) {
					Body b = Bodies.get(i);
					int n = 0;         
					for(int y = 0;y<tileObject.punchBagsArray.size;y++) {
						if(tileObject.punchBagsArray.get(y).positionX == Bodies.get(i).getPosition().x &&
								tileObject.punchBagsArray.get(y).positionY == Bodies.get(i).getPosition().y) {
							 n = tileObject.punchBagsArray.get(y).num;
						}
					}
					this.map.getLayers().get(n).setOpacity(0);
					screen.world.destroyBody(b);
				}
				Bodies.clear();
			}
		}
		/****************************************/
		//destroy level1 pillar 
		public void destroyLevel1Pillar() {
			Array<Body>Bodies = PillarToRemove;
			if(Bodies.size != 0) { // check if array not empty
				for(int i=0 ;i<Bodies.size ;i++) {
					Body b = Bodies.get(i);
					int n = 0;         
					for(int y = 0;y<tileObject.pillarArray.size;y++) {
						if(tileObject.pillarArray.get(y).positionX == Bodies.get(i).getPosition().x &&
								tileObject.pillarArray.get(y).positionY == Bodies.get(i).getPosition().y) {
							 n = tileObject.pillarArray.get(y).num;
						}
					}
					this.map.getLayers().get(n).setOpacity(0);
					screen.world.destroyBody(b);
				}
				Bodies.clear();
			}
		}
		/****************************************/


		public void fightWoodenEnemy() {

			if(screen.player.previousState == State.sword || screen.player.previousState == State.super_sword) {
				if(screen.player.b2body.getPosition().x <= 23.0f && screen.player.b2body.getPosition().x >= 20.0f) {
					herc_fight_woodenEnemy1 = true;
					herc_fight_woodenEnemy2 = false;
					herc_fight_woodenEnemy3 = false;
				}
				else if(screen.player.b2body.getPosition().x <= 26.0f && screen.player.b2body.getPosition().x >= 24.0f) {
					herc_fight_woodenEnemy2 = true;
					herc_fight_woodenEnemy1 = false;
					herc_fight_woodenEnemy3 = false;
				}
				else if(screen.player.b2body.getPosition().x <= 30.0f && screen.player.b2body.getPosition().x >= 24.1f) {
					herc_fight_woodenEnemy2 = false;
			     	herc_fight_woodenEnemy1 = false;
			     	herc_fight_woodenEnemy3 = true;
				}
			}
		}
			
/**********************************************************/

public void fightpunchBag() {

	if(screen.player.previousState == State.punching || screen.player.previousState == State.super_punch
			|| screen.player.previousState == State.sword || screen.player.previousState == State.super_sword ) {
		
		if(screen.player.b2body.getPosition().x <= 8.5f && screen.player.b2body.getPosition().x >= 8f) {
			herc_fight_punchBag1 = true ;
			herc_fight_punchBag2 = false ;
			herc_fight_punchBag3 = false ;
			herc_fight_punchBag4 = false ;
			herc_fight_punchBag5 = false ;
			herc_fight_punchBag6 = false ;
			herc_fight_punchBag7 = false ;
			herc_fight_punchBag8 = false ;
		}
		else if(screen.player.b2body.getPosition().x>8.5f && screen.player.b2body.getPosition().x<=8.8f) {
			herc_fight_punchBag1 = false ;
			herc_fight_punchBag2 = true ;
			herc_fight_punchBag3 = false ;
			herc_fight_punchBag4 = false ;
			herc_fight_punchBag5 = false ;
			herc_fight_punchBag6 = false ;
			herc_fight_punchBag7 = false ;
			herc_fight_punchBag8 = false ;
		}
		else if(screen.player.b2body.getPosition().x >8.8f && screen.player.b2body.getPosition().x<=9.1f) {
			herc_fight_punchBag1 = false ;
			herc_fight_punchBag2 = false ;
			herc_fight_punchBag3 = true ;
			herc_fight_punchBag4 = false ;
			herc_fight_punchBag5 = false ;
			herc_fight_punchBag6 = false ;
			herc_fight_punchBag7 = false ;
			herc_fight_punchBag8 = false ;
		}
		else if(screen.player.b2body.getPosition().x > 9.1f &&screen.player.b2body.getPosition().x<=9.5f) {
			herc_fight_punchBag1 = false ;
			herc_fight_punchBag2 = false ;
			herc_fight_punchBag3 = false ;
			herc_fight_punchBag4 = true ;
			herc_fight_punchBag5 = false ;
			herc_fight_punchBag6 = false ;
			herc_fight_punchBag7 = false ;
			herc_fight_punchBag8 = false ;
		}
		else if(screen.player.b2body.getPosition().x > 9.5f && screen.player.b2body.getPosition().x<=9.8f) {
			herc_fight_punchBag1 = false ;
			herc_fight_punchBag2 = false ;
			herc_fight_punchBag3 = false ;
			herc_fight_punchBag4 = false ;
			herc_fight_punchBag5 = true ;
			herc_fight_punchBag6 = false ;
			herc_fight_punchBag7 = false ;
			herc_fight_punchBag8 = false ;
		}
		else if(screen.player.b2body.getPosition().x >=16.1f && screen.player.b2body.getPosition().x <= 16.4f) {
			herc_fight_punchBag1 = false ;
			herc_fight_punchBag2 = false ;
			herc_fight_punchBag3 = false ;
			herc_fight_punchBag4 = false ;
			herc_fight_punchBag5 = false ;
			herc_fight_punchBag6 = true ;
			herc_fight_punchBag7 = false ;
			herc_fight_punchBag8 = false ;
		}
		else if(screen.player.b2body.getPosition().x >=  17.3f && screen.player.b2body.getPosition().x<=17.7f) {
			herc_fight_punchBag1 = false ;
			herc_fight_punchBag2 = false ;
			herc_fight_punchBag3 = false ;
			herc_fight_punchBag4 = false ;
			herc_fight_punchBag5 = false ;
			herc_fight_punchBag6 = false ;
			herc_fight_punchBag7 = true ;
			herc_fight_punchBag8 = false ;
		}
		else if(screen.player.b2body.getPosition().x >= 18.6f && screen.player.b2body.getPosition().x <= 19f) {
			herc_fight_punchBag1 = false ;
			herc_fight_punchBag2 = false ;
			herc_fight_punchBag3 = false ;
			herc_fight_punchBag4 = false ;
			herc_fight_punchBag5 = false ;
			herc_fight_punchBag6 = false ;
			herc_fight_punchBag7 = false ;
			herc_fight_punchBag8 = true ;
		}
	}
}

/**********************************************************/

public void fightpillar() {

	if(screen.player.previousState == State.super_punch || screen.player.previousState == State.punching) {
		if(screen.player.b2body.getPosition().x >= 10.5f && screen.player.b2body.getPosition().x <= 10.9f) {
			herc_fight_pillar1 = true;
			herc_fight_pillar2 = false;
			herc_fight_pillar3 = false;
		}
		else if(screen.player.b2body.getPosition().x >= 27.5f && screen.player.b2body.getPosition().x <= 28f) {
			herc_fight_pillar1 = false;
			herc_fight_pillar2 = true;		
			herc_fight_pillar3 = false;
		}
		else if(screen.player.b2body.getPosition().x >= 38.4f  && screen.player.b2body.getPosition().x <= 40f) {
			herc_fight_pillar1 = false;
			herc_fight_pillar2 = false;
			herc_fight_pillar3 = true;
		}
	}
}

/***************************************************/
public void BrownManEnemy() {

if(screen.player.previousState == State.sword || screen.player.previousState == State.super_sword) {
	if(screen.player.b2body.getPosition().x <= 47f && screen.player.b2body.getPosition().x >= 40.4f) {
		herc_fight_BrownMan = true;
	}
  }
}

/***************************************************/

	@Override
	public void resize(int width, int height)
	{
		screen.gameport.update(width, height);
	}
	
	public TiledMap getMap()
	{
	    return map;
	}
	
	@Override
	public void dispose()
	{
		 map.dispose();
	     renderer.dispose();
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