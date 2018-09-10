package com.zzti.lsy.ninetingapp.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zzti.lsy.ninetingapp.App;
import com.zzti.lsy.ninetingapp.R;
import com.zzti.lsy.ninetingapp.entity.CarStatusEntity;
import com.zzti.lsy.ninetingapp.entity.CarTypeEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author：anxin on 2018/8/7 14:54
 * 车辆类型适配器
 */
public class CarStatusAdapter extends BaseAdapter {
    private List<CarStatusEntity> carStatusEntities;

    public CarStatusAdapter(List<CarStatusEntity> carStatusEntities) {
        this.carStatusEntities = carStatusEntities;
    }

    @Override
    public int getCount() {
        return carStatusEntities == null ? 0 : carStatusEntities.size();
    }

    @Override
    public CarStatusEntity getItem(int i) {
        return carStatusEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(App.get()).inflate(R.layout.item_text, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (tag == 1) {
            convertView.setBackgroundResource(R.drawable.black_selector);
            viewHolder.tvName.setTextColor(App.get().getResources().getColor(R.color.color_white));
        } else {
            convertView.setBackgroundResource(R.drawable.white_gray_selector);
            viewHolder.tvName.setTextColor(App.get().getResources().getColor(R.color.color_666666));
        }
        viewHolder.tvName.setText(getItem(i).getName());
        return convertView;
    }

    private int tag;

    public void setTag(int tag) {
        this.tag = tag;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
