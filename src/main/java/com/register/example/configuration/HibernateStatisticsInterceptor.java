package com.register.example.configuration;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;

@Slf4j
public class HibernateStatisticsInterceptor extends EmptyInterceptor {

    private ThreadLocal<Long> queryCount = new ThreadLocal<>();

    public void startCounter() {
        queryCount.set(0L);
    }

    public Long getQueryCount() {
        return queryCount.get();
    }

    public void clearCounter() {
        queryCount.remove();
    }

    @Override
    public String onPrepareStatement(String sql) {
        Long count = queryCount.get();
        if (count != null) {
            queryCount.set(count + 1);
        }
        if(count!=null)
            log.info((count+1)+"Wywołanie sql ="+sql);
        else log.info(sql);
        return super.onPrepareStatement(sql);
    }
}

