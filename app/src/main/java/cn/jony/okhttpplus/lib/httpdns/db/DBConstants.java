package cn.jony.okhttpplus.lib.httpdns.db;

public interface DBConstants {
    /**
     * db 数据库名字
     */
    String DATABASE_NAME = "dns_cache_info.db";
    /**
     * db 版本
     */
    int DATABASE_VERSION = 1;

    /**
     * ip表名称、列名定义
     */
    String TABLE_IP = "ip";
    /**
     * 域名
     */
    String COLUMN_HOST = "host";
    /**
     * 运营商
     */
    String COLUMN_OPERATOR = "operator";
    /**
     * source ip
     */
    String COLUMN_SOURCE_IP = "source_ip";
    /**
     * target ip
     */
    String COLUMN_TARGET_IP = "target_ip";
    /**
     * save time in millis
     */
    String COLUMN_SAVE_MILLIS = "save_millis";
    /**
     * ttl
     */
    String COLUMN_TTL = "ttl";
    /**
     * ip服务器访问延时时间(可用ping或http发送空包实现)。单位ms
     */
    String COLUMN_RTT = "rtt";
    /**
     * ip服务器链接产生的成功数
     */
    String COLUMN_SUCCESS_NUM = "success_num";
    /**
     * ip服务器链接产生的错误数
     */
    String COLUMN_FAIL_NUM = "fail_num";
    /**
     * ip服务访问数
     */
    String COLUMN_VISIT_NUM = "visit_num";

    /**
     * 创建 ip 表 sql 语句
     */
    String CREATE_IP_TEBLE_SQL =
            "CREATE TABLE " + TABLE_IP + " (" +
                    COLUMN_TARGET_IP + " TEXT," +
                    COLUMN_HOST + " TEXT," +
                    COLUMN_SOURCE_IP + " TEXT," +
                    COLUMN_OPERATOR + " TEXT," +
                    COLUMN_TTL + " INTEGER," +
                    COLUMN_RTT + " LONG," +
                    COLUMN_SAVE_MILLIS + " LONG," +
                    COLUMN_SUCCESS_NUM + " INTEGER," +
                    COLUMN_FAIL_NUM + " INTEGER," +
                    COLUMN_VISIT_NUM + " INTEGER," +
                    "PRIMARY KEY(" + COLUMN_TARGET_IP + "," + COLUMN_SOURCE_IP + ")" +
                    ");";

    String CREATE_HOST_INDEX_SQL = "CREATE INDEX idx_host_sourceId ON " + TABLE_IP +
            "(" + COLUMN_HOST + "," + COLUMN_SOURCE_IP + ");";

}
