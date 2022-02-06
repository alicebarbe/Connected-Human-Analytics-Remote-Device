package com.example.chard;

import android.media.metrics.Event;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView eventRV;

    // Arraylist for storing data
    private ArrayList<EventModel> eventModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventRV = findViewById(R.id.idRVCourse);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventModelArrayList = new ArrayList<>();
        eventModelArrayList.add(new EventModel("Antidepressant pill",
                LocalDateTime.of(2022, Month.JANUARY, 1, 9, 30),
                LocalDateTime.of(2022, Month.JANUARY, 1, 9, 45)));
        eventModelArrayList.add(new EventModel("Coffee",
                LocalDateTime.of(2022, Month.JANUARY, 1, 10, 30),
                LocalDateTime.of(2022, Month.JANUARY, 1, 10, 45)));
        eventModelArrayList.add(new EventModel("Exercise",
                LocalDateTime.of(2022, Month.JANUARY, 1, 11, 30),
                LocalDateTime.of(2022, Month.JANUARY, 1, 11, 45)));

        // we are initializing our adapter class and passing our arraylist to it.
        EventAdapter courseAdapter = new EventAdapter(this, eventModelArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        eventRV.setLayoutManager(linearLayoutManager);
        eventRV.setAdapter(courseAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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