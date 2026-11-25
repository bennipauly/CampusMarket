package de.dhbw.campusmarket.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// All database access to the table "ads". Room generates the implementation during the build (KSP).
@Dao
interface AdDao {

    // Flow: as soon as the table changes, Room delivers the new list automatically.
    @Query("SELECT * FROM ads ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Ad>>

    // null if there is no ad with this id (e.g. after deleting).
    @Query("SELECT * FROM ads WHERE id = :id")
    fun getById(id: Long): Flow<Ad?>

    // Returns the new id.
    @Insert
    suspend fun insert(ad: Ad): Long

    // For the test data. IGNORE: an ad whose remoteId already exists is skipped, so the local
    // state (favorite, sold) is not overwritten on refresh. Its texts are updated by updateTestData.
    // Returns the new row ids; -1 means "already there, skipped".
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(ads: List<Ad>): List<Long>

    @Update
    suspend fun update(ad: Ad)

    // For test ads that already exist: takes over the texts and the image from the JSON file.
    // isFavorite, isSold and createdAt are not touched, so the local state stays.
    @Query(
        "UPDATE ads SET title = :title, description = :description, price = :price, " +
            "category = :category, condition = :condition, location = :location, imageUri = :imageUri " +
            "WHERE remoteId = :remoteId"
    )
    suspend fun updateTestData(
        remoteId: Long,
        title: String,
        description: String,
        price: Double,
        category: Category,
        condition: Condition,
        location: String,
        imageUri: String?
    )

    // Changes only this one column. This way a tap on the heart cannot overwrite other values with an old state.
    @Query("UPDATE ads SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    // Like setFavorite: changes only the column isSold.
    @Query("UPDATE ads SET isSold = :isSold WHERE id = :id")
    suspend fun setSold(id: Long, isSold: Boolean)

    @Delete
    suspend fun delete(ad: Ad)
}
