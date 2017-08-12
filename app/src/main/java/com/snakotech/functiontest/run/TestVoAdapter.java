package com.snakotech.functiontest.run;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.snakotech.functiontest.R;

import java.util.List;

/**
 * 测试参数 adapter
 * Created by Administrator on 2017/8/12.
 */

public class TestVoAdapter extends RecyclerView.Adapter<TestVoAdapter.ViewHolder>{

    private TestParamVo[] mList;
    private Context context;
    private TestVoCheckedListener mListener;

    public TestVoAdapter(Context context, TestParamVo[] list, TestVoCheckedListener listener) {
        this.context = context;
        mList = list;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_test_vo, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TestParamVo testParamVo = mList[position];
        holder.name.setText(String.valueOf(position + 1));
        if (testParamVo.isChecked()) {
            holder.check.setChecked(true);
        } else {
            holder.check.setChecked(false);
        }
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < mList.length; i ++) {
                        TestParamVo vo = mList[i];
                        if (vo.isChecked()) {
                            vo.setChecked(false);
                            notifyDataSetChanged();
                        }
                    }
                }
                testParamVo.setChecked(isChecked);
                if (isChecked) {
                    mListener.onTestChecked();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CheckBox check;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.check_name);
            check = (CheckBox) itemView.findViewById(R.id.check_item);
        }
    }

    interface TestVoCheckedListener {
        void onTestChecked();
    }
}
