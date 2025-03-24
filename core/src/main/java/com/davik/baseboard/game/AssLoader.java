package com.davik.baseboard.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;

public class AssLoader implements Disposable, AssetErrorListener {
    /**
     * scaleUnit -> multiply with all game objects to scale their size across screens density
     * WARNING: this unit is only plus with skeleton, font, images...don't use with Box2D unit
     */
    public static float scaleUnit = ((Gdx.graphics.getHeight() / 1920f + Gdx.graphics.getWidth() / 1080f) / 2f);
    public static float BOX2D_DEFAULT_SCALE = 0.001f; //scale ratio for Spine Skeleton size when render in Box2D world (meter unit)

    private static AssLoader instance;

    public SkeletonJson mainTreeJson, mainTree2Json, mainTree3Json, midTree1Json, midTree2Json, midTree3Json, grassJson, foreGrassJson, slotJson;
    public com.badlogic.gdx.scenes.scene2d.ui.Skin guiSkin;
    public BitmapFont giantBitmap, resultScoreBimap, bigBitmap, tierBitmap, bigBoldBitmap, smallBitmap, mediumBitmap, tinyBitmap, superTinyBitmap;
    public BitmapFont genericBoldBitmap, genericBitmap, genericTinyBitmap;
    public Label.LabelStyle labelStyleSuperTiny = new Label.LabelStyle();
    public Label.LabelStyle labelStyleTiny = new Label.LabelStyle();
    public Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
    public Label.LabelStyle labelStyleMedium = new Label.LabelStyle();
    public Label.LabelStyle labelStyleBig = new Label.LabelStyle();
    public Label.LabelStyle labelStyleTier = new Label.LabelStyle();
    public Label.LabelStyle labelStyleBigBold = new Label.LabelStyle();
    public Label.LabelStyle labelStyleGiant = new Label.LabelStyle();
    public Label.LabelStyle labelStyleScore = new Label.LabelStyle();
    public Label.LabelStyle genericLabelStyle = new Label.LabelStyle();
    public Label.LabelStyle genericBoldStyle = new Label.LabelStyle();
    public Label.LabelStyle genericTinyStyle = new Label.LabelStyle();

    public TextureAtlas atlasStatic, guiSkinAtlas, guiImageAtlas, slotItemAtlas;

    private AssetManager assetManager;

    //free-type font
    private final FreeTypeFontGenerator genericFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/unifont.otf"));
    private final FreeTypeFontGenerator.FreeTypeFontParameter genericParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
    private final FreeTypeFontGenerator.FreeTypeFontParameter genericBoldParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
    private final FreeTypeFontGenerator.FreeTypeFontParameter genericTinyParams = new FreeTypeFontGenerator.FreeTypeFontParameter();

    private AssLoader() {
    }

    private final AssetDescriptor<TextureAtlas> atlasStaticDesc = new AssetDescriptor<>("static/static.atlas", TextureAtlas.class);
    private final AssetDescriptor<TextureAtlas> guiSkinDesc = new AssetDescriptor<>("skin/gui_skin.atlas", TextureAtlas.class);

    private final AssetDescriptor<TextureAtlas> atlasMainTreeDesc = new AssetDescriptor<>("spine/main_tree.atlas", TextureAtlas.class);
    private final AssetDescriptor<TextureAtlas> atlasMainTree2Desc = new AssetDescriptor<>("spine/main_tree2.atlas", TextureAtlas.class);
    private final AssetDescriptor<TextureAtlas> atlasMainTree3Desc = new AssetDescriptor<>("spine/main_tree3.atlas", TextureAtlas.class);
    private final AssetDescriptor<TextureAtlas> atlasMidTree1Desc = new AssetDescriptor<>("spine/mid_tree1.atlas", TextureAtlas.class);
    private final AssetDescriptor<TextureAtlas> atlasMidTree2Desc = new AssetDescriptor<>("spine/mid_tree2.atlas", TextureAtlas.class);
    private final AssetDescriptor<TextureAtlas> atlasMidTree3Desc = new AssetDescriptor<>("spine/mid_tree3.atlas", TextureAtlas.class);
    private final AssetDescriptor<TextureAtlas> atlasGrassDesc = new AssetDescriptor<>("spine/grass.atlas", TextureAtlas.class);

