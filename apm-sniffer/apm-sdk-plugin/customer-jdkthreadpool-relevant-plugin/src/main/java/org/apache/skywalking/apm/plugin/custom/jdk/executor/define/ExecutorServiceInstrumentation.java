package org.apache.skywalking.apm.plugin.custom.jdk.executor.define;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.StaticMethodsInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.HierarchyMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.IndirectMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.logical.LogicalMatchOperation;
import org.apache.skywalking.apm.plugin.custom.jdk.executor.config.ThreadingConfig;

import java.util.concurrent.Callable;

public class ExecutorServiceInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {
    private static final String ENHANCE_CLASS = "java.util.concurrent.ExecutorService";
    private static final String ENHANCE_EXECUTE_METHOD = "execute";
    private static final String ENHANCE_SUBMIT_METHOD = "submit";
    private static final String INTERCEPTOR_CLASS = "org.apache.skywalking.apm.plugin.custom.jdk.executor.ExecutorServiceInterceptor";

    @Override
    protected ClassMatch enhanceClass() {
        IndirectMatch indirectMatch = ThreadingConfig.prefixesMatchesForJdkThreading();
        if (indirectMatch == null) {
            return null;
        }
        return LogicalMatchOperation.and(HierarchyMatch.byHierarchyMatch(ENHANCE_CLASS));
    }

    @Override
    public ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return new ConstructorInterceptPoint[0];
    }

    @Override
    public boolean isBootstrapInstrumentation() {
        return true;
    }

    @Override
    public StaticMethodsInterceptPoint[] getStaticMethodsInterceptPoints() {
        return new StaticMethodsInterceptPoint[0];
    }

    @Override
    public InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[]{
                new InstanceMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return ElementMatchers.named(ENHANCE_EXECUTE_METHOD).and(ElementMatchers.takesArguments(Runnable.class));
                    }

                    @Override
                    public String getMethodsInterceptor() {
                        return INTERCEPTOR_CLASS;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                },
                new InstanceMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return ElementMatchers.named(ENHANCE_SUBMIT_METHOD).and(ElementMatchers.takesArguments(Callable.class));
                    }

                    @Override
                    public String getMethodsInterceptor() {
                        return INTERCEPTOR_CLASS;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                }
        };


    }
}
