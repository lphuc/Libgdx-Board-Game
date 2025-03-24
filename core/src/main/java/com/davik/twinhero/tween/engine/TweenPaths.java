package com.davik.twinhero.tween.engine;


import com.davik.twinhero.tween.engine.paths.CatmullRom;
import com.davik.twinhero.tween.engine.paths.Linear;

public interface TweenPaths {
	public static final Linear linear = new Linear();
	public static final CatmullRom catmullRom = new CatmullRom();
}
