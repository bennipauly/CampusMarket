package de.dhbw.campusmarket.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Alle Datenbankzugriffe auf die Tabelle "ads". Room erzeugt die Implementierung beim Build (KSP).
@Dao
interface AdDao {

    // Flow: Sobald sich die Tabelle ändert, liefert Room automatisch die neue Liste.
    @Query("SELECT * FROM ads ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Ad>>

    // null, wenn es keine Anzeige mit dieser id gibt (z. B. nach dem Löschen).
    @Query("SELECT * FROM ads WHERE id = :id")
    fun getById(id: Long): Flow<Ad?>

    @Query("SELECT COUNT(*) FROM ads")
    suspend fun count(): Int

    // Gibt die neue id zurück.
    @Insert
    suspend fun insert(ad: Ad): Long

    @Insert
    suspend fun insertAll(ads: List<Ad>)

    @Update
    suspend fun update(ad: Ad)

    // Ändert nur die eine Spalte. So kann ein Tipp aufs Herz keine anderen Werte mit altem Stand überschreiben.
    @Query("UPDATE ads SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    // Wie setFavorite: ändert nur die Spalte isSold.
    @Query("UPDATE ads SET isSold = :isSold WHERE id = :id")
    suspend fun setSold(id: Long, isSold: Boolean)

    @Delete
    suspend fun delete(ad: Ad)
}