    private final AssetDescriptor<TextureAtlas> atlasForeGrassDesc = new AssetDescriptor<>("spine/fore_grass.atlas", TextureAtlas.class);
    private final AssetDescriptor<TextureAtlas> guiImageAtlasDesc = new AssetDescriptor<>("skin/gui_images.atlas", TextureAtlas.class);
    private final AssetDescriptor<TextureAtlas> atlasSlotDesc = new AssetDescriptor<>("spine/spine_slot.atlas", TextureAtlas.class);

    //world box2D sound
    public Sound weaponHitBodySound, weaponStickBodySound, itemSpawnSound, itemDisappearSound, itemDropSound,dieGruntSound, bodyHitGroundSound, throwSound;
    //gui sound
    public Sound itemGoChestSound, chestClickSound, popupShowSound, clickSound, clickSound2, clickSound3, clickSound4, menuTabSound,
            claimSound, openBagSound, openBoxSound, chestCloseSound, hidePopupShow, levelUpSound, equipItemSound, equipAllSound,
            changeSlotSound, toastSound, toggleSound, upgradeFailedSound, upgradeSuccessSound, boughtItemSound, wooshSound, wooshEndSound,
            wrongSlotSound, alertSound, matchEndSound, matchWinSound, matchLoseSound, jackpotSound, jackpotSound2, shakingSound,
            activateSound, drumRollSound, warWin, warLose;

    public boolean resourceLoaded = false; //only change to true one time in game

    public static AssLoader INST() {
        if (instance == null) {
            instance = new AssLoader();
        }
        return instance;
    }

    // must init all resources here otherwise black screen will appear when game restart (split screen)
    public void initResources() {
        assetManager = new AssetManager();
        resourceLoaded = false; //re-assign to false otherwise black screen when game restart
        assetManager.load(atlasStaticDesc);
        assetManager.load(guiImageAtlasDesc);
        assetManager.load(guiSkinDesc);
        assetManager.load(atlasMainTreeDesc);
        assetManager.load(atlasMainTree2Desc);
        assetManager.load(atlasMainTree3Desc);
        assetManager.load(atlasMidTree1Desc);
        assetManager.load(atlasMidTree2Desc);
        assetManager.load(atlasMidTree3Desc);
        assetManager.load(atlasGrassDesc);
        assetManager.load(atlasForeGrassDesc);
        assetManager.load(atlasSlotDesc);

        assetManager.load("sounds/menu_music.mp3", Sound.class);
        assetManager.load("sounds/music1.mp3", Sound.class);
        assetManager.load("sounds/click.mp3", Sound.class);
        assetManager.load("sounds/click2.mp3", Sound.class);
        assetManager.load("sounds/click3.mp3", Sound.class);
        assetManager.load("sounds/click4.mp3", Sound.class);
        assetManager.load("sounds/toggle_on_off.mp3", Sound.class);
        assetManager.load("sounds/upgrade_success.mp3", Sound.class);
        assetManager.load("sounds/bought_item.mp3", Sound.class);
        assetManager.load("sounds/toast.mp3", Sound.class);
        assetManager.load("sounds/menu_tab.mp3", Sound.class);
        assetManager.load("sounds/claim.mp3", Sound.class);
        assetManager.load("sounds/hide_popup.mp3", Sound.class);
        assetManager.load("sounds/chest_click.mp3", Sound.class);
        assetManager.load("sounds/popup_show2.mp3", Sound.class);
        assetManager.load("sounds/chest_close.mp3", Sound.class);
        assetManager.load("sounds/item_drop.mp3", Sound.class);
        assetManager.load("sounds/weapon_stick_body.ogg", Sound.class);
        assetManager.load("sounds/hit.mp3", Sound.class);
        assetManager.load("sounds/item_spawn.ogg", Sound.class);
        assetManager.load("sounds/item_disappear.mp3", Sound.class);
        assetManager.load("sounds/item_go_chest.ogg", Sound.class);
        assetManager.load("sounds/level_up.mp3", Sound.class);
        assetManager.load("sounds/equip_item2.mp3", Sound.class);
        assetManager.load("sounds/equip_all.mp3", Sound.class);
        assetManager.load("sounds/open_bag.mp3", Sound.class);
        assetManager.load("sounds/open_box.mp3", Sound.class);
        assetManager.load("sounds/jackpot.mp3", Sound.class);
        assetManager.load("sounds/jackpot2.mp3", Sound.class);
        assetManager.load("sounds/shaking.mp3", Sound.class);
        assetManager.load("sounds/change_item_slot.mp3", Sound.class);
        assetManager.load("sounds/body_hit_ground.mp3", Sound.class);
        assetManager.load("sounds/woosh.mp3", Sound.class);
        assetManager.load("sounds/woosh2.mp3", Sound.class);
        assetManager.load("sounds/die.mp3", Sound.class);

        assetManager.load("sounds/activate_pillar.mp3", Sound.class);
        assetManager.load("sounds/drum_roll.mp3", Sound.class);
        assetManager.load("sounds/war_win.mp3", Sound.class);
        assetManager.load("sounds/match_lose2.mp3", Sound.class);
        assetManager.load("sounds/level_failed.mp3", Sound.class);
        assetManager.load("sounds/match_lose.mp3", Sound.class);
        assetManager.load("sounds/match_win.mp3", Sound.class);
        assetManager.load("sounds/upgrade_failed.mp3", Sound.class);
        assetManager.load("sounds/wrong_slot.mp3", Sound.class);
        assetManager.load("sounds/upgrade_success.mp3", Sound.class);
        assetManager.load("sounds/match_end.mp3", Sound.class);
        assetManager.load("sounds/alert.mp3", Sound.class);
        initFont();
    }

