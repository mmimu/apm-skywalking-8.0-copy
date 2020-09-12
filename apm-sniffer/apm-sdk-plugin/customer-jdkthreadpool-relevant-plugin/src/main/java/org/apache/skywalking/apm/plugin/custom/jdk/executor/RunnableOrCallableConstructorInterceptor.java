package org.apache.skywalking.apm.plugin.custom.jdk.executor;

import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceConstructorInterceptor;

public class RunnableOrCallableConstructorInterceptor implements InstanceConstructorInterceptor {
    @Override
    public void onConstruct(EnhancedInstance objInst, Object[] allArguments) {
        if (ContextManager.isActive()) {
            objInst.setSkyWalkingDynamicField(ContextManager.capture());
        }
    }
}
