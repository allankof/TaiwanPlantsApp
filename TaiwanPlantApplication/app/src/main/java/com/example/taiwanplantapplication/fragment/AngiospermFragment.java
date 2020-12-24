package com.example.taiwanplantapplication.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taiwanplantapplication.R;
import com.example.taiwanplantapplication.activity.LoginActivity;
import com.example.taiwanplantapplication.activity.PlantsInfoActivity;
import com.example.taiwanplantapplication.bean.InfoData;
import com.example.taiwanplantapplication.en.TypeEn;
import com.example.taiwanplantapplication.utils.BitmapReSize;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AngiospermFragment extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;

    private List<InfoData> infoList;        // 文字的資料
    private List<List<String>> photoList;         // 圖片路徑的資料

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_angiosperm, container, false);
        recyclerView=view.findViewById(R.id.recycleView_angiosperm_id);

        infoList=new ArrayList<>();
        photoList=new ArrayList<>();

        getDatabaseToRecyclerView();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

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

        public RecycleViewHolder(LayoutInflater inflater, ViewGroup container){
            super(inflater.inflate(R.layout.frag_card_view, container, false));
            // 建立元件
            mCardView= itemView.findViewById(R.id.cardView_id);
            mTextView= itemView.findViewById(R.id.tvCard_name_id);
            mImageView= itemView.findViewById(R.id.ivCard_img_id);
        }
    }

    private class RecycleViewAdapter extends RecyclerView.Adapter<AngiospermFragment.RecycleViewHolder> {
        private List<InfoData> mList;
        private List<List<String>> mPhoto;

        public RecycleViewAdapter(List<InfoData> list, List<List<String>> photo){
            this.mList=list;
            this.mPhoto=photo;
        }

        // onCreateViewHolder 建立子項目的Layout
        @NonNull
        @Override
        public AngiospermFragment.RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            return new AngiospermFragment.RecycleViewHolder(inflater, parent);
        }

        // onBindViewHolder 定義項目card的內容
        @Override
        public void onBindViewHolder(@NonNull final AngiospermFragment.RecycleViewHolder holder, int position) {
            InfoData infoData=mList.get(position);
            // 設定card標題
            holder.mTextView.setText(infoData.getName());

            // 設定card圖片
            int s=mPhoto.size();
            if(position < s){
                String path=mPhoto.get(position).get(0);
                Log.i("=Tag RecyclerView=", path);
                Bitmap bitmap=BitmapReSize.getImageThumbnail(path, 320, 200);
                holder.mImageView.setImageBitmap(bitmap);
            }else{
                holder.mImageView.setImageResource(R.drawable.ic_without_image);
                holder.mImageView.setScaleType(ImageView.ScaleType.CENTER);
            }

            // card點擊監聽，傳遞資料到植物頁面
            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InfoData infoData=mList.get(holder.getAdapterPosition());
                    Intent intent=new Intent(getContext(), PlantsInfoActivity.class);

                    Bundle bundle=new Bundle();
                    bundle.putString(TypeEn.TYPE.getCode(), TypeEn.ANGI.getCode());
                    bundle.putString(TypeEn.ID.getCode(), infoData.getId());
                    bundle.putString(TypeEn.NAME.getCode(), infoData.getName());
                    bundle.putString(TypeEn.FAMILY.getCode(), infoData.getFamily());
                    bundle.putString(TypeEn.LOCATION.getCode(), infoData.getLocation());
                    bundle.putString(TypeEn.DATE.getCode(), infoData.getDate());
                    bundle.putString(TypeEn.Number.getCode(), infoData.getNumber());
                    bundle.putString(TypeEn.LNG.getCode(), infoData.getLng());
                    bundle.putString(TypeEn.LAT.getCode(), infoData.getLat());
                    bundle.putString(TypeEn.DES.getCode(), infoData.getDes());

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void update(List<InfoData> datas){
        infoList.clear();
        infoList.addAll(datas);
        this.notifyAll();
    }

    /**
     * 取得 Firebase database 的資料，建立 recyclerView
     */
    private void getDatabaseToRecyclerView(){

        // database/angi/Allan
        databaseReference= FirebaseDatabase.getInstance().getReference(TypeEn.ANGI.getCode()).child(LoginActivity.inputUser);

        // 監聽firebase資料
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                infoList.clear();
                GenericTypeIndicator<List<String>> t=new GenericTypeIndicator<List<String>>() {
                };
                // dataSnapshot包含指定的位置的data，取得資料並加入list中
                for (DataSnapshot dataSnapshots : dataSnapshot.getChildren()) {
                    InfoData infoData = dataSnapshots.getValue(InfoData.class);
                    infoList.add(infoData);

                    String id=dataSnapshots.getKey();
                    List list=dataSnapshot.child(id).child(TypeEn.PHOTO.getCode()).getValue(t);
                    if(list!=null){
                        photoList.add(dataSnapshot.child(id).child(TypeEn.PHOTO.getCode()).getValue(t));
                    }
                }
                if (infoList.isEmpty()) {
                    InfoData infoData=new InfoData("","標題","","","", "","","","");
                    infoList.add(infoData);
                }
                recyclerView.setAdapter(new AngiospermFragment.RecycleViewAdapter(infoList, photoList));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}