    /**
     * assign all loaded resources to be used
     */
    public void assignResources() {
        resourceLoaded = true;
        atlasStatic = assetManager.get(atlasStaticDesc);
        guiImageAtlas = assetManager.get(guiImageAtlasDesc);
        guiSkinAtlas = assetManager.get(guiSkinDesc);
        guiSkin = new com.badlogic.gdx.scenes.scene2d.ui.Skin(Gdx.files.internal("skin/gui_skin.json"), guiSkinAtlas);
        guiSkin.add("default-font", mediumBitmap);

        slotItemAtlas = assetManager.get(atlasSlotDesc);
        slotJson = new SkeletonJson(slotItemAtlas);
        slotJson.setScale(Gdx.graphics.getHeight() / 720f);
        assignSceneAsset();
        assignSoundAsset();
    }

    private void initFont() {
        //chinese glyphs can support all other language glyphs
        genericParams.size = 40;
        genericParams.color = Color.WHITE;
        genericParams.borderWidth = 1;
        genericParams.incremental = true; // WARNING: must set true for large pixmap file
        genericBitmap = genericFontGenerator.generateFont(genericParams);
        genericLabelStyle.font = genericBitmap;

        genericBoldParams.size = 30;
        genericBoldParams.color = Color.WHITE;
        genericBoldParams.borderWidth = 0.8f;
        genericBoldParams.borderColor = Color.WHITE;
        genericBoldParams.incremental = true; // WARNING: must set true for large pixmap file
        genericBoldBitmap = genericFontGenerator.generateFont(genericBoldParams);
        genericBoldBitmap.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        genericBoldStyle.font = genericBoldBitmap;

        genericTinyParams.size = 20;
        genericTinyParams.color = Color.WHITE;
        genericTinyParams.borderWidth = 0.8f;
        genericTinyParams.borderColor = Color.WHITE;
        genericTinyParams.incremental = true; // WARNING: must set true for large pixmap file
        genericTinyBitmap = genericFontGenerator.generateFont(genericTinyParams);
        genericTinyStyle.font = genericTinyBitmap;

        giantBitmap = new BitmapFont(Gdx.files.internal("fonts/lingming_manuscript_giant.fnt"), Gdx.files.internal("fonts/lingming_manuscript_giant.png"), false);
        labelStyleGiant.font = giantBitmap;

        resultScoreBimap = new BitmapFont(Gdx.files.internal("fonts/result_score.fnt"), Gdx.files.internal("fonts/result_score.png"), false);
        resultScoreBimap.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        labelStyleScore.font = resultScoreBimap;

        bigBitmap = new BitmapFont(Gdx.files.internal("fonts/lingming_manuscript_big.fnt"), Gdx.files.internal("fonts/lingming_manuscript_big.png"), false);
        labelStyleBig.font = bigBitmap;

        bigBoldBitmap = new BitmapFont(Gdx.files.internal("fonts/lingming_manuscript_big_bold.fnt"), Gdx.files.internal("fonts/lingming_manuscript_big_bold.png"), false);
        bigBoldBitmap.setUseIntegerPositions(false);
        bigBoldBitmap.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        labelStyleBigBold.font = bigBoldBitmap;

        mediumBitmap = new BitmapFont(Gdx.files.internal("fonts/lingming_manuscript_medium.fnt"), Gdx.files.internal("fonts/lingming_manuscript_medium.png"), false);
        labelStyleMedium.font = mediumBitmap;

        smallBitmap = new BitmapFont(Gdx.files.internal("fonts/lingming_manuscript_small.fnt"), Gdx.files.internal("fonts/lingming_manuscript_small.png"), false);
        labelStyleSmall.font = smallBitmap;

        tinyBitmap = new BitmapFont(Gdx.files.internal("fonts/lingming_manuscript_tiny.fnt"), Gdx.files.internal("fonts/lingming_manuscript_tiny.png"), false);
        labelStyleTiny.font = tinyBitmap;

        superTinyBitmap = new BitmapFont(Gdx.files.internal("fonts/super_tiny.fnt"), Gdx.files.internal("fonts/super_tiny.png"), false);
        labelStyleSuperTiny.font = superTinyBitmap;

        tierBitmap = new BitmapFont(Gdx.files.internal("fonts/MorrisRoman_Black.fnt"), Gdx.files.internal("fonts/MorrisRoman_Black.png"), false);
        labelStyleTier.font = tierBitmap;
    }

