package sprites;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import hercules.game.main;
import screens.Level1;
import screens.Level2;
import screens.PlayScreen;

public class TileObject2 
{
	private TiledMap map;
	private World world;
	private Level2 level2;
	
	BodyDef bdef ;
	PolygonShape shape ;
	FixtureDef fdef;
	Body body;
	
	public Array<Body> ArrayOfBodies; // to delete them in level2
	public Array<Level1Bodies>coinArray; //hold all coins
	public Array<Level1Bodies>punchBagsArray; //hold all punch bags
	public TileObject2(Level2 level2,PlayScreen screen2)
	{
		this.map = level2.getMap();
		this.world = screen2.getWorld();
		this.level2 = level2;;
		
		//create body and fixture variables for ropes and ground
		bdef = new BodyDef();
		shape = new PolygonShape();
		fdef = new FixtureDef();
		
		ArrayOfBodies = new Array<>(); //array to hold all  bodies to destroy when move to level 2
		
		//all arrays of bodies
		coinArray = new Array<Level1Bodies>(); 
		punchBagsArray = new Array<Level1Bodies>();
		
		//creating function objects
		createGround();
		CreateBorders();	
		createRope();
		CreateCoin();
		CreatePillar();
		CreatePunchBags();
		CreateFireSword();
		CreateCheckPoint();
		CreateShield();
		CreateJuice();
		CreateFireSword();
	}
	
	
	/******************************************************/
		//border object
		private void CreateBorders() {
		for(MapObject object : map.getLayers().get(57).getObjects().getByType(RectangleMapObject.class) )
		{
			Rectangle rec = ((RectangleMapObject) object).getRectangle();
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rec.getX() + rec.getWidth() /2)/main.PPM,(rec.getY() + rec.getHeight() /2)/main.PPM);
			body = world.createBody(bdef);
			shape.setAsBox(rec.getWidth()/2/main.PPM,rec.getHeight()/2/main.PPM);
			fdef.shape = shape;
			body.createFixture(fdef);
			ArrayOfBodies.add(body);
		}
	}
		//rope object
		private void createRope()
		{
			for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class) )
			{
				Rectangle rec = ((RectangleMapObject) object).getRectangle();
				bdef.type = BodyDef.BodyType.StaticBody;
				bdef.position.set((rec.getX() + rec.getWidth() /2)/main.PPM,(rec.getY() + rec.getHeight() /2)/main.PPM);
				body = world.createBody(bdef);
				shape.setAsBox(rec.getWidth()/2/main.PPM,rec.getHeight()/2/main.PPM);
				fdef.shape = shape;
				body.createFixture(fdef);
				ArrayOfBodies.add(body);
			}
		}
		//ground object 
		private void createGround()
		{
		   for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class) )
		   {
			    Rectangle rec = ((RectangleMapObject) object).getRectangle();
				bdef.type = BodyDef.BodyType.StaticBody;
				bdef.position.set((rec.getX() + rec.getWidth() /2)/main.PPM,(rec.getY() + rec.getHeight() /2)/main.PPM);
				body = world.createBody(bdef);
				shape.setAsBox(rec.getWidth()/2/main.PPM,rec.getHeight()/2/main.PPM);
				fdef.shape = shape;
				body.createFixture(fdef);
				ArrayOfBodies.add(body);
		   }
		}
		// coin object
		private void CreateCoin() {
			
			MapLayer layer = this.map.getLayers().get("silver coin");
			
			BodyDef bdefCoin = new BodyDef() ;
			FixtureDef fdefCoin = new FixtureDef();
			int num = 17;  //number of this layer in tiled map 
			
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
				ArrayOfBodies.add(bodyCoin);
				num++;
			}
		}
		//object of pillar
		private void CreatePillar() {
			
			MapLayer layer = this.map.getLayers().get("pillar");
			
			BodyDef bdefPillar = new BodyDef() ;
			FixtureDef fdefPillar = new FixtureDef();
			
			for(MapObject object :layer.getObjects()) {
				
				bdefPillar.type = BodyDef.BodyType.StaticBody;
				float x = Float.parseFloat((object.getProperties().get("x").toString()))/main.PPM;
				float y = Float.parseFloat((object.getProperties().get("y").toString()))/main.PPM;
				
				bdefPillar.position.set(x,y);
				
				PolygonShape shapePillar = new PolygonShape();
				Vector2[] ver = new Vector2[4];
				ver[0]=new Vector2(0,160).scl(1/main.PPM);
				ver[1]=new Vector2(20,160).scl(1/main.PPM);
				ver[2]=new Vector2(0,0).scl(1/main.PPM);
				ver[3]=new Vector2(20,0).scl(1/main.PPM);
				shapePillar.set(ver);
				fdefPillar.shape = shapePillar;
				fdefPillar.filter.categoryBits = main.PILLAR_BIT;
				fdefPillar.filter.maskBits = main.HERCLUES_BIT;
				
				Body bodyPillar = world.createBody(bdefPillar);
				bodyPillar.createFixture(fdefPillar).setUserData("Pillar");
				ArrayOfBodies.add(bodyPillar);
			}
		}
		/************************/
		//PUNCH BAGS 
				private void CreatePunchBags() {
					
					MapLayer layer = this.map.getLayers().get("punch bag");
					
					BodyDef bdefPunchBag = new BodyDef() ;
					FixtureDef fdefPunchBag = new FixtureDef();
					int num = 47;  //number of this layer in tiled map 

					for(MapObject object :layer.getObjects()) {
						bdefPunchBag.type = BodyDef.BodyType.StaticBody;
						float x = Float.parseFloat((object.getProperties().get("x").toString()))/main.PPM;
						float y = Float.parseFloat((object.getProperties().get("y").toString()))/main.PPM;
						
						bdefPunchBag.position.set(x,y);
						
						Vector2[] ver = new Vector2[6];
						ver[0]=new Vector2(0,0).scl(1/main.PPM);
						ver[1]=new Vector2(-25,0).scl(1/main.PPM);
						ver[2]=new Vector2(-27,28).scl(1/main.PPM);
						ver[3]=new Vector2(-20,33).scl(1/main.PPM);
						ver[4]=new Vector2(-5,33).scl(1/main.PPM);
						ver[5]=new Vector2(3,28).scl(1/main.PPM);
						
						PolygonShape shapePunchBag = new PolygonShape();
						shapePunchBag.set(ver);
						
						fdefPunchBag.shape = shapePunchBag;
						fdefPunchBag.filter.categoryBits = main.PUNCH_BAG_BIT; 
						fdefPunchBag.filter.maskBits = main.HERCLUES_BIT; //can collide with hercules
						
						Body bodyPunchBag = world.createBody(bdefPunchBag);
						bodyPunchBag.createFixture(fdefPunchBag).setUserData("punch bag");
						
						Level1Bodies punchBagClass = new Level1Bodies(bodyPunchBag , x , y , num);
						punchBagsArray.add(punchBagClass);
						ArrayOfBodies.add(bodyPunchBag);
						num++;
					}
				}
				
				/******************/
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
						coinArray.add(CheckPointClass);
						ArrayOfBodies.add(bodyCheckPoint);
						num++;
					} 
				}
				
				
   /********************************************/
				
				//create shield
				private void CreateShield() {
                    MapLayer layer = this.map.getLayers().get("sheild");
					
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
						
						Level1Bodies shieldClass = new Level1Bodies(bodyshield , x , y , num);
						coinArray.add(shieldClass);
						ArrayOfBodies.add(bodyshield);
						num++;
					}
				}
  /*********************************************/
				
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
						
						Level1Bodies juiceClass = new Level1Bodies(bodyjuice , x , y , num);
						coinArray.add(juiceClass);
						ArrayOfBodies.add(bodyjuice);
						num++;
					}
				}
				
   /**********************************************/
				
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
						
						Level1Bodies fireswordClass = new Level1Bodies(bodyfiresword , x , y , num);
						coinArray.add(fireswordClass);
						ArrayOfBodies.add(bodyfiresword);
						num++;
					}
				}
}
	
	
