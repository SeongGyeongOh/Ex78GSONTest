package com.osg.ex78gsontest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);


    }

    public void clickBtn(View view) {
        //GSON Library를 이용하여 JSON 문자열을 곧바로 내가 원하는 Person 객체로 parsing
        String jsonString = "{'name':'robin', 'age':25}";

        Gson gson = new Gson();
        Person p = gson.fromJson(jsonString, Person.class); //이미 Person 객체가 만들어졌다

        tv.setText(p.name+" , "+p.age);

    }

    public void clickBtn2(View view) {
        //역 parsing - Object를 Jason 문자열로
        Person p = new Person("sam", 25);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(p);

        tv.setText(jsonStr);
    }

    ArrayAdapter adapter;
    List<String> items = new ArrayList<>();
    ListView listView;

    public void clickBtn3(View view) {
        //JSON Array 손쉽게 객체로 생성하기
        String str = "[{'name':'sam', 'age':25}, {'name':'hong', 'age':30}]";
        Gson gson = new Gson();
        Person[] personArr = gson.fromJson(str, Person[].class);

        //배열 ->리스트뷰
        for( Person p : personArr){
            items.add(p.name+" , " +p.age);
        }

        listView = findViewById(R.id.listview);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

    }

    public void clickBtn4(View view) {
        //네트워크 JSON 읽어오기
        final String serverUrl = "http://kamniang.dothome.co.kr/test.json";

        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(serverUrl);

                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                    connection.setUseCaches(false);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    InputStream is = connection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    //InputStreamReader까지만 있으면 Gson이 알아서 읽어와서 객체로 파싱해줌
                    Gson gson = new Gson();
                    final Person p = gson.fromJson(isr, Person.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(p.name + " , " + p.age);
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