    private void assignSoundAsset() {
        weaponHitBodySound = assetManager.get("sounds/hit.mp3", Sound.class);
        weaponStickBodySound = assetManager.get("sounds/weapon_stick_body.ogg", Sound.class);
        itemSpawnSound = assetManager.get("sounds/item_spawn.ogg", Sound.class);
        itemDisappearSound = assetManager.get("sounds/item_disappear.mp3", Sound.class);
        itemDropSound = assetManager.get("sounds/item_drop.mp3", Sound.class);
        itemGoChestSound = assetManager.get("sounds/item_go_chest.ogg", Sound.class);
        chestClickSound = assetManager.get("sounds/chest_click.mp3", Sound.class);
        popupShowSound = assetManager.get("sounds/popup_show2.mp3", Sound.class);
        chestCloseSound = assetManager.get("sounds/chest_close.mp3", Sound.class);
        hidePopupShow = assetManager.get("sounds/hide_popup.mp3", Sound.class);
        levelUpSound = assetManager.get("sounds/level_up.mp3", Sound.class);
        equipItemSound = assetManager.get("sounds/equip_item2.mp3", Sound.class);
        equipAllSound = assetManager.get("sounds/equip_all.mp3", Sound.class);
        changeSlotSound = assetManager.get("sounds/change_item_slot.mp3", Sound.class);
        toastSound = assetManager.get("sounds/toast.mp3", Sound.class);
        upgradeFailedSound = assetManager.get("sounds/upgrade_failed.mp3", Sound.class);
        upgradeSuccessSound = assetManager.get("sounds/upgrade_success.mp3", Sound.class);
        matchEndSound = assetManager.get("sounds/match_end.mp3", Sound.class);
        matchWinSound = assetManager.get("sounds/match_win.mp3", Sound.class);
        matchLoseSound = assetManager.get("sounds/match_lose.mp3", Sound.class);
        boughtItemSound = assetManager.get("sounds/bought_item.mp3", Sound.class);
        clickSound = assetManager.get("sounds/click.mp3", Sound.class);
        clickSound2 = assetManager.get("sounds/click2.mp3", Sound.class);
        clickSound3 = assetManager.get("sounds/click3.mp3", Sound.class);
        clickSound4 = assetManager.get("sounds/click4.mp3", Sound.class);
        toggleSound = assetManager.get("sounds/toggle_on_off.mp3", Sound.class);
        menuTabSound = assetManager.get("sounds/menu_tab.mp3", Sound.class);
        claimSound = assetManager.get("sounds/claim.mp3", Sound.class);
        bodyHitGroundSound = assetManager.get("sounds/body_hit_ground.mp3", Sound.class);
        openBagSound = assetManager.get("sounds/open_bag.mp3", Sound.class);
        openBoxSound = assetManager.get("sounds/open_box.mp3", Sound.class);
        jackpotSound = assetManager.get("sounds/jackpot.mp3", Sound.class);
        jackpotSound2 = assetManager.get("sounds/jackpot2.mp3", Sound.class);
        shakingSound = assetManager.get("sounds/shaking.mp3", Sound.class);
        wooshSound = assetManager.get("sounds/woosh.mp3", Sound.class);
        wooshEndSound = assetManager.get("sounds/woosh2.mp3", Sound.class);
        wrongSlotSound = assetManager.get("sounds/wrong_slot.mp3", Sound.class);
        alertSound = assetManager.get("sounds/alert.mp3", Sound.class);
        dieGruntSound = assetManager.get("sounds/die.mp3", Sound.class);
        activateSound = assetManager.get("sounds/activate_pillar.mp3", Sound.class);
        drumRollSound = assetManager.get("sounds/drum_roll.mp3", Sound.class);
        warWin = assetManager.get("sounds/war_win.mp3", Sound.class);
        warLose = assetManager.get("sounds/match_lose2.mp3", Sound.class);
    }

