package com.example.kcco.csmap;

import android.app.Application;

import com.example.kcco.csmap.DAO.ParseConstant;

/**
 * Created by gaojieli on 5/14/15.
 */
public class InitActivity extends Application {
    public void onCreate () {
        ParseConstant.initial(this);
        //Parse.initialize(this, "kmTRHY0JpinZU1glacEhcRRrkYj9BvXwCoTwJZdR", "cWGd0GJdvcnqJ5uIfuCthGEQYQf7r5uF46j1R7on");
    }
}
