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
public class ShowMyCollectAdapter extends RecyclerView.Adapter {
    private List<GoodsDetail> goods;
    private LayoutInflater inflater;
    private ClickListener listener;
    private LongClickListener longListener;

    public ShowMyCollectAdapter(List<GoodsDetail> goods, Context context) {
        this.goods = goods;
        this.inflater = LayoutInflater.from(context);
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public void setLongListener(LongClickListener longListener) {
        this.longListener = longListener;
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
        Date date = new Date(good.getDatetime());
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm");
        String string = sdf.format(date);
        goodViewHolder.time.setText(string);
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }
    private class GoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView goodName;
        private TextView goodPrice;
        private TextView time;
        public GoodViewHolder(View itemView) {
            super(itemView);
            goodName = (TextView) itemView.findViewById(R.id.goodName);
            goodPrice = (TextView) itemView.findViewById(R.id.goodPrice);
            time = (TextView) itemView.findViewById(R.id.time);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener!=null){
                listener.click(v,getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            longListener.click(v,getPosition());
            return true;
        }
    }
    public interface ClickListener{
        void click(View v, int position);
    }
    public interface LongClickListener{
        void click(View v, int position);
    }
}
