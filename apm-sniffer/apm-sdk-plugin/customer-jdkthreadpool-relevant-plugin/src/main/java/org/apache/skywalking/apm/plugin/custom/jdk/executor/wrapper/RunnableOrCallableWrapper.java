package org.apache.skywalking.apm.plugin.custom.jdk.executor.wrapper;


import java.util.concurrent.Callable;

public class RunnableOrCallableWrapper implements Runnable, Callable {
    private Runnable runnable;
    private Callable callable;

    public RunnableOrCallableWrapper(Runnable runnable) {
        this.runnable = runnable;
    }

    public RunnableOrCallableWrapper(Callable callable) {
        this.callable = callable;
    }

    @Override
    public void run() {
        runnable.run();
    }

    @Override
    public Object call() throws Exception {
        return callable.call();
    }
}
