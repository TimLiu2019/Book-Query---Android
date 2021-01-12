package ca.qt.Liu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

/**
 * this activity is made for show book detail, and can add it to my favorite
 */
public class Main2BookDetail extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout myDrawer;
    NavigationView myNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_book_detail);

        // get book  details  via intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("bookTitle");
        String author = intent.getStringExtra("bookAuthor");
        String description = intent.getStringExtra("bookDescription");
        String imageURL = intent.getStringExtra("bookImage");


        // pass values to view
        TextView titleTextView = (TextView) findViewById(R.id.book_title_detail);
        titleTextView.setText(title);
        TextView authorTextView = (TextView) findViewById(R.id.book_author_detail);
        authorTextView.setText("By " + author);
        TextView descriptTextView = (TextView) findViewById(R.id.book_description_detail);
        descriptTextView.setText(description);
        ImageView imageView = (ImageView) findViewById(R.id.book_image_detail);
        Picasso.get().load(imageURL).into(imageView);


        // Nav drawer part in book detail
        myDrawer = findViewById(R.id.drawerLayout_detail);
        myNavView = findViewById(R.id.navView);
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(true);
        myNavView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle myActionBarToggle = new ActionBarDrawerToggle
                (this, myDrawer, R.string.open, R.string.close);
        myDrawer.addDrawerListener(myActionBarToggle);
        myActionBarToggle.syncState();
    }

    /**
     * When click "Add to favorite" functon
     *
     * @param view
     */
    public void displayFavorite(View view) {
        // put book title and authro to sqlite
        Intent intent = getIntent();
        String title = intent.getStringExtra("bookTitle");
        String author = intent.getStringExtra("bookAuthor");
        Intent MyIntent = new Intent(this, FavoriteList.class);
        MyDBHelper mydbhelper = new MyDBHelper(this);
        SQLiteDatabase db = mydbhelper.getWritableDatabase();


        // A contentValues a class that provides an easy helper function to add values
        ContentValues values = new ContentValues();

        // Similar to a bundle - put values in
        values.put("title", title);
        values.put("author", author);
        //    values.put("description", description);

        // db.insert command will do a SQL insert and return the new row ID
        long newrowID = db.insert("booksTable", null, values);

        Log.d("databaselog", "New ID: " + newrowID);

        Toast.makeText(this, "book added", Toast.LENGTH_LONG).show();
        startActivity(MyIntent);
    }

    /**
     * Nav Drawer part in this activity, 2 selectons home and favoriate
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        myNavView.setCheckedItem(menuItem.getItemId());
        myDrawer.closeDrawer(GravityCompat.START);
        if (menuItem.getItemId() == R.id.nav_home) {
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(myIntent);
        } else if (menuItem.getItemId() == R.id.nav_favorite) {
            Intent myIntent = new Intent(getApplicationContext(), FavoriteList.class);
            startActivity(myIntent);
        }

        return false;
    }

    /**
     * drawer open and close
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        boolean isOpen = myDrawer.isDrawerOpen(GravityCompat.START);

        if (item.getItemId() == android.R.id.home) {
            if (isOpen == true) {
                myDrawer.closeDrawer(GravityCompat.START);
            } else {
                myDrawer.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
