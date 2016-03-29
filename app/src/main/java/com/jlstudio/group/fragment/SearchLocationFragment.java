package com.jlstudio.group.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.jlstudio.R;
import com.jlstudio.group.adapter.SearchAdapter;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.group.util.SearchUtil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchLocationFragment extends Fragment {
    private View view;
    private ListView lv;
    private SearchAdapter sa;
    String s = "";
    public ArrayList<Contacts> arrayList;

    public SearchLocationFragment() {
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }
    private void MyfileList(String s,ArrayList<Contacts> arrayList) {
        Pattern p = Pattern.compile(s);
        ArrayList<Contacts> a = new ArrayList<Contacts>();
        for (int i = 0; i < arrayList.size(); i++) {
            Contacts c = arrayList.get(i);
            //最好将所有信息全部+起来，用于搜索便利
            Matcher matcher = p.matcher(c.getRealname()+c.getPinYinName());
            if(matcher.find()){
                a.add(c);
            }
        }
        sa = new SearchAdapter(getActivity(),a);
        lv.setAdapter(sa);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        sa = new SearchAdapter(getActivity(),arrayList);
        lv.setAdapter(sa);
    }

    private void initData() {
        /*----------------以下是获取数据的地方--------------------*/
        arrayList = SearchUtil.getContactsFromLocalJson(arrayList,getActivity());
        /*-----------------------------------------------------*/
        MyfileList(s,arrayList);
    }

    private void initView() {
        lv = (ListView) view.findViewById(R.id.search_loca);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_location, container, false);
        Bundle b = getArguments();
        Toast.makeText(getActivity(),"来啦"+s,Toast.LENGTH_SHORT).show();
        s = b.getString("val");
        return view;
    }

}
