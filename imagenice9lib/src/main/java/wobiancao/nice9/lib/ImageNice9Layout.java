package wobiancao.nice9.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelper;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by wxy on 2017/5/25.
 * 模仿nice首页列表 9种样式图片
 * 依赖淘宝vLayout开源控件 实现
 * 1
 * -------------------------
 * |                       |
 * |                       |
 * |           1           |
 * |                       |
 * |                       |
 * |                       |
 * -------------------------
 * <p>
 * 2
 * * -------------------------
 * |           |           |
 * |           |           |
 * |           |           |
 * |     1     |     2     |
 * |           |           |
 * |           |           |
 * |           |           |
 * -------------------------
 * 3
 * -------------------------
 * |                       |
 * |           1           |
 * |                       |
 * |-----------------------|
 * |           |           |
 * |     2     |     3     |
 * |           |           |
 * -------------------------
 * 4
 * -------------------------
 * |         |              |
 * |    1    |      2       |
 * |         |              |
 * |----------------------- |
 * |               |        |
 * |            3  |    4   |
 * |               |        |
 * -------------------------
 * 5
 * -------------------------
 * |          |            |
 * |    1     |   2        |
 * |          |            |
 * |-----------------------|
 * |      |        |       |
 * |   3  |    4   |    5  |
 * |      |        |       |
 * -------------------------
 * 6
 * -------------------------
 * |           |           |
 * |           |     2     |
 * |           |           |
 * |     1     |-----------|
 * |           |           |
 * |           |     3     |
 * |           |           |
 * -------------------------
 * |      |        |       |
 * |   4  |   5    |    6  |
 * |      |        |       |
 * -------------------------
 * <p>
 * 7
 * -------------------------
 * |           |            |
 * |     1     |      2     |
 * |           |            |
 * |-----------------------|
 * |      |        |       |
 * |   2  |     3  |    4  |
 * |      |        |       |
 * -------------------------
 * |      |        |       |
 * |   5  |     6  |    7  |
 * |      |        |       |
 * -------------------------
 * 8
 * -------------------------
 * |          |            |
 * |    1     |   2        |
 * |          |            |
 * |-----------------------|
 * |      |        |       |
 * |   3  |    4   |    5  |
 * |      |        |       |
 * -------------------------
 * |      |        |       |
 * |   6  |     7  |    8  |
 * |      |        |       |
 * -------------------------
 * 9
 * |-----------------------|
 * |      |        |       |
 * |   1  |     2  |    3  |
 * |      |        |       |
 * -------------------------
 * |      |        |       |
 * |   4  |     5  |    6  |
 * |      |        |       |
 * -------------------------
 * |      |        |       |
 * |   7  |     8  |    9  |
 * |      |        |       |
 * -------------------------
 */

public class ImageNice9Layout extends LinearLayout implements MyItemTouchCallback.OnDragListener {
    private RecyclerView mRecycler;
    private VirtualLayoutManager layoutManager;
    private List<LayoutHelper> helpers;
    private ItemTouchHelper itemTouchHelper;
    private GridLayoutHelper gridLayoutHelper;
    private OnePlusNLayoutHelper onePlusHelper;
    private ImageMulitVAdapter mMulitVAdapter;
    private boolean canDrag = false;
    private Context mContext;
    private int itemMargin = 10;
    private Drawable errorDrawable;
    private int displayW;
    private int num;

    public ImageNice9Layout(Context context) {
        this(context, null);
    }

    public ImageNice9Layout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageNice9Layout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageNice9Layout);
        final int N = typedArray.getIndexCount();//取得本集合里面总共有多少个属性
        for (int i = 0; i < N; i++) {//遍历这些属性，拿到对应的属性
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
        mMulitVAdapter = new ImageMulitVAdapter(layoutManager, mContext, canDrag, itemMargin,errorDrawable);
        mRecycler.setAdapter(mMulitVAdapter);
        setBackgroundColor(Color.parseColor("#ffffff"));
//        setPadding(itemMargin,itemMargin,itemMargin,itemMargin);
    }

    private void initAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.ImageNice9Layout_nice9_candrag) {
            canDrag = typedArray.getBoolean(attr, false);
        }
        if (attr == R.styleable.ImageNice9Layout_nice9_itemMargin) {
            itemMargin = (int) typedArray.getDimension(attr, 5);
        }

