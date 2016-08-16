package com.boredream.bdcodehelper.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boredream.bdcodehelper.R;

/**
 * 加载更多装饰适配器,用于包装普通RecyclerView.Adapter增添一个加载更多功能
 */
public class LoadMoreAdapter extends RecyclerView.Adapter {

    /**
     * 下拉状态 - 无内容。footer位置不做任何显示
     */
    public static final int STATUS_NONE = 0;

    /**
     * 下拉状态 - 有更多。footer位置有progressbar进度框
     */
    public static final int STATUS_HAVE_MORE = 1;

    /**
     * 下拉状态 - 已经加载全部数据。footer位置显示文字
     */
    public static final int STATUS_LOADED_ALL = 2;

    /**
     * 当前状态, 默认为 STATUS_NONE
     */
    private int status = STATUS_NONE;

    private int ITEM_VIEW_TYPE_FOOTER = 0x10002;

    /**
     * 正在加载更多中
     */
    private boolean isLoadingMore = false;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private OnLoadMoreListener mOnLoadMoreListener;
    private Drawable mLoadMoreProgressDrawable;

    public LoadMoreAdapter(RecyclerView recyclerView,
                           RecyclerView.Adapter adapter,
                           OnLoadMoreListener onLoadMoreListener) {
        // 不设置加载更多progressbar的drawable,使用默认的样式
        this(recyclerView, adapter, onLoadMoreListener, null);
    }

    public LoadMoreAdapter(RecyclerView recyclerView,
                           RecyclerView.Adapter adapter,
                           OnLoadMoreListener onLoadMoreListener,
                           Drawable loadMoreProgressDrawable) {
        mRecyclerView = recyclerView;
        mAdapter = adapter;
        mOnLoadMoreListener = onLoadMoreListener;
        mLoadMoreProgressDrawable = loadMoreProgressDrawable;
        setScrollListener();
    }

    public RecyclerView.Adapter getSrcAdapter() {
        return mAdapter;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        isLoadingMore = false;
        this.status = status;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mAdapter.getItemCount()) {
            // 最后一个位置是FOOTER
            return ITEM_VIEW_TYPE_FOOTER;
        } else {
            return mAdapter.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        // 原adapter数量上+1,多了个footer
        return mAdapter.getItemCount() + 1;
    }

    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar pb_footer_progress;
        public TextView tv_footer_progress;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);

            pb_footer_progress = (ProgressBar) itemView.findViewById(R.id.pb_footer_progress);
            tv_footer_progress = (TextView) itemView.findViewById(R.id.tv_footer_progress);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View loadMore = inflater.inflate(R.layout.footer_progress, parent, false);
            LoadMoreViewHolder holder = new LoadMoreViewHolder(loadMore);
            return holder;
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadMoreViewHolder) {
            handleFooter((LoadMoreViewHolder) holder);
        } else {
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    /**
     * 处理footer的view显示
     */
    private void handleFooter(final LoadMoreViewHolder holder) {
        if (mLoadMoreProgressDrawable != null) {
            // 设置自定义进度框样式
            holder.pb_footer_progress.setIndeterminateDrawable(mLoadMoreProgressDrawable);
        }

        // 设置item占满屏幕宽度, 网格列表类型使用
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams layoutParams =
                    (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        } else if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int spanSize = 1;
                    if (getItemViewType(position) == ITEM_VIEW_TYPE_FOOTER) {
                        spanSize = manager.getSpanCount();
                    }
                    return spanSize;
                }
            });
        }

        // 根据不同状态显示footer样式
        switch (status) {
            case STATUS_HAVE_MORE:
                holder.itemView.setVisibility(View.VISIBLE);

                holder.tv_footer_progress.setVisibility(View.GONE);
                holder.pb_footer_progress.setVisibility(View.VISIBLE);
                break;
            case STATUS_LOADED_ALL:
                holder.itemView.setVisibility(View.VISIBLE);

                holder.tv_footer_progress.setVisibility(View.VISIBLE);
                holder.pb_footer_progress.setVisibility(View.GONE);
                break;
            case STATUS_NONE:
            default:
                holder.itemView.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 设置滚动监听, 判断当列表滚动到底部时, 触发加载更多回调
     */
    private void setScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    int pastVisibleItems = -1;
                    int visibleItemCount = staggeredGridLayoutManager.getChildCount();
                    int totalItemCount = staggeredGridLayoutManager.getItemCount();
                    int[] firstVisibleItems = null;
                    firstVisibleItems = staggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                    if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                        pastVisibleItems = firstVisibleItems[0];
                    }

                    if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                        triggerLoadMore();
                    }
                } else if (layoutManager instanceof LinearLayoutManager) {
                    // GridLayoutManager 是 LinearLayoutManager 的子类, 也符合这个条件
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                        triggerLoadMore();
                    }
                }
            }
        });
    }

    /**
     * 触发加载更多回调
     */
    private synchronized void triggerLoadMore() {
        // 如果是正在加载更多中,不再重复触发
        if (isLoadingMore) {
            return;
        }

        // 如果状态不是STATUS_HAVE_MORE则不触发
        if (status != STATUS_HAVE_MORE) {
            return;
        }

        isLoadingMore = true;
        mOnLoadMoreListener.onLoadMore();
        onLoadMore();
    }

    protected void onLoadMore() {
        // sub
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}
