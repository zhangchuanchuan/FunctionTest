package com.snakotech.functiontest.run;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.snakotech.functiontest.R;

import java.util.List;

/**
 * 测试参数 adapter
 * Created by Administrator on 2017/8/12.
 */

public class ParamItemAdapter extends RecyclerView.Adapter<ParamItemAdapter.ViewHolder>{

    private List<ParamVo> mList;
    private Context context;

    public ParamItemAdapter(Context context, List<ParamVo> list) {
        this.context = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_param, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ParamVo paramVo = mList.get(position);
        holder.name.setText(paramVo.getName());
        if (paramVo.getValue() != null) {
            holder.value.setText(paramVo.getValue());
        }
        holder.value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                paramVo.setValue(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        EditText value;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.param_name);
            value = (EditText) itemView.findViewById(R.id.param_value);
        }
    }
}
