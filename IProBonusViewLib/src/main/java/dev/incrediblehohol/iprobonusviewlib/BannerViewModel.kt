package dev.incrediblehohol.iprobonusviewlib

import androidx.lifecycle.*
import dev.incrediblehohol.iprobonusapilib.IProBonusApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private const val DEF_TIME = "2020-05-07"
private const val DEF_QUANTITY: Long = 0

private const val SERVER_TIME_FORMAT = "yyyy-MM-dd"
private const val VIEW_TIME_FORMAT = "dd.MM"

class BannerViewModel(private val clientId: String, private val deviceId: String) : ViewModel() {

    private val _totalBonuses = MutableLiveData(DEF_QUANTITY)
    val totalBonuses: LiveData<Long> = _totalBonuses

    private val _burningBonuses = MutableLiveData(DEF_QUANTITY)
    val burningBonuses: LiveData<Long> = _burningBonuses

    private val _burningDate = MutableLiveData(DEF_TIME)
    val burningDate: LiveData<String> = _burningDate.map {
        val simpleDateFormat = SimpleDateFormat(SERVER_TIME_FORMAT, Locale.getDefault())
        val date = simpleDateFormat.parse(it).orNow()
        val sdf = SimpleDateFormat(VIEW_TIME_FORMAT, Locale.getDefault())
        sdf.format(date)
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val api = IProBonusApi.newInstance(clientId, deviceId)
            val info = api.getGeneralInfo()
            info?.let {
                _burningBonuses.postValue(it.forBurningQuantity.orIfNull { 0 })
                _totalBonuses.postValue(it.currentQuantity.orIfNull { 0 })
                _burningDate.postValue(it.dateBurning)
            }
        }
    }
}