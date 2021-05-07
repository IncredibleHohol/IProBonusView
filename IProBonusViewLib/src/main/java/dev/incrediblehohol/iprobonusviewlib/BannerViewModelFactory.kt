package dev.incrediblehohol.iprobonusviewlib

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BannerViewModelFactory(private val clientId: String, private val deviceId: String) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        BannerViewModel(clientId, deviceId) as T

}