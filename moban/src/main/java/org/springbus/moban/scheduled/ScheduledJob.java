package org.springbus.moban.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springbus.moban.web.LoginUserCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
//ScheduledJob 类的主要作用是通过定时任务的方式定期清理不再活跃的用户缓存。这有助于减少内存占用，确保缓存中的数据都是最新的和有效的。
public class ScheduledJob {

//这是一个定时任务注解，使用 CRON 表达式来指定任务的执行频率。
    @Scheduled(cron = "0 0/5 * * * ? *")
    public void cacheUserInfoJob(){
        log.info("开始执行用户缓存清理");
        log.info("*************************************************");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currDate = sdf.format(new Date());
        long time = sdf.parse(currDate, new ParsePosition(0)).getTime();
        for (String key : LoginUserCache.GUAVA_CACHE.keySet()) {
            Date activityTime = LoginUserCache.GUAVA_CACHE.get(key).getActivityTime();
            if (time-activityTime.getTime()>30*1000*60){
                LoginUserCache.GUAVA_CACHE.remove(key);
            }
        }
        log.info("用户缓存清理执行完毕");
    }

}
