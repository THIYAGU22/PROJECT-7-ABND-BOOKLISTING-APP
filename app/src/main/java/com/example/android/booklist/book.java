package com.example.android.booklist;

/**
 * Created by ravi on 5/25/2017.
 */

public class book {


    //initializing the bookname and author to get in to play by private method
    private String bookname;

    private String author;

    /**
     * @param bookname
     * @param author
     */
    public book(String bookname, String author) {
        this.bookname = bookname;
        this.author = author;
    }

    /**
     * Returns bookname
     */
    public String getBookname() {
        return this.bookname;

    }

    /**
     * Returns authorname
     */
    public String getAuthor() {
        return this.author;
    }
}
