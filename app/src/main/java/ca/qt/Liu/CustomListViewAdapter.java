package ca.qt.Liu;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.util.List;

/**
 * this class is an adaptor for the book list, including thumbnail image, book title, author and  author
 */
public class CustomListViewAdapter extends ArrayAdapter<Items> {


    // private Items;
    private List<Items> items;

    // View that helps display in list
    //   Context context;
    public CustomListViewAdapter(Activity context, Books books) {
        super(context, 0, books);
    }


    /**
     * make adapter for list view
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */

    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Items item = getItem(position);


        // Add the data to the listItem in the listView
        if (item.volumeInfo.title != null) {
            String title = item.volumeInfo.title;
            TextView titleTextView = (TextView) listItemView.findViewById(R.id.book_title);
            titleTextView.setText(title);
        }

        if (item.volumeInfo.authors != null) {
            String author = item.volumeInfo.authors.get(0);
            TextView authorTextView = (TextView) listItemView.findViewById(R.id.book_author);
            authorTextView.setText("By " + author);
        }

        if (item.volumeInfo.description != null) {
            String description = item.volumeInfo.description;
            TextView descriptTextView = (TextView) listItemView.findViewById(R.id.book_description);
            descriptTextView.setText(description);
        }

        //Add image to the listItem
        if (item.volumeInfo.imageLinks.smallThumbnail != null) {
            String imageURL = item.volumeInfo.imageLinks.smallThumbnail;
            // String imageURL = item.volumeInfo.imageLinks.getJSONObject("imageLinks").getString("smallThumbnail");
            ImageView imageView = (ImageView) listItemView.findViewById(R.id.book_image);
            Picasso.get().load(imageURL).into(imageView);
        }


        return listItemView;


    }

}
