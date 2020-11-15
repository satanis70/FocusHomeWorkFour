package ermilov.focushomeworkfour.SaveData

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.room.Room
import ermilov.focushomeworkfour.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.six_work.*

class SaveDataActivity : AppCompatActivity() {


    companion object{
        var namePhoneMap: MutableMap<String, String> = HashMap()
        var mutableList: MutableList<Contact> = ArrayList()
        var resultList: MutableList<Contact> = ArrayList()

    }
     @SuppressLint("CheckResult")
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.six_work)

        val resolver = applicationContext.contentResolver
        val cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null)

        while(cursor?.moveToNext()!!){
            val name: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            phoneNumber.replace("[()\\s-]+", "")
            namePhoneMap[phoneNumber] = name

        }
         val db = Room.databaseBuilder(applicationContext, DataBase::class.java, "DbContact").build()
         for ((key, value) in namePhoneMap) {
            val contact = Contact(key, value)
            Observable.just(db)
                    .subscribeOn(Schedulers.io())
                    .subscribe {
                        db.daoContact().insertAll(contact)
                    }
            mutableList.add(contact)
        }




         buttonSave.setOnClickListener {
             Observable.just(db)
                     .subscribeOn(Schedulers.io())
                     .subscribe {
                         Log.i("TAGMy", db.daoContact().getAll()[2].number)
                         resultList.addAll(db.daoContact().getAll())
                     }
         }

         buttonOpen.setOnClickListener {
             Toast.makeText(applicationContext, resultList.toString(), Toast.LENGTH_SHORT).show()
         }



    }
}