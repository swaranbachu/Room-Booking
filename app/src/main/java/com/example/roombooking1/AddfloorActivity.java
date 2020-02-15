package com.a.roombooking1.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.a.roombooking.EndPointUrl;
import com.a.roombooking.R;
import com.a.roombooking.ResponseData;
import com.a.roombooking.RetrofitInstance;
import com.a.roombooking.model.GetBlocksPojo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRoomActivity extends AppCompatActivity {
    EditText et_Room_name,et_add_capacity,et_other_equipment,et_desc_room,et_add_staff;
    Button btn_submit;
    Spinner spinner_block_name;
    ProgressDialog progressDialog;
    String[] block;
    List<GetBlocksPojo> a1;
    TextView tv_equipment_sw,tv_equipment_hw;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addroom);
        initilization();

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

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinner_block_name.getSelectedItem().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please select Block Name",Toast.LENGTH_LONG).show();
                    return;
                }
                if(et_Room_name.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Add Room Name",Toast.LENGTH_LONG).show();
                    return;
                }
                if(et_add_capacity.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Add Room Capacity",Toast.LENGTH_LONG).show();
                    return;
                }
                if(tv_equipment_sw.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Add Softwere Equipment",Toast.LENGTH_LONG).show();
                    return;
                }

                if (tv_equipment_hw.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Add Hardwere Equipment",Toast.LENGTH_LONG).show();
                    return;
                }


                submitData();



            }
        });
        getBlocks();
    }

    private void submitData() {
        String rname = et_Room_name.getText().toString();
        String capacity = et_add_capacity.getText().toString();
        String swequipment=tv_equipment_sw.getText().toString();
        String bname=spinner_block_name.getSelectedItem().toString();
        String hwequipment=tv_equipment_hw.getText().toString();
        // String other_equipment=et_other_equipment.getText().toString();
        String room_desc=et_desc_room.getText().toString();


        progressDialog = new ProgressDialog(AddRoomActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        EndPointUrl service = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
        Call<ResponseData> call = service.addRoom(bname,rname,capacity,swequipment,hwequipment,room_desc);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if (response.body().status.equals("true")) {
                    progressDialog.dismiss();
                    Toast.makeText(AddRoomActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(OurFamiliesActivity.this, MainActivity.class));

                } else {
                    Toast.makeText(AddRoomActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddRoomActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getBlocks() {
        EndPointUrl apiService = RetrofitInstance.getRetrofitInstance().create(EndPointUrl.class);
        Call<List<GetBlocksPojo>> call = apiService.get_blocks();
        call.enqueue(new Callback<List<GetBlocksPojo>>() {
            @Override
            public void onResponse(Call<List<GetBlocksPojo>> call, Response<List<GetBlocksPojo>> response) {
                a1 = response.body();
                Log.d("TAG", "Response = " + a1);
                // Toast.makeText(getApplicationContext(),a1.size()+" ",Toast.LENGTH_LONG).show();
                block = new String[a1.size() + 1];
                block[0] = "Select Block Name";
                for (int i = 0; i < a1.size(); i++) {
                    block[i + 1] = a1.get(i).getBname();
                }
                ArrayAdapter aa = new ArrayAdapter(AddRoomActivity.this, android.R.layout.simple_spinner_item, block);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_block_name.setAdapter(aa);
                spinner_block_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                        if (pos > 0) {
                            Toast.makeText(getApplicationContext(), block[pos], Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<GetBlocksPojo>> call, Throwable t) {
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }
    public void initilization() {
        getSupportActionBar().setTitle("Add Rooms");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_equipment_sw=(TextView) findViewById(R.id.tv_equipment_sw);
        tv_equipment_hw=(TextView) findViewById(R.id.tv_equipment_hw);

        // et_other_equipment=(EditText)findViewById(R.id.et_other_equipment);
        et_Room_name=(EditText)findViewById(R.id.et_Room_name);
        spinner_block_name=(Spinner)findViewById(R.id.spinner_block_name);
        et_add_capacity=(EditText)findViewById(R.id.et_add_capacity);
        et_desc_room=(EditText)findViewById(R.id.et_desc_room);


        btn_submit=(Button)findViewById(R.id.btn_submit);


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
