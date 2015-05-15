package com.bill.zhihu.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bill.zhihu.R;
import com.bill.zhihu.api.ZhihuApi;
import com.bill.zhihu.api.bean.TimeLineItem;
import com.bill.zhihu.api.cmd.CmdLoadHomePage;
import com.bill.zhihu.api.cmd.CmdLoadMore;
import com.bill.zhihu.api.utils.ZhihuLog;
import com.bill.zhihu.view.SwipyRefreshLayout;
import com.bill.zhihu.view.SwipyRefreshLayout.OnRefreshListener;
import com.bill.zhihu.view.SwipyRefreshLayoutDirection;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 *
 * @author Bill Lv
 */
public class FragmentHome extends Fragment {

    private static final String TAG = "FragmentHome";

    private RecyclerView timelineRv;
    private SwipyRefreshLayout refreshLayout;

    private List<TimeLineItem> timelineItems;
    private TimeLineRecyclerAdapter adapter;
    private View rootView;

    private ProgressWheel progressWheel;

    public FragmentHome() {
        timelineItems = new ArrayList<>();
        ZhihuLog.setDebugable(TAG, true);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home_page, container,
                false);

        initView();

        playLoadingAnim();

        loadHomePage();

        return rootView;

    }

    private void initView() {
        timelineRv = (RecyclerView) rootView.findViewById(R.id.time_line_list);
        refreshLayout = (SwipyRefreshLayout) rootView
                .findViewById(R.id.swipe_to_refresh);
//        loadingImage = (ImageView) rootView.findViewById(R.id.loading_img);
        progressWheel = (ProgressWheel) rootView.findViewById(R.id.loading_img);
        // 设置下拉刷新圆圈的颜色
        refreshLayout.setColorSchemeResources(R.color.swipe_color1,
                R.color.swipe_color2, R.color.swipe_color3,
                R.color.swipe_color4);

        adapter = new TimeLineRecyclerAdapter(getActivity(), timelineItems);
        // adapter
        timelineRv.setAdapter(adapter);
        // 设置layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        timelineRv.setLayoutManager(layoutManager);
        // divider
        timelineRv.addItemDecoration(new TimeLineItemDecoration());

        // 下拉刷新监听器
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (timelineItems.isEmpty()) {
                    refreshLayout.setRefreshing(false);
                    return;
                }
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    loadHomePage();
                } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    loadMore();
                }
            }

        });
    }

    /**
     * 首页初始加载动画
     */
    private void playLoadingAnim() {
        progressWheel.spin();
    }

    private void stopLoadingAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(progressWheel, "alpha", 1, 0);
        animator.setDuration(500);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                progressWheel.stopSpinning();
            }
        });
        animator.start();

    }

    /**
     * 获取首页
     */
    private void loadHomePage() {

        CmdLoadHomePage.CallbackListener listener = new CmdLoadHomePage.CallbackListener() {

            @Override
            public void callback(List<TimeLineItem> timelineitems) {
                if (timelineitems != null && !timelineitems.isEmpty()) {
                    timelineItems.clear();
                    timelineItems.addAll(timelineitems);
                }
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
                stopLoadingAnim();
            }

        };

        ZhihuApi.loadHomePage(listener);

    }

    /**
     * 上拉加载更多
     */
    private void loadMore() {

        if (timelineItems.isEmpty())
            return;

        TimeLineItem item = timelineItems.get(timelineItems.size() - 1);
        long blockId = Long.valueOf(item.getDataBlock());
        int offset = Integer.valueOf(item.getDataOffset());

        CmdLoadMore.CallbackListener listener = new CmdLoadMore.CallbackListener() {

            @Override
            public void callback(List<TimeLineItem> timelineitems) {
                int scrollToPosition = timelineItems.size();
                if (timelineitems != null && !timelineitems.isEmpty()) {
                    timelineItems.addAll(timelineitems);
                }
                adapter.notifyDataSetChanged();
                timelineRv.smoothScrollToPosition(scrollToPosition);
                refreshLayout.setRefreshing(false);
            }

        };
        ZhihuApi.loadMore(blockId, offset, listener);

    }

}
