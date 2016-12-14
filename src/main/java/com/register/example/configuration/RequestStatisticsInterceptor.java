package com.register.example.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class RequestStatisticsInterceptor implements AsyncHandlerInterceptor {
    private ThreadLocal<Long> time = new ThreadLocal<>();


    @Autowired
    private HibernateStatisticsStopWatch hibernateStatisticsStopWatch;

    @Autowired
    private HibernateStatisticsInterceptor statisticsInterceptor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        time.set(System.currentTimeMillis());
        statisticsInterceptor.startCounter();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Long queryCount = statisticsInterceptor.getQueryCount();
        if (modelAndView != null) {
            modelAndView.addObject("_queryCount", queryCount);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long duration = System.currentTimeMillis() - time.get();
        Long queryCount = statisticsInterceptor.getQueryCount();
        statisticsInterceptor.clearCounter();
        time.remove();
        String authentication = "nie zalogowany";
        if (request.getRemoteUser() != null && request.getRemoteUser().length() > 0) {
            authentication = request.getRemoteUser();
        }
        log.info(String.valueOf(
                "Wczytanych encji dotychczas = " + hibernateStatisticsStopWatch.getObject().getEntityLoadCount()));
        log.info(String.valueOf("Wykonanych zapytan dotychczas = " + hibernateStatisticsStopWatch.getObject()
                .getQueryExecutionCount()));
        log.info(String.valueOf("Transakcji zakonczonych dotychczas = " + hibernateStatisticsStopWatch.getObject()
                .getTransactionCount()));
        log.info("[Time: {} ms] [Queries: {}] [Authentication: {}] {} {}", duration, queryCount, authentication,
                request.getMethod(), request.getRequestURI());
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //concurrent handling cannot be supported here
        statisticsInterceptor.clearCounter();
        time.remove();
    }
}