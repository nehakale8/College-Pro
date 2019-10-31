package com.example.spit_app.admin;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spit_app.R;
import com.example.spit_app.user.home.CommitteeAnnouncementDisplay;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CommitteeAnnouncementDisplayAdmin extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    DatabaseReference myRef,reference;
    String Date;
    EditText eventname;
    TextView mDisplayDate;
    EditText Description;
    Button delete, update;
    String idu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.committee_announcements_admin_display);


        eventname=findViewById(R.id.eventn);
        mDisplayDate=findViewById(R.id.daten);
        Description=findViewById(R.id.desn);
        delete=findViewById(R.id.delete);
        update=findViewById(R.id.update);

        eventname.setText(getIntent().getStringExtra("Eventname"));
        mDisplayDate.setText(getIntent().getStringExtra("Date"));
        Description.setText(getIntent().getStringExtra("Description"));

        mDisplayDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CommitteeAnnouncementDisplayAdmin.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar cal = Calendar.getInstance();
                int cyear = cal.get(Calendar.YEAR);
                int cmonth = cal.get(Calendar.MONTH);
                int cday = cal.get(Calendar.DAY_OF_MONTH);
                if (year < cyear) {
                    Toast.makeText(CommitteeAnnouncementDisplayAdmin.this, "Enter valid date", Toast.LENGTH_SHORT).show();
                } else if (year == cyear && month < cmonth) {
                    Toast.makeText(CommitteeAnnouncementDisplayAdmin.this, "Enter valid date", Toast.LENGTH_SHORT).show();
                } else if (year == cyear && month == cmonth && day < cday) {
                    Toast.makeText(CommitteeAnnouncementDisplayAdmin.this, "Enter valid date", Toast.LENGTH_SHORT).show();
                } else {
                    month = month + 1;
                    String date = day + "/" + month + "/" + year;
                    mDisplayDate.setText(date);
                    Date = year + "/" + month + "/" + day;
                }
            }
        };

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idu = getIntent().getStringExtra("AnnouncementId");
                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("CommitteeAnnouncements").child(getIntent().getStringExtra("Name")).child(idu);
                dR.removeValue();

                myRef=FirebaseDatabase.getInstance().getReference("Users");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot idSnapshot: dataSnapshot.getChildren()){

                            reference=myRef.child(idSnapshot.getKey()).child("Announcements");
                            reference.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot annoucements: dataSnapshot.getChildren()){
                                        String id1=annoucements.getKey();
                                        if(id1.equals(idu)){
                                            reference=reference.child(id1);
                                            reference.removeValue();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Toast.makeText(getApplicationContext(), "Announcement Deleted", Toast.LENGTH_SHORT).show();
                finish();


            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getIntent().getStringExtra("AnnouncementId");
                String event = eventname.getText().toString();
                String data = Description.getText().toString();

                if (TextUtils.isEmpty(Date)) {
                    Date = getIntent().getStringExtra("Date");
                }

                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("CommitteeAnnouncements").child(getIntent().getStringExtra("Name")).child(id);
                CommitteeAnnouncements announceobj = new CommitteeAnnouncements(id, data, event,getIntent().getStringExtra("Name"), Date);
                dR.setValue(announceobj);
                Toast.makeText(getApplicationContext(), "Announcement updated", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
