package de.dhbw.campusmarket.data

import android.util.Log
import de.dhbw.campusmarket.data.network.AdApi
import de.dhbw.campusmarket.data.network.ConnectionChecker
import de.dhbw.campusmarket.data.network.toAds
import kotlinx.coroutines.flow.Flow

private const val TAG = "AdRepository"

// The only way from the ViewModels to the data. Room is only used here (and in the DAO),
// the network only here (and in AdApi and ConnectionChecker).
class AdRepository(
    private val adDao: AdDao,
    private val adApi: AdApi,
    private val connectionChecker: ConnectionChecker
) {

    fun getAllAds(): Flow<List<Ad>> = adDao.getAll()

    fun getAd(id: Long): Flow<Ad?> = adDao.getById(id)

    // Ads may only be saved with an internet connection (decision of the group).
    fun isOnline(): Boolean = connectionChecker.isOnline()

    suspend fun addAd(ad: Ad): Long = adDao.insert(ad)

    suspend fun updateAd(ad: Ad) = adDao.update(ad)

    suspend fun setFavorite(id: Long, isFavorite: Boolean) = adDao.setFavorite(id, isFavorite)

    suspend fun setSold(id: Long, isSold: Boolean) = adDao.setSold(id, isSold)

    suspend fun deleteAd(ad: Ad) = adDao.delete(ad)

    // Loads testdata/anzeigen.json into Room: new ads are inserted, existing test ads get the
    // current texts from GitHub (GitHub is the only source of the test data; favorite and sold stay).
    // Room stays the single source of truth: the screens never read the network result directly.
    // Does not catch anything on purpose: IOException, HttpException and SerializationException
    // go to the ViewModel, which shows a matching message for each of them.
    suspend fun refreshAdsFromNetwork() {
        val dtos = adApi.getAds()
        val ads = toAds(dtos, System.currentTimeMillis())
        val rowIds = adDao.insertOrIgnore(ads)
        for (dto in dtos) {
            adDao.updateTestData(
                remoteId = dto.remoteId,
                title = dto.title,
                description = dto.description,
                price = dto.price,
                category = dto.category,
                condition = dto.condition,
                location = dto.location,
                imageUri = dto.imageUrl
            )
        }
        val newCount = rowIds.count { it != -1L }
        Log.i(TAG, "Loaded ${dtos.size} test ads, $newCount new")
    }
}
