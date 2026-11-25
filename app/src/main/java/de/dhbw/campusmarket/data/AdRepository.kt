package de.dhbw.campusmarket.data

import android.util.Log
import de.dhbw.campusmarket.data.network.AdApi
import de.dhbw.campusmarket.data.network.toAds
import kotlinx.coroutines.flow.Flow

private const val TAG = "AdRepository"

// The only way from the ViewModels to the data. Room is only used here (and in the DAO),
// the network only here (and in AdApi).
class AdRepository(private val adDao: AdDao, private val adApi: AdApi) {

    fun getAllAds(): Flow<List<Ad>> = adDao.getAll()

    fun getAd(id: Long): Flow<Ad?> = adDao.getById(id)

    suspend fun addAd(ad: Ad): Long = adDao.insert(ad)

    suspend fun updateAd(ad: Ad) = adDao.update(ad)

    suspend fun setFavorite(id: Long, isFavorite: Boolean) = adDao.setFavorite(id, isFavorite)

    suspend fun setSold(id: Long, isSold: Boolean) = adDao.setSold(id, isSold)

    suspend fun deleteAd(ad: Ad) = adDao.delete(ad)

    // Loads testdata/anzeigen.json and stores ads that are not in Room yet.
    // Room stays the single source of truth: the screens never read the network result directly.
    // Does not catch anything on purpose: IOException, HttpException and SerializationException
    // go to the ViewModel, which shows a matching message for each of them.
    // Returns how many ads were new.
    suspend fun refreshAdsFromNetwork(): Int {
        val dtos = adApi.getAds()
        val rowIds = adDao.insertOrIgnore(toAds(dtos, System.currentTimeMillis()))
        val newCount = rowIds.count { it != -1L }
        Log.i(TAG, "Loaded ${dtos.size} test ads, $newCount new")
        return newCount
    }
}
