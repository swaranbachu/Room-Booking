package com.example.roombooking1;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.a.roombooking.EndPointUrl;
import com.a.roombooking.R;
import com.a.roombooking.ResponseData;
import com.a.roombooking.RetrofitInstance;
import com.a.roombooking.Utils;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookaRoomActivity extends AppCompatActivity {
    EditText et_block_name,et_Room_name,et_add_capacity,et_equipment,et_reason_for_book,et_other_equipment, et_duration;
    TextView tv_dob;
    String date;
    Button btn_submit;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    String session,id;
    TextView tv_equipment_sw,tv_equipment_hw;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_rooms);
        getSupportActionBar().setTitle("Room Booking");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getApplication().getSharedPreferences(Utils.SHREF, Context.MODE_PRIVATE);
        session = sharedPreferences.getString("user_name", "def-val");

        tv_equipment_sw=(TextView) findViewById(R.id.tv_equipment_sw);
        tv_equipment_hw=(TextView) findViewById(R.id.tv_equipment_hw);

        et_other_equipment=(EditText)findViewById(R.id.et_other_equipment);
        et_reason_for_book=(EditText)findViewById(R.id.et_reason_for_book);
        et_block_name=(EditText)findViewById(R.id.et_block_name);
        et_Room_name=(EditText)findViewById(R.id.et_Room_name);
        et_add_capacity=(EditText)findViewById(R.id.et_add_capacity);
        et_equipment=(EditText)findViewById(R.id.et_equipment);
        tv_dob=(TextView)findViewById(R.id.tv_dob);
        et_duration=(EditText)findViewById(R.id.et_duration);
        btn_submit=(Button) findViewById(R.id.btn_submit);

        et_block_name.setText(getIntent().getStringExtra("bname"));
        et_Room_name.setText(getIntent().getStringExtra("rname"));
        et_add_capacity.setText(getIntent().getStringExtra("capacity"));
        tv_equipment_sw.setText(getIntent().getStringExtra("softwere"));
        tv_equipment_hw.setText(getIntent().getStringExtra("hardwere"));
        //et_equipment.setText(getIntent().getStringExtra("Equipment"));

        tv_equipment_sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multicheckboxsw();
            }
        });
        tv_equipment_hw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multicheckboxhw();
            }
        });

        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timedatepicker();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
                Intent intent=new Intent(BookaRoomActivity.this,SlashScreenActivity.class);
                startActivity(intent);
            }
        });
    }
    private void submitData() {
        String sware=tv_equipment_sw.getText().toString();
        String hware=tv_equipment_hw.getText().toString();
        String reason_book=et_reason_for_book.getText().toString();
        String other=et_other_equipment.getText().toString();

        progressDialog = new ProgressDialog(BookaRoomActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        EndPointUrl service = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
        Call<ResponseData> call = service.bookMyRoom(getIntent().getStringExtra("id"),session,date,sware,hware,reason_book,other,et_duration.getText().toString());
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.body().status.equals("true")) {
                    progressDialog.dismiss();
                    Toast.makeText(BookaRoomActivity.this, response.body().message, Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(BookaRoomActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(BookaRoomActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public void timedatepicker() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(String.valueOf(BookaRoomActivity.this), "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                //date = month + "/" + day + "/" + year;
                date=year + "/" + month + "/" + day;
                tv_dob.setText(date);
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(
                BookaRoomActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void multicheckboxsw(){
        Dialog dialog;
        final String[] items = {" Eclipse", "Photoshop", "Android Studio", "NetBeans", "Adobe Premiere"};
        final ArrayList itemsSelected = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Softwere Equipment : ");
        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {
                            itemsSelected.add(items[selectedItemId]);
                            //Toast.makeText(getApplicationContext(), items[selectedItemId], Toast.LENGTH_SHORT).show();
                        } else if (itemsSelected.contains(items[selectedItemId])) {
                            itemsSelected.remove(items[selectedItemId]);
                        }
                    }
                })
                .setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Your logic when OK button is clicked

                        tv_equipment_sw.setText(itemsSelected.toString());


                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        dialog = builder.create();
        dialog.show();
    }
    public void multicheckboxhw(){
        Dialog dialog;
        final String[] items = {"Smart board", "Projector", "Sound Systems", "laptop"};
        final ArrayList itemsSelected = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Hardwere Equipment : ");
        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {
                            itemsSelected.add(items[selectedItemId]);
                            //Toast.makeText(getApplicationContext(), items[selectedItemId], Toast.LENGTH_SHORT).show();
                        } else if (itemsSelected.contains(items[selectedItemId])) {
                            itemsSelected.remove(items[selectedItemId]);
                        }
                    }
                })
                .setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Your logic when OK button is clicked

                        tv_equipment_hw.setText(itemsSelected.toString());


                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        dialog = builder.create();
        dialog.show();
    }
}
