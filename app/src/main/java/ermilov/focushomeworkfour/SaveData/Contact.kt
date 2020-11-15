package ermilov.focushomeworkfour.SaveData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact (
    @PrimaryKey val number: String,
    @ColumnInfo(name = "Name") val nameContact: String?
)