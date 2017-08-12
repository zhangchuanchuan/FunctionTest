package com.snakotech.functiontest.container;

import android.app.AlertDialog;
import android.content.Context;

import com.snakotech.functiontest.test.APITest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 本地方法的API
 * Created by Administrator on 2017/8/12.
 */

public class LocalFunctionAPI {

    //运行环境，假设没有运行的环境方法都无法运行。
    private Context context;

    public LocalFunctionAPI(Context context) {
        this.context = context;
    }

    /**
     * 弹窗的api，参数{"msg":"hello world"}
     * @param object json Object
     */
    @APITest(name = "alert", param = "msg",
            testParam = "[{\"msg\":\"hello world\"}, {\"msg\":\"hello android\"}]")
    public void alert(JSONObject object) {
        // check runtime
        if (context == null) {
            throw new RuntimeException("无运行环境");
        }
        String msg = "";
        // check param
        if (object.has("msg")) {
            try {
                msg = object.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("传入参数不正确");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(msg).create().show();
    }

    /**
     * 弹窗的api，参数{"msg":"hello world"}
     * @param object json Object
     */
    @APITest(name = "alert2", param = "msg",
            testParam = "[{\"msg\":\"hello world\"}, {\"msg\":\"hello android\"}]")
    public void alert2(JSONObject object) {
        // check runtime
        if (context == null) {
            throw new RuntimeException("无运行环境");
        }
        String msg = "";
        // check param
        if (object.has("msg")) {
            try {
                msg = object.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("传入参数不正确");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(msg).create().show();
    }
}
