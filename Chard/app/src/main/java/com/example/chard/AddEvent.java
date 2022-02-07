package com.example.chard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.Locale;

public class AddEvent extends AppCompatActivity {

    Button startTimeButton;
    int startHour, startMin;
    Button endTimeButton;
    int endHour, endMin;
    Switch instantaneousSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        startTimeButton = findViewById(R.id.buttonStartTime);
        endTimeButton = findViewById(R.id.buttonEndTime);
        instantaneousSwitch = findViewById(R.id.switchInstantaneous);
        instantaneousSwitch.setChecked(false);

        endTimeButton.setVisibility(View.VISIBLE);
        instantaneousSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // remove end time button if instantaneous switch is checked
                if (isChecked) {
                    endTimeButton.setVisibility(View.GONE);
                } else {
                    endTimeButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void popStartTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                startHour = selectedHour;
                startMin = selectedMinute;
                startTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", startHour, startMin));
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
                endHour = selectedMinute;
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
                Log.d("dbtest",  startHour + " " + endHour);
            }
        });
    }


}