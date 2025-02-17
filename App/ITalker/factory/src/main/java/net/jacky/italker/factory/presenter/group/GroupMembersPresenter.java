package net.jacky.italker.factory.presenter.group;

import net.jacky.italker.factory.Factory;
import net.jacky.italker.factory.data.helper.GroupHelper;
import net.jacky.italker.factory.model.db.Group;
import net.jacky.italker.factory.model.db.view.MemberUserModel;
import net.jacky.italker.factory.persistence.Account;
import net.jacky.italker.factory.presenter.BaseRecyclerPresenter;

import java.util.List;

/**
 * 群成员的Presenter
 * @author jacky
 * @version 1.0.0
 */
public class GroupMembersPresenter extends BaseRecyclerPresenter<MemberUserModel, GroupMembersContract.View>
        implements GroupMembersContract.Presenter {

    public GroupMembersPresenter(GroupMembersContract.View view) {
        super(view);
    }

    @Override
    public void refresh() {
        // 显示Loading
        start();

        // 异步加载
        Factory.runOnAsync(loader);
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            GroupMembersContract.View view = getView();
            if (view == null)
                return;

            String groupId = view.getGroupId();

            //拿群的信息
            Group group = GroupHelper.findFromLocal(groupId);
            boolean isAdmin = Account.getUserId().equalsIgnoreCase(group.getOwner().getId());
          // view.showAdminOptions(isAdmin);

            // 传递数量为-1 代表查询所有
            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId, -1);

            refreshData(models);
        }
    };
}
