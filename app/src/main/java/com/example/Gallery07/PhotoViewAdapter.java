package com.example.Gallery07;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoViewAdapter extends RecyclerView.Adapter {
    private List listImgPaths;
    private Context mContext;
    private RecyclerView parentLayout;
    private boolean isItemClickable = false;

    public void setItemClickable(boolean isItemClickable) {
        this.isItemClickable = isItemClickable;
        notifyDataSetChanged();
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }


    public void setData(List listImgPaths, RecyclerView parent) {
        this.listImgPaths = listImgPaths;
        this.parentLayout = parent;
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.recyclerview_textview, parent, false);
            return new TextViewHolder(view);
        }
        view = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        CImage item = (CImage) listImgPaths.get(position);
        if (item.getType() == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int tmp = position;
        CImage item = (CImage) listImgPaths.get(position);
        if (item.getType() == 0) {
            TextViewHolder textViewHolder = (TextViewHolder) holder;
            textViewHolder.textView.setText(item.getDay() + " Tháng " + item.getMonth());
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setFullSpan(true);
            holder.itemView.setLayoutParams(layoutParams);
        } else {
            PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;
            String curPath = ((CImage) listImgPaths.get(tmp)).getImageUri();

            ObjectKey obj = new ObjectKey(System.currentTimeMillis());

            Glide.with(mContext)
                    .load(curPath)
                    .apply(new RequestOptions().centerCrop())
                    .signature(obj)
                    .into(photoViewHolder.imgPhoto);

            if (isItemClickable) {
                ((PhotoViewHolder) holder).imgCheckBox.setVisibility(View.VISIBLE);
                ((PhotoViewHolder) holder).imgCheckBox.setChecked(item.isChecked());
                ((PhotoViewHolder) holder).imgCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.setChecked(((PhotoViewHolder) holder).imgCheckBox.isChecked());
                        if (item.isChecked())
                            Toast.makeText(mContext, "check at " + tmp, Toast.LENGTH_SHORT).show();
                        ((MainActivity) mContext).fragment1.changeTitleContextualActionBar();
                    }
                });
            } else {
                ((PhotoViewHolder) holder).imgCheckBox.setChecked(false);
                ((PhotoViewHolder) holder).imgCheckBox.setVisibility(View.GONE);
                ((PhotoViewHolder) holder).imgPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, FullscreenImgActivity.class);
                        intent.putExtra("curPath", curPath);
                        ((MainActivity) mContext).startActivity(intent);
                    }
                });
            }
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        if (listImgPaths != null)
            return listImgPaths.size();
        else
            return 0;
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.recyclerViewTextView);
        }
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPhoto;
        private CheckBox imgCheckBox;
        private boolean clickable = false;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            imgCheckBox = itemView.findViewById(R.id.imgCheckBox);
//            imgCheckBox.setVisibility(View.INVISIBLE);
        }

        public void setClickable(boolean val) {
            clickable = val;
            if (val) {
                imgCheckBox.setChecked(false);
                imgCheckBox.setVisibility(View.VISIBLE);
            } else
                imgCheckBox.setVisibility(View.INVISIBLE);
        }

        public boolean isClickable() {
            return clickable;
        }

        public void toggleChecked(boolean isChecked) {
            imgCheckBox.setChecked(isChecked);
        }


//        public boolean isChecked() {
//            return imgCheckBox.isChecked();
//        }
    }
}