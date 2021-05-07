package dev.incrediblehohol.iprobonusviewlib

import androidx.lifecycle.*
import dev.incrediblehohol.iprobonusapilib.IProBonusApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BannerViewModel(private val clientId: String, private val deviceId: String) : ViewModel() {

    private val _totalBonuses = MutableLiveData<Long>(0)
    val totalBonuses: LiveData<Long> = _totalBonuses

    private val _burningBonuses = MutableLiveData<Long>(0)
    val burningBonuses: LiveData<Long> = _burningBonuses

    private val _burningDate = MutableLiveData("2020-05-07")
    val burningDate: LiveData<String> = _burningDate.map {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = simpleDateFormat.parse(it).orNow()
        val sdf = SimpleDateFormat("dd.MM", Locale.getDefault())
        sdf.format(date)
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val api = IProBonusApi(clientId, deviceId)
            val info = api.getGeneralInfo()
            info?.let {
                _burningBonuses.postValue(it.forBurningQuantity)
                _totalBonuses.postValue(it.currentQuantity)
                _burningDate.postValue(it.dateBurning)
            }
        }
    }
}