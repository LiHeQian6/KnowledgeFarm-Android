/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50506
Source Host           : localhost:3306
Source Database       : farm_db

Target Server Type    : MYSQL
Target Server Version : 50506
File Encoding         : 65001

Date: 2019-12-07 10:28:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accout` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `exist` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', 'admin', 'admin', '1');
INSERT INTO `admin` VALUES ('2', 'zsh', '123', '1');
INSERT INTO `admin` VALUES ('6', 'yxt', '123', '1');
INSERT INTO `admin` VALUES ('7', 'jgd', '123', '0');
INSERT INTO `admin` VALUES ('8', 'lhq', '1234', '0');

-- ----------------------------
-- Table structure for `crop`
-- ----------------------------
DROP TABLE IF EXISTS `crop`;
CREATE TABLE `crop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `img1` varchar(150) DEFAULT NULL,
  `img2` varchar(150) DEFAULT NULL,
  `img3` varchar(150) DEFAULT NULL,
  `cropPhotoName` varchar(100) DEFAULT NULL,
  `matureTime` int(11) unsigned DEFAULT NULL,
  `value` int(11) DEFAULT NULL,
  `experience` int(11) DEFAULT NULL,
  `exist` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of crop
-- ----------------------------
INSERT INTO `crop` VALUES ('15', '玫瑰', '100', 'http://10.7.87.220:8080/img/0afasfa.png', 'http://10.7.87.220:8080/img/1afasfa.png', 'http://10.7.87.220:8080/img/2afasfa.png', 'afasfa.png', '100', '100', '100', '1');
INSERT INTO `crop` VALUES ('17', 'cake', '500', 'http://10.7.87.220:8080/img/cake0delete.png', 'http://localhost:8080/img/cake1cake1.png', 'http://localhost:8080/img/cake2cake1.png', null, '5', '500', '500', '1');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accout` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `nickName` varchar(100) DEFAULT NULL,
  `photo` varchar(150) DEFAULT NULL,
  `photoName` varchar(100) DEFAULT NULL,
  `level` int(11) DEFAULT '1',
  `experience` bigint(11) DEFAULT '0',
  `grade` int(11) DEFAULT '1',
  `money` bigint(11) DEFAULT '0',
  `rewardCount` int(11) DEFAULT '15',
  `water` int(11) DEFAULT '0',
  `fertilizer` int(11) DEFAULT '0',
  `online` int(11) DEFAULT '1',
  `exist` int(11) DEFAULT '1',
  `land1` int(11) DEFAULT '0',
  `land2` int(11) DEFAULT '0',
  `land3` int(11) DEFAULT '0',
  `land4` int(11) DEFAULT '0',
  `land5` int(11) DEFAULT '-1',
  `land6` int(11) DEFAULT '-1',
  `land7` int(11) DEFAULT '-1',
  `land8` int(11) DEFAULT '-1',
  `land9` int(11) DEFAULT '-1',
  `land10` int(11) DEFAULT '-1',
  `land11` int(11) DEFAULT '-1',
  `land12` int(11) DEFAULT '-1',
  `land13` int(11) DEFAULT '-1',
  `land14` int(11) DEFAULT '-1',
  `land15` int(11) DEFAULT '-1',
  `land16` int(11) DEFAULT '-1',
  `land17` int(11) DEFAULT '-1',
  `land18` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('37', '71007839', null, 'test1', 'http://10.7.87.220:8080/img/71007839deletecar.png', '71007839deletecar.png', '1', '0', '1', '0', '15', '0', '0', '1', '1', '0', '0', '0', '0', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1');
INSERT INTO `user` VALUES ('38', '14984820', null, 'test2', 'http://10.7.87.220:8080/img/14984820addnum.png', '14984820addnum.png', '1', '0', '1', '0', '15', '0', '0', '1', '1', '0', '0', '0', '0', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1');
INSERT INTO `user` VALUES ('39', '46405606', null, 'test3', 'http://10.7.87.220:8080/img/46405606cha.png', '46405606cha.png', '1', '0', '1', '0', '15', '0', '0', '1', '1', '0', '0', '0', '0', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1');
INSERT INTO `user` VALUES ('40', '43641746', null, 'test4', 'http://10.7.87.220:8080/img/43641746decreasenum.png', '43641746decreasenum.png', '1', '0', '1', '0', '15', '0', '0', '1', '1', '0', '0', '0', '0', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1');
INSERT INTO `user` VALUES ('43', '89838845', null, 'test5', 'http://10.7.87.220:8080/img/89838845delete.png', null, '1', '0', '1', '0', '15', '0', '0', '1', '1', '0', '0', '0', '0', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1');
INSERT INTO `user` VALUES ('44', '78317468', null, 'test6', 'http://10.7.87.220:8080/img/78317468decreasenum.png', '78317468decreasenum.png', '1', '0', '1', '0', '15', '0', '0', '1', '1', '0', '0', '0', '0', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1', '-1');

-- ----------------------------
-- Table structure for `userauthority`
-- ----------------------------
DROP TABLE IF EXISTS `userauthority`;
CREATE TABLE `userauthority` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `openId` varchar(150) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `exist` int(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userauthority
-- ----------------------------
INSERT INTO `userauthority` VALUES ('33', '37', 'test1', 'QQ', '1');
INSERT INTO `userauthority` VALUES ('34', '38', 'test2', 'QQ', '1');
INSERT INTO `userauthority` VALUES ('35', '39', 'test3', 'QQ', '1');
INSERT INTO `userauthority` VALUES ('36', '40', 'test4', 'QQ', '1');
INSERT INTO `userauthority` VALUES ('39', '43', 'test5', 'QQ', '1');
INSERT INTO `userauthority` VALUES ('40', '44', 'test6', 'QQ', '1');

-- ----------------------------
-- Table structure for `userbag`
-- ----------------------------
DROP TABLE IF EXISTS `userbag`;
CREATE TABLE `userbag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `cropId` int(11) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userbag
-- ----------------------------
INSERT INTO `userbag` VALUES ('1', '26', '1', '13');
INSERT INTO `userbag` VALUES ('2', '27', '2', '3');

-- ----------------------------
-- Table structure for `usercrop`
-- ----------------------------
DROP TABLE IF EXISTS `usercrop`;
CREATE TABLE `usercrop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cropId` int(11) DEFAULT NULL,
  `waterLimit` int(11) DEFAULT NULL,
  `fertilizerLimit` int(11) DEFAULT NULL,
  `progress` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of usercrop
-- ----------------------------
