package example.com.myping.util;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {

    private ThreadPoolExecutor executor;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;//9
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;//17
    private static final int KEEP_ALIVE = 1;

    private static ThreadPoolManager mInstance;

    public static ThreadPoolManager getInstance() {
        if (mInstance == null) {
            synchronized (ThreadPoolManager.class) {
                if (mInstance == null) {
                    mInstance = new ThreadPoolManager();
                }
            }
        }
        return mInstance;
    }

    private ThreadPoolManager() {
        Log.e("666","CPU_COUNT=="+CPU_COUNT);
        executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    /**
     * 执行任务
     */
    public void execute(Runnable runnable) {
        if (runnable == null) return;

        executor.execute(runnable);
    }

    /**
     * 移除任务
     * <p>
     * 注意：此方法起作用有一个必要的前提，就是这个任务还没有开始执行，如果已经开始执行了，就停止不了该任务了，这个方法就不会起作用
     */
    public void remove(Runnable runnable) {
        if (runnable == null) return;

        executor.remove(runnable);
    }

    /**
     * 提交任务
     */
    public Future<?> submit(Runnable runnable) {
        if (runnable == null) return null;

        return executor.submit(runnable);
    }
}