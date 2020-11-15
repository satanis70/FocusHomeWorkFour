package ermilov.focushomeworkfour.SaveData

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Contact::class), version = 1)
abstract class DataBase : RoomDatabase(){
    abstract fun daoContact(): DaoContactInterface
}