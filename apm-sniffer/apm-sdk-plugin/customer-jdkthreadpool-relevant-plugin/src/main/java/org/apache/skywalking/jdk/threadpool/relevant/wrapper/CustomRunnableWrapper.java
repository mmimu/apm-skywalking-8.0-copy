package org.apache.skywalking.jdk.threadpool.relevant.wrapper;

import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.ContextSnapshot;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractSpan;
import org.apache.skywalking.apm.network.trace.component.ComponentsDefine;

public class CustomRunnableWrapper implements Runnable {
    private Runnable runnable;
    private ContextSnapshot contextSnapshot;

    public CustomRunnableWrapper(Runnable runnable, ContextSnapshot snapshot) {
        this.runnable = runnable;
        this.contextSnapshot = snapshot;
    }


    @Override
    public void run() {
        AbstractSpan span = ContextManager.createLocalSpan("runnablewrapper");
        span.setComponent(ComponentsDefine.JDK_THREADING);
        ContextManager.continued(contextSnapshot);
        runnable.run();
        ContextManager.stopSpan();
    }
}
