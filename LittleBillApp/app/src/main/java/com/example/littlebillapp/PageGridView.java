package com.example.littlebillapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class PageGridView extends RecyclerView {
    private int mRows = 0;
    private int mColums = 0;
    private int mPageSize = 0;
    private int mOnePageSize = 0;
    private int mWidth = -1;
    private boolean needReorder = false;

    public PageGridView(Context context) {
        this(context, null);
    }

    public PageGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PageGridView);
        mRows = array.getInteger(R.styleable.PageGridView_PagingRows, 0);
        mColums = array.getInteger(R.styleable.PageGridView_PagingColums, 0);

        LayoutManager layoutManager;
        if (mRows > 0) {
            if (mColums > 0) {
                needReorder = true;
                addOnScrollListener(new PagingScrollListener());
            }
            layoutManager = new StaggeredGridLayoutManager(mRows, HORIZONTAL);
        } else {
            layoutManager = new StaggeredGridLayoutManager(mColums, VERTICAL);
        }
        array.recycle();
        setLayoutManager(layoutManager);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mWidth = getWidth();
    }

    @Override
    public final void setAdapter(Adapter adapter) {
        if (needReorder) {
            if (!(adapter instanceof PagingAdapter)) {
                throw new RuntimeException("must use PagingAdapter");
            }
            PagingAdapter pagingAdapter = (PagingAdapter) adapter;
            List data = pagingAdapter.getData();
            List formatData = new ArrayList();

            mOnePageSize = mRows * mColums;
            mPageSize = data.size() / mOnePageSize;
            if (data.size() % mOnePageSize != 0) {
                mPageSize++;
            }
            for (int p = 0; p < mPageSize; p++) {
                for (int c = 0; c < mColums; c++) {
                    for (int r = 0; r < mRows; r++) {
                        int index = c + r * mColums + p * mOnePageSize;
                        if (index > data.size() - 1) {
                            formatData.add(pagingAdapter.getEmpty());
                        } else {
                            formatData.add(data.get(index));
                        }
                    }
                }
            }
            data.clear();
            data.addAll(formatData);
        }
        super.setAdapter(adapter);
        if (pageIndicator != null && pageIndicaotrNeedInit) {
            pageIndicator.InitIndicatorItems(mPageSize);
            pageIndicator.onPageSelected(0);
            pageIndicaotrNeedInit = false;
        }
        if (onPageChangeListenerList != null) {
            for (OnPageChangeListener listener : onPageChangeListenerList) {
                listener.onPageChanged(0);
            }
        }
    }

    int dX, dY;
    long dTime;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (onItemClickListener != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = (int) ev.getRawX();
                    dY = (int) ev.getRawY();
                    dTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    int mx = (int) Math.abs(ev.getRawX() - dX);
                    int my = (int) Math.abs(ev.getRawY() - dY);
                    int time = (int) (System.currentTimeMillis() - dTime);
                    if (mx <= 10 && my <= 10 && time < 200) {
                        int position = getPositionByXY((int) ev.getRawX(), (int) ev.getRawY());
                        if (position != -1) {
                            onItemClickListener.onItemClick(this, position);
                        }
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private int getPositionByXY(int x, int y) {
        int position = -1;
        Rect rect = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.getGlobalVisibleRect(rect);
            if (rect.contains(x, y)) {
                position = i;
                break;
            }
        }
        if (mRows > 0) {
            int offset = getChildPosition(getLayoutManager().getChildAt(0));
            position += offset;
        }
        return position;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    private PageIndicator pageIndicator;
    private boolean pageIndicaotrNeedInit = false;

    public void setPageIndicator(PageIndicator pageIndicator) {
        this.pageIndicator = pageIndicator;
        pageIndicaotrNeedInit = true;
        if (getAdapter() != null && needReorder) {
            pageIndicator.InitIndicatorItems(mPageSize);
            pageIndicator.onPageSelected(currentPage);
            pageIndicaotrNeedInit = false;
        }
    }

    public interface PageIndicator {
        void InitIndicatorItems(int itemsNumber);
        void onPageSelected(int pageIndex);
        void onPageUnSelected(int pageIndex);
    }

    private List<OnPageChangeListener> onPageChangeListenerList;

    public interface OnPageChangeListener {
        void onPageChanged(int index);
    }

    public interface OnItemClickListener {
        void onItemClick(PageGridView pageGridView, int position);
    }

    public static abstract class PagingAdapter<VH extends ViewHolder> extends Adapter<VH> {
        public abstract Object getEmpty();
        public abstract List getData();
    }


    int scrollX = 0;
    boolean isAuto = false;
    int Target = 0;
    int currentPage = 0;
    int lastPage = 0;

    public class PagingScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == 0) {
                if (!isAuto) {
                    int p = scrollX / getWidth();
                    int offset = scrollX % getWidth();
                    if (offset > getWidth() / 2) {
                        p++;
                    }
                    Target = p * getWidth();
                    isAuto = true;
                    currentPage = p;
                    if (pageIndicator != null) {
                        pageIndicator.onPageUnSelected(lastPage);
                        pageIndicator.onPageSelected(currentPage);
                    }
                    if (onPageChangeListenerList != null) {
                        for (OnPageChangeListener listener : onPageChangeListenerList) {
                            listener.onPageChanged(currentPage);
                        }
                    }
                    recyclerView.smoothScrollBy(Target - scrollX, 0);
                }
            } else if (newState == 2) {
                isAuto = false;
                lastPage = currentPage;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            scrollX += dx;
        }
    }
}