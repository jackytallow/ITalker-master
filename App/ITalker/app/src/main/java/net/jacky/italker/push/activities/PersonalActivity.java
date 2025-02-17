package net.jacky.italker.push.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.jacky.italker.common.app.Application;
import net.jacky.italker.common.app.PresenterToolbarActivity;
import net.jacky.italker.common.widget.PortraitView;
import net.jacky.italker.factory.Factory;
import net.jacky.italker.factory.model.card.UserCard;
import net.jacky.italker.factory.model.db.User;
import net.jacky.italker.factory.presenter.contact.FollowContract;
import net.jacky.italker.factory.presenter.contact.FollowPresenter;
import net.jacky.italker.factory.presenter.contact.PersonalContract;
import net.jacky.italker.factory.presenter.contact.PersonalPresenter;
import net.jacky.italker.push.R;
import net.qiujuer.genius.res.Resource;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author jacky
 * @version 1.0.0
 */
public  class PersonalActivity extends PresenterToolbarActivity<PersonalContract.Presenter>
        implements PersonalContract.View{
    private static final String BOUND_KEY_ID = "BOUND_KEY_ID";
    private String userId;

    @BindView(R.id.im_header)
    ImageView mHeader;
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    @BindView(R.id.txt_name)
    TextView mName;
    @BindView(R.id.txt_desc)
    TextView mDesc;
    @BindView(R.id.txt_follows)
    TextView mFollows;
    @BindView(R.id.txt_following)
    TextView mFollowing;
    @BindView(R.id.btn_say_hello)
    Button mSayHello;
    @BindView(R.id.btn_log_out)
    Button mLogOut;

    // 关注
    private MenuItem mFollowItem;
    private boolean mIsFollowUser = false;

    public static void show(Context context, String userId) {
        Intent intent = new Intent(context, PersonalActivity.class);
        intent.putExtra(BOUND_KEY_ID, userId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        userId = bundle.getString(BOUND_KEY_ID);
        return !TextUtils.isEmpty(userId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.personal, menu);
        mFollowItem = menu.findItem(R.id.action_follow);
        changeFollowItemStatus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_follow) {
            //进行关注操作
            //TODO 有两种情况吧，如果已经关注了点击取消关注,后续扩展
            if (mIsFollowUser){
                //已经关注了
                //do no thing
            }else{
                //没有关注
                mPresenter.follow(userId);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_say_hello)
    void onSayHelloClick() {
        // 发起聊天的点击
        User user = mPresenter.getUserPersonal();
        if (user == null)
            return;
        MessageActivity.show(this, user);
    }

    @OnClick(R.id.btn_log_out)
    void onLogOutClick(){
        //跳转到账户登陆界面
        Application.getInstance().finishAll();
        AccountActivity.show(this);
        //TODO 发送退出登陆推送
    }


    /**
     * 更改关注菜单状态
     */
    private void changeFollowItemStatus() {
        if (mFollowItem == null)
            return;

        // 根据状态设置颜色
        Drawable drawable = mIsFollowUser ? getResources()
                .getDrawable(R.drawable.ic_favorite) :
                getResources().getDrawable(R.drawable.ic_favorite_border);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, Resource.Color.WHITE);
        mFollowItem.setIcon(drawable);
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onLoadDone(User user) {
        if (user == null)
            return;
        mPortrait.setup(Glide.with(this), user);
        mName.setText(user.getName());
        mDesc.setText(user.getDesc());
        mFollows.setText(String.format(getString(R.string.label_follows), user.getFollows()));
        mFollowing.setText(String.format(getString(R.string.label_following), user.getFollowing()));
        hideLoading();
    }

    @Override
    public void allowSayHello(boolean isAllow) {
        mSayHello.setVisibility(isAllow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void allowLogOut(boolean isSelf) {
        mLogOut.setVisibility(isSelf ? View.GONE : View.GONE);
    }

    @Override
    public void setFollowStatus(boolean isFollow) {
        mIsFollowUser = isFollow;
        changeFollowItemStatus();
    }

    @Override
    public void onFollowSucceed(UserCard userCard) {

        //关注成功后更改当前页面图标状态
        mIsFollowUser = true;
        changeFollowItemStatus();

    }

    @Override
    protected PersonalContract.Presenter initPresenter() {
        return new PersonalPresenter(this);
    }
}