    private void assignSceneAsset() {
        AtlasAttachmentLoader atlasLoader = new AtlasAttachmentLoader(assetManager.get(atlasMainTreeDesc));
        AtlasAttachmentLoader atlasLoader2 = new AtlasAttachmentLoader(assetManager.get(atlasMainTree2Desc));
        AtlasAttachmentLoader atlasLoader3 = new AtlasAttachmentLoader(assetManager.get(atlasMainTree3Desc));
        midTree1Json = new SkeletonJson(new AtlasAttachmentLoader(assetManager.get(atlasMidTree1Desc)));
        midTree1Json.setScale(0.8f);

        midTree2Json = new SkeletonJson(new AtlasAttachmentLoader(assetManager.get(atlasMidTree2Desc)));
        midTree2Json.setScale(0.8f);

        midTree3Json = new SkeletonJson(new AtlasAttachmentLoader(assetManager.get(atlasMidTree3Desc)));
        midTree3Json.setScale(0.8f);

        mainTreeJson = new SkeletonJson(atlasLoader);
        mainTreeJson.setScale(0.8f);
        mainTree2Json = new SkeletonJson(atlasLoader2);
        mainTree2Json.setScale(0.8f);
        mainTree3Json = new SkeletonJson(atlasLoader3);
        mainTree3Json.setScale(0.8f);

        grassJson = new SkeletonJson(assetManager.get(atlasGrassDesc));
        grassJson.setScale(0.8f);
        foreGrassJson = new SkeletonJson(assetManager.get(atlasForeGrassDesc));
        foreGrassJson.setScale(1f);
    }

    // TODO: 4/19/2023 carefully check again (maybe should dispose all sounds as well)
    @Override
    public void dispose() {
        assetManager.dispose();
        atlasStatic.dispose();
        guiImageAtlas.dispose();
        guiSkinAtlas.dispose();
        guiSkin.dispose();
        giantBitmap.dispose();
        bigBitmap.dispose();
        mediumBitmap.dispose();
        smallBitmap.dispose();
        tinyBitmap.dispose();
        bigBoldBitmap.dispose();
        tierBitmap.dispose();
        resultScoreBimap.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error("DeadTurn", "Couldn't load asset '" + asset.fileName + "'", throwable);
    }
}
