package org.apache.skywalking.apm.plugin.jdk.executor.relevant;

import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;
import org.apache.skywalking.apm.plugin.jdk.executor.relevant.wrapper.RunnableOrCallableWrapper;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class ExecutorServiceInterceptor implements InstanceMethodsAroundInterceptor {
    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
        if (allArguments == null || allArguments.length != 1) {
            return;
        }
        Object parameter = allArguments[0];
        if (!(parameter instanceof RunnableOrCallableWrapper)) {
            if (parameter instanceof Runnable) {
                allArguments[0] = new RunnableOrCallableWrapper((Runnable) parameter);
            }
            if (parameter instanceof Callable) {
                allArguments[0] = new RunnableOrCallableWrapper((Callable) parameter);
            }
        }
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
