package org.apache.skywalking.jdk.threadpool.relevant.wrapper;

import java.util.concurrent.Callable;

public class CallableWrapper implements Callable {
    private Callable callable;

    public CallableWrapper(Callable callable) {
        this.callable = callable;
    }

    @Override
    public Object call() throws Exception {
        return callable.call();
    }
}
