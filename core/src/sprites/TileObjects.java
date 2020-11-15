package sprites;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import hercules.game.main;
import screens.Level1;
import screens.PlayScreen;

public class TileObjects 
{
	private TiledMap map;
	private World world;
	private Level1 level1;
	

	//create body and fixture variables for ropes and ground
	BodyDef bdef ;
	PolygonShape shape ;
	FixtureDef fdef;
	Body body;
	
	public Array<Body> arrayOfBodies; // to delete them in level2
	public Array<Level1Bodies>coinArray; //hold all coins
	public Array<Level1Bodies>punchBagsArray; //hold all punch bags
	public Array<Level1Bodies>pillarArray;
	public Array<Level1Bodies>checkPointsArray;
	
	public Array<Level1Bodies>AirBagArray;
	public Body juiceBody;
	
	public TileObjects(Level1 level1,PlayScreen screen)
	{
		this.map = level1.getMap();
		this.world = screen.getWorld();
		this.level1 = level1;
		
		//create body and fixture variables for ropes and ground
		bdef = new BodyDef();
		shape = new PolygonShape();
	    fdef = new FixtureDef();
	    
		arrayOfBodies = new Array<>(); //array to hold all  bodies to destroy when move to level 2
		
		//all arrays of bodies
		coinArray = new Array<Level1Bodies>(); 
		punchBagsArray = new Array<Level1Bodies>();
		pillarArray = new Array<Level1Bodies>();
		checkPointsArray = new Array<Level1Bodies>();
		AirBagArray = new Array<Level1Bodies>();
		
		//calling draw functions
		CreateGround();
		CreateLeftWall();
		CreateRightWall();
		//CreateRope();
		CreateCoin();
		CreatePunchBags();
		CreatePillar();
		//CreateBigStatue();
		CreateCheckPoint();
		//CreateShield();
		CreateJuice();
		//CreateFireSword();
		
	}
	
	/****************************************************/
	
	//COINS
	private void CreateCoin() {
		
		MapLayer layer = this.map.getLayers().get("silver coin");
		
		BodyDef bdefCoin = new BodyDef() ;
		FixtureDef fdefCoin = new FixtureDef();
		int num = 14;  //number of this layer in tiled map 
		
		for(MapObject object :layer.getObjects()) {
			
			bdefCoin.type = BodyDef.BodyType.StaticBody;
			float x = Float.parseFloat((object.getProperties().get("x").toString()))/main.PPM;
			float y = Float.parseFloat((object.getProperties().get("y").toString()))/main.PPM;
			
			bdefCoin.position.set(x,y);
			
			CircleShape shapeCoin = new CircleShape();
			shapeCoin.setRadius(7f/main.PPM);
			
			fdefCoin.shape = shapeCoin;
			fdefCoin.filter.categoryBits = main.COIN_BIT;
			fdefCoin.filter.maskBits = main.HERCLUES_BIT;
			
			Body bodyCoin = world.createBody(bdefCoin);
			bodyCoin.createFixture(fdefCoin).setUserData("coin");
			
			Level1Bodies coinClass = new Level1Bodies(bodyCoin , x , y , num);
			coinArray.add(coinClass);
			arrayOfBodies.add(bodyCoin);
			num++;
		}
	}
	
	/****************************************************/
	
	//PUNCH BAGS 
	private void CreatePunchBags() {
		
		MapLayer layer = this.map.getLayers().get("punch bag");
		
		BodyDef bdefPunchBag = new BodyDef() ;
		FixtureDef fdefPunchBag = new FixtureDef();
		
		int num = 60;  //number of this layer in tiled map 
		
		for(MapObject object :layer.getObjects()) {
			bdefPunchBag.type = BodyDef.BodyType.StaticBody;
			float x = Float.parseFloat((object.getProperties().get("x").toString()))/main.PPM;
			float y = Float.parseFloat((object.getProperties().get("y").toString()))/main.PPM;
			
			bdefPunchBag.position.set(x,y);
			
			Vector2[] ver = new Vector2[4];
			ver[0]=new Vector2(0,0).scl(1/main.PPM);
			ver[1]=new Vector2(0,-45).scl(1/main.PPM);
			ver[2]=new Vector2(20,0).scl(1/main.PPM);
			ver[3]=new Vector2(20,-45).scl(1/main.PPM);
			
			PolygonShape shapePunchBag = new PolygonShape();
			shapePunchBag.set(ver);
			
			fdefPunchBag.shape = shapePunchBag;
			fdefPunchBag.filter.categoryBits = main.PUNCH_BAG_BIT; 
			fdefPunchBag.filter.maskBits = main.HERCLUES_BIT; //can collide with hercules
			
			Body bodyPunchBag = world.createBody(bdefPunchBag);
			bodyPunchBag.createFixture(fdefPunchBag).setUserData("PunchBag");
			
			Level1Bodies punchBagsClass = new Level1Bodies(bodyPunchBag , x , y , num);
			punchBagsArray.add(punchBagsClass);
			arrayOfBodies.add(bodyPunchBag);
			num++;
		}
	}
		
