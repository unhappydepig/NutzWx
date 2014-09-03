package cn.xuetang.modules.wx.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author Wizzer
 * @time 2014-04-28 16:08:44
 */
@Table("weixin_push")
public class Weixin_push {
    @Id
    private int id;
    @Column
    private int pid;
    @Column
    private int appid;
    @Column
    private String push_name;
    @Column
    private int push_type;
    @Column
    private String push_birthday;
    @Column
    private String push_age;
    @Column
    private int push_sex;
    @Column
    private int push_tv;
    @Column
    private String push_province;
    @Column
    private String push_city;
    @Column
    private int push_num;
    @Column
    private String push_text;
    @Column
    private String push_title;
    @Column
    private String push_description;
    @Column
    private String type;
    @Column
    private String media_id;
    @Column
    private String created_at;
    @Column
    private int errcode;
    @Column
    private String errmsg;
    @Column
    private int status;
    @Column
    private int op_userid;
    @Column
    private String op_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getPush_name() {
        return push_name;
    }

    public void setPush_name(String push_name) {
        this.push_name = push_name;
    }

    public int getPush_type() {
        return push_type;
    }

    public void setPush_type(int push_type) {
        this.push_type = push_type;
    }

    public String getPush_age() {
        return push_age;
    }

    public void setPush_age(String push_age) {
        this.push_age = push_age;
    }

    public int getPush_sex() {
        return push_sex;
    }

    public void setPush_sex(int push_sex) {
        this.push_sex = push_sex;
    }

    public int getPush_tv() {
        return push_tv;
    }

    public void setPush_tv(int push_tv) {
        this.push_tv = push_tv;
    }

    public String getPush_province() {
        return push_province;
    }

    public void setPush_province(String push_province) {
        this.push_province = push_province;
    }

    public String getPush_city() {
        return push_city;
    }

    public void setPush_city(String push_city) {
        this.push_city = push_city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOp_userid() {
        return op_userid;
    }

    public void setOp_userid(int op_userid) {
        this.op_userid = op_userid;
    }

    public String getOp_time() {
        return op_time;
    }

    public void setOp_time(String op_time) {
        this.op_time = op_time;
    }

    public int getPush_num() {
        return push_num;
    }

    public void setPush_num(int push_num) {
        this.push_num = push_num;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getPush_text() {
        return push_text;
    }

    public void setPush_text(String push_text) {
        this.push_text = push_text;
    }

    public String getPush_title() {
        return push_title;
    }

    public void setPush_title(String push_title) {
        this.push_title = push_title;
    }

    public String getPush_description() {
        return push_description;
    }

    public void setPush_description(String push_description) {
        this.push_description = push_description;
    }

    public String getPush_birthday() {
        return push_birthday;
    }

    public void setPush_birthday(String push_birthday) {
        this.push_birthday = push_birthday;
    }
}