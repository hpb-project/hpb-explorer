
-- Dumping database structure for hpbscan
CREATE DATABASE IF NOT EXISTS `hpbscan` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `hpbscan`;

-- Dumping structure for table hpbscan.address_erc_holder
CREATE TABLE IF NOT EXISTS `address_erc_holder` (
  `address` varchar(66) DEFAULT NULL COMMENT '账户地址',
  `contract_address` varchar(66) DEFAULT NULL COMMENT '代币智能合约地址',
  `balance_amount` varchar(32) DEFAULT NULL COMMENT '持币余额',
  `create_timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.addrs
CREATE TABLE IF NOT EXISTS `addrs` (
  `address` varchar(45) NOT NULL,
  `number` bigint(20) unsigned NOT NULL ,
  `start_block` bigint(20) unsigned DEFAULT NULL ,
  `lastest_block` bigint(20) unsigned DEFAULT NULL ,
  `from_count` bigint(20) unsigned DEFAULT NULL ,
  `to_count` bigint(20) unsigned DEFAULT NULL ,
  `account_type` int(2) unsigned DEFAULT '0' ,
  `balance` decimal(65,0) unsigned DEFAULT NULL,
  `create_timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`address`),
  UNIQUE KEY `addrs_ix1` (`number`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户地址信息表';

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.api_key
CREATE TABLE IF NOT EXISTS `api_key` (
  `id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `api_key` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `private_key` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `usage_limit` bigint(10) DEFAULT NULL ,
  `app_name` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `registration_date` datetime DEFAULT NULL ,
  `activation_date` datetime DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `level_str` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `first_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `company` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `website` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sector` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deprecation_date` datetime DEFAULT NULL,
  `lastaccess_date` datetime DEFAULT NULL,
  `usage_number` bigint(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.block_addrs_1
CREATE TABLE IF NOT EXISTS `block_addrs_1` (
  `b_number` bigint(20) NOT NULL ,
  `block_hash` varchar(128) DEFAULT NULL,
  `block_timestamp` bigint(20) DEFAULT NULL,
  `addrs` mediumtext NOT NULL ,
  `txcount` int(11) NOT NULL ,
  PRIMARY KEY (`b_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='区块账户信息表';

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.block_big_record
CREATE TABLE IF NOT EXISTS `block_big_record` (
  `block_number` bigint(20) NOT NULL,
  `block_hash` varchar(128) DEFAULT NULL,
  `tx_count` bigint(20) DEFAULT NULL,
  `block_info_json_str` text,
  PRIMARY KEY (`block_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.block_max_size
CREATE TABLE IF NOT EXISTS `block_max_size` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `block_number` bigint(20) DEFAULT NULL,
  `max_size` decimal(28,4) DEFAULT NULL,
  `create_timestamp` datetime DEFAULT NULL,
  `update_timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `block_max_size` (`id`, `block_number`, `max_size`, `create_timestamp`, `update_timestamp`) VALUES
	(1, 3578800, 2412733971.0000, NULL, '2019-09-24 17:08:57');

CREATE TABLE IF NOT EXISTS `hpb_node` (
  `id` int(11) NOT NULL AUTO_INCREMENT  ,
  `node_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL ,
  `node_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL ,
  `node_type_description` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bandwidth` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL ,
  `node_address` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL ,
  `lock_amount` decimal(18,8) DEFAULT '0.00000000',
  `country` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL ,
  `country_description` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `poll_all` bigint(20) DEFAULT NULL ,
  `location_detail` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `location_detail_description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL ,
  `node_status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL ,
  `create_time` datetime DEFAULT NULL ,
  `update_time` datetime DEFAULT NULL ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- Data exporting was unselected.

-- Dumping structure for table hpbscan.common_dictionary
CREATE TABLE IF NOT EXISTS `common_dictionary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) DEFAULT NULL,
  `label` varchar(64) DEFAULT NULL,
  `group_name` varchar(32) DEFAULT NULL,
  `group_description` varchar(64) DEFAULT NULL,
  `display_index` int(11) DEFAULT NULL,
  `parent_code` varchar(32) DEFAULT NULL,
  `resource_key` varchar(128) DEFAULT NULL,
  `is_visible` tinyint(1) DEFAULT NULL,
  `is_predefined` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

INSERT INTO `common_dictionary` (`id`, `code`, `label`, `group_name`, `group_description`, `display_index`, `parent_code`, `resource_key`, `is_visible`, `is_predefined`) VALUES
	(1, 'USA', '美国', 'nationality', '国籍', 2, NULL, NULL, 1, NULL),
	(2, 'China', '中国', 'nationality', '国籍', 1, NULL, NULL, 1, NULL),
	(3, 'UK', '英国', 'nationality', '国籍', 3, NULL, NULL, 1, NULL),
	(5, 'hpbnode', '高性能', 'nodetype', '节点类型', 1, NULL, NULL, 1, NULL),
	(6, 'prenode', '候选', 'nodetype', '节点类型', 2, NULL, NULL, 1, NULL),
	(7, 'Thailand', '泰国', 'nationality', '国籍', 5, NULL, NULL, 1, NULL),
	(8, 'Germany ', '德国', 'nationality', '国籍', 7, NULL, NULL, 1, NULL),
	(9, 'Canada', '加拿大', 'nationality', '国籍', 6, NULL, NULL, 1, NULL),
	(10, 'Norway ', '挪威', 'nationality', '国籍', 8, NULL, NULL, 1, NULL),
	(11, 'Singapore', '新加坡', 'nationality', '国籍', 9, NULL, NULL, 1, NULL),
	(12, 'New Zealand', '新西兰', 'nationality', '国籍', 10, NULL, NULL, 1, NULL),
	(13, 'Sweden', '瑞士', 'nationality', '国籍', 11, NULL, NULL, 1, NULL),
	(14, 'Malaysia', '马来西亚', 'nationality', '国籍', 12, NULL, NULL, 1, NULL),
	(15, 'Kazakhstan', '哈萨克斯坦', 'nationality', '国籍', 13, NULL, NULL, 1, NULL),
	(16, 'Netherlands', '荷兰', 'nationality', '国籍', 14, NULL, NULL, 1, NULL);
-- Data exporting was unselected.

-- Dumping structure for table hpbscan.contract_erc_profile_summary
CREATE TABLE IF NOT EXISTS `contract_erc_profile_summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `token_symbol_image_url` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '代币图片地址',
  `token_name` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '代币名称',
  `contract_address` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '合约地址',
  `contract_type` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '合约类型',
  `profile_en` text COLLATE utf8_bin COMMENT '介绍_英文',
  `profile_zh` text CHARACTER SET utf8mb4 COMMENT '介绍_中文',
  `official_site` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '官方网站地址',
  `email` varchar(256) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'email',
  `facebook` text CHARACTER SET utf8mb4 COMMENT 'facebook',
  `twitter` text CHARACTER SET utf8mb4 COMMENT '推特',
  `weibo` text CHARACTER SET utf8mb4 COMMENT '微博',
  `github` text CHARACTER SET utf8mb4 COMMENT 'github',
  `telegram` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_contact_address` (`contract_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.contract_erc_profile_summary_approve
CREATE TABLE IF NOT EXISTS `contract_erc_profile_summary_approve` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `token_symbol_image_url` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '代币图片地址',
  `token_name` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '代币名称',
  `contract_address` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '合约地址',
  `contract_type` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '合约类型',
  `profile_en` text COLLATE utf8_bin COMMENT '介绍_英文',
  `profile_zh` text CHARACTER SET utf8mb4 COMMENT '介绍_中文',
  `official_site` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '官方网站地址',
  `email` varchar(256) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'email',
  `facebook` text CHARACTER SET utf8mb4 COMMENT 'facebook',
  `twitter` text CHARACTER SET utf8mb4 COMMENT '推特',
  `weibo` text CHARACTER SET utf8mb4 COMMENT '微博',
  `github` text CHARACTER SET utf8mb4 COMMENT 'github',
  `telegram` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
  `approve_status` varchar(12) CHARACTER SET utf8 DEFAULT '' COMMENT '审批状态,T 待审批， Y 审批通过，N审批失败',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_contact_address` (`contract_address`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.contract_erc_standard_info
CREATE TABLE IF NOT EXISTS `contract_erc_standard_info` (
  `id` varchar(32) NOT NULL,
  `token_symbol` varchar(128) DEFAULT NULL COMMENT 'token',
  `token_symbol_image_url` varchar(128) DEFAULT NULL ,
  `token_name` varchar(128) DEFAULT NULL ,
  `decimals` bigint(32) DEFAULT NULL ,
  `deploy_tx_hash` varchar(128) DEFAULT NULL,
  `contract_creater` varchar(128) DEFAULT NULL,
  `contract_address` varchar(128) DEFAULT NULL ,
  `token_total_supply` bigint(32) DEFAULT NULL,
  `contract_type` varchar(128) DEFAULT NULL,
  `verified_status` varchar(128) DEFAULT NULL ,
  `price` varchar(45) DEFAULT NULL ,
  `change_rate` varchar(45) DEFAULT NULL ,
  `volume_24h` varchar(45) DEFAULT NULL ,
  `market_cap` decimal(32,8) DEFAULT NULL ,
  `holders` int(11) DEFAULT NULL  ,
  `transfers_num` int(11) DEFAULT NULL,
  `status` varchar(32) DEFAULT NULL ,
  `create_timestamp` datetime DEFAULT NULL,
  `update_timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_tx_hash` (`deploy_tx_hash`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.contract_event_info
CREATE TABLE IF NOT EXISTS `contract_event_info` (
  `id` varchar(32) NOT NULL,
  `contract_addr` varchar(42) NOT NULL ,
  `event_hash` varchar(128) DEFAULT NULL ,
  `event_name` varchar(256) DEFAULT NULL ,
  `event_abi` text,
  `create_timestamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.contract_info
CREATE TABLE IF NOT EXISTS `contract_info` (
  `contract_addr` varchar(42) NOT NULL ,
  `contract_creater` varchar(42) NOT NULL ,
  `contract_name` varchar(32) DEFAULT NULL ,
  `contract_src` text ,
  `contract_abi` text ,
  `contract_bin` text ,
  `contract_type` varchar(128) DEFAULT NULL ,
  `optimize_flag` varchar(32) DEFAULT NULL ,
  `balance` decimal(32,8) DEFAULT NULL ,
  `tx_count` bigint(20) DEFAULT NULL ,
  `verified_status` varchar(32) DEFAULT NULL,
  `dapp_url` varchar(128) DEFAULT NULL ,
  `misc_setting_runs` bigint(20) DEFAULT NULL ,
  `hvm_version` varchar(32) DEFAULT NULL ,
  `compiler_type` varchar(256) DEFAULT NULL ,
  `compiler_version` varchar(256) DEFAULT NULL,
  `create_timestamp` timestamp NULL DEFAULT NULL ,
  `verified_timestamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`contract_addr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;


-- Data exporting was unselected.

-- Dumping structure for table hpbscan.contract_method_info
CREATE TABLE IF NOT EXISTS `contract_method_info` (
  `id` varchar(32) NOT NULL,
  `contract_addr` varchar(42) NOT NULL ,
  `method_id` varchar(10) DEFAULT NULL ,
  `method_name` varchar(256) DEFAULT NULL ,
  `method_abi` text,
  `create_timestamp` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;


-- Data exporting was unselected.

-- Dumping structure for table hpbscan.feedback
CREATE TABLE IF NOT EXISTS `feedback` (
  `id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contact_information` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL ,
  `feedback_content` varchar(4096) COLLATE utf8mb4_unicode_ci DEFAULT NULL ,
  `feedback_status` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL ,
  `create_timestamp` datetime DEFAULT NULL,
  `create_by` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `update_timestamp` datetime DEFAULT NULL,
  `update_by` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='意见反馈';

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.hpb_campaign_period_vote_result
CREATE TABLE IF NOT EXISTS `hpb_campaign_period_vote_result` (
  `id` varchar(32) NOT NULL,
  `node_name` varchar(128) NOT NULL,
  `node_address` varchar(64) DEFAULT NULL,
  `vote_round` int(11) NOT NULL,
  `rank_order` int(11) NOT NULL,
  `vote_total_amount` varchar(24) NOT NULL,
  `create_timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.hpb_instant_price
CREATE TABLE IF NOT EXISTS `hpb_instant_price` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cny_price` varchar(60) DEFAULT NULL,
  `usd_price` varchar(60) DEFAULT '' ,
  `change_percent` varchar(60) DEFAULT NULL ,
  `update_time` datetime DEFAULT NULL ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='最新更新时间';

INSERT INTO `hpb_instant_price` (`id`, `cny_price`, `usd_price`, `change_percent`, `update_time`) VALUES
	(1, '1.2022', '0.1718', '-12.41%', '2020-02-07 23:28:00');

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.hpb_market_price
CREATE TABLE IF NOT EXISTS `hpb_market_price` (
  `price_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `price` decimal(10,2) DEFAULT NULL COMMENT '人民币价格',
  `price_usd` decimal(10,2) DEFAULT NULL COMMENT '美元价格',
  `usd_rate` decimal(10,2) DEFAULT NULL COMMENT '人民币对美元汇率',
  `price_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '价格记录时间',
  `market_cap` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '市值',
  `volume_24h` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '24小时流通量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`price_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

INSERT INTO `hpb_market_price` (`price_id`, `price`, `price_usd`, `usd_rate`, `price_time`, `market_cap`, `volume_24h`, `create_time`, `update_time`, `remark`) VALUES
	(1, 0.89, 0.13, 0.00, '2020-01-31 00:00:00', NULL, '138906', '2020-01-31 00:00:00', '2020-01-31 00:00:00', NULL),
	(2, 1.20, 0.17, 6.97, '2020-02-07 23:26:23', NULL, '46464', '2020-02-07 23:26:23', '2020-02-07 23:26:23', NULL);

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.hpb_node_reward_record
CREATE TABLE IF NOT EXISTS `hpb_node_reward_record` (
  `node_address` varchar(64) DEFAULT NULL ,
  `node_name` varchar(128) DEFAULT NULL,
  `total_vote_amount` varchar(255) DEFAULT NULL ,
  `balance` decimal(32,18) DEFAULT NULL,
  `node_type` varchar(32) DEFAULT NULL ,
  `node_mint_reward_amount` decimal(32,18) DEFAULT NULL ,
  `node_vote_reward_amount` decimal(32,18) DEFAULT NULL ,
  `node_total_reward_amount` decimal(32,18) DEFAULT NULL,
  `reward_vote_percent_rate` decimal(32,18) DEFAULT NULL,
  `block_number` bigint(20) DEFAULT NULL,
  `block_hash` varchar(128) DEFAULT NULL,
  `block_timestamp` bigint(32) DEFAULT NULL,
  `create_timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `idx_node_address_bknum` (`node_address`,`block_number`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.stat_transaction_amount_daily
CREATE TABLE IF NOT EXISTS `stat_transaction_amount_daily` (
  `id` varchar(32) NOT NULL,
  `transaction_amount` bigint(20) NOT NULL,
  `transaction_date` datetime DEFAULT NULL ,
  `create_timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.tx_internal_record
CREATE TABLE IF NOT EXISTS `tx_internal_record` (
  `tx_hash` varchar(66) DEFAULT NULL,
  `block_hash` varchar(66) DEFAULT NULL,
  `block_number` bigint(22) DEFAULT NULL,
  `block_timestamp` bigint(20) DEFAULT NULL,
  `contract_address` varchar(66) DEFAULT NULL,
  `from_address` varchar(66) DEFAULT NULL,
  `to_address` varchar(66) DEFAULT NULL,
  `quantity` varchar(128) DEFAULT NULL,
  `depth` varchar(64) NOT NULL COMMENT 'evmlog 深度',
  `log_id` varchar(64) DEFAULT NULL COMMENT 'evmlog id',
  `token_type` varchar(64) DEFAULT NULL,
  `gaslimit` varchar(45) DEFAULT NULL COMMENT 'gaslimit',
  `create_timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  KEY `tx_idx` (`tx_hash`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.

-- Dumping structure for table hpbscan.tx_transfer_record
CREATE TABLE IF NOT EXISTS `tx_transfer_record` (
  `tx_hash` varchar(66) DEFAULT NULL,
  `block_hash` varchar(66) DEFAULT NULL,
  `block_number` bigint(22) DEFAULT NULL,
  `block_timestamp` bigint(20) DEFAULT NULL,
  `contract_address` varchar(66) DEFAULT NULL,
  `from_address` varchar(66) DEFAULT NULL,
  `to_address` varchar(66) DEFAULT NULL,
  `quantity` varchar(32) DEFAULT NULL,
  `token_type` varchar(64) DEFAULT NULL,
  `log_index` bigint(12) DEFAULT NULL,
  `token_id` bigint(12) DEFAULT NULL,
  `create_timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Data exporting was unselected.