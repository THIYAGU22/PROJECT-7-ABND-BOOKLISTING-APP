package com.example.android.booklist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    EditText editText;

    Button button;
    ListView listView;
    ArrayList<book> books;
    BookAdapter adapter;
    bookAsyncTask bookAsyncTask;
    TextView descriptionTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //setting up the activities (OR) GIVING LIFE TO ALL THE SOULS
        //mentioning all the activites with their respective id's
        editText = (EditText) findViewById(R.id.search_bar);
        button = (Button) findViewById(R.id.search_button);
        listView = (ListView) findViewById(R.id.book_list);
        books = new ArrayList<book>();
        adapter = new BookAdapter(this, books);
        descriptionTextView = (TextView) findViewById(R.id.description);


        //do something when the button is clicked
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = setStringUrl();

                //connectivity manager helps us to pass through solid internet connection
                ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected() && url != "") {
                    bookAsyncTask = new bookAsyncTask(new AsyncIndicator() {
                        @Override
                        public void processEnds(ArrayList<String> name, ArrayList<String> description) {
                            clearBookList();
                            //this method is really interesting
                            //if the search is complete then list view(all the books will be displayed)
                            //if do so then description text view (instructions will be disappeared)
                            if (name.size() != 0) {
                                descriptionTextView.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                                for (int i = 0; i < name.size(); i++) {
                                    addBook(name.get(i), description.get(i));
                                }
                            }
                            //if the search isnt complete descripton remains
                            else {
                                listView.setVisibility(View.GONE);
                                descriptionTextView.setVisibility(View.VISIBLE);
                            }
                            listView.setAdapter(adapter);
                        }
                    });
                    bookAsyncTask.execute(url);
                }
            }
        });
    }

    /**
     * WIPES OUT THE ARRAYLIST
     */

    private void clearBookList() {
        books.clear();
    }

    /**
     * add the info to the books arraylist
     * <p>
     * name
     * description
     * image
     */
    private void addBook(String name, String description) {
        books.add(new book(name, description));
    }

    /**
     * linking google books api
     */

    private String setStringUrl() {
        String keyword = "";
        if (!editText.getText().toString().equals("")) {
            keyword = editText.getText().toString();
            Toast.makeText(MainActivity.this, "searching...plz wait!!!", Toast.LENGTH_SHORT).show();
            return "https://www.googleapis.com/books/v1/volumes?" + "q=" + keyword;
        } else {
            Toast.makeText(MainActivity.this, "plz enter the correct keyword", Toast.LENGTH_SHORT).show();
        }
        return keyword;
    }
}
