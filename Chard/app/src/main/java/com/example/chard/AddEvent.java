package com.example.chard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddEvent extends AppCompatActivity {

    Button startTimeButton;
    Button endTimeButton;
    private int startHour, startMin;
    private int endHour, endMin;
    private Date startDate, endDate;
    private String eventTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        startTimeButton = findViewById(R.id.buttonStartTime);
        endTimeButton = findViewById(R.id.buttonEndTime);
        // set time pickers to now by default
        Calendar calendar = Calendar.getInstance();
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinutes = calendar.get(Calendar.MINUTE);
        startTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", nowHour, nowMinutes));
        endTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", nowHour, nowMinutes));
        startHour = nowHour;
        startMin = nowMinutes;
        endHour = nowHour;
        endMin = nowMinutes;
    }

    public void popStartTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                startHour = selectedHour;
                startMin = selectedMinute;
                startTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", startHour, startMin));

                endTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", startHour, startMin));
                endHour = startHour;
                endMin = startMin;
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, startHour, startMin, true);
        timePickerDialog.setTitle("Start Time");
        timePickerDialog.show();
    }

    public void popEndTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                endHour = selectedHour;
                endMin = selectedMinute;
                endTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", endHour, endMin));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, endHour, endMin, true);
        timePickerDialog.setTitle("End Time");
        timePickerDialog.show();
    }

    public void pushSubmitButton(View view) {
        view.findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextTitle = findViewById(R.id.editTextTitle);
                eventTitle = editTextTitle.getText().toString();
                if (eventTitle.matches("")) {
                    Toast.makeText(view.getContext(), "You did not enter an event title", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create Date objects from start and end times
                Calendar startCal = Calendar.getInstance();
                startCal.set(Calendar.HOUR_OF_DAY, startHour);
                startCal.set(Calendar.MINUTE, startMin);
                startDate = startCal.getTime();
                Calendar endCal = Calendar.getInstance();
                endCal.set(Calendar.HOUR_OF_DAY, endHour);
                endCal.set(Calendar.MINUTE, endMin);
                endDate = endCal.getTime();

                // Add a new document with a generated id.
                Map<String, Object> data = new HashMap<>();
                data.put("uid", 1);
                data.put("id", 0);
                data.put("title", eventTitle);
                data.put("starttime", startDate);
                data.put("endtime", endDate);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("events").document(String.valueOf(System.currentTimeMillis()))
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("dbtest", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("dbtest", "Error writing document", e);
                            }
                        });

                Log.d("dbtest", eventTitle + " " + startHour + ":" + startMin
                        + " " + endHour + ":" + endMin);

                Intent i = new Intent(view.getContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

}