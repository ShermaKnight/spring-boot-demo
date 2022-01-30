package org.example.service;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.handler.ServiceException;
import org.springframework.stereotype.Service;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Service
public class ConfigureServiceImpl implements ConfigureService {

    private static final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("longPolling-timeout-checker-%d").build();

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, threadFactory);

    private static final Multimap<String, AsyncTask> dataContext = Multimaps.synchronizedSetMultimap(HashMultimap.create());

    @Override
    public void getConfigure(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        String serviceName = servletRequest.getParameter("serviceName");
        if (StringUtils.isEmpty(serviceName)) {
            throw new ServiceException(1001, "参数serviceName不允许为空");
        }
        AsyncContext asyncContext = servletRequest.startAsync(servletRequest, servletResponse);
        AsyncTask asyncTask = new AsyncTask(asyncContext, true);
        dataContext.put(serviceName, asyncTask);
        executorService.schedule(() -> {
            if (asyncTask.isTimeout()) {
                if (dataContext.remove(serviceName, asyncTask)) {
                    servletResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    asyncTask.getAsyncContext().complete();
                }
            }
        }, 30, TimeUnit.SECONDS);
    }

    @Data
    private static class AsyncTask {

        private AsyncContext asyncContext;
        private boolean timeout;

        public AsyncTask(AsyncContext asyncContext, boolean timeout) {
            this.asyncContext = asyncContext;
            this.timeout = timeout;
        }
    }

    @Override
    @SneakyThrows
    public void configureChanged(String serviceName, List<String> data) {
        if (StringUtils.isEmpty(serviceName) || CollectionUtils.isEmpty(data)) {
            return;
        }
        Collection<AsyncTask> asyncTasks = dataContext.get(serviceName);
        if (CollectionUtils.isEmpty(asyncTasks)) {
            return;
        }
        String information = JSONArray.toJSONString(data);
        for (AsyncTask asyncTask : asyncTasks) {
            asyncTask.setTimeout(false);
            HttpServletResponse response = (HttpServletResponse) asyncTask.getAsyncContext().getResponse();
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(information);
            asyncTask.getAsyncContext().complete();
        }
    }
}
