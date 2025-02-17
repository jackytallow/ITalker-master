package net.jacky.italker.factory.presenter.group;

import android.support.v7.util.DiffUtil;

import net.jacky.italker.factory.data.group.GroupsDataSource;
import net.jacky.italker.factory.data.group.GroupsRepository;
import net.jacky.italker.factory.data.helper.GroupHelper;
import net.jacky.italker.factory.data.helper.UserHelper;
import net.jacky.italker.factory.model.db.Group;
import net.jacky.italker.factory.presenter.BaseSourcePresenter;
import net.jacky.italker.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 我的群组Presenter实现
 * @author jacky
 * @version 1.0.0
 * @date 2020/4/8
 */
public class GroupsPresenter extends BaseSourcePresenter<Group,Group, GroupsDataSource,
        GroupsContract.View> implements GroupsContract.Presenter {


    public GroupsPresenter(GroupsContract.View view) {
        super(new GroupsRepository(), view);
    }

    @Override
    public void start() {
        super.start();

        // 加载网络数据,以后可以优化到下拉刷新中
        //只有用户下拉的时候才进行网络请求刷新
        GroupHelper.refreshGroups();
    }


    @Override
    public void onDataLoaded(List<Group> groups) {
        final GroupsContract.View view = getView();
        if (view == null)
            return;

        //对比差异
        List<Group> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(old,groups);
        //计算差异
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //界面刷新
        refreshData(result,groups);
    }
}
