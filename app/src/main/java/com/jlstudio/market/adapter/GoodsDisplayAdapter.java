package com.jlstudio.market.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.Downloadimgs;
import com.jlstudio.main.util.CircleImageView;
import com.jlstudio.market.bean.GoodsDetail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by gzw on 2016/3/24.
 */
public class GoodsDisplayAdapter extends RecyclerView.Adapter {
    public static final int FOOTER = 1;
    private List<GoodsDetail> goods;
    private Context context;
    private LayoutInflater inflater;
    private ClickListener click;
    private ImageClickListener imageClick;
    private int screenWidth;
    public GoodsDisplayAdapter(List<GoodsDetail> goods, Context context) {
        this.goods = goods;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
    }

    public void setGoods(List<GoodsDetail> goods) {
        this.goods = goods;
    }

    public void setClick(ClickListener click) {
        this.click = click;
    }

    public void setImageClick(ImageClickListener imageClick) {
        this.imageClick = imageClick;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == goods.size()-1&&goods.size()>=20)
            return FOOTER;
        else
            return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view ;
        if(i == FOOTER){
            view = LayoutInflater.from(context).inflate(R.layout.sub_show_goods_footer,viewGroup,false);
            return new FooterViewHolder(view);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.sub_goods,viewGroup,false);
            return new GoodsViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if(i == goods.size()-1&&goods.size()>=20){

        }else{
            GoodsViewHolder gViewHolder = (GoodsViewHolder) viewHolder;
            gViewHolder.position = i;
            gViewHolder.username.setText(goods.get(i).getUsername());
            gViewHolder.browse.setText(goods.get(i).getViews()+"");
            gViewHolder.price.setText(goods.get(i).getPrice() + "");
            gViewHolder.description.setText(goods.get(i).getDescription());
            Date date = new Date(goods.get(i).getDatetime());
            SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm");
            String time = sdf.format(date);
            gViewHolder.time.setText(time);
            String url = Config.URL+ "faces/"+goods.get(i).getUserid() +".jpg";
            Uri uri = Uri.parse(url);
            gViewHolder.face.setImageURI(uri);
            //Downloadimgs.initImageLoader(context).displayImage(url,gViewHolder.face,Downloadimgs.getOption());
            initRecycleView(gViewHolder.picture, i);
        }

    }

    private void initRecycleView(RecyclerView picture,final int i) {
        List<String> images = goods.get(i).getImages();
        SubGoodsDisplayAdapter adapter = new SubGoodsDisplayAdapter(images,context,screenWidth);
        picture.setAdapter(adapter);
        picture.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        picture.setLayoutManager(layout);
        adapter.setClick(new SubGoodsDisplayAdapter.ClickListener() {
            @Override
            public void click(View v, int position) {
                if(click!=null){
                    imageClick.click(v,i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }
    private class GoodsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public SimpleDraweeView face;
        public RecyclerView picture;
        public TextView username;
        public TextView time;
        public TextView price;
        public TextView description;
        public TextView from;
        public TextView browse;
        private TextView moneyIcon;
        private TextView eyeIcon;
        public int position;
        public GoodsViewHolder(View itemView) {
            super(itemView);
            face = (SimpleDraweeView) itemView.findViewById(R.id.face);
            picture = (RecyclerView) itemView.findViewById(R.id.picture);
            username = (TextView) itemView.findViewById(R.id.username);
            time = (TextView) itemView.findViewById(R.id.time);
            price = (TextView) itemView.findViewById(R.id.price);
            description = (TextView) itemView.findViewById(R.id.description);
            from = (TextView) itemView.findViewById(R.id.from);
            browse = (TextView) itemView.findViewById(R.id.browse);
            moneyIcon = (TextView) itemView.findViewById(R.id.moneyicon);
            eyeIcon = (TextView) itemView.findViewById(R.id.eyeicon);
            Typeface icon = Typeface.createFromAsset(context.getAssets(),"fonts/moneyeye.ttf");
            moneyIcon.setTypeface(icon);
            eyeIcon.setTypeface(icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(click!=null){
                click.click(v,v,getPosition());
            }
        }
    }
    private class FooterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout loading;
        public TextView clickLoadMore;
        public FooterViewHolder(View itemView) {
            super(itemView);
            loading = (LinearLayout) itemView.findViewById(R.id.loading);
            loading.setVisibility(View.GONE);
            clickLoadMore = (TextView) itemView.findViewById(R.id.clickLoadMore);
            clickLoadMore.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(click!=null){
                        click.click(loading,clickLoadMore,getPosition());
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            loading.setVisibility(View.VISIBLE);
            clickLoadMore.setVisibility(View.GONE);
            if(click!=null){
                click.click(loading,clickLoadMore,getPosition());
            }
        }
    }
    public interface ClickListener{
        void click(View v1,View v2,int position);
    }
    public interface ImageClickListener{
        void click(View v1,int position);
    }
}