		/****************************************************/
		
		//PILLARS
	private void CreatePillar() {
		
		MapLayer layer = this.map.getLayers().get("pillar");
		
		BodyDef bdefPillar = new BodyDef() ;
		FixtureDef fdefPillar = new FixtureDef();
		
		int num = 68;  //number of this layer in tiled map 
		
		for(MapObject object :layer.getObjects()) {
			
			bdefPillar.type = BodyDef.BodyType.StaticBody;
			float x = Float.parseFloat((object.getProperties().get("x").toString()))/main.PPM;
			float y = Float.parseFloat((object.getProperties().get("y").toString()))/main.PPM;
			
			bdefPillar.position.set(x,y);
			
			PolygonShape shapePillar = new PolygonShape();
			Vector2[] ver = new Vector2[4];
			ver[0]=new Vector2(0,0).scl(1/main.PPM);
			ver[1]=new Vector2(20,0).scl(1/main.PPM);
			ver[2]=new Vector2(0,-100).scl(1/main.PPM);
			ver[3]=new Vector2(20,-100).scl(1/main.PPM);
			shapePillar.set(ver);
			fdefPillar.shape = shapePillar;
			fdefPillar.filter.categoryBits = main.PILLAR_BIT;
			fdefPillar.filter.maskBits = main.HERCLUES_BIT;
			
			Body bodyPillar = world.createBody(bdefPillar);
			bodyPillar.createFixture(fdefPillar).setUserData("Pillar");
			
			Level1Bodies pillarClass = new Level1Bodies(bodyPillar , x , y , num);
			pillarArray.add(pillarClass);
			arrayOfBodies.add(bodyPillar);
			num++ ;
		}
	}	
		
		/****************************************************/
		
			//BIG STATUE
		/*		private void CreateBigStatue() {
					
					MapLayer layer = this.map.getLayers().get(" statue 1");
					
					BodyDef bdefBigStatue = new BodyDef() ;
					FixtureDef fdefBigStatue = new FixtureDef();
					
					for(MapObject object :layer.getObjects()) {
						
						bdefBigStatue.type = BodyDef.BodyType.StaticBody;
						float x = Float.parseFloat((object.getProperties().get("x").toString()))/main.PPM;
						float y = Float.parseFloat((object.getProperties().get("y").toString()))/main.PPM;
						
						bdefBigStatue.position.set(x,y);
						
						PolygonShape shapeBigStatue = new PolygonShape();
						Vector2[] ver = new Vector2[4];
						ver[0]=new Vector2(-60,93).scl(1/main.PPM);
						ver[1]=new Vector2(0,93).scl(1/main.PPM);
						ver[2]=new Vector2(0,-2).scl(1/main.PPM);
						ver[3]=new Vector2(-65,-2).scl(1/main.PPM);
						shapeBigStatue.set(ver);
						fdefBigStatue.shape = shapeBigStatue;
						fdefBigStatue.filter.categoryBits = main.BIG_STATUE_BIT;
						fdefBigStatue.filter.maskBits = main.HERCLUES_BIT;
						
						Body bodyBigStatue = world.createBody(bdefBigStatue);
						bodyBigStatue.createFixture(fdefBigStatue).setUserData("BigStatue");
						arrayOfBodies.add(bodyBigStatue);
					}
				}		*/
				
		/****************************************************/
				
