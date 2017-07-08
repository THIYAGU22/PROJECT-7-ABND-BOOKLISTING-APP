package com.example.android.booklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ravi on 5/25/2017.
 */

public class BookAdapter extends ArrayAdapter<book> {

    //using arrayadapter extracting books java file
    public BookAdapter(Context context, ArrayList<book> books) {
        super(context, 0, books);
    }

    @Override
    //getview method is called to do the positions and list the views
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;

        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.book_list, parent, false);
        }

        // the book infos will be extracted from book.java class
        book currentBook = getItem(position);

        //setting up the book textview to the book_name id
        TextView bookNameTextView = (TextView) listView.findViewById(R.id.book_name);
        bookNameTextView.setText(currentBook.getBookname());

        //setting up the author textview to the bauthor_name id
        TextView authorNameTextView = (TextView) listView.findViewById(R.id.author_name);
        authorNameTextView.setText(currentBook.getAuthor());

        return listView;
    }
}
