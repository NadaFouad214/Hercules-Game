package screens;

import java.util.EventListener;

import javax.swing.Spring;

import com.badlogic.ashley.utils.Bag;
import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import hercules.game.main;
import hercules.game.*;
public class GameOver implements Screen 
{
	private Viewport gameport;
 //   private Game game;
    private SpriteBatch batch;
    private Texture gameOver_texture;
    
    public  static boolean die = false;
    private Texture back_texture;
    Sound sound ; 
    BitmapFont scoreText ;
	FreeTypeFontGenerator.FreeTypeFontParameter fontParameter ; //to load font
	PlayScreen screen;
    private main game;
    
    public GameOver(main game,PlayScreen screen)
    {
    	this.screen = screen;
        this.game = game;
       
        gameport = new  StretchViewport(main.v_width / main.PPM, main.v_height / main.PPM,new OrthographicCamera());
        batch = new SpriteBatch();
        
        gameOver_texture = new Texture(Gdx.files.internal("game_over.jpg"));
        back_texture = new Texture(Gdx.files.internal("button5.png"));
       
        sound = Gdx.audio.newSound(Gdx.files.internal("gameover.wav"));
        main.getSound().stop();
		sound.play();
		
		scoreText = new BitmapFont();
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 60;
        fontParameter.color = Color.WHITE;
        scoreText = screen.fontGenerator.generateFont(fontParameter);
		
    }
    
    
	@Override
	public void render(float delta) 
	{ 
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
       batch.begin();
       batch.draw(gameOver_texture,0,0,1200,640);
       batch.draw(back_texture,450,-80,350,250);
     
       scoreText.draw(batch,screen.level1.level1Score.toString(), 760, 235);
       scoreText.draw(batch,"HIGH SCORE : ", 410, 240);
       
      // CLICKED ON play_button
       if(Gdx.input.getX()>470 && Gdx.input.getX() < 470+ back_texture.getWidth() && Gdx.graphics.getHeight()-
       		Gdx.input.getY()>-10 && Gdx.graphics.getHeight() -Gdx.input.getY()<-10+back_texture.getHeight() ) {
       	if(Gdx.input.justTouched()) {
       		game.setScreen(new menu(game)); 
       		main.click_sound.play();
       	}
       }
        batch.end();
	}
	
	

	

	@Override
	public void resize(int width, int height)
	{
		gameport.update(width, height);
	}

	@Override
	public void dispose()
	{
		
	}
	
	@Override
	public void pause() {
	
		
	}
	
	@Override
	public void show()
	{
		
	}

	@Override
	public void resume() {
	
		
	}

	@Override
	public void hide() {
		
		
	}

	
	
}