				//rope object
				private void CreateRope() {
					for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class) )
					{
						Rectangle rec = ((RectangleMapObject) object).getRectangle();
						bdef.type = BodyDef.BodyType.StaticBody;
						bdef.position.set((rec.getX() + rec.getWidth() /2)/main.PPM,(rec.getY() + rec.getHeight() /2)/main.PPM);
						body = world.createBody(bdef);
						shape.setAsBox(rec.getWidth()/2/main.PPM,rec.getHeight()/2/main.PPM);
						fdef.shape = shape;
						body.createFixture(fdef);
						arrayOfBodies.add(body);
					}
				}
				
		/****************************************************/
				
				//create ground bodies/fixtures
				private void CreateGround() {
					for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class) )
					{
						Rectangle rec = ((RectangleMapObject) object).getRectangle();
						bdef.type = BodyDef.BodyType.StaticBody;
						bdef.position.set((rec.getX() + rec.getWidth() /2)/main.PPM,(rec.getY() + rec.getHeight() /2)/main.PPM);
						body = world.createBody(bdef);
						shape.setAsBox(rec.getWidth()/2/main.PPM,rec.getHeight()/2/main.PPM);
						fdef.shape = shape;
						body.createFixture(fdef);
						arrayOfBodies.add(body);
					}
			
				}
		/****************************************************/
				
				//create LeftWall
				private void CreateLeftWall() {
					for(MapObject object : map.getLayers().get(81).getObjects().getByType(RectangleMapObject.class) )
					{
						Rectangle rec = ((RectangleMapObject) object).getRectangle();
						bdef.type = BodyDef.BodyType.StaticBody;
						bdef.position.set((rec.getX() + rec.getWidth() /2)/main.PPM,(rec.getY() + rec.getHeight() /2)/main.PPM);
						body = world.createBody(bdef);
						shape.setAsBox(rec.getWidth()/2/main.PPM,rec.getHeight()/2/main.PPM);
						fdef.shape = shape;
						body.createFixture(fdef);
						arrayOfBodies.add(body);
					}
				}
				
		/****************************************************/
				
				//create RightWall
				private void CreateRightWall() {
					for(MapObject object : map.getLayers().get(82).getObjects().getByType(RectangleMapObject.class) )
					{
						Rectangle rec = ((RectangleMapObject) object).getRectangle();
						bdef.type = BodyDef.BodyType.StaticBody;
						bdef.position.set((rec.getX() + rec.getWidth() /2)/main.PPM,(rec.getY() + rec.getHeight() /2)/main.PPM);
						body = world.createBody(bdef);
						shape.setAsBox(rec.getWidth()/2/main.PPM,rec.getHeight()/2/main.PPM);
						fdef.shape = shape;
						body.createFixture(fdef);
						arrayOfBodies.add(body);
					}
				}
				
	/****************************************************/		
				
				//create check point
				private void CreateCheckPoint() {
					
					MapLayer layer = this.map.getLayers().get("check point");
					
					BodyDef bdefCheckPoint = new BodyDef() ;
					FixtureDef fdefCheckPoint = new FixtureDef();
					int num = 72;  //number of this layer in tiled map 
					
					for(MapObject object :layer.getObjects()) {
						
						bdefCheckPoint.type = BodyDef.BodyType.StaticBody;
						float x = Float.parseFloat((object.getProperties().get("x").toString()))/main.PPM;
						float y = Float.parseFloat((object.getProperties().get("y").toString()))/main.PPM;
						
						bdefCheckPoint.position.set(x,y);
						
						PolygonShape shapeCheckPoint = new PolygonShape();
						Vector2[] ver = new Vector2[4];
						ver[0]=new Vector2(10,-60).scl(1/main.PPM);
						ver[1]=new Vector2(10,15).scl(1/main.PPM);
						ver[2]=new Vector2(0,15).scl(1/main.PPM);
						ver[3]=new Vector2(-20,-60).scl(1/main.PPM);
						shapeCheckPoint.set(ver);
						
						fdefCheckPoint.shape = shapeCheckPoint;
						fdefCheckPoint.filter.categoryBits = main.CHECK_POINT_BIT;
						fdefCheckPoint.filter.maskBits = main.HERCLUES_BIT;
						
						Body bodyCheckPoint = world.createBody(bdefCheckPoint);
						bodyCheckPoint.createFixture(fdefCheckPoint).setUserData("CheckPoint");
						
						Level1Bodies CheckPointClass = new Level1Bodies(bodyCheckPoint , x , y , num);
						checkPointsArray.add(CheckPointClass);
						arrayOfBodies.add(bodyCheckPoint);
						num++;
					} 
				}
				
			
				
   /****************************************************/
				
				//create shield
				private void CreateShield() {
					MapLayer layer = this.map.getLayers().get("shield");
					
					BodyDef bdefshield = new BodyDef() ;
					FixtureDef fdefshield = new FixtureDef();
					int num = 80;  //number of this layer in tiled map 
					
					for(MapObject object :layer.getObjects()) {
						
						bdefshield.type = BodyDef.BodyType.StaticBody;
						float x = Float.parseFloat((object.getProperties().get("x").toString()))/main.PPM;
						float y = Float.parseFloat((object.getProperties().get("y").toString()))/main.PPM;
						
						bdefshield.position.set(x,y);
						
						PolygonShape shapeshield = new PolygonShape();
						Vector2[] ver = new Vector2[4];
						ver[0]=new Vector2(-15,35).scl(1/main.PPM);
						ver[1]=new Vector2(10,30).scl(1/main.PPM);
						ver[2]=new Vector2(-15,0).scl(1/main.PPM);
						ver[3]=new Vector2(10,0).scl(1/main.PPM);
						shapeshield.set(ver);
						
						fdefshield.shape = shapeshield;
						fdefshield.filter.categoryBits = main.SHIELD_BIT ;
						fdefshield.filter.maskBits = main.HERCLUES_BIT;
						
						Body bodyshield = world.createBody(bdefshield);
						bodyshield.createFixture(fdefshield).setUserData("shield");
						
						/*Level1Bodies shieldClass = new Level1Bodies(bodyshield , x , y , num);
						coinArray.add(shieldClass);
						arrayOfBodies.add(bodyshield);*/
						num++;
					}
				}
				
  /****************************************************/
				
				//create juice
				private void CreateJuice() {
					MapLayer layer = this.map.getLayers().get("juice");
					
					BodyDef bdefjuice = new BodyDef() ;
					FixtureDef fdefjuice = new FixtureDef();
					int num = 78;  //number of this layer in tiled map 
					
					for(MapObject object :layer.getObjects()) {
						
						bdefjuice.type = BodyDef.BodyType.StaticBody;
						float x = Float.parseFloat((object.getProperties().get("x").toString()))/main.PPM;
						float y = Float.parseFloat((object.getProperties().get("y").toString()))/main.PPM;
						
						bdefjuice.position.set(x,y);
						
						PolygonShape shapejuice = new PolygonShape();
						Vector2[] ver = new Vector2[4];
						ver[0]=new Vector2(5,35).scl(1/main.PPM);
						ver[1]=new Vector2(-10,20).scl(1/main.PPM);
						ver[2]=new Vector2(-10,0).scl(1/main.PPM);
						ver[3]=new Vector2(5,0).scl(1/main.PPM);
						shapejuice.set(ver);
						
						fdefjuice.shape = shapejuice;
						fdefjuice.filter.categoryBits = main.JUICE_BIT;
						fdefjuice.filter.maskBits = main.HERCLUES_BIT;
						
						Body bodyjuice = world.createBody(bdefjuice);
						bodyjuice.createFixture(fdefjuice).setUserData("juice");
						juiceBody = bodyjuice;
						
					}
				}
				
   /****************************************************/
				
				//create fire sword
				private void CreateFireSword() {
					MapLayer layer = this.map.getLayers().get("fire sword");
					
					BodyDef bdeffiresword = new BodyDef() ;
					FixtureDef fdeffiresword = new FixtureDef();
					int num = 76;  //number of this layer in tiled map 
					
					for(MapObject object :layer.getObjects()) {
						
						bdeffiresword.type = BodyDef.BodyType.StaticBody;
						float x = Float.parseFloat((object.getProperties().get("x").toString()))/main.PPM;
						float y = Float.parseFloat((object.getProperties().get("y").toString()))/main.PPM;
						
						bdeffiresword.position.set(x,y);
						
						PolygonShape shapefiresword = new PolygonShape();
						Vector2[] ver = new Vector2[4];
						ver[0]=new Vector2(15,35).scl(1/main.PPM);
						ver[1]=new Vector2(0,25).scl(1/main.PPM);
						ver[2]=new Vector2(0,0).scl(1/main.PPM);
						ver[3]=new Vector2(15,0).scl(1/main.PPM);
						shapefiresword.set(ver);
						
						fdeffiresword.shape = shapefiresword;
						fdeffiresword.filter.categoryBits = main.FIRE_SWORD_BIT;
						fdeffiresword.filter.maskBits = main.HERCLUES_BIT;
						
						Body bodyfiresword = world.createBody(bdeffiresword);
						bodyfiresword.createFixture(fdeffiresword).setUserData("fire sword");
						
						/*Level1Bodies fireswordClass = new Level1Bodies(bodyfiresword , x , y , num);
						coinArray.add(fireswordClass);
						arrayOfBodies.add(bodyfiresword);*/
						num++;
					}
				}
}

		
		