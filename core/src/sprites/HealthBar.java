package sprites;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import Tools.WorldContactListener;
import hercules.game.main;
import screens.PlayScreen;
import sprites.herclus.State;

public class HealthBar extends Sprite
{
	public static int hit=0;
	Texture []arr=new Texture[11];
	
	public boolean drink_juice = false;
	
	Texture bar0_texture=new Texture(Gdx.files.internal("health_bar0.png"));
	Texture bar1_texture=new Texture(Gdx.files.internal("health_bar1.png"));
	Texture bar2_texture=new Texture(Gdx.files.internal("health_bar2.png"));
	Texture bar3_texture=new Texture(Gdx.files.internal("health_bar3.png"));
	Texture bar4_texture=new Texture(Gdx.files.internal("health_bar4.png"));
	Texture bar5_texture=new Texture(Gdx.files.internal("health_bar5.png"));
	Texture bar6_texture=new Texture(Gdx.files.internal("health_bar6.png"));
	Texture bar7_texture=new Texture(Gdx.files.internal("health_bar7.png"));
	Texture bar8_texture=new Texture(Gdx.files.internal("health_bar8.png"));
	Texture bar9_texture=new Texture(Gdx.files.internal("health_bar9.png"));
	Texture bar10_texture=new Texture(Gdx.files.internal("health_bar10.png"));
	

	//health bar
	private Texture bar_texture;
	private SpriteBatch bar_batch;
	private BitmapFont HeartsNumberText ;
	FreeTypeFontGenerator.FreeTypeFontParameter fontParameter ;
	PlayScreen screen;
	
	public HealthBar(PlayScreen screen)
	{
	   this.screen = screen;
	   arr[0]=bar0_texture;
	   arr[1]=bar1_texture;
	   arr[2]=bar2_texture;
	   arr[3]=bar3_texture;
	   arr[4]=bar4_texture;
	   arr[5]=bar5_texture;
	   arr[6]=bar6_texture;
	   arr[7]=bar7_texture;
	   arr[8]=bar8_texture;
	   arr[9]=bar9_texture;
	   arr[10]=bar10_texture;
	   
		//health bar
		bar_texture = NextBar();
		bar_batch = new SpriteBatch();
		HeartsNumberText = new BitmapFont();
		fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 40;
        fontParameter.color = Color.GOLD;
        //Assigning the font generator to the bitmap font
        HeartsNumberText = screen.fontGenerator.generateFont(fontParameter);
		
	   
	}
	
	public Texture NextBar()
	{
		Texture next=arr[hit];
		hit++;
		return next;
	}
	
	public void drawHealthBar() {

		//health_bar
		bar_batch.begin();
		bar_batch.draw(bar_texture, 20, 480, 370, 150);
		HeartsNumberText.draw(bar_batch,screen.level1.HeartsNum.toString(), 94, 580);
		bar_batch.end();
		
	}
	
	public void drinkJuice() {
		if(hit < 11 && hit >6) {
			hit = 1; bar_texture = NextBar();
		}
		else if(hit < 11 && hit >2){
			hit = 2; bar_texture = NextBar();
		}
		main.juice_Collect_sound.play();
	}
	
	public void updateHealthBar() {
		
		if(hit < 11 && screen.level1.HeartsNum >= 0) {
			bar_texture = NextBar();
			main.hurt_sound.play();
		}
		else if(hit == 11 && screen.level1.HeartsNum > 0) {
			screen.player.currentState=State.die;
			screen.player.hercDie=true;
		    main.die_sound.play();
			hit = 0;
			screen.level1.HeartsNum--;
			bar_texture = NextBar();
		}
		else if(hit == 11 && screen.level1.HeartsNum == 0) screen.game_over_screen.die = true; 
	}	
	
}