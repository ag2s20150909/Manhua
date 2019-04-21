/**
 *
 */
package cn.jony.okhttpplus.lib.httpdns.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.jony.okhttpplus.lib.httpdns.model.HostIP;
import cn.jony.okhttpplus.lib.httpdns.util.IOUtils;

public class DNSCacheDatabaseHelper extends SQLiteOpenHelper implements DBConstants {
    private static final String TAG = DNSCacheDatabaseHelper.class.getSimpleName();
    /**
     * 资源锁
     */
    private static final byte synLock[] = new byte[1];

    /**
     * 构造函数
     *
     * @param context
     */
    public DNSCacheDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 创建数据库
     * <p>
     * 残酷的现实告诉我们，创建多个表时，要分开多次执行db.execSQL方法！！
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB", "onCreate");
        db.execSQL(CREATE_IP_TEBLE_SQL);
        db.execSQL(CREATE_HOST_INDEX_SQL);
    }

    /**
     * 数据库版本更新策略（直接放弃旧表）
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB", "onUpgrade");
        if (oldVersion != newVersion) {
            // 其它情况，直接放弃旧表.
            db.beginTransaction();
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_IP + ";");
            db.setTransactionSuccessful();
            db.endTransaction();
            onCreate(db);
        }
    }

    /**
     * 根据域名id 删除域名相关信息
     */
    private void deleteHost(String hostname) {
        synchronized (synLock) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                db.delete(TABLE_IP, COLUMN_HOST + " = ?", new String[]{hostname});
            } catch (Exception e) {
                error(e);
            } finally {
                IOUtils.closeQuietly(db);
            }
        }
    }

    /**
     * 根据 targetId 删除数据
     */
    private void deleteTargetId(String targetId) {
        synchronized (synLock) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                db.delete(TABLE_IP, COLUMN_TARGET_IP + " = ?", new String[]{targetId});
            } catch (Exception e) {
                error(e);
            } finally {
                db.close();
            }
        }
    }

    private void error(Exception e) {
        Log.e(TAG, Log.getStackTraceString(e));
    }

    /**
     * 清除缓存数据
     */
    public void clear() {
        synchronized (synLock) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                db.delete(TABLE_IP, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
    }

    public HostIP getIPByID(String sourceId, String targetId) {
        synchronized (synLock) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM ");
            sql.append(TABLE_IP);
            sql.append(" where ").append(COLUMN_SOURCE_IP).append(" = ? ");
            sql.append(" and ").append(COLUMN_TARGET_IP).append(" = ? ");
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = null;
            try {
                cursor = db.rawQuery(sql.toString(), new String[]{sourceId, targetId});
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    return ipFromDB(cursor);
                }
            } catch (Exception e) {
                error(e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                db.close();
            }

            return null;
        }
    }

    public List<HostIP> getIPByHostAndSourceIP(String hostname, String sourceIP) {
        synchronized (synLock) {
            List<HostIP> list = new ArrayList<>();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM ");
            sql.append(TABLE_IP);
            sql.append(" where ").append(COLUMN_HOST).append(" = ? ");
            sql.append(" and ").append(COLUMN_SOURCE_IP).append(" = ?");
            sql.append("order by ").append(COLUMN_FAIL_NUM).append(" asc, ").append(COLUMN_RTT)
                    .append(" asc, ").append(COLUMN_TTL).append(" asc;");

            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = null;
            try {
                cursor = db.rawQuery(sql.toString(), new String[]{hostname, sourceIP});
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        list.add(ipFromDB(cursor));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                error(e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }

                IOUtils.closeQuietly(db);
            }
            return list;
        }
    }

    public List<HostIP> getAllIP() {
        synchronized (synLock) {
            List<HostIP> list = new ArrayList<>();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM ");
            sql.append(TABLE_IP);
            sql.append(" ; ");

            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = null;
            try {
                cursor = db.rawQuery(sql.toString(), null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        list.add(ipFromDB(cursor));
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                error(e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                IOUtils.closeQuietly(db);
            }
            return list;
        }
    }

    /**
     * @param ipList
     * @return 添加成功数目
     */
    public int addIPList(List<HostIP> ipList) {
        int suc = 0;
        synchronized (synLock) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                for (HostIP ip : ipList) {
                    try {
                        suc += db.insert(TABLE_IP, null, ip2ContentValues(ip)) >= 0 ? 1 : 0;
                    } catch (Exception e) {
                        error(e);
                    }
                }
            } finally {
                IOUtils.closeQuietly(db);
            }
        }
        return suc;
    }

    /**
     * 更新数据库中的一条ip记录
     */
    public void updateIp(HostIP ip) {
        synchronized (synLock) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                StringBuilder where = new StringBuilder();
                where.append(COLUMN_TARGET_IP);
                where.append(" = ? ").append(" and ");
                where.append(COLUMN_SOURCE_IP).append("= ?;");
                String[] args = new String[]{ip.targetIP, ip.sourceIP};
                db.update(TABLE_IP, ip2ContentValues(ip), where.toString(), args);
            } finally {
                IOUtils.closeQuietly(db);
            }

        }
    }

    public void updateIpList(List<HostIP> ipList) {
        synchronized (synLock) {
            SQLiteDatabase db = getWritableDatabase();
            try {
                StringBuilder where = new StringBuilder();
                where.append(COLUMN_TARGET_IP);
                where.append(" = ? ").append(" and ");
                where.append(COLUMN_SOURCE_IP).append("= ?;");
                for (HostIP ip : ipList) {
                    String[] args = new String[]{ip.targetIP, ip.sourceIP};
                    db.update(TABLE_IP, ip2ContentValues(ip), where.toString(), args);
                }
            } catch (Exception e) {
                error(e);
            } finally {
                IOUtils.closeQuietly(db);
            }
        }
    }

    private static HostIP ipFromDB(Cursor cursor) {
        HostIP ip = new HostIP();
        ip.targetIP = cursor.getString(cursor.getColumnIndex(COLUMN_TARGET_IP));
        ip.sourceIP = cursor.getString(cursor.getColumnIndex(COLUMN_SOURCE_IP));
        ip.host = cursor.getString(cursor.getColumnIndex(COLUMN_HOST));
        ip.saveMillis = cursor.getLong(cursor.getColumnIndex(COLUMN_SAVE_MILLIS));
        ip.rtt = cursor.getLong(cursor.getColumnIndex(COLUMN_RTT));
        ip.operator = cursor.getString(cursor.getColumnIndex(COLUMN_OPERATOR));
        ip.ttl = cursor.getInt(cursor.getColumnIndex(COLUMN_TTL));
        ip.setSucNum(cursor.getInt(cursor.getColumnIndex(COLUMN_SUCCESS_NUM)));
        ip.setFailNum(cursor.getInt(cursor.getColumnIndex(COLUMN_FAIL_NUM)));
        ip.visitSinceSaved = cursor.getInt(cursor.getColumnIndex(COLUMN_VISIT_NUM));

        return ip;
    }

    private static ContentValues ip2ContentValues(HostIP ip) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TARGET_IP, ip.targetIP);
        cv.put(COLUMN_SOURCE_IP, ip.sourceIP);
        cv.put(COLUMN_OPERATOR, ip.operator);
        cv.put(COLUMN_SAVE_MILLIS, ip.saveMillis);
        cv.put(COLUMN_TTL, ip.ttl);

        cv.put(COLUMN_RTT, ip.rtt);
        cv.put(COLUMN_SUCCESS_NUM, ip.getSucNum());
        cv.put(COLUMN_FAIL_NUM, ip.getFailNum());
        cv.put(COLUMN_HOST, ip.host);
        cv.put(COLUMN_VISIT_NUM, ip.visitSinceSaved);
        return cv;
    }
}
