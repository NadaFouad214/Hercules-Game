package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import hercules.game.main;

public class DesktopLauncher
{
	public static void main (String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		  config.title = "HERCULES";
	      config.width = 1200;
	      config.height = 624; 
	      config.x = 200;
	      config.y = 150;
		new LwjglApplication(new main(), config);
	}
}