//        if (attr == R.styleable.ImageNice9Layout_nice9_error) {
//            errorDrawable = typedArray.getDrawable(attr);
//        }
    }


    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_imagemulit_layout, this);
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRecycler = view.findViewById(R.id.drag_recycler);
        layoutManager = new VirtualLayoutManager(mContext);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setNestedScrollingEnabled(false);
    }

    public void setItemDelegate(ItemDelegate itemDelegate) {
        mMulitVAdapter.setItemDelegate(itemDelegate);
    }

    /**
     * 设置是否可以拖拽
     **/
    public void setCanDrag(boolean canDrag) {
        this.canDrag = canDrag;
    }

    //绑定数据，根据数据，先行计算recyclerview高度，固定高度，防止多重滑动时候冲突
    public void bindData(List<String> pictures) {
        if (pictures != null) {
            num = pictures.size();
            Log.i("TAG","bindData"+ num);

            if (num == 0)return;

            helpers = new LinkedList<>();

            gridLayoutHelper = new GridLayoutHelper(6);
            gridLayoutHelper.setGap(itemMargin);
            gridLayoutHelper.setHGap(itemMargin);
            gridLayoutHelper.setAutoExpand(true);

            onePlusHelper = new OnePlusNLayoutHelper(3,itemMargin,itemMargin,itemMargin,itemMargin);

            gridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (num == 1) {
                        return 6;
                    } else if (num == 2) {
                        return 3;
                    } else if (num == 3) {
                        return position == 0?6:3;
                    } else if (num == 4) {
                        gridLayoutHelper.setSpanCount(7);
                        if (position == 0 || position == 3) {
                            return 3;
                        } else {
                            return 4;
                        }
                    } else if (num == 5) {
                        if(position == 1){
                            return 4;
                        }else {
                            return 2;
                        }
                    } else if (num == 6) {
                        return position == 0?4:2;
                    } else if (num == 7) {
                        if (position < 4) {
                            return 3;
                        } else {
                            return 2;
                        }
                    } else if (num == 8) {
                        gridLayoutHelper.setSpanCount(4);
                        if (position == 0 || position == 3 || position == 4|| position == 7) {
                            return 2;
                        } else {
                            return 1;
                        }
                    } else {
                        return position == 0?4:2;
                    }
                }
            });

            setWH(num);

            mMulitVAdapter.bindData(pictures);


            if (canDrag) {
                itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(mMulitVAdapter).setOnDragListener(this));
                itemTouchHelper.attachToRecyclerView(mRecycler);
                mRecycler.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecycler) {
                    @Override
                    public void onItemClick(RecyclerView.ViewHolder vh) {
                    }

                    @Override
                    public void onLongClick(RecyclerView.ViewHolder vh) {
                        itemTouchHelper.startDrag(vh);
                    }
                });
            }
        }

    }

    private void setWH(final int num) {

        if (num == 0 || displayW == 0)return;

        ViewGroup.LayoutParams layoutParams = mRecycler.getLayoutParams();
        layoutParams.height = layoutParams.width = displayW;
        mRecycler.setLayoutParams(layoutParams);

        helpers.clear();
        if (num == 6) {
            onePlusHelper.setMargin(0,0,0,itemMargin);
            onePlusHelper.setColWeights(new float[]{
                ((displayW - 2*itemMargin) * 2f/3f+2*itemMargin+1)/displayW*100

            });
            helpers.add(onePlusHelper);
            gridLayoutHelper.setItemCount(3);
        } else if (num == 9) {
            onePlusHelper.setMargin(0,0,0,itemMargin);
            onePlusHelper.setColWeights(new float[]{
                ((displayW - 2*itemMargin) * 2f/3f+2*itemMargin+1)/displayW*100

            });
            helpers.add(onePlusHelper);
            gridLayoutHelper.setItemCount(6);

        } else {
            gridLayoutHelper.setItemCount(num);
        }
        helpers.add(gridLayoutHelper);

        mMulitVAdapter.setDisplayW(displayW);
        layoutManager.setLayoutHelpers(helpers);
        mMulitVAdapter.notifyDataSetChanged();
        postInvalidate();

    }

    /**
     * 获取更改后的图片列表
     **/
    public List<String> getAfterPicList() {
        return mMulitVAdapter.getPictures();
    }

    @Override
    public void onFinishDrag() {
        mMulitVAdapter.notifyDataSetChanged();
    }


    public interface ItemDelegate {
        void onItemClick(int position);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        if (displayW == getMeasuredWidth())return;
        displayW = getMeasuredWidth();
        setWH(num);
        Log.i("TAG","onMeasure");
    }
}