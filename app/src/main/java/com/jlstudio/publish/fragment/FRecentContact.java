package com.jlstudio.publish.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.jlstudio.R;
import com.jlstudio.group.bean.Contacts;
import com.jlstudio.main.application.Config;
import com.jlstudio.main.db.DBOption;
import com.jlstudio.publish.adapter.RecentFriendListViewAdapter;
import com.jlstudio.publish.bean.MyContact;

import java.util.ArrayList;
import java.util.List;

/**最近联系人显示
 * A simple {@link Fragment} subclass.
 */
public class FRecentContact extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView recent_listview;
    private View view;
    private Button publish;
    private List<MyContact> contacts_list;
    private DBOption db;//数据库连接
    private RecentFriendListViewAdapter recentFriendListViewAdapter;

    public FRecentContact() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frecentcontact, container, false);
        publish = (Button) view.findViewById(R.id.publish);
        publish.setOnClickListener(this);
        db = new DBOption(getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        contacts_list = new ArrayList<>();
        contacts_list = db.getRecentContacts(Config.loadUser(getContext()).getUsername());
        if (contacts_list != null) {
            contacts_list = db.getRecentContacts(Config.loadUser(getContext()).getUsername());
            initView();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initView() {
        recent_listview = (ListView) view.findViewById(R.id.recent_listview);
        recentFriendListViewAdapter = new RecentFriendListViewAdapter(getActivity(), contacts_list);
        recent_listview.setAdapter(recentFriendListViewAdapter);
        recent_listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(contacts_list.get(position).isSelected()){
            contacts_list.get(position).setIsSelected(false);
        }else{
            contacts_list.get(position).setIsSelected(true);
        }
        recentFriendListViewAdapter.setContacst_list(contacts_list);
    }

    @Override
    public void onClick(View v) {
        anaysis();
    }

    private void anaysis() {
        for(int i=0;i<contacts_list.size();i++){
            if(contacts_list.get(i).isSelected()){
                Config.persons.add(contacts_list.get(i));
            }
        }
        for (int i=0;i<Config.persons.size();i++){
            Log.d("haha",Config.persons.get(i).getName());
        }
    }
}
