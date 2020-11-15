package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import hercules.game.main;

public class menu implements Screen
{
	private Viewport gameport;
	private SpriteBatch batch;
	private Texture menu_texture;
	private main game;  
	OrthographicCamera camera ;
	// BUTTONS 
	private Texture play_button;
	
	private Texture exit_button;
	

	
	public menu(  main game ) // CONSTRUCTOR 
	{
		this.game = game;
		gameport = new  StretchViewport(main.v_width / main.PPM, main.v_height / main.PPM,new OrthographicCamera());
		batch = new SpriteBatch();
		menu_texture = new Texture(Gdx.files.internal("meun_screen.jpg"));
		play_button = new Texture ("play_button.png");
		exit_button =  new Texture ("exit_button.png");
   }
 
	public void render(float delta )
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        batch.draw(menu_texture,0,0,1205,640);
       
       batch.draw(play_button,90,100,350,250);
      
       batch.draw(exit_button,90,-40,350,250);
       
       /***************************************************/
       
       // CLICKED ON play_button
       if(Gdx.input.getX()>90 && Gdx.input.getX() < 90+ play_button.getWidth() && Gdx.graphics.getHeight()-
       		Gdx.input.getY()>100 && Gdx.graphics.getHeight() -Gdx.input.getY() <100 + play_button.getHeight() ) {
       	if(Gdx.input.justTouched()) {
       		  main.click_sound.play(); 
       		game.setScreen(new PlayScreen(game)); 
       	}
       }
       /***************************************************/
    // CLICKED ON exit_button
       else if(Gdx.input.getX()>90 && Gdx.input.getX() < 90+ exit_button.getWidth() && Gdx.graphics.getHeight()-
    		Gdx.input.getY()>-40 && Gdx.graphics.getHeight() -Gdx.input.getY() <-40 + exit_button.getHeight() ) {
    	if(Gdx.input.justTouched()) {
    		  main.click_sound.play();
    		  Gdx.app.exit();
    	}
    }
      

       batch.end();
	}
	
	
	@Override
	public void show()
	{
		
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
	public void pause()
	{
		
		
	}

	@Override
	public void resume()
	{
		
	}

	@Override
	public void hide()
	{
		
	}


}
