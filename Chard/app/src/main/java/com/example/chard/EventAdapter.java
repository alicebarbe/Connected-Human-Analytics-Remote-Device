package com.example.chard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.Viewholder> {

    private Context context;
    private ArrayList<EventModel> eventModelArrayList;

    // Constructor
    public EventAdapter(Context context, ArrayList<EventModel> eventModelArrayList) {
        this.context = context;
        this.eventModelArrayList = eventModelArrayList;
    }

    @NonNull
    @Override
    public EventAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        EventModel model = eventModelArrayList.get(position);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        holder.eventName.setText(model.getEvent_name());

        Date startTime = model.getStart_time();
        String formattedStartTime = formatter.format(startTime);
        holder.startTime.setText(formattedStartTime);

        Date endTime = model.getEnd_time();
        String formattedEndTime = formatter.format(endTime);
        holder.endTime.setText(formattedEndTime);
        //holder.courseIV.setImageResource(model.getCourse_image());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return eventModelArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        //private ImageView courseIV;
        //private TextView courseNameTV, courseRatingTV;
        private TextView eventName, startTime, endTime;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.idEventName);
            startTime = itemView.findViewById(R.id.idStartTime);
            endTime = itemView.findViewById(R.id.idEndTime);
        }
    }
}
