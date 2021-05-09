CREATE DATABASE cloud;
USE cloud;

DROP TABLE IF EXISTS USER;
CREATE TABLE USER(
	id INT(255) PRIMARY KEY AUTO_INCREMENT COMMENT '用户id',
	username VARCHAR(40) NOT NULL UNIQUE COMMENT '用户名',
	PASSWORD VARCHAR(40) NOT NULL COMMENT '用户密码',
	nickname VARCHAR(40) DEFAULT 'sb' COMMENT '用户昵称',
	birthday DATE COMMENT '用户出生日期',
	sex VARCHAR(10) DEFAULT 'male' COMMENT '用户性别',
	register_time TIMESTAMP DEFAULT NOW() COMMENT '用户注册时间',
	image_path VARCHAR(200) COMMENT '用户头像链接',
	file_storehouse_id INT(11) COMMENT '文件仓库id'
)ENGINE=INNODB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS file_storehouse;
CREATE TABLE file_storehouse(
	id INT(255) PRIMARY KEY AUTO_INCREMENT COMMENT '文件仓库id',
	user_id INT(255) COMMENT '仓库所属用户id',
	current_size INT(11) DEFAULT 0 COMMENT '当前容量(KB)',
	MAX_SIZE INT(11) DEFAULT 1048576 COMMENT '最大容量(KB)'
)ENGINE=INNODB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS my_file_folder;
CREATE TABLE my_file_folder(
	id INT(255) PRIMARY KEY AUTO_INCREMENT COMMENT '文件夹id',
	file_folder_name VARCHAR(255) NOT NULL COMMENT '文件夹名称',
	parent_folder_id INT(255) DEFAULT -1 COMMENT '父文件夹id',
	file_folder_path VARCHAR(255) DEFAULT '/' COMMENT '文件夹存储路径',
	file_storehouse_id INT(255) COMMENT '所属文件仓库id',
	create_time TIMESTAMP DEFAULT NOW() COMMENT '创建时间'
)ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS my_file;
CREATE TABLE my_file(
	id INT(255) PRIMARY KEY AUTO_INCREMENT COMMENT '文件id',
	file_name VARCHAR(255) NOT NULL COMMENT '文件名',
	file_path VARCHAR(255) DEFAULT '/' COMMENT '文件存储路径',
	download_count INT(11) DEFAULT 0 COMMENT '文件下载次数',
	upload_time TIMESTAMP DEFAULT NOW() COMMENT '文件上传时间',
	parent_folder_id INT(255) DEFAULT -1 COMMENT '父文件夹id',
	file_size BIGINT(255) COMMENT '文件大小',
	file_postfix VARCHAR(11) COMMENT '文件后缀',
	file_storehouse_id INT(255) COMMENT '所属文件仓库id'
)ENGINE=INNODB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS file_download_task;
CREATE TABLE file_download_task(
	id INT(255) PRIMARY KEY AUTO_INCREMENT COMMENT '文件任务id',
	parent_folder_id INT(255) DEFAULT -1 COMMENT '父文件夹id',
	file_storehouse_id INT(255) COMMENT '所属文件仓库id',
	download_url VARCHAR(255) NOT NULL COMMENT '下载链接',
	target_path VARCHAR(255) NOT NULL COMMENT '下载目标目录',
	file_size BIGINT(255) COMMENT '下载文件的大小',
	download_size BIGINT DEFAULT 0 COMMENT '已经下载的大小',
	start_time TIMESTAMP DEFAULT NOW() COMMENT '文件开始时间',
	download_status INT(11) DEFAULT 0 COMMENT '任务状态,0,等待下载,1,正在下载,'
)ENGINE=INNODB DEFAULT CHARSET=utf8;





