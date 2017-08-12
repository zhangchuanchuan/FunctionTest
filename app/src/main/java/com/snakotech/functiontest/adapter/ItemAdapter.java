package com.snakotech.functiontest.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.snakotech.functiontest.R;
import com.snakotech.functiontest.activity.TestAPIActivity;
import com.snakotech.functiontest.test.APITest;
import com.snakotech.functiontest.test.APITestVo;

import java.util.List;

/**
 * adapter
 * Created by Administrator on 2017/8/12.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<APITestVo> mList;
    private Activity mActivity;

    public ItemAdapter(Activity activity, List<APITestVo> list) {
        mList = list;
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_api_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final APITestVo apiTestVo = mList.get(position);
        final APITest apiTest = apiTestVo.getApiTest();
        if (TextUtils.isEmpty(apiTest.name()) || TextUtils.isEmpty(apiTest.param())) {
            throw new RuntimeException("APITest 配置缺少name或者param");
        }
        holder.itemName.setText(apiTest.name());
        holder.itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mActivity, TestAPIActivity.class);
                intent.putExtra("method", apiTestVo.getMethod());
                intent.putExtra("param", apiTest.param());
                intent.putExtra("testParam", apiTest.testParam());
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private Button itemName;
        public ViewHolder(View itemView) {
            super(itemView);
            itemName = (Button) itemView.findViewById(R.id.item_name);
        }
    }
}
