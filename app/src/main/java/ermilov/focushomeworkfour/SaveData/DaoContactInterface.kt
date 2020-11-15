package ermilov.focushomeworkfour.SaveData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DaoContactInterface {
    @Query("SELECT * FROM Contact")
    fun getAll(): MutableList<Contact>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg contacts:Contact)
}