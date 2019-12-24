package com.example.taiwanplantapplication.fragment;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taiwanplantapplication.R;

import java.util.ArrayList;
import java.util.List;

public class GymnospermFragment extends Fragment {

    String plantList[]={"One","Two","Three"};

    public GymnospermFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_gymnosperm, container, false);

        List<String> list=new ArrayList<>();

        for (String title:plantList){
            list.add(title);
        }

        RecyclerView recyclerView=view.findViewById(R.id.recycleView_gymnosperm_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecycleViewAdapter(list));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private class RecycleViewHolder extends RecyclerView.ViewHolder{
        private CardView mCardView;
        private TextView mTextView;
        private ImageView mImageView;

        public RecycleViewHolder(View itemView) {
            super(itemView);
        }

        //建立元件
        public RecycleViewHolder(LayoutInflater inflater, ViewGroup container){
            super(inflater.inflate(R.layout.cardview_gymnosperm, container, false));
            mCardView= itemView.findViewById(R.id.cardView_gymnosperm_id);
            mTextView= itemView.findViewById(R.id.textView_name_id);
            mImageView= itemView.findViewById(R.id.imageView_gymnosperm_id);
        }
    }

    private class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewHolder> {
        private List<String> mList;

        public RecycleViewAdapter(List<String> list){
            this.mList=list;
        }

        @NonNull
        @Override
        public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            return new RecycleViewHolder(inflater, parent);
        }
        //設定card標題
        @Override
        public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
            holder.mTextView.setText(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}