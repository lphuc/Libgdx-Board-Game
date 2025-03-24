package com.davik.twinhero.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class LocalDataManager {

    private static LocalDataManager instance;

    public static final String FIRST_TIME_LAUNCH = "first_time_launch";
    private static final String PREFS_NAME = "dead_turn_chest_rpg";
    public static final String PLAYER_ID = "playerId"; //use for login also
    public static final String DEVICE_ID = "device_id"; //use for login also
    public static final String MATMA = "matma"; //should be 8 characters

    public static final String WAVE_LEVEL = "wave_level";
    public static final String CHARACTER_LV = "character_lv";
    public static final String CHARACTER_XP = "character_xp";

    // Basic tutorial -> should be as short & simple as possible to increase player's first impression (IMPORTANT)
    public static final String FINISH_TUTORIAL_DRAG_UPDOWN = "finish_tutorial_drag_updown";

    //ADVANCED Tutorial -> don't show for new users, only check to show at appropriate time after players play a few WAR matches
    public static final String FINISH_TUTORIAL_A = "";
    public static final String FINISH_TUTORIAL_B = "";

    //special
    public static final String REJECT_REVIEW_GAME = "reject_review_game";

    public static final String INTERSTITIAL_FOR_TEST = "ca-app-pub-3940256099942544/1033173712";
    public static final String INTERSTITIAL_FOR_RELEASE = "ca-app-pub-2590987192817381/7675494470";
    public static final String VIDEO_ID_FOR_TEST = "ca-app-pub-3940256099942544/5224354917";
    public static final String VIDEO_ID_FOR_RELEASE = "ca-app-pub-2590987192817381/3775777912";
    public static final String GDPR_ACCEPTED = "gdpr_accepted";

    public static final String PRIVACY_ACCEPTED = "privacy_accepted";

    private LocalDataManager() {

    }

    public static LocalDataManager INST() {
        if (instance == null) {
            synchronized (LocalDataManager.class) {
                if (instance == null) {
                    instance = new LocalDataManager();
                }
            }
        }
        return instance;
    }

    public static class Settings {
        public static final String SOUND_ON = "is_sound_on";
        public static final String MUSIC_ON = "is_music_on";
    }

    public Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }
}
