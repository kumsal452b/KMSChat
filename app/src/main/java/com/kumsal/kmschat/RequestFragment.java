package com.kumsal.kmschat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.kongzue.dialog.v3.WaitDialog;

import java.util.ArrayList;
import java.util.List;

public class RequestFragment extends Fragment {


    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       
        ListAdapter adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,deney);
        listView.setAdapter(adapter);
        WaitDialog.dismiss();
        return view;
    }
}