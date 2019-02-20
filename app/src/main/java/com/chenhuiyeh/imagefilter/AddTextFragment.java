package com.chenhuiyeh.imagefilter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.chenhuiyeh.imagefilter.Adapter.ColorAdapter;
import com.chenhuiyeh.imagefilter.Interfaces.AddTextFragmentListner;


public class AddTextFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener {
    // default text color is black
    int colorSelected = Color.parseColor("#000000");

    AddTextFragmentListner listener;

    EditText edt_add_txt;
    RecyclerView recycler_color;
    Button btn_done;

    static AddTextFragment instance;

    public static AddTextFragment getInstance() {
        if(instance == null)
            instance = new AddTextFragment();
        return instance;
    }

    public void setListener(AddTextFragmentListner listener) {
        this.listener = listener;
    }

    public AddTextFragment() {
        // Required empty public constructor
    }


    public static AddTextFragment newInstance(String param1, String param2) {
        AddTextFragment fragment = new AddTextFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_add_text, container, false);

        edt_add_txt = itemView.findViewById(R.id.edt_add_text);
        btn_done = itemView.findViewById(R.id.btn_done);
        recycler_color = itemView.findViewById(R.id.recycler_color);
        recycler_color.setHasFixedSize(true);
        recycler_color.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        ColorAdapter mColorAdapter = new ColorAdapter(getContext(),this);
        recycler_color.setAdapter(mColorAdapter);

        // Event
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddTextButtonClick(edt_add_txt.getText().toString(), colorSelected);
            }
        });
        return itemView;

    }


    @Override
    public void onColorSelected(int color) {
        colorSelected = color;
    }
}
