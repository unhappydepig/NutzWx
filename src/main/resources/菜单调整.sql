/*
SQLyog Ultimate v8.32 
MySQL - 5.6.0-m4 : Database - weixin
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `sys_resource` */

CREATE TABLE `sys_resource` (
  `ID` varchar(100) NOT NULL,
  `NAME` varchar(50) DEFAULT '',
  `URL` varchar(100) DEFAULT '',
  `STATE` int(1) DEFAULT '0',
  `SUBTYPE` int(11) DEFAULT '0',
  `LOCATION` int(11) DEFAULT '0',
  `DESCRIPT` varchar(100) DEFAULT '',
  `BUTTON` varchar(1000) DEFAULT '',
  `STYLE` varchar(100) DEFAULT '',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_resource` */

insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('0001','系统管理','',0,0,1,'','','icon-cog');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00010001','机构管理','/private/sys/unit',0,0,2,'','增加/BtnAdd;删除/BtnDel;修改/BtnUpdate;排序/BtnSort;','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00010002','用户管理','/private/sys/user',0,0,3,'','增加/BtnAdd;修改/BtnUpdate;删除/BtnDel;禁用/BtnLocked;启用/BtnUnlocked;','fa fa-users');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00010003','角色管理','/private/sys/role',0,0,4,'','增加/BtnAdd;删除/BtnDel;修改/BtnUpdate;添加用户到角色/BtnAddRole;从角色中删除用户/BtnDelRole;分配权限/BtnMenu;','fa fa-user');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00010004','资源管理','/private/sys/res',0,0,5,'','增加/BtnAdd;删除/BtnDel;修改/BtnUpdate;排序/BtnSort;','fa fa-user');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00010005','参数配置','/private/sys/config',0,0,6,' ','新建/BtnAdd;编辑/BtnUpdate;删除/BtnDel;','fa fa-user');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00010006','数字字典','/private/sys/dict',0,0,7,' ','增加/BtnAdd;修改/BtnUpdate;删除/BtnDel;排序/BtnPaixu;','fa fa-user');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00010007','定时任务','/private/sys/task',0,0,8,' ','新建/BtnAdd;编辑/BtnUpdate;删除/BtnDel;','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00010008','访问控制','/private/sys/safe',0,0,9,'','','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00010009','登陆日志','/private/sys/user/log',0,0,10,'','','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('0002','微信管理','',0,0,15,'','','icon-desktop');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00020001','微信回复','/private/wx/txt',0,0,17,'','','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00020002','客服群发','',0,0,18,'','','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00020003','高级群发','/private/wx/push',0,0,19,'','新建/BtnAdd;删除/BtnDel;查询/BtnSearch;','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00020004','微信相册','/private/wx/image',0,0,20,'','删除照片/BtnDel;选入节目/BtnIn;节目管理/BtnJM;节目通知/BtnWx;高级群发/BtnQf;查询/BtnSearch;下载照片/BtnDown;','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00020005','微信视频','/private/wx/video',0,0,21,'','删除照片/BtnDel;选入节目/BtnIn;节目管理/BtnJM;节目通知/BtnWx;高级群发/BtnQf;查询/BtnSearch;下载视频/BtnDown;','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00020006','微信菜单','',0,0,16,' ','','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('0003','内容管理','',0,0,22,'','','icon-text-width');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00030001','文章管理','/private/wx/content',0,0,23,'','新建/BtnAdd;编辑/BtnUpdate;删除/BtnDel;发布/BtnPub;撤回/BtnRecll;推送/BtnPush;','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00030002','栏目管理','/private/wx/channel',0,0,24,'','新建/BtnAdd;编辑/BtnUpdate;删除/BtnDel;排序/BtnSort;新建属性/BtnAddAttr;编辑属性/BtnUpdateAttr;删除属性/BtnDelAttr;','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('0004','会员中心','',0,0,25,'','','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00040001','会员资料','/private/user/info',0,0,26,'','','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00040002','会员积分','/private/user/score',0,0,27,'','','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00040003','微信帐号','/private/user/connwx',0,0,28,'','','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('0005','应用管理','',0,0,11,' ','','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00050001','项目管理','/private/app/project',0,0,12,' ','新建/BtnAdd;编辑/BtnUpdate;删除/BtnDel;','');
insert  into `sys_resource`(`ID`,`NAME`,`URL`,`STATE`,`SUBTYPE`,`LOCATION`,`DESCRIPT`,`BUTTON`,`STYLE`) values ('00050003','接口管理','/private/app/info',0,0,13,' ','新建/BtnAdd;编辑/BtnUpdate;删除/BtnDel;','');

/*Table structure for table `sys_role_resource` */

CREATE TABLE `sys_role_resource` (
  `ROLEID` int(11) NOT NULL,
  `RESOURCEID` varchar(100) NOT NULL,
  `BUTTON` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`ROLEID`,`RESOURCEID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_role_resource` */

insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'0001','');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00010001','');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00010002','');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00010003','BtnAdd,BtnDel,BtnUpdate,BtnAddRole,BtnDelRole,BtnMenu,');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00010004','BtnAdd,BtnDel,BtnUpdate,BtnSort,');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00010005','BtnAdd,BtnUpdate,BtnDel,');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00010006','BtnAdd,BtnUpdate,BtnDel,BtnPaixu,');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00010007','BtnAdd,BtnUpdate,BtnDel,');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00010008','');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00010009','');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'0002','');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00020001','');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00020002','');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00020003','');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00020004','BtnDel,BtnIn,BtnJM,BtnWx,BtnQf,BtnSearch,BtnDown,');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00020005','BtnDel,BtnIn,BtnJM,BtnWx,BtnQf,BtnSearch,BtnDown,');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00020006','');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'0003','');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00030001','');
insert  into `sys_role_resource`(`ROLEID`,`RESOURCEID`,`BUTTON`) values (2,'00030002','');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
