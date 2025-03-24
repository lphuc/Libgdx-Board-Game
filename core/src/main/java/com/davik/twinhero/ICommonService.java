package com.davik.twinhero;

public interface ICommonService {
    void setUpAndroidBilling();

    void showBuyShopItemForm(String itemId);

    void showInterstitialAd();

    void showVideoAds();

    void rateGame();

    void fetchRemoteConfig();
}
