package wobiancao.nice9.lib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.VirtualLayoutAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wxy on 2017/5/31.
 */

public class ImageMulitVAdapter extends VirtualLayoutAdapter<ImageMulitVAdapter.ImageViewHolder> implements MyItemTouchCallback.ItemTouchAdapter {
    private List<String> pictures = new ArrayList<>();
    private Context context;
    private boolean canDrag;
    private int itemMargin;
    private ImageNice9Layout.ItemDelegate mItemDelegate;
    int width = 0, height = 0;
    int imageCount;
    int displayW =200;
    private Drawable errorDrawable;

    public int getDisplayW() {
        return displayW;
    }

    public void setDisplayW(int displayW) {
        this.displayW = displayW;
    }
//    public ImageMulitVAdapter(@NonNull VirtualLayoutManager layoutManager, List<String> pictures, Context context, boolean canDrag, int itemMagrin) {
//        super(layoutManager);
//        this.pictures = pictures;
//        this.context = context;
//        this.canDrag = canDrag;
//        this.itemMargin = itemMagrin;
//    }


    public ImageMulitVAdapter(@NonNull VirtualLayoutManager layoutManager, Context context, boolean canDrag, int itemMargin, Drawable errorDrawable) {
        super(layoutManager);
        this.context = context;
        this.canDrag = canDrag;
        this.itemMargin = itemMargin;
        this.errorDrawable = errorDrawable;
    }

    public void bindData(List<String> pictures){
        this.pictures = pictures;
        imageCount = pictures.size();
        notifyDataSetChanged();
    }

    public void setItemDelegate(ImageNice9Layout.ItemDelegate itemDelegate) {
        mItemDelegate = itemDelegate;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mulit_image, null));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        VirtualLayoutManager.LayoutParams layoutParams = new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (imageCount == 1) {
            height = displayW;
        } else if (imageCount == 2) {
            height = displayW;
        } else if (imageCount == 3) {
            height = (int) ((displayW - itemMargin) * 0.5);
        } else if (imageCount == 4) {

            height = (int) ((displayW - itemMargin) * 0.5);

        } else if (imageCount == 5) {
            if (position == 0 || position == 1) {
                height = (int) ((displayW - itemMargin) * 2/3);
            } else {
                height = (int) ((displayW - itemMargin) * 1/3);
            }
        } else if (imageCount == 6) {

            if (position == 0){
                height =  (displayW - 2*itemMargin) * 2/3;

                layoutParams.rightMargin = itemMargin;
            }else if ( position >= 3 && position <=5){
                height =  (displayW - 2*itemMargin) * 1/3;
            }else if (position == 2){
                layoutParams.topMargin = itemMargin;
                height =  (displayW - (displayW - 2*itemMargin) * 1/3 - 2*itemMargin) * 1/2;
            }else {
                height =  (displayW - (displayW - 2*itemMargin) * 1/3 - 2*itemMargin) * 1/2;
            }

        } else if (imageCount == 7) {

            if (position <= 1){
                height = (displayW - 3*itemMargin)*2/4+itemMargin;
            }else {
                height = (displayW - 3*itemMargin)/4;
            }
        } else if (imageCount == 8) {
            if (position == 3 || position == 4) {
                height = (displayW - 3*itemMargin)*2/4+itemMargin;
            } else {
                height = (displayW - 3*itemMargin)/4;

            }
        } else {
            if (position == 0){
                layoutParams.rightMargin = itemMargin;
                height = (displayW - 3*itemMargin)*2/4+itemMargin;
            }else if (position == 2){
                layoutParams.topMargin = itemMargin;
                height = (displayW - 3*itemMargin)/4;
            }else {
                height = (displayW - 3*itemMargin)/4;
            }
        }
        layoutParams.width = width;
        layoutParams.height = height;
        holder.itemView.setLayoutParams(layoutParams);

        final String imageUrl = pictures.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!canDrag) {
                    if (mItemDelegate != null){
                        mItemDelegate.onItemClick(position);
                    }
                }
            }
        });
        Glide.with(context)
            .load(imageUrl)
            .placeholder(errorDrawable)
            .error(errorDrawable)
            .centerCrop()
            .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(pictures, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(pictures, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public List<String> getPictures() {
        return pictures;
    }

    @Override
    public void onSwiped(int position) {
        notifyItemRemoved(position);
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_mulit_image);
        }
    }

}