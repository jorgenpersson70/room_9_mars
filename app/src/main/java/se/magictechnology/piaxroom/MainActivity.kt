package se.magictechnology.piaxroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()

        val userDao = db.userDao()

        /*
        var someone1 = User(0, "Torsten", "Torstensson", 72)
        userDao.insertAll(someone1)
        var someone2 = User(0, "Babyperson", "Ungsson", 2)
        userDao.insertAll(someone2)
        var someone3 = User(0, "Medelperson", "Lagomsson", 30)
        userDao.insertAll(someone3)
        */

        val users: List<User> = userDao.getOlderThenPeople(7)

        Log.i("PIAXDEBUG", "ANTAL ANVÄNDARE: " + users.size.toString())


        for(theuser in users)
        {
            Log.i("PIAXDEBUG", theuser.uid.toString() + " " + theuser.firstName + " " + theuser.lastName + " " + theuser.age.toString())
        }
    }
}

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "age") val age: Int?
)

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE age > 25")
    fun getOldPeople(): List<User>

    @Query("SELECT * FROM user WHERE age > :years")
    fun getOlderThenPeople(years : Int): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}


@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}