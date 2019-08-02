/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- db_rimdrive_oss 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `db_rimdrive_oss` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `db_rimdrive_oss`;

-- 함수 db_rimdrive_oss.fc_pwd_hash 구조 내보내기
DROP FUNCTION IF EXISTS `fc_pwd_hash`;
DELIMITER //
CREATE DEFINER=`rim_admin`@`%` FUNCTION `fc_pwd_hash`(strpwd VARCHAR(40)) RETURNS varchar(255) CHARSET utf8
RETURN SHA2(strpwd, 256)//
DELIMITER ;

-- 함수 db_rimdrive_oss.fn_get_parentIdByPathWithName 구조 내보내기
DROP FUNCTION IF EXISTS `fn_get_parentIdByPathWithName`;
DELIMITER //
CREATE DEFINER=`rim_admin`@`%` FUNCTION `fn_get_parentIdByPathWithName`(p_storage_id varchar(64), p_path varchar(4000), p_name varchar(1000)) RETURNS bigint(19)
    READS SQL DATA
BEGIN
      DECLARE _return BIGINT(19);

      SET _return =  ( SELECT file_id
                         FROM rim_file
                        WHERE storage_id = p_storage_id
                          AND path_hash=MD5(LEFT(p_path,CHAR_LENGTH( p_path )-CHAR_LENGTH(p_name)-1)));
      RETURN _return;

END//
DELIMITER ;

-- 함수 db_rimdrive_oss.fn_get_pathByfileid 구조 내보내기
DROP FUNCTION IF EXISTS `fn_get_pathByfileid`;
DELIMITER //
CREATE DEFINER=`rim_admin`@`%` FUNCTION `fn_get_pathByfileid`(node BIGINT(19)) RETURNS text CHARSET utf8
    READS SQL DATA
BEGIN
      DECLARE _return_path TEXT;
      DECLARE _name TEXT;
      DECLARE _cpath TEXT;
      DECLARE _file_id BIGINT(19);
      DECLARE _id BIGINT(19);
      DECLARE _delimiter TEXT;
      DECLARE _source_path TEXT;
      DECLARE EXIT HANDLER FOR NOT FOUND RETURN _return_path;
     
      SET _delimiter = '/';
      SET _id = COALESCE(node, @id);
      SET _return_path =  '';
      
      LOOP
        SELECT  parent_id, file_id, name, path
          INTO    _id, _file_id, _name, _source_path
              FROM    rim_file
              WHERE   file_id = _id
               AND parent_id > 0
               AND COALESCE(file_id <> @start_with, TRUE);
               IF _source_path is not NULL THEN
                SET _return_path = CONCAT(_name, CASE WHEN _return_path = '' THEN '' ELSE _delimiter END, _return_path) ;
               ELSE
                SET _return_path = CONCAT( _delimiter , _return_path);
               END IF;

      END LOOP;
      
END//
DELIMITER ;

-- 함수 db_rimdrive_oss.fn_get_pathfileid 구조 내보내기
DROP FUNCTION IF EXISTS `fn_get_pathfileid`;
DELIMITER //
CREATE DEFINER=`rim_admin`@`%` FUNCTION `fn_get_pathfileid`(node BIGINT(19)) RETURNS text CHARSET utf8
    READS SQL DATA
BEGIN
      DECLARE _return_path TEXT;
      DECLARE _name TEXT;
      DECLARE _cpath TEXT;
      DECLARE _file_id BIGINT(19);
      DECLARE _id BIGINT(19);
      DECLARE _delimiter TEXT;
      DECLARE _source_path TEXT;
      DECLARE EXIT HANDLER FOR NOT FOUND RETURN _return_path;
     
      SET _delimiter = '/';
      SET _id = COALESCE(node, @id);
      SET _return_path =  '';
      
      LOOP
        SELECT  parent_id, file_id, name, path
          INTO    _id, _file_id, _name, _source_path
              FROM    rim_file
              WHERE   file_id = _id
               AND parent_id > 0
               AND COALESCE(file_id <> @start_with, TRUE);
               IF _source_path is not NULL THEN
                SET _return_path = CONCAT(_file_id, _delimiter , _return_path);
               ELSE
                SET _return_path = CONCAT( _delimiter , _return_path);
               END IF;

      END LOOP;
      
END//
DELIMITER ;

