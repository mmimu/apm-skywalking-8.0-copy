package org.apache.skywalking.jdk.threadpool.relevant;

import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;
import org.apache.skywalking.jdk.threadpool.relevant.wrapper.CustomRunnableWrapper;

import java.lang.reflect.Method;

public class ThreadPoolRunnableInterceptor implements InstanceMethodsAroundInterceptor {
    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
        if (!ContextManager.isActive()) {
            return;
        }
        if (allArguments == null || allArguments.length != 1) {
            return;
        }
        Object parameter = allArguments[0];
        if (!(parameter instanceof Runnable)) {
            return;
        }
        allArguments[0] = new CustomRunnableWrapper((Runnable) parameter, ContextManager.capture());
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret) throws Throwable {
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t) {
        if (ContextManager.isActive()) {
            ContextManager.activeSpan().errorOccurred().log(t);
        }
    }

}
