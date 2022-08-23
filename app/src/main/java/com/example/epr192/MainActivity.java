package com.example.epr192;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

class MyAsyncTask extends AsyncTask<String,Void,Bitmap>{
    private AlertDialog dia ;
    private Context context;
    private ImageView imageView;

    public MyAsyncTask(Context context, ImageView imageView) {
        super();
        this.context = context;
        this.imageView =imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       // getDialog();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setIcon(R.drawable.img );
        dialog.setTitle("友情提示！");
        dialog.setMessage("正在玩命为您加载中");
        //下面show()方法不要忘了，否则不显示进度对话框
        dia = dialog.show();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }
        else{
            Toast.makeText(context,"下载图片失败",Toast.LENGTH_LONG).show();
        }
        //关闭进度对话框
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        dia.dismiss();
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        //网络请求：
        /*创建HttpClient的实例*/
        HttpClient httpClient = new DefaultHttpClient();
//        /*创建连接方法的实例，HttpGet()的构造中传入url地址*/
        HttpGet httpGet = new HttpGet(params[0]);
        try {
            /*调用创建好的HttpClient的实例的execute方法来发送创建好的HttpGet或HttpPost请求，并返回HttpResponse对象*/
            HttpResponse httpResponse = httpClient.execute(httpGet);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                /*返回实体对象*/
                HttpEntity entity = httpResponse.getEntity();
                byte[] data = EntityUtils.toByteArray(entity);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }


      return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {

        super.onProgressUpdate(values);

    }
}

public class MainActivity extends Activity {
    private ImageView imageView;
    private Button button;
    private Button button2;
    private MyAsyncTask myAsyncTask;
    private MyAsyncTask myAsyncTask2;
    private String url= "http://p5.qhimg.com/dmt/490_350_/t01405cf23f986e5ef6.jpg";
    private String url2= "https://i0.hdslb.com/bfs/activity-plat/static/d5cdbd520d8b1993024f2c347670b46e/7IAuwo8QgZ_w750_h203.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        imageView = findViewById(R.id.imageView);
        //创建AsyncTask的实例
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAsyncTask = new MyAsyncTask(MainActivity.this,imageView);

                downloadPic(myAsyncTask,url);
            }
        });

       // myAsyncTask2 = new MyAsyncTask(this, imageView);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAsyncTask2 = new MyAsyncTask(MainActivity.this, imageView);

                downloadPic(myAsyncTask2,url2);
            }
        });

    }


    //点击按钮，开始异步下载图片
    public void downloadPic(MyAsyncTask myAsyncTask,String url){
        /*AsyncTask的实例只能被执行一次*/
        myAsyncTask.execute(url);
    }
}
