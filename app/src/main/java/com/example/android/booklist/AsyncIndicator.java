package com.example.android.booklist;

import java.util.ArrayList;

/**
 * Created by ravi on 5/30/2017.
 */

/**
 * ayncindicator interface is created which acts as a holder to hold the array list of name and description
 */
public interface AsyncIndicator {

    void processEnds ( ArrayList<String> list, ArrayList<String> description);
}
