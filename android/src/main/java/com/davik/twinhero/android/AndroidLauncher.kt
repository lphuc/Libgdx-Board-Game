package com.davik.twinhero.android

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.davik.twinhero.TwinHero
import com.davik.twinhero.ICommonService

class AndroidLauncher : AndroidApplication(), ICommonService {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val configuration = AndroidApplicationConfiguration()
        configuration.useImmersiveMode = true // Recommended, but not required.
        initialize(TwinHero(this), configuration)
    }

    override fun setUpAndroidBilling() {
    }

    override fun showBuyShopItemForm(itemId: String?) {
    }

    override fun showInterstitialAd() {
    }

    override fun showVideoAds() {
    }

    override fun rateGame() {
    }

    override fun fetchRemoteConfig() {
    }
}