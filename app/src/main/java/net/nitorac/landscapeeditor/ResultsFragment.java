package net.nitorac.landscapeeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.dd.processbutton.iml.ActionProcessButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public Bitmap currentBitmap = createImage(512, 512, -1);

    public ActionProcessButton validateBtn;
    public ImageView resView;
    public RequestTask reqTask;

    public ResultsFragment() {
        // Required empty public constructor
    }

    public static ResultsFragment newInstance() {
        ResultsFragment fragment = new ResultsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume(){
        super.onResume();
        if(reqTask == null || !reqTask.getStatus().equals(AsyncTask.Status.RUNNING)){
            validateBtn.setProgress(0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_results, container, false);
        resView = v.findViewById(R.id.resultImageView);
        resView.setImageBitmap(currentBitmap);
        validateBtn = v.findViewById(R.id.validateBtn);
        validateBtn.setMode(ActionProcessButton.Mode.ENDLESS);

        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateBtn.getProgress() == 0){
                    reqTask = new RequestTask(ResultsFragment.this);
                    reqTask.execute();
                }
            }
        });

        return v;
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

    public static Bitmap createImage(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }
}
