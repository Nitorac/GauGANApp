package net.nitorac.landscapeeditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Nitorac.
 */
public class RequestTask extends AsyncTask<Void, Void, Integer> {

    private ResultsFragment resFrag;

    private Bitmap resBitmap;

    public RequestTask(ResultsFragment resFrag){
        this.resFrag = resFrag;
    }

    @Override
    protected void onPreExecute(){
        resFrag.validateBtn.setProgress(50);
    }

    @Override
    protected void onPostExecute(final Integer res){
        if(res == 100){
            resFrag.currentBitmap = resBitmap;
            resFrag.resView.setImageBitmap(resBitmap);
        }
        resFrag.validateBtn.setProgress(res);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                resFrag.validateBtn.setProgress(0);
            }
        }, 5000);
    }

    @Override
    protected Integer doInBackground(Void... v) {
        OkHttpClient httpClient = new OkHttpClient();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap.createScaledBitmap(((MainActivity)resFrag.getActivity()).inputImage, 512, 512, false).compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String inputEncoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        int randomNumber = new Random().nextInt(1000000000);
        String name = format.format(new Date()) + ", " + System.currentTimeMillis() + "-" + randomNumber;

        RequestBody formBody = new FormBody.Builder()
                .add("imageBase64", "data:image/png;base64," + inputEncoded)
                .add("name", name)
                .build();
        Request submitReq = new Request.Builder()
                .url("http://54.71.84.47:443/nvidia_gaugan_submit_map")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody)
                .build();

        try {
            Response response = httpClient.newCall(submitReq).execute();
            if(!response.isSuccessful() || response.body() == null){
                System.out.println(response.body());
                return -1;
            }

            RequestBody formBody2 = new FormBody.Builder()
                    .add("name", name)
                    .add("style_name", "random")
                    .add("artistic_style_name", "none")
                    .build();
            Request receiveReq = new Request.Builder()
                    .url("http://54.71.84.47:443/nvidia_gaugan_receive_image")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0")
                    .post(formBody2)
                    .build();

            Response finalResp = httpClient.newCall(receiveReq).execute();

            if(!finalResp.isSuccessful() || finalResp.body() == null){
                System.out.println(response.body());
                return -1;
            }

            resBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(finalResp.body().byteStream()), resFrag.resView.getMeasuredWidth(), resFrag.resView.getMeasuredHeight(), true);
            return 100;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
