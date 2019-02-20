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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.chenhuiyeh.imagefilter.Adapter.ColorAdapter;
import com.chenhuiyeh.imagefilter.Interfaces.BrushFragmentListener;

import java.util.ArrayList;
import java.util.List;


public class BrushFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener {
    SeekBar seekBar_brush_size, seekBar_opacity_size;
    RecyclerView recycler_color;
    ToggleButton btn_brush_state;
    ColorAdapter mColorAdapter;

    BrushFragmentListener mBrushFragmentListener;

    static BrushFragment instance;

    public static BrushFragment getInstance() {
        if (instance == null) instance = new BrushFragment();
        return instance;
    }

    public BrushFragment(){

    }

    public void setBrushFragmentListener(BrushFragmentListener brushFragmentListener) {
        mBrushFragmentListener = brushFragmentListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_brush, container, false);

        seekBar_brush_size = itemView.findViewById(R.id.seekbar_brush_size);
        seekBar_opacity_size = itemView.findViewById(R.id.seekbar_opacity);
        btn_brush_state = itemView.findViewById(R.id.btn_brush_State);
        recycler_color = itemView.findViewById(R.id.recycler_color);
        recycler_color.setHasFixedSize(true);
        recycler_color.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        mColorAdapter = new ColorAdapter(getContext(),this);
        recycler_color.setAdapter(mColorAdapter);

        // Event
        seekBar_opacity_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBrushFragmentListener.onBrushOpacityChangedListener(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar_brush_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBrushFragmentListener.onBrushSizeChangedListener(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btn_brush_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBrushFragmentListener.onBrushStateChangedListener(isChecked);
            }
        });
        return itemView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onColorSelected(int color) {
        mBrushFragmentListener.onBrushColorChangedListener(color);
    }
}
