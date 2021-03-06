package com.example.spit_app.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spit_app.R;
import com.example.spit_app.user.home.CommitteeAnnouncementDisplay;

import java.util.List;

public class RecyclerAdapterCommitteeAnnouncementListAdmin extends RecyclerView.Adapter<RecyclerAdapterCommitteeAnnouncementListAdmin.ViewHolder>{
    private List<CommitteeAnnouncements> list;
    private Context context;
    public RecyclerAdapterCommitteeAnnouncementListAdmin(List<CommitteeAnnouncements> list, Context context){
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.committee_announcements_view_admin, parent, false);
        RecyclerAdapterCommitteeAnnouncementListAdmin.ViewHolder viewHolder= new RecyclerAdapterCommitteeAnnouncementListAdmin.ViewHolder(view,context,list);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterCommitteeAnnouncementListAdmin.ViewHolder holder, int position) {
        holder.eventname.setText(list.get(position).getEvent());
        holder.date.setText(list.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        TextView eventname;
        TextView date;
        Context context;
        List<CommitteeAnnouncements> list;

        public ViewHolder(@NonNull View itemView, Context context, List<CommitteeAnnouncements> list) {
            super(itemView);
            eventname = itemView.findViewById(R.id.events);
            date = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
            this.context = context;
            this.list = list;
        }

        @Override
        public void onClick(View v) {

            Intent intent= new Intent(context,CommitteeAnnouncementDisplayAdmin.class);
            intent.putExtra("Date", list.get(getAdapterPosition()).getDate());
            intent.putExtra("Description", list.get(getAdapterPosition()).getDt());
            intent.putExtra("Eventname", list.get(getAdapterPosition()).getEvent());
            intent.putExtra("AnnouncementId",list.get(getAdapterPosition()).getAnnounceid());
            intent.putExtra("Name", list.get(getAdapterPosition()).getName());
            context.startActivity(intent);

        }
    }
}


