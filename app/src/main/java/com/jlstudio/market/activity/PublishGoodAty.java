package com.jlstudio.market.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jlstudio.R;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.net.UplaodFace;
import com.jlstudio.main.net.Uptoqiniu;
import com.jlstudio.main.util.ProgressUtil;
import com.jlstudio.market.adapter.PublishGoodAdapter;
import com.jlstudio.market.bean.GoodsDetail;
import com.jlstudio.market.dialog.DeleteDialog;
import com.jlstudio.market.dialog.ProgressDialog;
import com.jlstudio.publish.util.ShowToast;
import com.jlstudio.publish.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class PublishGoodAty extends Activity implements View.OnClickListener {
    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int PUBLISH = 0;
    public static final int MODIFY = 2;
    private EditText price;
    private EditText description;
    private EditText name;
    private RecyclerView pictures;
    private Button submit;
    private Button modify;
    private PublishGoodAdapter adapter;
    private List<String> goodPics;//显示的图片路径
    private List<String> localPics;//本地的图片路径,这些图片是要上传的
    private HashSet<String> servicePics;//保存要删除的图片路径
    private List<String> oriServicePics;//服务器的图片路径,这些图片是要修改的
    private TextView back;
    private int type;
    private int goodId;
    private Uptoqiniu uptoqiniu;
    private String action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_good_aty);
        if(getIntent().getAction()!=null)
            type = Integer.valueOf(getIntent().getAction());
        initView();
        initData();
    }

    private void initView() {
        price = (EditText) findViewById(R.id.price);
        description = (EditText) findViewById(R.id.descriptiom);
        name = (EditText) findViewById(R.id.name);
        submit = (Button) findViewById(R.id.submit);
        modify = (Button) findViewById(R.id.modify);
        if(type == PUBLISH){
            modify.setVisibility(View.GONE);
        }else{
            submit.setText("提交修改");
        }

        pictures = (RecyclerView) findViewById(R.id.pictures);
        back = (TextView) findViewById(R.id.back);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf");
        back.setTypeface(iconfont);
        goodPics = new ArrayList<>();
        goodPics.add("header");//第一个总是添加图标
        adapter = new PublishGoodAdapter(goodPics, this);
        pictures.setAdapter(adapter);
        pictures.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.HORIZONTAL);
        pictures.setLayoutManager(layout);
        initEvent();
    }

    private void initEvent() {
        submit.setOnClickListener(this);
        modify.setOnClickListener(this);
        back.setOnClickListener(this);
        adapter.setListener(new PublishGoodAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(position == 0)
                    openPic();
            }
        });
        adapter.setLongListener(new PublishGoodAdapter.LongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                new DeleteDialog(PublishGoodAty.this, position, new DeleteDialog.DeleteListener() {
                    @Override
                    public void delete(int position) {
                        if(type == MODIFY){
                            if(oriServicePics.contains(goodPics.get(position))){
                                servicePics.add(goodPics.get(position));
                            }else if(localPics.contains(goodPics.get(position))){
                                localPics.remove(goodPics.get(position));
                            }
                        }
                        goodPics.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });
    }
    private void initData(){
        uptoqiniu = new Uptoqiniu();
        servicePics = new HashSet<>();
        oriServicePics = new ArrayList<>();
        localPics = new ArrayList<>();
        if(type == MODIFY){
            GoodsDetail goods = (GoodsDetail) getIntent().getSerializableExtra("good");
            for(String gd:goods.getImages()){
                goodPics.add(gd);
                oriServicePics.add(gd);
            }
            adapter.notifyDataSetChanged();
            price.setText(goods.getPrice() + "");
            name.setText(goods.getDescription().split(":")[0]);
            description.setText(goods.getDescription());
            goodId = goods.getId();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if(!Config.isNetworkAvailable(this)){
                    ShowToast.show(this,"无网络，请检查网络连接");
                    return;
                }
                publishGood();
                break;
            case R.id.modify:
                if(!Config.isNetworkAvailable(this)){
                    ShowToast.show(this,"无网络，请检查网络连接");
                    return;
                }
                deleteGood();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void deleteGood() {
        JSONObject json = new JSONObject();
        try {
            json.put("goodsId",goodId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UplaodFace.deleteData(Config.URL + "deletegoods", json.toString(), new UplaodFace.Success() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(PublishGoodAty.this, "宝贝下架成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new UplaodFace.Failure() {
            @Override
            public void onFailure() {
                Toast.makeText(PublishGoodAty.this, "宝贝下架失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void publishGood() {
        String strName = name.getText().toString();
        String strPrice = price.getText().toString();
        String strDes = description.getText().toString();
        if(TextUtils.isEmpty(strPrice)){
            Toast.makeText(this, "出个价吧", Toast.LENGTH_SHORT).show();
            return;
        } else if(TextUtils.isEmpty(strDes)){
            Toast.makeText(this, "描述一下你的宝贝吧", Toast.LENGTH_SHORT).show();
            return;
        }else if(goodPics.size() == 0){
            Toast.makeText(this, "为宝贝爆几张照吧", Toast.LENGTH_SHORT).show();
            return;
        }else if(StringUtil.isEmpty(strName)){
            Toast.makeText(this, "你的宝贝叫什么名字", Toast.LENGTH_SHORT).show();
            return;
        }
        final JSONObject json = new JSONObject();
        try {
            if(type == MODIFY){
                json.put("goodsId",goodId);
                action = "updategoods";
            }else{
                action = "uploadgoods";
            }
            json.put("userid",Config.loadUser(this).getUsername());
            json.put("price",strPrice);
            json.put("description", strName + ":" + strDes);
            if(servicePics.size()>0){
                JSONArray array = new JSONArray();
                for(String str:servicePics){
                    array.put(str);
                }
                json.put("deleted",array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<File> files = new ArrayList<>();
        for(int i=0;i<localPics.size();i++){
            files.add(new File(localPics.get(i)));
        }
        new ProgressDialog(this,files,json,action,type).show();
//        uptoqiniu.UploadImages(files, new Uptoqiniu.SuccessListener() {
//            @Override
//            public void success(List<String> list) {
//                JSONArray jsonArray = new JSONArray(list);
//                try {
//                    json.put("images",jsonArray);
//                    Uptoserver(action,json);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Uptoqiniu.FaulerListener() {
//            @Override
//            public void fauler() {
//                if(type == MODIFY){
//                    Toast.makeText(PublishGoodAty.this, "宝贝修改失败,请检查网络连接", Toast.LENGTH_SHORT).show();
//                }
//                Toast.makeText(PublishGoodAty.this, "宝贝发布失败,请检查网络连接", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void openPic() {
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setMutiSelectMaxSize(8)
                .setEnablePreview(true)
                .build();
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int i, List<PhotoInfo> list) {
                for (PhotoInfo po : list) {
                    goodPics.add(po.getPhotoPath());
                    localPics.add(po.getPhotoPath());
                    if(goodPics.size() > 6){
                        ShowToast.show(PublishGoodAty.this,"图片最多添加6张哦");
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onHanlderFailure(int i, String s) {
                Toast.makeText(PublishGoodAty.this, "获取文件失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 获取文件的类型
     * @param fileName ：文件名
     * @return 文件类型
     */
    private String getFileType(String fileName) {
        // TODO Auto-generated method stub
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }
    private void Uptoserver(String action,JSONObject json){
        UplaodFace.uploadPic(Config.URL+action, null, json.toString(), new UplaodFace.Success() {
            @Override
            public void onSuccess(String s) {
                if(type == MODIFY){
                    Toast.makeText(PublishGoodAty.this, "宝贝修改成功", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(PublishGoodAty.this, "宝贝发布成功", Toast.LENGTH_SHORT).show();
                ProgressUtil.closeProgressDialog();
                finish();
            }
        }, new UplaodFace.Failure() {
            @Override
            public void onFailure() {
                if(type == MODIFY){
                    Toast.makeText(PublishGoodAty.this, "宝贝修改失败", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(PublishGoodAty.this, "宝贝发布失败", Toast.LENGTH_SHORT).show();
                ProgressUtil.closeProgressDialog();
            }
        });
    }
}
