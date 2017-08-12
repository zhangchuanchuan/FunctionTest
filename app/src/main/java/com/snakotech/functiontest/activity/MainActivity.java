package com.snakotech.functiontest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.snakotech.functiontest.R;
import com.snakotech.functiontest.api.LocalFunctionAPI;
import com.snakotech.functiontest.adapter.ItemAdapter;
import com.snakotech.functiontest.test.APITest;
import com.snakotech.functiontest.test.APITestVo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.jump_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ContainerActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        RecyclerView list = (RecyclerView) findViewById(R.id.api_list);

        //通过注解获取list
        List<APITestVo> apiTestList = getApiTestList();
        //展示列表
        ItemAdapter itemAdapter = new ItemAdapter(this, apiTestList);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(itemAdapter);
    }

    private List<APITestVo> getApiTestList() {
        List<APITestVo> list = new ArrayList<>();
        Method[] declaredMethods = LocalFunctionAPI.class.getDeclaredMethods();
        for (Method method : declaredMethods) {
            APITest apiTest = method.getAnnotation(APITest.class);
            if (apiTest != null) {
                APITestVo apiTestVo = new APITestVo();
                apiTestVo.setApiTest(apiTest);
                apiTestVo.setMethod(method.getName());
                list.add(apiTestVo);
            }
        }
        return list;
    }
}
