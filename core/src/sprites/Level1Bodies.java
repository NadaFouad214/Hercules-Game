package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import hercules.game.main;
import screens.PlayScreen;

public class Level1Bodies extends Sprite{
 
	public Body body;
	public float positionX;
	public float positionY;
	public int num;
	public int hit_num = 3;
	public boolean destroy = false;
	
	public Level1Bodies(Body body , float x , float y ,int num) {
		this.body = body;
		positionX = x;
		positionY = y;
		this.num = num;
	}
	
	public void update_hit_object(int num) {
		if(destroy == false) {
			if(hit_num <= 0) destroy = true;
			else hit_num -=num;
		}
	}
	
}
