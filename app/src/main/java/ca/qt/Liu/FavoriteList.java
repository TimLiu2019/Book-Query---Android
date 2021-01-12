package ca.qt.Liu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.material.navigation.NavigationView;

/**
 * this activity can see and delete items in my favorite list
 */
public class FavoriteList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout myDrawer;
    NavigationView myNavView;
    MyDBHelper mydbhelper = new MyDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);


        // List of columns we want to display
        String columnNames[] = {"_id", "title", "author"};

        // Get an instance of the database
        SQLiteDatabase db = mydbhelper.getReadableDatabase();

        // A projection defines what fields we want to retrieve
        String[] projection = {"_id, title, author"};

        // Rerieve the data and returns a cursor
        Cursor myCursor = db.query("booksTable", projection, null, null, null, null, null);


        // Make a cursorAdapater for our listview
        SimpleCursorAdapter myAdapater = new SimpleCursorAdapter(
                this,
                R.layout.list_item_favoriate, // Layout of each row in the list
                myCursor, // Cursor containing the information
                columnNames, // Array of columns we want to display
                new int[]{R.id.txtID, R.id.txtTitle, R.id.txtAuthor}, // List of textviews to use (Must be in the same order as the columnNames)
                0
        );

        // Create a reference to the listView
        ListView myList = findViewById(R.id.listFavoriate);

        // Set the adapter
        myList.setAdapter(myAdapater);

        // Nav view part
        myDrawer = findViewById(R.id.drawerLayout_fav);
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
     * delete item from my favorite list and update list
     *
     * @param view
     */
    public void deleteFavoriteItem(View view) {

        SQLiteDatabase db = mydbhelper.getWritableDatabase();
        // List of columns we want to display
        String columnNames[] = {"_id", "title", "author"};
        String[] projection = {"_id, title, author"};
        Cursor myCursor = db.query("booksTable", projection, null, null, null, null, null);
        SimpleCursorAdapter myAdapater = new SimpleCursorAdapter(
                this,
                R.layout.list_item_favoriate, // Layout of each row in the list
                myCursor, // Cursor containing the information
                columnNames, // Array of columns we want to display
                new int[]{R.id.txtID, R.id.txtTitle, R.id.txtAuthor}, // List of textviews to use (Must be in the same order as the columnNames)
                0
        );
        View parentRow = (View) view.getParent().getParent();
        ListView listView = (ListView) view.getParent().getParent().getParent();
        int position = listView.getPositionForView(parentRow);
        long id = myAdapater.getItemId(position);
        // long id = cursorAdaptor.getPosition();

        Log.d("delete btn id", "onclick delete btn: " + id);
        Log.d("delete btn position", "onclick delete btn: " + position);


        this.deleteRow("booksTable", "_id=" + id);


        // update listview
        myAdapater.swapCursor(myCursor);
        myAdapater.notifyDataSetChanged();
        Intent myIntent = new Intent(FavoriteList.this, FavoriteList.class);
        startActivity(myIntent);


    }

    /**
     * delete a row from database
     *
     * @param tableName
     * @param where
     * @return
     */
    public int deleteRow(String tableName, String where) {

        SQLiteDatabase db = mydbhelper.getWritableDatabase();
        int deletedVal = -1;
        try {
            deletedVal = db.delete(tableName, where, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return deletedVal;
    }

    /**
     * nav view , click to enter home
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
