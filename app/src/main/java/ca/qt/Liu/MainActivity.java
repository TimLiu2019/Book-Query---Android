
package ca.qt.Liu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * this is main activity can search books and show searched book list, if click an item in the list
 * will go to view book details
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout myDrawer;
    private CustomListViewAdapter mAdapter;
    NavigationView myNavView;
    private TextView mEmptyStateTextView;
    private static final int BOOK_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // navView part
        myDrawer = findViewById(R.id.drawerLayout);
        myNavView = findViewById(R.id.navView);
        myNavView.setNavigationItemSelectedListener(this);
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle myActionBarToggle = new ActionBarDrawerToggle
                (this, myDrawer, R.string.open, R.string.close);
        myDrawer.addDrawerListener(myActionBarToggle);
        myActionBarToggle.syncState();

    }


    /**
     *  download from google  books api
     * @param view
     */
    public void doDownload(View view) {
        EditText query = findViewById(R.id.searchTxt);

        String testquery = query.getText().toString();

        // Better to Uri.builder to create url
        if (!testquery.isEmpty()) {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("www.googleapis.com")
                    //  .appendPath("api")
                    .appendPath("books")
                    .appendPath("v1")
                    .appendPath("volumes")
                    .appendQueryParameter("q", "title:" + testquery); // Use .appendQueryParameter when adding query params to a request
            String myUrl = builder.build().toString();

            //    String myUrl = "https://www.googleapis.com/books/v1/volumes?maxResults=20&q=title:android";

            new DownloadTask() {
                @Override
                protected void onPostExecute(String s) {

                    // When using employees (API returns results nested inside of a property (example: nested inside .data :

                    Gson gson = new Gson();

                    try {
                        // Create object from string
                        JSONObject jObjc = new JSONObject(s);

                        // Get data property
                        String data = jObjc.getString("items");
                        Log.d("data", "raw search data: " + data);
                        if (data != null && !data.isEmpty()) {
                            // Create the list of books
                            Books books = gson.fromJson(data, Books.class);
                            if (books.size() > 0) {
                                Log.d("books", "raw search data: " + books);
                                // Get a reference to our list view
                                ListView lstOutput = findViewById(R.id.lstOutput);
                                // Create the adapter and set the adapter to display in listView
                                mAdapter = new CustomListViewAdapter(MainActivity.this, books);
                                lstOutput.setAdapter(mAdapter);

                                mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

                                lstOutput.setEmptyView(mEmptyStateTextView);


                                lstOutput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                        // Find current book that was clicked on
                                        Items thisBook = mAdapter.getItem(position);
                                        Intent myIntent = new Intent(MainActivity.this, Main2BookDetail.class);
                                        myIntent.putExtra("bookTitle", thisBook.volumeInfo.title);
                                        myIntent.putExtra("bookDescription", thisBook.volumeInfo.description);
                                        myIntent.putExtra("bookAuthor", thisBook.volumeInfo.authors.get(0));
                                        myIntent.putExtra("bookImage", thisBook.volumeInfo.imageLinks.smallThumbnail);
                                        startActivity(myIntent);

                                    }
                                });
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }.execute(myUrl);
        } else if (testquery.isEmpty()) {
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            mEmptyStateTextView.setText("\t      No books found.\nPlease begin new Search.");
        }

    }

    /**
     * click to enter into book detail view
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Books selectedBook = (Books) parent.getItemAtPosition(position);
        String message = selectedBook.toString();

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * nav view part, click to nav to favoriate page
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        myNavView.setCheckedItem(menuItem.getItemId());
        myDrawer.closeDrawer(GravityCompat.START);
        if (menuItem.getItemId() == R.id.nav_favorite) {
            Intent myIntent = new Intent(MainActivity.this, FavoriteList.class);
            startActivity(myIntent);
            finish();
        }
        return false;
    }

    /**
     *  drawer open and close
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
