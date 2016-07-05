package com.example.okhttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Country>adapter;

    private OkHttpClient httpClient;

    //拦截器
    private HttpLoggingInterceptor loggingInterceptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView= (ListView) findViewById(R.id.list_item);
        adapter=new ArrayAdapter<Country>(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        //初始okhttp 相关
        loggingInterceptor=new HttpLoggingInterceptor();
        //设置级别 body 都要
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient=new OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)//添加一个日志拦截器
                        .build();

        //okhttp 网络连
        //实例化
       // httpClient = new OkHttpClient();
        //获取request 对象    默认 GEt请求

        String owner="square";//公司
        String repo="retrofit";//产品
//"https://api.github.com/repos/squre/retrofit/contributors"
        Request request=new Request.Builder().
                         url("https://api.github.com/repos/"+owner+"/"+repo+"/contributors").
                         build();

        //获取response 对象
        //.enqueue 异步   excute 同步
       //开启子线程
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body=response.body();

                String data=body.string();
                //解析 data里的信息  [{login:, contributions:},{},{}]
                //1.创建实体类 2.Gson
                Gson gson=new Gson();

                //TypeToken 里面是一个集合
                TypeToken<List<Country>> typeToken=new TypeToken<List<Country>>(){};

                final List<Country> datas= gson.fromJson(data,typeToken.getType());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter.addAll(datas);

                        adapter.notifyDataSetChanged();
                    }
                });
//
//
//                if(response.isSuccessful()){
//                    //通过body（）方法获取 responseBody 对象 就是获取的数据
//                   ResponseBody body= response.body();
//                    if(body==null){
//                        Log.d("2222222", "onResponse: ");
//                    }
//                    Log.d("000000", body.string());
//                }
            }
        });

        //同步方法 直接在当前线程操作  直接返回response对象
//        try {
//            Response response=httpClient.newCall(request).execute();
//
//            ResponseBody body= response.body();
//            if(body==null){
//                Log.d("2222222", "onResponse: ");
//            }
//            Log.d("000000", "onResponse: "+body.string());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
