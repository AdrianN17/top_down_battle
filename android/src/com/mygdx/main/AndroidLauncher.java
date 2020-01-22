package com.mygdx.main;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Escena_juego escena = new Base_Juego();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(escena, config);
	}
}
