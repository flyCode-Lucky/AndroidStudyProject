package com.example.mywechat;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class frdFragment extends Fragment {

    private List<Friend> friendList = new ArrayList<>();

    public frdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.tab02, container, false);
        View view = inflater.inflate(R.layout.tab02, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.RecyclerView);
        initFrd();
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        friendAdapter adapter = new friendAdapter(friendList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initFrd() {
        for(int i = 0; i < 10; i++) {
            Friend frdMoon=new Friend(i, R.drawable.moon, "金星亮相天宇");
            friendList.add(frdMoon);
            Friend frdDoctor=new Friend(i, R.drawable.doctor, "医护人员凯旋");
            friendList.add(frdDoctor);
            Friend frdFlower=new Friend(i, R.drawable.flower, "紫金花城绽放");
            friendList.add(frdFlower);
            Friend frdShelters=new Friend(i, R.drawable.shelters, "美国纽约方舱");
            friendList.add(frdShelters);
            Friend frdTest=new Friend(i, R.drawable.test, "复学前的演练");
            friendList.add(frdTest);
            Friend frdSnow=new Friend(i, R.drawable.snow, "张家界“桃花雪”");
            friendList.add(frdSnow);
            Friend frdSupport=new Friend(i, R.drawable.support, "中国医疗组支援巴基斯坦");
            friendList.add(frdSupport);
            Friend frdCompany=new Friend(i, R.drawable.company, "太平洋保险公司..");
            friendList.add(frdCompany);


        }
    }
}
