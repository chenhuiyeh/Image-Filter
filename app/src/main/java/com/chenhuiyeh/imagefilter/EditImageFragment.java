package com.chenhuiyeh.imagefilter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.chenhuiyeh.imagefilter.Interfaces.EditImageFragmentListener;

public class EditImageFragment extends BottomSheetDialogFragment implements SeekBar.OnSeekBarChangeListener{

    private EditImageFragmentListener listener;
    SeekBar seekbar_brightness, seekbar_contrast, seekbar_saturation;

    static EditImageFragment instance;

    public static EditImageFragment getInstance() {
        if (instance == null)
            instance  = new EditImageFragment();
        return instance;
    }

    public EditImageFragment() {
        // Required empty public constructor
    }

    public void setListener(EditImageFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_edit_image, container, false);

        seekbar_brightness = itemView.findViewById(R.id.seekbar_brightness);
        seekbar_contrast = itemView.findViewById(R.id.seekbar_contrast);
        seekbar_saturation = itemView.findViewById(R.id.seekbar_saturation);

        seekbar_brightness.setMax(200);
        seekbar_brightness.setProgress(100);

        seekbar_contrast.setMax(20);
        seekbar_contrast.setProgress(0);

        seekbar_saturation.setMax(30);
        seekbar_saturation.setProgress(10);

        seekbar_saturation.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener)this);
        seekbar_contrast.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener)this);
        seekbar_brightness.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener)this);

        return itemView;
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(listener!=null) {
            if (seekBar.getId() == R.id.seekbar_brightness) {
                listener.onBrightnessChanged(progress-100);
            } else if (seekBar.getId() == R.id.seekbar_contrast) {
                progress+=10;
                float val = .10f*progress;
                listener.onContrastChanged(val);
            } else if (seekBar.getId() == R.id.seekbar_saturation) {
                float value = .10f*progress;
                listener.onSaturationChanged(value);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if(listener!=null) {
            listener.onEditStarted();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(listener!= null) {
            listener.onEditCompleted();
        }
    }

    public void resetControls() {
        seekbar_brightness.setProgress(100);
        seekbar_contrast.setProgress(0);
        seekbar_saturation.setProgress(10);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
