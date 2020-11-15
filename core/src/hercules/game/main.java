package hercules.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import screens.*;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class main extends Game
{
	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	public static final int v_width = 400;
	public static final int v_height = 208;
	public static final float PPM = 100;  //pixels per meter
	
	//Box2D Collision Bits
	public static final short GROUND_BIT = 1;
	public static final short HERCLUES_BIT = 2;
	public static final short COIN_BIT = 4;
	public static final short PUNCH_BAG_BIT = 8;
	public static final short PILLAR_BIT = 16;
	public static final short BIG_STATUE_BIT = 32;
	public static final short BIRD_BIT = 64;
	public static final short WOODEN_ENEMY_BIT = 128;
	public static final short JUICE_BIT = 256;
	public static final short SHIELD_BIT = 512;
	public static final short CHECK_POINT_BIT = 1024;
	public static final short FIRE_SWORD_BIT = 2048;
	public static final short LEFT_WALL_BIT = 4096;
	public static final short RIGHT_WALL_BIT = 8192;
	public static final short BROWN_MAN_BIT = 16384;
	
	public SpriteBatch batch;
	
	//sounds
	public static Music gameSound ;
	public static Sound punch_sound;
	public static Sound sword_sound;
	public static Sound coin_sound;
	public static Sound die_sound;
	public static Sound jump_sound;
	public static Sound super_sword_sound;
	public static Sound woodenMove_sound;
	public static Sound hurt_sound;
	public static Sound punch_bag_sound;
	public static Sound Pillar_Crash_sound;
	public static Sound juice_Collect_sound;
	public static Sound click_sound;
	public static Sound MonsterDeath_sound;
	
	@Override
	public void create ()
	{
		
		//sounds 
		punch_sound = Gdx.audio.newSound(Gdx.files.internal("Punch sound.mp3"));
        sword_sound = Gdx.audio.newSound(Gdx.files.internal("Sword sound (1).mp3"));
        coin_sound = Gdx.audio.newSound(Gdx.files.internal("coin.mp3"));
        jump_sound = Gdx.audio.newSound(Gdx.files.internal("jump.mp3"));
        super_sword_sound = Gdx.audio.newSound(Gdx.files.internal("super sword1.mp3"));
        die_sound = Gdx.audio.newSound(Gdx.files.internal("die.mp3"));
        woodenMove_sound = Gdx.audio.newSound(Gdx.files.internal("woodenMove.wav"));
        hurt_sound = Gdx.audio.newSound(Gdx.files.internal("hurt.wav"));
        punch_bag_sound = Gdx.audio.newSound(Gdx.files.internal("punch_bag.wav"));
        Pillar_Crash_sound = Gdx.audio.newSound(Gdx.files.internal("pillar crash.mp3"));
        juice_Collect_sound = Gdx.audio.newSound(Gdx.files.internal("vectory.mp3"));
        click_sound = Gdx.audio.newSound(Gdx.files.internal("click_sound.mp3"));
        MonsterDeath_sound=Gdx.audio.newSound(Gdx.files.internal("Monster Death.mp3"));
        
		batch = new SpriteBatch();
		gameSound = Gdx.audio.newMusic(Gdx.files.internal("game_sound.mp3"));
		gameSound.setLooping(true);
		gameSound.setVolume(1f);
		//gameSound.play();
		setScreen(new menu(this));
		
	}

	@Override
	public void render () 
	{
		super.render();
	}
	
	@Override
	public void dispose () 
	{
		gameSound.dispose();
	}

	public static Music getSound()
	{
		return gameSound;
	}
}