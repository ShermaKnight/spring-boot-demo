package org.example.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.example.annotation.Log;

import java.lang.reflect.Method;
import java.util.Optional;

public class LogInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Log log = method.getAnnotation(Log.class);
        if (Optional.ofNullable(log).isPresent()) {
            System.out.println("Before invocation, method name " + method.getName() + ", get annotation value: " + log.value());
        }
        Object proceed = invocation.proceed();
        return proceed;
    }
}
