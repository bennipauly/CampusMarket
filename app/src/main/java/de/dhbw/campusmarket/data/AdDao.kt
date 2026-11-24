package de.dhbw.campusmarket.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
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

    @Query("SELECT COUNT(*) FROM ads")
    suspend fun count(): Int

    // Returns the new id.
    @Insert
    suspend fun insert(ad: Ad): Long

    @Insert
    suspend fun insertAll(ads: List<Ad>)

    @Update
    suspend fun update(ad: Ad)

    // Changes only this one column. This way a tap on the heart cannot overwrite other values with an old state.
    @Query("UPDATE ads SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    // Like setFavorite: changes only the column isSold.
    @Query("UPDATE ads SET isSold = :isSold WHERE id = :id")
    suspend fun setSold(id: Long, isSold: Boolean)

    @Delete
    suspend fun delete(ad: Ad)
}