-- 테이블 db_rimdrive_oss.rim_bookmark 구조 내보내기
DROP TABLE IF EXISTS `rim_bookmark`;
CREATE TABLE IF NOT EXISTS `rim_bookmark` (
  `storage_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `user_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `file_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`,`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



-- 테이블 db_rimdrive_oss.rim_dept 구조 내보내기
DROP TABLE IF EXISTS `rim_dept`;
CREATE TABLE IF NOT EXISTS `rim_dept` (
  `dept_cd` varchar(64) COLLATE utf8_bin NOT NULL,
  `dept_nm` varchar(300) COLLATE utf8_bin NOT NULL,
  `upr_dept_cd` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `whle_dept_cd` varchar(1000) COLLATE utf8_bin NOT NULL,
  `dept_level` smallint(6) NOT NULL,
  `opt_yn` varchar(1) COLLATE utf8_bin NOT NULL,
  `sort_sord` smallint(6) DEFAULT NULL,
  `create_dt` datetime DEFAULT NULL,
  `create_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `modify_dt` datetime DEFAULT NULL,
  `modify_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `dept_type` smallint(6) DEFAULT '1',
  PRIMARY KEY (`dept_cd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


/*!40000 ALTER TABLE `rim_dept` DISABLE KEYS */;
INSERT INTO `rim_dept` (`dept_cd`, `dept_nm`, `upr_dept_cd`, `whle_dept_cd`, `dept_level`, `opt_yn`, `sort_sord`, `create_dt`, `create_uid`, `modify_dt`, `modify_uid`) VALUES
	('0', 'test_company', '0', '0;', 0, 'Y', 1, now(), 'admin', now(), 'admin'),
	('cloud00000', 'dev', '0', '0;cloud00000;', 1, 'Y', 1, now(), 'admin', now(), 'admin'),
	('cloud01001', 'test1', 'cloud00000', '0;cloud00000;cloud01001;', 2, 'Y', 1, now(), 'admin', now(), 'admin'),
	('cloud01002', 'test2', 'cloud00000', '0;cloud00000;cloud01002;', 2, 'Y', 2, now(), 'admin', now(), 'admin');
/*!40000 ALTER TABLE `rim_dept` ENABLE KEYS */;


-- 테이블 db_rimdrive_oss.rim_dept_service_config 구조 내보내기
DROP TABLE IF EXISTS `rim_dept_service_config`;
CREATE TABLE IF NOT EXISTS `rim_dept_service_config` (
  `dept_cd` varchar(64) COLLATE utf8_bin NOT NULL,
  `ci_text` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `quota` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `user_count` int(11) NOT NULL DEFAULT '0',
  `win_client_use_yn` varchar(1) COLLATE utf8_bin DEFAULT 'N',
  `win_client_sync_use_yn` varchar(1) COLLATE utf8_bin DEFAULT 'N',
  `win_client_drive_nm` varchar(40) COLLATE utf8_bin DEFAULT NULL,
  `start_ymd` varchar(8) COLLATE utf8_bin DEFAULT NULL,
  `end_ymd` varchar(8) COLLATE utf8_bin DEFAULT NULL,
  `service_tp` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `modify_dt` datetime DEFAULT NULL,
  `modify_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_dt` datetime DEFAULT NULL,
  `create_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `win_client_version` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `win_client_version_memo` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`dept_cd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



-- 테이블 db_rimdrive_oss.rim_emp 구조 내보내기
DROP TABLE IF EXISTS `rim_emp`;
CREATE TABLE IF NOT EXISTS `rim_emp` (
  `emp_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `emp_nm` varchar(200) COLLATE utf8_bin NOT NULL,
  `hlofc_yn` varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y',
  `cloud_allow_yn` varchar(1) COLLATE utf8_bin NOT NULL DEFAULT 'Y',
  `create_tp` varchar(1) COLLATE utf8_bin NOT NULL,
  `login_id` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `pwd_chg_obj_yn` varchar(1) COLLATE utf8_bin NOT NULL,
  `grade` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `sort_sord` smallint(6) DEFAULT NULL,
  `dept_cd` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `modify_dt` datetime DEFAULT NULL,
  `modify_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_dt` datetime DEFAULT NULL,
  `create_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `quota` int(11) DEFAULT NULL,
  PRIMARY KEY (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


/*!40000 ALTER TABLE `rim_emp` DISABLE KEYS */;
INSERT INTO `rim_emp` (`emp_id`, `emp_nm`, `hlofc_yn`, `cloud_allow_yn`, `create_tp`, `login_id`, `pwd_chg_obj_yn`, `grade`, `sort_sord`, `dept_cd`, `modify_dt`, `modify_uid`, `create_dt`, `create_uid`, `quota`) VALUES
	('test01', 'testuser1', 'Y', 'Y', 'C', 'test01', 'N', '', 1, 'cloud01001', now(), 'admin', now(), 'admin', 20),
	('test02', 'testuser2', 'Y', 'Y', 'C', 'test02', 'N', '', 1, 'cloud01001', now(), 'admin', now(), 'admin', 20),
	('test03', 'testuser3', 'Y', 'Y', 'C', 'test03', 'N', '', 1, 'cloud01002', now(), 'admin', now(), 'admin', 20),
	('test04', 'testuser4', 'Y', 'Y', 'C', 'test04', 'N', '', 1, 'cloud01002', now(), 'admin', now(), 'admin', 20);
/*!40000 ALTER TABLE `rim_emp` ENABLE KEYS */;



-- 테이블 db_rimdrive_oss.rim_file 구조 내보내기
DROP TABLE IF EXISTS `rim_file`;
CREATE TABLE IF NOT EXISTS `rim_file` (
  `file_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `storage_id` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `file_tp` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  `path` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `path_hash` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `name` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  `search_tag` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `mimetype` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `create_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_dt` datetime DEFAULT NULL,
  `modify_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `modify_dt` datetime DEFAULT NULL,
  PRIMARY KEY (`file_id`),
  KEY `idx_rim_file_storage_id` (`storage_id`),
  KEY `idx_rim_file_storage_id_pathhash` (`storage_id`,`path_hash`),
  KEY `idx_rim_file_storage_id_parent_id_file_tp` (`storage_id`,`parent_id`,`file_tp`),
  KEY `idx_rim_file_storage_id_path_file_id` (`storage_id`),
  KEY `idx_rim_file_storage_id_file_tp` (`storage_id`,`file_tp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



-- 테이블 db_rimdrive_oss.rim_file_trash 구조 내보내기
DROP TABLE IF EXISTS `rim_file_trash`;
CREATE TABLE IF NOT EXISTS `rim_file_trash` (
  `trash_no` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `file_id` bigint(20) DEFAULT NULL,
  `original_name` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `original_path` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `modify_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `modify_dt` datetime DEFAULT NULL,
  `timestamp` varchar(12) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`trash_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



-- 테이블 db_rimdrive_oss.rim_file_version 구조 내보내기
DROP TABLE IF EXISTS `rim_file_version`;
CREATE TABLE IF NOT EXISTS `rim_file_version` (
  `version_no` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `file_id` bigint(20) DEFAULT NULL,
  `name` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `path` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `create_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_dt` datetime DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  `mimetype` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`version_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



-- 테이블 db_rimdrive_oss.rim_group_storage 구조 내보내기
DROP TABLE IF EXISTS `rim_group_storage`;
CREATE TABLE IF NOT EXISTS `rim_group_storage` (
  `grp_strg_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `grp_strg_nm` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `use_yn` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  `service_tp` varchar(1) COLLATE utf8_bin DEFAULT 'N',
  `create_dt` datetime DEFAULT NULL,
  `create_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `modify_dt` datetime DEFAULT NULL,
  `modify_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `dept_cd` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`grp_strg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



-- 테이블 db_rimdrive_oss.rim_group_storage_member 구조 내보내기
DROP TABLE IF EXISTS `rim_group_storage_member`;
CREATE TABLE IF NOT EXISTS `rim_group_storage_member` (
  `grp_strg_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `member_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `member_tp` varchar(2) COLLATE utf8_bin DEFAULT NULL ,
  `permissions` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `create_dt` datetime DEFAULT NULL,
  `create_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `modify_dt` datetime DEFAULT NULL,
  `modify_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `sub_dept_yn` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`grp_strg_id`,`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



-- 테이블 db_rimdrive_oss.rim_rownum 구조 내보내기
DROP TABLE IF EXISTS `rim_rownum`;
CREATE TABLE IF NOT EXISTS `rim_rownum` (
  `seq_no` int(11) NOT NULL,
  `varchar_num` varchar(11) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`seq_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


/*!40000 ALTER TABLE `rim_rownum` DISABLE KEYS */;
INSERT INTO `rim_rownum` (`seq_no`, `varchar_num`) VALUES
	(1, '1'),
	(2, '2'),
	(3, '3'),
	(4, '4'),
	(5, '5'),
	(6, '6'),
	(7, '7'),
	(8, '8'),
	(9, '9'),
	(10, '10'),
	(11, '11'),
	(12, '12'),
	(13, '13'),
	(14, '14'),
	(15, '15'),
	(16, '16'),
	(17, '17'),
	(18, '18'),
	(19, '19'),
	(20, '20'),
	(26, '26'),
	(27, '27'),
	(28, '28'),
	(29, '29'),
	(30, '30'),
	(31, '31'),
	(32, '32'),
	(33, '33'),
	(34, '34'),
	(35, '35'),
	(41, '41'),
	(42, '42'),
	(43, '43'),
	(44, '44'),
	(45, '45'),
	(46, '46'),
	(47, '47'),
	(48, '48'),
	(49, '49'),
	(50, '50'),
	(56, '56'),
	(57, '57'),
	(58, '58'),
	(59, '59'),
	(60, '60'),
	(61, '61'),
	(62, '62'),
	(63, '63'),
	(64, '64'),
	(65, '65'),
	(71, '71'),
	(72, '72'),
	(73, '73'),
	(74, '74'),
	(75, '75'),
	(76, '76'),
	(77, '77'),
	(78, '78'),
	(79, '79'),
	(80, '80'),
	(86, '86'),
	(87, '87'),
	(88, '88'),
	(89, '89'),
	(90, '90'),
	(91, '91'),
	(92, '92'),
	(93, '93'),
	(94, '94'),
	(95, '95'),
	(96, '96'),
	(97, '97'),
	(98, '98'),
	(99, '99'),
	(100, '100');
/*!40000 ALTER TABLE `rim_rownum` ENABLE KEYS */;

-- 테이블 db_rimdrive_oss.rim_share 구조 내보내기
DROP TABLE IF EXISTS `rim_share`;
CREATE TABLE IF NOT EXISTS `rim_share` (
  `share_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `share_tp` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  `storage_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `file_id` bigint(20) DEFAULT NULL,
  `token` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `modify_dt` datetime DEFAULT NULL,
  `modify_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_dt` datetime DEFAULT NULL,
  `create_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `share_nm` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`share_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



-- 테이블 db_rimdrive_oss.rim_share_target 구조 내보내기
DROP TABLE IF EXISTS `rim_share_target`;
CREATE TABLE IF NOT EXISTS `rim_share_target` (
  `share_target_no` bigint(20) NOT NULL AUTO_INCREMENT,
  `share_id` bigint(20) NOT NULL DEFAULT '0',
  `share_with_uid` varchar(64) COLLATE utf8_bin NOT NULL,
  `permissions` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  `target_tp` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  `modify_dt` datetime DEFAULT NULL,
  `modify_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `create_dt` datetime DEFAULT NULL,
  `create_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `share_nm` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`share_target_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



-- 테이블 db_rimdrive_oss.rim_storage 구조 내보내기
DROP TABLE IF EXISTS `rim_storage`;
CREATE TABLE IF NOT EXISTS `rim_storage` (
  `storage_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `owner_id` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `quota` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `owner_tp` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `create_dt` datetime DEFAULT NULL,
  `create_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `modify_dt` datetime DEFAULT NULL,
  `modify_uid` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`storage_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



-- 테이블 db_rimdrive_oss.rim_user 구조 내보내기
DROP TABLE IF EXISTS `rim_user`;
CREATE TABLE IF NOT EXISTS `rim_user` (
  `user_id` varchar(64) COLLATE utf8_bin NOT NULL,
  `display_nm` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `passwd` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*!40000 ALTER TABLE `rim_user` DISABLE KEYS */;
INSERT INTO `rim_user` (`user_id`, `display_nm`, `passwd`) VALUES
	('test01', 'test1', fc_pwd_hash('test01')),
	('test02', 'test2', fc_pwd_hash('test02')),
	('test03', 'test3', fc_pwd_hash('test03')),
	('test04', 'test4', fc_pwd_hash('test04'));
/*!40000 ALTER TABLE `rim_user` ENABLE KEYS */;



/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
