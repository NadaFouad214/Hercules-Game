package screens;

import java.io.ObjectInputStream.GetField;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import hercules.game.*;
import sprites.*;
import sprites.herclus.State;

public class Level2 implements Screen
{
	//to load map 
		private TmxMapLoader maploader;
		private TiledMap map;
		private OrthogonalTiledMapRenderer renderer;
		
		//objects render
		public TileObject2 tileObject;
		
		//PlayScreen
		public PlayScreen screen2;
		
		//hercules score 
		public Integer level2Score ;
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
		
		public boolean level2_done=false;
		
	public Level2(PlayScreen screen2)
	{
		this.screen2 = screen2;
		level2_done=true;
		
		//Load our map
		maploader = new TmxMapLoader();
		map = maploader.load("level2-map.tmx");
		renderer = new OrthogonalTiledMapRenderer(map,1/main.PPM);
		tileObject = new TileObject2(this,screen2);
		
		//HERCUES VARIABLES 
		level2Score = 0;
		HeartsNum = 3;
		
		//SCORE BAR
		score_batch = new SpriteBatch();
		scoreText = new BitmapFont();
	    fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameter.size = 40;
		fontParameter.color = Color.GOLD;

        //Assigning the font generator to the bitmap font
		scoreText = screen2.fontGenerator.generateFont(fontParameter);

		//health bar
		health_bar= new HealthBar(screen2);
		health_bar.hit=0;
		
		
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
		if(level2Score >= 10 && level2Score < 100) scoreText.draw(score_batch,level2Score.toString(), 1055, 560);
		else if(level2Score >= 100) scoreText.draw(score_batch,level2Score.toString(), 1050, 560);
		else scoreText.draw(score_batch,level2Score.toString(), 1062, 560);
		score_batch.end();
		
		//HEALTH BAR
		health_bar.drawHealthBar();
		
	}
	
	public void update(float delta)
	{
		renderer.setView(screen2.gamecam);
		
	}
	
			
	@Override
	public void resize(int width, int height)
	{
		screen2.gameport.update(width, height);
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