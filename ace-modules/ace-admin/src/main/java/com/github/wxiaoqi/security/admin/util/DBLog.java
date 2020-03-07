package com.github.wxiaoqi.security.admin.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.github.wxiaoqi.security.admin.biz.base.GateLogBiz;
import com.github.wxiaoqi.security.common.entity.admin.GateLog;

import lombok.extern.slf4j.Slf4j;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-07-01 15:28
 */
@Slf4j
public class DBLog extends Thread {
    private static DBLog dblog = null;
    private static BlockingQueue<GateLog> logInfoQueue = new LinkedBlockingQueue<GateLog>(1024);


    private GateLogBiz logService;

    public GateLogBiz getLogService() {
        return logService;
    }

    public DBLog setLogService(GateLogBiz logService) {
        if(this.logService==null) {
            this.logService = logService;
        }
        return this;
    }
    public static synchronized DBLog getInstance() {
        if (dblog == null) {
            dblog = new DBLog();
        }
        return dblog;
    }

    private DBLog() {
        super("CLogOracleWriterThread");
    }

    public void offerQueue(GateLog logInfo) {
        try {
            logInfoQueue.offer(logInfo);
//            log.error("日志写入成功",logInfo);
        } catch (Exception e) {
            log.error("日志写入失败", e);
        }
    }

    @Override
    public void run() {
        List<GateLog> bufferedLogList = new ArrayList<GateLog>(); // 缓冲队列
        while (true) {
            try {
                bufferedLogList.add(logInfoQueue.take());
                logInfoQueue.drainTo(bufferedLogList);
                if (bufferedLogList != null && bufferedLogList.size() > 0) {
                    // 写入日志
                    for(GateLog log:bufferedLogList){
                        logService.insertSelective(log);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 防止缓冲队列填充数据出现异常时不断刷屏
                try {
                    Thread.sleep(1000);
                } catch (Exception eee) {
                }
            } finally {
                if (bufferedLogList != null && bufferedLogList.size() > 0) {
                    try {
                        bufferedLogList.clear();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}