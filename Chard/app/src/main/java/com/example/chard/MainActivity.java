package com.example.chard;

import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventRV = findViewById(R.id.idRVCourse);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventModelArrayList = new ArrayList<>();

        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                eventModelArrayList.add(new EventModel(document.getString("title"),
                                        document.getTimestamp("starttime").toDate(),
                                        document.getTimestamp("endtime").toDate()));
                                Log.d("dbtest", document.getId() + " => " + document.getData());
                                Log.d("dbtest", "title is " + document.getString("title"));
                                Log.d("dbtest", "array is " + String.valueOf(eventModelArrayList));
                            }
                            // we are initializing our adapter class and passing our arraylist to it.
                            EventAdapter courseAdapter = new EventAdapter(eventRV.getContext(), eventModelArrayList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(eventRV.getContext(), LinearLayoutManager.VERTICAL, false);
                            // in below two lines we are setting layoutmanager and adapter to our recycler view.
                            eventRV.setLayoutManager(linearLayoutManager);
                            eventRV.setAdapter(courseAdapter);
                        } else {
                            Log.w("dbtest", "Error getting documents.", task.getException());
                        }
                    }
                });

        FloatingActionButton fab = findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });
    }

    public void openNewActivity(){
        Intent intent = new Intent(this, AddEvent.class);
        Log.i("hi", "hi");
        startActivity(intent);
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