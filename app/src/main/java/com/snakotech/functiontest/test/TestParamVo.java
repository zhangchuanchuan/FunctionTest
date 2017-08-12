package com.snakotech.functiontest.test;

import com.google.gson.JsonObject;


import org.json.JSONObject;

import java.util.List;

/**
 * 测试参数
 * Created by Administrator on 2017/8/12.
 */

public class TestParamVo {
    private boolean checked;
    private JSONObject param;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public JSONObject getParam() {
        return param;
    }

    public void setParam(JSONObject param) {
        this.param = param;
    }
}
