package net.jackytallow.web.italker.push.bean.card;

import com.google.gson.annotations.Expose;
import net.jackytallow.web.italker.push.bean.db.User;
import net.jackytallow.web.italker.push.utils.Hib;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;

/**
 * @author jacky
 * @version 1.0.0
 */
public class UserCard {
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String phone;
    @Expose
    private String portrait;
    @Expose
    private String desc;
    @Expose
    private int sex = 0;

    // 用户关注人的数量
    @Expose
    private int follows;

    // 用户粉丝的数量
    @Expose
    private int following;

    // 我与当前User的关系状态，是否已经关注了这个人
    @Expose
    private boolean isFollow;

    // 用户信息最后的更新时间
    @Expose
    private LocalDateTime modifyAt;

    public UserCard(final User user) {
        this(user, false);
    }

    public UserCard(final User user, boolean isFollow) {
        this.isFollow = isFollow;

        this.id = user.getId();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.portrait = user.getPortrait();
        this.desc = user.getDescription();
        this.sex = user.getSex();
        this.modifyAt = user.getUpdateAt();

        // user.getFollowers().size()
        // 懒加载会报错，因为没有Session
        Hib.queryOnly(session -> {
            // 重新加载一次用户信息
            session.load(user, user.getId());
            // 这个时候仅仅只是进行了数量查询，并没有查询整个集合
            // 要查询集合，必须在session存在情况下进行遍历
            // 或者使用Hibernate.initialize(user.getFollowers());
            follows = user.getFollowers().size();
            following = user.getFollowing().size();
        });

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public LocalDateTime getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(LocalDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }
}