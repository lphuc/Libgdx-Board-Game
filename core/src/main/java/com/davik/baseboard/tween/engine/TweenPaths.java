package com.davik.baseboard.tween.engine;


import com.davik.baseboard.tween.engine.paths.CatmullRom;
import com.davik.baseboard.tween.engine.paths.Linear;

public interface TweenPaths {
	public static final Linear linear = new Linear();
	public static final CatmullRom catmullRom = new CatmullRom();
}
