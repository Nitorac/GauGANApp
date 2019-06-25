package net.nitorac.landscapeeditor;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import net.nitorac.landscapeeditor.colorview.ColorListAdapter;
import net.nitorac.landscapeeditor.colorview.ColorView;
import net.nitorac.landscapeeditor.colorview.FloorListView;
import net.nitorac.landscapeeditor.drawview.BrushView;
import net.nitorac.landscapeeditor.drawview.DrawingView;
import net.nitorac.landscapeeditor.drawview.brushes.BrushSettings;
import net.nitorac.landscapeeditor.drawview.brushes.Brushes;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InputFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private DrawingView drawingView;
    private ColorView colorView;
    private SeekBar sizeView;
    private BrushView brushView;

    private Dialog colorDialog;

    private Bundle currentBundle;

    public InputFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InputFragment newInstance() {
        InputFragment fragment = new InputFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_input, container, false);
        drawingView = v.findViewById(R.id.drawingView);
        ((BrushView)v.findViewById(R.id.brushView)).setDrawingView(drawingView);

        sizeView = v.findViewById(R.id.sizeSeekBar);

        colorView = v.findViewById(R.id.colorView);

        brushView = v.findViewById(R.id.brushView);

        brushView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrushSettings set = drawingView.getBrushSettings();
                if(set.getSelectedBrush() == Brushes.PEN){
                    set.setSelectedBrush(Brushes.FILL);
                }else if(set.getSelectedBrush() == Brushes.FILL){
                    set.setSelectedBrush(Brushes.ERASER);
                }else if(set.getSelectedBrush() == Brushes.ERASER){
                    set.setSelectedBrush(Brushes.PEN);
                }
            }
        });

        colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorDialog = new Dialog(getActivity());
                colorDialog.setContentView(R.layout.color_dialog);

                FloorListView listView = colorDialog.findViewById(R.id.colorList);
                ColorListAdapter adapter = new ColorListAdapter(InputFragment.this, new ArrayList<>(ColorView.colors.entrySet()));

                listView.setAdapter(adapter);
                colorDialog.show();
            }
        });

        sizeView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drawingView.getBrushSettings().setSelectedBrushSize(progress/100.0f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(currentBundle != null){
            drawingView.setBackgroundImage(((MainActivity)getActivity()).inputImage);
            sizeView.setProgress(currentBundle.getInt("savedSize"));
            updateColor(currentBundle.getInt("savedColor"));
            drawingView.getBrushSettings().setSelectedBrush(currentBundle.getInt("savedBrush"));
        }else{
            sizeView.setProgress(25);
            updateColor(colorView.getColor());
        }
        return v;
    }

    public Dialog getColorDialog(){
        return colorDialog;
    }

    public void updateColor(int color){
        colorView.setColor(color);
        drawingView.getBrushSettings().setColor(color);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        currentBundle = new Bundle();
        ((MainActivity)getActivity()).inputImage = drawingView.exportDrawing();
        currentBundle.putInt("savedSize", sizeView.getProgress());
        currentBundle.putInt("savedColor", colorView.getColor());
        currentBundle.putInt("savedBrush", drawingView.getBrushSettings().getSelectedBrush());
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
        void onFragmentInteraction(Uri uri);
    }
}
