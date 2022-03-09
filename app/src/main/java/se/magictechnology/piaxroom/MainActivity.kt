package se.magictechnology.piaxroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*

class MainActivity : AppCompatActivity() {
    var minAdapter = MinAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()

        val userDao = db.userDao()


        var someone1 = User(0, "Torsten", "Torstensson", 72)
        userDao.insertAll(someone1)
        var someone2 = User(0, "Babyperson", "Ungsson", 2)
        userDao.insertAll(someone2)
        var someone3 = User(0, "Medelperson", "Lagomsson", 30)
        userDao.insertAll(someone3)


        /*
        val users: List<User> = userDao.getOlderThenPeople(7)

        Log.i("PIAXDEBUG", "ANTAL ANVÄNDARE: " + users.size.toString())


        for(theuser in users)
        {
            Log.i("PIAXDEBUG", theuser.uid.toString() + " " + theuser.firstName + " " + theuser.lastName + " " + theuser.age.toString())
        }

         */


        userDao.getOldPeople().observe(this, Observer {
            for(theuser in it)
            {
                Log.i("PIAXDEBUG", theuser.uid.toString() + " " + theuser.firstName + " " + theuser.lastName + " " + theuser.age.toString())
                minAdapter.firstnames.add(theuser.firstName.toString())
                minAdapter.lastnames.add(theuser.lastName.toString())
                minAdapter.ages.add(theuser.age.toString())
                minAdapter.notifyDataSetChanged()
            }
        })


        findViewById<Button>(R.id.addbutton).setOnClickListener {
            var addperson = User(0, "Ungperson", "Litensson", 3)

            userDao.insertAll(addperson)
        }

        var myRecyclerView = findViewById<RecyclerView>(R.id.myRV)

        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerView.adapter = minAdapter

        var radButton = findViewById<Button>(R.id.testBtn)
        var firstNameET = findViewById<EditText>(R.id.firstNameET)
        var lastNameET = findViewById<EditText>(R.id.lastNameET)
        var ageET = findViewById<EditText>(R.id.ageET)
        //var testa : Editable = ageET.text.toString().toEdi

        radButton.setOnClickListener {
            minAdapter.firstnames.add(firstNameET.text.toString())
            minAdapter.lastnames.add(lastNameET.text.toString())
            minAdapter.ages.add(ageET.text.toString())

            var addperson = User(0, firstNameET.text.toString(), lastNameET.text.toString(), 58
            )

            userDao.insertAll(addperson)

            minAdapter.notifyDataSetChanged()
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
    fun getOldPeople(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE age > :years ORDER BY age DESC")
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


/*

- Category
-- id
-- title

- Book
-- id
-- category_id
-- bookname
-- bookdescription

- Chapter
-- id
-- book_id
-- chaptername

SELECT * category

SELECT * FROM book WHERE category_id = X

SELECT * FROM chapter WHERE book_id = Y

 */