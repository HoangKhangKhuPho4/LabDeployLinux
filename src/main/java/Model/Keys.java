package Model;

import java.sql.Date;

public class Keys {
    private int id;
    private User userId;
    private String keys;
    private Date createTime;
    private Date endTime;

    public Keys(int id, User userId, String keys, Date createTime, Date endTime) {
        this.id = id;
        this.userId = userId;
        this.keys = keys;
        this.createTime = createTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Keys{" +
                "id=" + id +
                ", userId=" + userId +
                ", keys='" + keys + '\'' +
                ", createTime=" + createTime +
                ", endTime=" + endTime +
                '}';
    }
}
