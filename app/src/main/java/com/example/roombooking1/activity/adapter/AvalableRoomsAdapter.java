package com.example.roombooking1.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.roombooking1.R;
import com.example.roombooking1.activity.BookaRoomActivity;
import com.example.roombooking1.activity.model.AvalableRoomsPojo;

import java.util.List;

public class AvalableRoomsAdapter extends BaseAdapter {
    List<AvalableRoomsPojo> ar;
    Context cnt;

    public AvalableRoomsAdapter(List<AvalableRoomsPojo> ar, Context cnt) {
        this.ar = ar;
        this.cnt = cnt;
    }

    @Override
    public int getCount() {
        return ar.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup) {
        LayoutInflater obj1 = (LayoutInflater) cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View obj2 = obj1.inflate(R.layout.list_avalable_rooms, null);

        TextView tv_bname = (TextView) obj2.findViewById(R.id.tv_bname);
        tv_bname.setText("Floor Name  :" + ar.get(pos).getBname());

        TextView tv_rname = (TextView) obj2.findViewById(R.id.tv_rname);
        tv_rname.setText("Room Name  :" + ar.get(pos).getRname());

        TextView tv_capacity = (TextView) obj2.findViewById(R.id.tv_capacity);
        tv_capacity.setText("Capacity  :" + ar.get(pos).getCapacity());

        TextView tv_equipment = (TextView) obj2.findViewById(R.id.tv_equipment);
        tv_equipment.setText("Equipment  :" + ar.get(pos).getCapacity());
        Button btn_book = (Button) obj2.findViewById(R.id.btn_book);


        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(cnt, BookaRoomActivity.class);
                intent.putExtra("Block",ar.get(pos).getBname());
                intent.putExtra("Room",ar.get(pos).getRname());
                intent.putExtra("Capacity",ar.get(pos).getCapacity());
                intent.putExtra("Equipment",ar.get(pos).getEquipment());
                intent.putExtra("id",ar.get(pos).getId());

                cnt.startActivity(intent);


            }
        });


        return obj2;
    }
}
