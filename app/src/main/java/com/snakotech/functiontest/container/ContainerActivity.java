package com.snakotech.functiontest.container;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.snakotech.functiontest.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 容器Activity
 * Created by Administrator on 2017/8/12.
 */

public class ContainerActivity extends Activity {


    LocalFunctionAPI mAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        mAPI = new LocalFunctionAPI(this);

        findViewById(R.id.alert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realDo();
            }
        });
    }

    //业务方调用我们api
    private void realDo() {
        JSONObject object = new JSONObject();
        try {
            object.put("msg", "Hello World");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAPI.alert(object);
    }


}
