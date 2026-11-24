package de.dhbw.campusmarket.data

import android.util.Log
import kotlinx.coroutines.flow.Flow

private const val TAG = "AdRepository"

// The only way from the ViewModels to the data. Room is only used here (and in the DAO).
class AdRepository(private val adDao: AdDao) {

    fun getAllAds(): Flow<List<Ad>> = adDao.getAll()

    fun getAd(id: Long): Flow<Ad?> = adDao.getById(id)

    suspend fun addAd(ad: Ad): Long = adDao.insert(ad)

    suspend fun updateAd(ad: Ad) = adDao.update(ad)

    suspend fun setFavorite(id: Long, isFavorite: Boolean) = adDao.setFavorite(id, isFavorite)

    suspend fun setSold(id: Long, isSold: Boolean) = adDao.setSold(id, isSold)

    suspend fun deleteAd(ad: Ad) = adDao.delete(ad)

    // Called when the app starts. Inserts the sample data only if the table is empty,
    // so that it does not appear twice after a restart.
    suspend fun insertSampleDataIfEmpty() {
        if (adDao.count() == 0) {
            val ads = sampleAds(System.currentTimeMillis())
            adDao.insertAll(ads)
            Log.i(TAG, "Database was empty, inserted ${ads.size} sample ads")
        }
    }
}
