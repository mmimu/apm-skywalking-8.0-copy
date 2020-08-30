package org.apache.skywalking.jdk.threadpool.relevant;

import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.ContextSnapshot;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractSpan;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;
import org.apache.skywalking.apm.network.trace.component.ComponentsDefine;

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
        Object allArgument = allArguments[0];
        if (!(allArgument instanceof Runnable)) {
            return;
        }
        AbstractSpan span = ContextManager.createLocalSpan(generateOperationName(objInst, method));
        span.setComponent(ComponentsDefine.JDK_THREADING);
        final Object storedField = objInst.getSkyWalkingDynamicField();
        if (storedField != null) {
            final ContextSnapshot contextSnapshot = (ContextSnapshot) storedField;
            ContextManager.continued(contextSnapshot);
        }
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object ret) throws Throwable {
        final Object storedField = objInst.getSkyWalkingDynamicField();
        if (storedField != null) {
            ContextManager.stopSpan();
        }
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t) {
        if (ContextManager.isActive()) {
            ContextManager.activeSpan().errorOccurred().log(t);
        }
    }

    private String generateOperationName(final EnhancedInstance objInst, final Method method) {
        return "threadpool/" + objInst.getClass().getName() + "/" + method.getName();
    }
}
