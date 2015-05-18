package com.example.kcco.csmap;

import com.example.kcco.csmap.ClassroomSchedule.*;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ClassroomInfoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_classroom_info);

        //TODO: Get info from parse

        //The stuff below is temporary to test functionality
        String buildingName = (String) getIntent().getExtras().get("BuildingName");
        UCSDBuilding thisBuilding = new UCSDBuilding(buildingName);

        thisBuilding.addClass("217B", "BENG 168 - Biomolecular Engineering", "M", "12:00p-12:50p");
        thisBuilding.addClass("217A", "BENG 168 - Biomolecular Engineering", "F", "10:00a-10:50a");
        thisBuilding.addClass("222", "BENG 172 - Bioengineering Laboratory", "M", "10:00a-10:50a");
        thisBuilding.addClass("113", "BENG 186A - Principles/Biomaterials Design", "TuTh", "12:30p-1:50p");
        thisBuilding.addClass("220", "BENG 186A - Principles/Biomaterials Design", "M", "12:00p-12:50p");
        thisBuilding.addClass("203", "BENG 186A - Principles/Biomaterials Design", "M", "4:00p-4:50p");
        thisBuilding.addClass("217B", "BIEB 140 - Biodiversity", "W", "5:00p-5:50p");
        thisBuilding.addClass("217B", "BIEB 140 - Biodiversity", "W", "6:00p-6:50p");
        thisBuilding.addClass("217B", "BIEB 140 - Biodiversity", "W", "7:00p-7:50p");
        thisBuilding.addClass("201", "BILD 1 - The Cell", "W", "1:00p-1:50p");
        thisBuilding.addClass("217B", "BILD 2 - Multicellular Life", "Tu", "2:00p-2:50p");
        thisBuilding.addClass("217B", "BILD 2 - Multicellular Life", "Th", "2:00p-2:50p");
        thisBuilding.addClass("220", "BILD 2 - Multicellular Life", "F", "9:00a-9:50a");
        thisBuilding.addClass("220", "BILD 2 - Multicellular Life", "F", "10:00a-10:50a");
        thisBuilding.addClass("201", "BILD 2 - Multicellular Life", "F", "4:00p-4:50p");
        thisBuilding.addClass("119", "BILD 2 - Multicellular Life", "TuTh", "2:00p-3:20p");
        thisBuilding.addClass("222", "BILD 3 - Organismic&Evolutionary Biol", "M", "3:00p-3:50p");
        thisBuilding.addClass("222", "BILD 3 - Organismic&Evolutionary Biol", "M", "4:00p-4:50p");
        thisBuilding.addClass("222", "BILD 3 - Organismic&Evolutionary Biol", "M", "5:00p-5:50p");
        thisBuilding.addClass("222", "BILD 3 - Organismic&Evolutionary Biol", "M", "6:00p-6:50p");
        thisBuilding.addClass("222", "BILD 3 - Organismic&Evolutionary Biol", "M", "7:00p-7:50p");
        thisBuilding.addClass("222", "BILD 3 - Organismic&Evolutionary Biol", "M", "8:00p-8:50p");
        thisBuilding.addClass("201", "BILD 3 - Organismic&Evolutionary Biol", "W", "4:00p-4:50p");
        thisBuilding.addClass("201", "BILD 3 - Organismic&Evolutionary Biol", "W", "5:00p-5:50p");
        thisBuilding.addClass("201", "BILD 3 - Organismic&Evolutionary Biol", "W", "6:00p-6:50p");
        thisBuilding.addClass("201", "BILD 3 - Organismic&Evolutionary Biol", "W", "7:00p-7:50p");
        thisBuilding.addClass("201", "BILD 7 - The Beginning of Life", "Tu", "2:00p-2:50p");
        thisBuilding.addClass("217B", "BILD 7 - The Beginning of Life", "F", "12:00p-12:50p");
        thisBuilding.addClass("101", "BILD 18 - Human Impact on the Environmnt", "MWF", "1:00p-1:50p");
        thisBuilding.addClass("224A", "CHIN 20CN - Second Yr Chinese/NonNativeIII", "MW", "10:00a-10:50a");
        thisBuilding.addClass("223", "CHIN 20CN - Second Yr Chinese/NonNativeIII", "MW", "12:00p-12:50p");
        thisBuilding.addClass("223", "CHIN 20CN - Second Yr Chinese/NonNativeIII", "MW", "1:00p-1:50p");
        thisBuilding.addClass("217A", "CHIN 100CM - Third Yr Chinese/Heritage III", "TuTh", "9:30a-10:50a");
        thisBuilding.addClass("220", "CHIN 169B - Medical Chinese II", "TuTh", "3:30p-4:50p");
        thisBuilding.addClass("201", "CHIN 186C - Read/Chinese Econ,Pol,TradeIII", "TuTh", "12:30p-1:50p");
        thisBuilding.addClass("222", "COGS 1 - Introduction to Cognitive Sci", "W", "9:00a-9:50a");
        thisBuilding.addClass("222", "COGS 1 - Introduction to Cognitive Sci", "W", "10:00a-10:50a");
        thisBuilding.addClass("222", "COGS 14B - Intro. to Statistical Analysis", "M", "12:00p-12:50p");
        thisBuilding.addClass("115", "COGS 101C - Language", "TuTh", "2:00p-3:20p");
        thisBuilding.addClass("222", "COGS 101C - Language", "W", "11:00a-11:50a");
        thisBuilding.addClass("222", "COGS 101C - Language", "W", "12:00p-12:50p");
        thisBuilding.addClass("113", "COGS 110 - The Developing Mind", "MWF", "11:00a-11:50a");
        thisBuilding.addClass("113", "COGS 121 - Human Comptr Interac Prog Stud", "TuTh", "2:00p-3:20p");
        thisBuilding.addClass("113", "COGS 154 - Comm Disorders/Children&Adults", "TuTh", "3:30p-4:50p");
        thisBuilding.addClass("214", "COMM 10 - Introduction to Communication", "MWF", "2:00p-2:50p");
        thisBuilding.addClass("203", "COMM 10 - Introduction to Communication", "M", "1:00p-1:50p");
        thisBuilding.addClass("203", "COMM 10 - Introduction to Communication", "M", "3:00p-3:50p");
        thisBuilding.addClass("214", "CSE 3 - Fluency/Information Technology", "MWF", "11:00a-11:50a");
        thisBuilding.addClass("212", "CSE 3 - Fluency/Information Technology", "W", "1:00p-1:50p");
        thisBuilding.addClass("109", "CSE 3 - Fluency/Information Technology", "MWF", "1:00p-1:50p");
        thisBuilding.addClass("214", "CSE 3 - Fluency/Information Technology", "F", "4:00p-4:50p");
        thisBuilding.addClass("115", "CSE 7 - Intro Programming MATLAB", "MWF", "10:00a-10:50a");
        thisBuilding.addClass("115", "CSE 8A - Intro/Computer Sci: Java", "M", "3:00p-3:50p");
        thisBuilding.addClass("115", "CSE 8A - Intro/Computer Sci: Java", "W", "4:00p-4:50p");
        thisBuilding.addClass("119", "CSE 8B - Intro/Computer Sci. Java", "TuTh", "9:30a-10:50a");
        thisBuilding.addClass("115", "CSE 8B - Intro/Computer Sci. Java", "F", "4:00p-4:50p");
        thisBuilding.addClass("119", "CSE 8B - Intro/Computer Sci. Java", "TuTh", "8:00a-9:20a");
        thisBuilding.addClass("115", "CSE 8B - Intro/Computer Sci. Java", "F", "3:00p-3:50p");
        thisBuilding.addClass("119", "CSE 12 - Basic Data Struct & OO Design", "MWF", "1:00p-1:50p");
        thisBuilding.addClass("119", "CSE 12 - Basic Data Struct & OO Design", "M", "3:00p-3:50p");
        thisBuilding.addClass("119", "CSE 12 - Basic Data Struct & OO Design", "M", "4:00p-4:50p");
        thisBuilding.addClass("119", "CSE 12 - Basic Data Struct & OO Design", "MWF", "2:00p-2:50p");
        thisBuilding.addClass("113", "CSE 12 - Basic Data Struct & OO Design", "MWF", "12:00p-12:50p");
        thisBuilding.addClass("113", "CSE 12 - Basic Data Struct & OO Design", "F", "3:00p-3:50p");
        thisBuilding.addClass("113", "CSE 12 - Basic Data Struct & OO Design", "MWF", "1:00p-1:50p");
        thisBuilding.addClass("109", "CSE 12 - Basic Data Struct & OO Design", "F", "4:00p-4:50p");
        thisBuilding.addClass("115", "CSE 30 - Computer Organiz&Systms Progrm", "F", "11:00a-11:50a");
        thisBuilding.addClass("115", "CSE 30 - Computer Organiz&Systms Progrm", "F", "12:00p-12:50p");
        thisBuilding.addClass("119", "CSE 100 - Advanced Data Structures", "MWF", "10:00a-10:50a");
        thisBuilding.addClass("101", "CSE 100 - Advanced Data Structures", "MWF", "9:00a-9:50a");
        thisBuilding.addClass("115", "CSE 101 - Design & Analysis of Algorithm", "MW", "5:00p-6:20p");
        thisBuilding.addClass("115", "CSE 101 - Design & Analysis of Algorithm", "MW", "6:30p-7:50p");
        thisBuilding.addClass("119", "CSE 101 - Design & Analysis of Algorithm", "W", "8:00a-8:50a");
        thisBuilding.addClass("113", "CSE 101 - Design & Analysis of Algorithm", "MWF", "4:00p-4:50p");
        thisBuilding.addClass("113", "CSE 101 - Design & Analysis of Algorithm", "W", "3:00p-3:50p");
        thisBuilding.addClass("119", "CSE 105 - Intro/Theory of Computation", "F", "8:00a-8:50a");
        thisBuilding.addClass("119", "CSE 105 - Intro/Theory of Computation", "F", "9:00a-9:50a");
        thisBuilding.addClass("113", "CSE 110 - Software Engineering", "MW", "6:30p-7:50p");
        thisBuilding.addClass("222", "CSE 110 - Software Engineering", "W", "2:00p-2:50p");
        thisBuilding.addClass("222", "CSE 110 - Software Engineering", "W", "3:00p-3:50p");
        thisBuilding.addClass("109", "CSE 120 - Princ/Computer Operating Systm", "TuTh", "6:30p-7:50p");
        thisBuilding.addClass("216", "CSE 120 - Princ/Computer Operating Systm", "W", "5:00p-5:50p");
        thisBuilding.addClass("105", "CSE 123 - Computer Networks", "MWF", "10:00a-10:50a");
        thisBuilding.addClass("113", "CSE 123 - Computer Networks", "M", "3:00p-3:50p");
        thisBuilding.addClass("119", "CSE 127 - Intro to Computer Security", "F", "3:00p-3:50p");
        thisBuilding.addClass("115", "CSE 130 - Progrmng Lang:Princpl&Paradigm", "MWF", "2:00p-2:50p");
        thisBuilding.addClass("212", "CSE 130 - Progrmng Lang:Princpl&Paradigm", "W", "12:00p-12:50p");
        thisBuilding.addClass("105", "CSE 130 - Progrmng Lang:Princpl&Paradigm", "M", "4:00p-4:50p");
        thisBuilding.addClass("119", "CSE 131 - Compiler Construction", "TuTh", "5:00p-6:20p");
        thisBuilding.addClass("212", "CSE 131 - Compiler Construction", "F", "3:00p-3:50p");
        thisBuilding.addClass("214", "CSE 131 - Compiler Construction", "W", "4:00p-4:50p");
        thisBuilding.addClass("214", "CSE 134B - Web Client Languages", "MW", "5:00p-6:20p");
        thisBuilding.addClass("113", "CSE 135 - Server-side Web Applications", "TuTh", "5:00p-6:20p");
        thisBuilding.addClass("115", "CSE 135 - Server-side Web Applications", "W", "3:00p-3:50p");
        thisBuilding.addClass("119", "CSE 140 - Component&Desgn Tech/Digtl Sys", "TuTh", "11:00a-12:20p");
        thisBuilding.addClass("214", "CSE 140 - Component&Desgn Tech/Digtl Sys", "TuTh", "3:30p-4:50p");
        thisBuilding.addClass("212", "CSE 140 - Component&Desgn Tech/Digtl Sys", "M", "4:00p-4:50p");
        thisBuilding.addClass("119", "CSE 140L - Digital Systems Laboratory", "F", "4:00p-4:50p");
        thisBuilding.addClass("119", "CSE 140L - Digital Systems Laboratory", "F", "5:00p-5:50p");
        thisBuilding.addClass("214", "CSE 141 - Intro/Computer Architecture", "MWF", "1:00p-1:50p");
        //The stuff above is temporary to test functionality

        ((TextView) findViewById(R.id.classroom_info_text)).setText(thisBuilding.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_classroom_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}