package com.shuoxd.camera.adapter;

import androidx.annotation.Nullable;

import com.amap.api.maps.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuoxd.camera.R;
import com.shuoxd.camera.bean.MapSearchBean;

import java.util.List;


public class MapSearchAdapter extends BaseQuickAdapter<MapSearchBean, BaseViewHolder> {
    public MapSearchAdapter(@Nullable List<MapSearchBean> data) {
        super(R.layout.item_map_search,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MapSearchBean item) {
            String address = item.getAddress();
            LatLng pt = item.getPt();
            String text = "";
            if (pt == null){
               text = String.format("%s\n%s%s", address, item.getCity(), item.getDistrict());
            }else {
               text = String.format("%s\n%s%s | %s:%f %s:%f", address, item.getCity(), item.getDistrict(),mContext.getString(R.string.m202_longitude), item.getPt().longitude, mContext.getString(R.string.m203_Latitude),item.getPt().latitude);
            }
            helper.setText(R.id.tvTitle,text);
    }
}
