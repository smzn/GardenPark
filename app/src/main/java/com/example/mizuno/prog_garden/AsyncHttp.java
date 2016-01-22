package com.example.mizuno.prog_garden;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mizuno on 2015/12/11.
 */
public class AsyncHttp extends AsyncTask<Double, Integer, Boolean> {
    //HttpURLConnectionを利用したPOSTプログラム
    HttpURLConnection urlConnection = null; //HTTPコネクション管理用
    Boolean flg = false;

    int id, departure_id, arrival_id, flag = 0;

    public AsyncHttp(int id, int departure_id, int arrival_id, int flag){
        this.id = id;
        this.departure_id = departure_id;
        this.arrival_id = arrival_id;
        this.flag = flag;
    }


    //非同期処理ここから
    @Override
    protected Boolean doInBackground(Double... contents) {
        //URLの設定（AndroidからホストPCのローカルに接続するには10.0.2.2を利用する
        String urlinput = null;
        String postDataSample = null;
        try {

        if(this.flag == 1){
            urlinput = "http://garden.toitworks.com/measurements/add";
            postDataSample = "id="+this.id+"&departure="+this.departure_id+"&arrival="+this.arrival_id;
        }else if(this.flag == 2){
            urlinput = "http://garden.toitworks.com/positions/add";
            postDataSample = "measurement_id="+this.id+"&latitude="+contents[0]+"&longitude="+contents[1]+"&elevation="+contents[2];
        }

            //HttpURLConnectionの利用手順
           /*
           1.url.openConnection()を呼び出し接続開始
           取得できる型はURLConnection型なので、キャストする必要あり
           2.ヘッダーの設定
           3.bodyを設定する場合はHttpURLConnection.setDoOutputにbodyが存在することを明示
           4.connect()で接続を確立する
           5.レスポンスをgetInputStream()で取得する
           * */
            URL url = new URL(urlinput);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);


            //POSTパラメータ設定
            OutputStream out = urlConnection.getOutputStream();
            out.write(postDataSample.getBytes());
            out.flush();
            out.close();


            //レスポンスを受け取る
            //InputStream is = urlConnection.getInputStream();
            urlConnection.getInputStream();

            flg = true;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flg;
    }

}

