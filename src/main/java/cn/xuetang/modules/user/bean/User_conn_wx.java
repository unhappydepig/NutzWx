package cn.xuetang.modules.user.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.EL;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author Wizzer
 * @time 2014-04-05 00:45:11
 */
@Table("user_conn_wx")
public class User_conn_wx {
    @Name
    @Prev(els = { @EL("uuid()") })
    private String openid;
    @Column
    private int id;
    @Column
    private int pid;
    @Column
    private int appid;
    @Column
    private int uid;
    @Column
    private long fake_id;

    @Column
    private String wx_nickname;
    @Column
    private int wx_sex;
    @Column
    private String wx_headimgurl;
    @Column
    private String wx_country;
    @Column
    private String wx_province;
    @Column
    private String wx_city;
    @Column
    private String wx_time;
    @Column
    private int status;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getFake_id() {
        return fake_id;
    }

    public void setFake_id(long fake_id) {
        this.fake_id = fake_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getWx_nickname() {
        return wx_nickname;
    }

    public void setWx_nickname(String wx_nickname) {
        this.wx_nickname = wx_nickname;
    }

    public int getWx_sex() {
        return wx_sex;
    }

    public void setWx_sex(int wx_sex) {
        this.wx_sex = wx_sex;
    }

    public String getWx_headimgurl() {
        return wx_headimgurl;
    }

    public void setWx_headimgurl(String wx_headimgurl) {
        this.wx_headimgurl = wx_headimgurl;
    }

    public String getWx_country() {
        return wx_country;
    }

    public void setWx_country(String wx_country) {
        this.wx_country = wx_country;
    }

    public String getWx_province() {
        return wx_province;
    }

    public void setWx_province(String wx_province) {
        this.wx_province = wx_province;
    }

    public String getWx_city() {
        return wx_city;
    }

    public void setWx_city(String wx_city) {
        this.wx_city = wx_city;
    }

    public String getWx_time() {
        return wx_time;
    }

    public void setWx_time(String wx_time) {
        this.wx_time = wx_time;
    }
}