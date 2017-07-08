package com.example.android.booklist;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.R.id.list;


/**
 * Created by ravi on 5/30/2017.
 */

public class bookAsyncTask extends AsyncTask<String, Void, String> {

    //creating a arraylist for the  bookname
    private ArrayList<String> name = new ArrayList<>();

    //creating a arraylist for author names
    private ArrayList<String> author = new ArrayList<>();

    //implementing a asyncindicator which holds the author name and title name of the book
    public AsyncIndicator runner = null;

    //bookasynctask will be accompanied with asyncindicator interface class
    public bookAsyncTask(AsyncIndicator asyncIndicator) {
        runner = asyncIndicator;
    }

    //do in background method will fetch the data from url
    //and it will serves according to the jsonResponse
    @Override
    protected String doInBackground(String... url) {
        String jsonResponse = "";
        for (String searchUrl : url) {
            URL urlObject = createUrl(searchUrl);
            if (url != null) {
                try {
                    jsonResponse = makeHttpRequest(urlObject);
                } catch (IOException e) {
                    Log.e("BookAsyncTask", "Not able to get JsonResponse!!!");
                }
            }
        }
        return jsonResponse;
    }

    /**
     * on post execute method will maintain the works with json parsing
     * it will fetch the book details i.e title and author
     *
     * @param result
     */
    @Override
    protected void onPostExecute(String result) {

        //fetching the data from google book api
        //getting the title from volume info object
        try {
            JSONObject baseObject = new JSONObject(result);
            JSONArray bookArray = baseObject.getJSONArray("items");
            JSONObject bookObject;
            JSONObject volumeObject;

            for (int i = 0; i < bookArray.length(); i++) {
                bookObject = bookArray.getJSONObject(i);
                volumeObject = bookObject.getJSONObject("volumeInfo");
                try {
                    name.add("Title:" + volumeObject.getString("title"));

                } catch (JSONException jse) {
                    name.add("N/A");
                }

                //stringbuilder method is implemented to fetch the Author name as string type
                //json parsing the authors name will be fetched

                StringBuilder authorBuilder = new StringBuilder("Author");
                try {
                    JSONArray authorArray = volumeObject.getJSONArray("authors");

                    for (int j = 0; j < authorArray.length(); j++) {
                        if (j > 0) {
                            authorBuilder.append(" , ");
                        }
                        authorBuilder.append(authorArray.getString(j));
                    }
                    author.add(authorBuilder.toString());
                } catch (JSONException jse) {
                    author.add("N/A");
                }
            }
            runner.processEnds(name, author);
        } catch (Exception e) {
            Log.e("BookAsyncTask", "Not able to create a json object!!!");
            runner.processEnds(name, author);
        }
    }

    /**
     * doing the works in makehttprequest to work
     */

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";


        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        //providing the stable connection
        //the data will be retrieved by "get" method
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            // if the rqst was successful then it is 200!!!
            //then read the input  Stream and parse the respose

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("BookAsyncTask", "Response code : " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("BookAsyncTask", "IOException occur !!!");
        }
        //finally method is used here
        //if there is no stable connection it will be disconnected
        //if the input stream doesnt provide reqd info it will be closed
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * connecting with inputStream
     * inputstream reader deals with the unicode s.t UTF-8 (reads text from a character - input stream)
     * input stream reader will gets additional support by buffered stream reader
     * readline() will reads a line of text . if the line is terminated  a carriage return might follow by new line feed
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * making the url to work
     *
     * @param stringUrl
     */
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("BookAsyncTask", "Error in converting url to URL object!!!");
            return url;
        }
        return url;
    }
}

