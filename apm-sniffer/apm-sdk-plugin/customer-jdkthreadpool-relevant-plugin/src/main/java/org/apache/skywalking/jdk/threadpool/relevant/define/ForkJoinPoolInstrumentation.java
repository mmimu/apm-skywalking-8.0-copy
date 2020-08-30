package org.apache.skywalking.jdk.threadpool.relevant.define;

import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;

public class ForkJoinPoolInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {
    private static final String ENHANCE_CLASS = "java.util.concurrent.ForkJoinPool";
    private static final String ENHANCE_EXECUTE_METHOD = "execute";
    private static final String ENHANCE_SUBMIT_METHOD = "submit";
    private static final String INTERCEPTOR_CLASS = "org.apache.skywalking.jdk.threadpool.relevant.ForkJoinPoolInterceptor";
    @Override
    protected ClassMatch enhanceClass() {
        return null;
    }

    @Override
    public ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override
    public InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[0];
    }
}
