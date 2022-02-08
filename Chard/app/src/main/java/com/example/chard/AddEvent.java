package com.example.chard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Locale;

public class AddEvent extends AppCompatActivity {

    Button startTimeButton;
    Button endTimeButton;
    Switch instantaneousSwitch;
    private int startHour, startMin;
    private int endHour, endMin;
    private boolean startChanged = false;
    private boolean endChanged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        startTimeButton = findViewById(R.id.buttonStartTime);
        endTimeButton = findViewById(R.id.buttonEndTime);
        //instantaneousSwitch = findViewById(R.id.switchInstantaneous);
        /*
        instantaneousSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // remove end time button if instantaneous switch is checked
                if (isChecked) {
                    endTimeButton.setVisibility(View.GONE);
                } else {
                    endTimeButton.setVisibility(View.VISIBLE);
                }
            }
        });*/
    }

    public void popStartTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                startHour = selectedHour;
                startMin = selectedMinute;
                startTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", startHour, startMin));
                startChanged = true;

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
                endChanged = true;
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
                EditText editTextTitle = (EditText) findViewById(R.id.editTextTitle);
                String eventTitle = editTextTitle.getText().toString();
                if (eventTitle.matches("")) {
                    Toast.makeText(view.getContext(), "You did not enter an event title", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!startChanged){
                    Toast.makeText(view.getContext(), "You did not enter a start time", Toast.LENGTH_SHORT).show();
                    return;
                } /*else if (!endChanged) {
                    if (instantaneousSwitch.isChecked()) {
                        endHour = startHour;
                        endMin = startMin;
                    } else {
                        Toast.makeText(view.getContext(), "You did not enter an end time", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }*/

                Log.d("dbtest", eventTitle + " " + startHour + ":" + startMin
                        + " " + endHour + ":" + endMin);

            }
        });
    }

}