package de.dhbw.campusmarket.data

import kotlinx.coroutines.flow.Flow

// Einziger Weg der ViewModels zu den Daten. Nur hier (und im DAO) wird Room benutzt.
class AdRepository(private val adDao: AdDao) {

    fun getAllAds(): Flow<List<Ad>> = adDao.getAll()

    fun getAd(id: Long): Flow<Ad?> = adDao.getById(id)

    suspend fun addAd(ad: Ad): Long = adDao.insert(ad)

    suspend fun updateAd(ad: Ad) = adDao.update(ad)

    suspend fun deleteAd(ad: Ad) = adDao.delete(ad)

    // Wird beim App-Start aufgerufen. Fügt die Beispieldaten nur ein, wenn die Tabelle leer ist,
    // damit sie nach einem Neustart nicht doppelt auftauchen.
    suspend fun insertSampleDataIfEmpty() {
        if (adDao.count() == 0) {
            adDao.insertAll(sampleAds(System.currentTimeMillis()))
        }
    }
}
