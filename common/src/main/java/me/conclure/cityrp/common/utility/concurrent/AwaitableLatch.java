package me.conclure.cityrp.common.utility.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class AwaitableLatch {
    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    public void await() throws InterruptedException {
        this.countDownLatch.await();
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return this.countDownLatch.await(timeout, unit);
    }

    public void release() {
        this.countDownLatch.countDown();
    }

    public boolean hasReleased() {
        return this.countDownLatch.getCount() == 0L;
    }
}
