package com.snakotech.functiontest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.snakotech.functiontest.R;
import com.snakotech.functiontest.api.LocalFunctionAPI;
import com.snakotech.functiontest.adapter.ParamItemAdapter;
import com.snakotech.functiontest.test.ParamVo;
import com.snakotech.functiontest.test.TestParamVo;
import com.snakotech.functiontest.adapter.TestVoAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * Created by Administrator on 2017/8/12.
 */

public class TestAPIActivity extends Activity {

    LocalFunctionAPI mAPI;

    String mMethodName;
    String param;
    String testParam;
    //测试数据的参数
    private TestParamVo[] mTestVos;
    //接口的参数名称
    private String[] mParamValues;
    //展示的参数list
    List<ParamVo> mParamVos;

    //标题
    private TextView mTitleView;
    //test list
    private RecyclerView mTestListView;
    private TestVoAdapter mTestListAdapter;
    //param list
    private RecyclerView mParamListView;
    private ParamItemAdapter mParamListAdapter;


    //接口的
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api);
        mAPI = new LocalFunctionAPI(this);
        Bundle extras = getIntent().getExtras();
        mMethodName = extras.getString("method");
        param = extras.getString("param");
        testParam = extras.getString("testParam");
        initData();
        initView();
    }

    /**
     * 初始化view数据
     */
    private void initView() {
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setText(mMethodName);

        mParamListView = (RecyclerView) findViewById(R.id.param_list);
        mParamListAdapter = new ParamItemAdapter(this, mParamVos);
        mParamListView.setLayoutManager(new LinearLayoutManager(this));
        mParamListView.setAdapter(mParamListAdapter);

        mTestListView = (RecyclerView) findViewById(R.id.test_vo_list);
        mTestListAdapter = new TestVoAdapter(this, mTestVos, new TestVoAdapter.TestVoCheckedListener() {
            @Override
            public void onTestChecked() {
                initParamList();
                mParamListAdapter.notifyDataSetChanged();
            }
        });
        mTestListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTestListView.setAdapter(mTestListAdapter);

        //执行
        findViewById(R.id.execute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //反射执行
                JSONObject jsonObject = new JSONObject();
                for (ParamVo paramVo : mParamVos) {
                    if (!TextUtils.isEmpty(paramVo.getValue())) {
                        try {
                            jsonObject.put(paramVo.getName(), paramVo.getValue());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Method[] declaredMethods = mAPI.getClass().getDeclaredMethods();
                boolean hasExecute = false;
                for (Method method : declaredMethods) {
                    if (mMethodName.equals(method.getName())) {
                        try {
                            hasExecute = true;
                            method.invoke(mAPI, jsonObject);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();

                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (!hasExecute) {
                    Toast.makeText(TestAPIActivity.this, "没有这个方法", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        Gson gson = new Gson();
        JsonObject[] objects = gson.fromJson(testParam, JsonObject[].class);

        if (objects != null) {
            mTestVos = new TestParamVo[objects.length];
            for (int i = 0; i < objects.length; i++) {
                TestParamVo paramVo = new TestParamVo();
                JsonObject object = objects[i];
                Set<String> keys = object.keySet();
                JSONObject jsonObject = new JSONObject();
                for (String key : keys) {
                    try {
                        jsonObject.put(key, object.get(key).getAsString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                paramVo.setParam(jsonObject);
                mTestVos[i] = paramVo;
            }
        }

        if (mTestVos != null && mTestVos.length > 0) {
            mTestVos[0].setChecked(true);
        }

        mParamValues = param.split("\\|");
        mParamVos = new ArrayList<>();
        initParamList();
    }

    /**
     * 初始化参数列表数据
     */
    private void initParamList() {
        TestParamVo checkTest = getCheckTestVo();
        mParamVos.clear();
        for (String string : mParamValues) {
            ParamVo param = new ParamVo();
            param.setName(string);
            if (checkTest != null) {
                JSONObject object = checkTest.getParam();
                if (object != null && object.has(string)) {
                    try {
                        param.setValue(object.getString(string));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            mParamVos.add(param);
        }
    }


    public TestParamVo getCheckTestVo() {
        if (mTestVos == null) {
            return null;
        }
        for (TestParamVo test : mTestVos) {
            if (test.isChecked()) {
                return test;
            }
        }
        return null;
    }
}
