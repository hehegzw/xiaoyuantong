package com.jlstudio.market.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jlstudio.R;
import com.jlstudio.market.bean.GoodsDetail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by gzw on 2016/3/26.
 */
public class ShowMyGoodsAdapter extends RecyclerView.Adapter {
    private List<GoodsDetail> goods;
    private LayoutInflater inflater;
    private ClickListener listener;

    public ShowMyGoodsAdapter(List<GoodsDetail> goods, Context context) {
        this.goods = goods;
        this.inflater = LayoutInflater.from(context);
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public void setGoods(List<GoodsDetail> goods) {
        this.goods = goods;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.sub_show_goods,viewGroup,false);
        return new GoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        GoodViewHolder goodViewHolder = (GoodViewHolder) viewHolder;
        GoodsDetail good = goods.get(i);
        goodViewHolder.goodName.setText(good.getDescription().split(":")[0]);
        goodViewHolder.goodPrice.setText(good.getPrice()+"");
        goodViewHolder.viewCounts.setText(good.getViews()+"");
        Date date = new Date(good.getDatetime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd HH:MM:SS");
        String string = sdf.format(date);
        goodViewHolder.time.setText(string);
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }
    private class GoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView goodName;
        private TextView goodPrice;
        private TextView viewCounts;
        private TextView time;
        public GoodViewHolder(View itemView) {
            super(itemView);
            goodName = (TextView) itemView.findViewById(R.id.goodName);
            goodPrice = (TextView) itemView.findViewById(R.id.goodPrice);
            viewCounts = (TextView) itemView.findViewById(R.id.viewCounts);
            time = (TextView) itemView.findViewById(R.id.time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener!=null){
                listener.click(v,getPosition());
            }
        }
    }
    public interface ClickListener{
        void click(View v,int position);
    }
}
