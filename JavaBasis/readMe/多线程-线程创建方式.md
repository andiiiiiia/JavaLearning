# 1. extends Thread
## 1.1 使用方式
```java
public class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " generate num : " + i);
        }
    }
}
```
```java
public class Client {
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();

        // 主线程
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + "generate num : " + i);
        }
    }
}
```
执行结果
```text
maingenerate num : 0
maingenerate num : 1
Thread-0 generate num : 0
Thread-0 generate num : 1
Thread-0 generate num : 2
maingenerate num : 2
Thread-0 generate num : 3
maingenerate num : 3
maingenerate num : 4
Thread-0 generate num : 4
```

# 2. implement Runnable

## 2.1 使用方式-自定义实现类
```java
public class MyRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 6; i++) {
            System.out.println(Thread.currentThread().getName() + " generate num : " + i);
        }
    }
}
```
```java
public class Client {
    public static void main(String[] args) {
        Thread myThread = new Thread(new MyRunnable());
        myThread.start();
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " generate num : " + i);
        }
    }
}
```
执行结果
```text
main generate num : 0
main generate num : 1
main generate num : 2
main generate num : 3
Thread-0 generate num : 0
main generate num : 4
main generate num : 5
Thread-0 generate num : 1
main generate num : 6
Thread-0 generate num : 2
main generate num : 7
Thread-0 generate num : 3
main generate num : 8
Thread-0 generate num : 4
main generate num : 9
Thread-0 generate num : 5
```
## 2.2 使用方式-匿名内部类
```java
public class Client2 {
    public static void main(String[] args) {
        // 主线程
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + " generate num : " + i);
        }
        // 其他线程
        new Thread(() -> {
            for (int i = 0; i < 4; i++) {
                System.out.println(Thread.currentThread().getName() + " generate num : " + i);
            }
        }).start();
    }
}
```
执行结果
```text
main generate num : 0
main generate num : 1
main generate num : 2
main generate num : 3
main generate num : 4
Thread-0 generate num : 0
Thread-0 generate num : 1
Thread-0 generate num : 2
Thread-0 generate num : 3
```

# 3. implement Callable
## 3.1 接口定义(what)
```java
public interface Callable<V> {
    V call() throws Exception;
}
```
## 3.2 作用(why)
允许线程执行任务后返回一个结果 

可以抛出受检异常，便于错误处理 

适合线程池，与ExecutorService配合使用，适合并发任务 

适合异步任务，与Future配合使用，适合获取异步执行结果 

## 3.3 使用方式-与Future配合使用以获取异步执行结果-自定义实现类
```java
public class MyCallable implements Callable<String> {
    private int n;

    public MyCallable(int n) {
        this.n = n;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public String call() throws Exception {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += i;
        }
        return Thread.currentThread().getName() + ": sum of " + n + " is: " + sum;
    }
}
```
```java
public class Client {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 主线程
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + " generate Letter : " + (char) ('A' + i));
        }

        // 线程1：自定义线程
        Callable<String> callable = new MyCallable(10);
        FutureTask futureTask = new FutureTask(callable);
        Thread thread = new Thread(futureTask);
        thread.start();
        // 获取结果
        System.out.println(futureTask.get());
    }
}
```
执行结果
```text
main generate Letter : A
main generate Letter : B
main generate Letter : C
Thread-0: sum of 10 is: 45
```
## 3.4 使用方式-与Future配合使用以获取异步执行结果-匿名内部类
```java
public class Client {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 主线程
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + " generate letter : " + (char) ('A' + i));
        }

        // 线程2：匿名内部类
        FutureTask<String> stringFutureTask = new FutureTask<>(() -> {
            int sum = 0;
            for (int i = 0; i < 5; i++) {
                sum += i;
            }
            return Thread.currentThread().getName() + " sum of 5 is : " + sum;
        });
        new Thread(stringFutureTask).start();
        // 获取结果
        System.out.println(stringFutureTask.get());
    }
}
```
执行结果
```text
main generate letter : A
main generate letter : B
main generate letter : C
Thread-0 sum of 5 is : 10
```

# 4. ThreadPoolExecutor
## 4.1 类定义
### 4.1.1 ThreadPoolExecutor
```java
public class ThreadPoolExecutor extends AbstractExecutorService {
  // 核心线程数，线程池中始终保留的最小线程数量，即使这些线程处于空闲状态，也不会被销毁。
  private volatile int corePoolSize;

  // 最大线程数 
  private volatile int maximumPoolSize;

  // 用于保存等待执行的**任务**
  private final BlockingQueue<Runnable> workQueue;

  // 空闲线程等待任务的超时时间
  // 当线程池中的线程数量超过核心线程数（corePoolSize），或者允许核心线程超时（allowCoreThreadTimeOut 为 true）时，线程在没有任务可执行时，最多等待多长时间后会被销毁。
  private volatile long keepAliveTime;

  // 线程工厂，用于创建线程池中的工作线程，所有线程池中的线程都是通过这个工厂创建的。
  private volatile ThreadFactory threadFactory;

  /**
   * 拒绝策略处理器
   */
  private volatile RejectedExecutionHandler handler;
  
  // 抛出 RejectedExecutionException 异常
  private static final RejectedExecutionHandler defaultHandler = new AbortPolicy();

  // 工作线程集合.保存线程池中所有的 Worker 对象，每个 Worker 对象代表一个线程池中的工作线程。线程池中执行任务的最小单位。
  private final HashSet<Worker> workers = new HashSet<Worker>();

  // 用于保护对线程池中 workers 集合的访问，以及线程池中的一些状态管理操作。

  
  // 构造方法：default thread factory
  public ThreadPoolExecutor(int corePoolSize,
                            int maximumPoolSize,
                            long keepAliveTime,
                            TimeUnit unit,
                            BlockingQueue<Runnable> workQueue,
                            RejectedExecutionHandler handler) {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
         Executors.defaultThreadFactory(), handler);
  }
  
  // 构造方法：default rejected execution handler.  default thread factory
  public ThreadPoolExecutor(int corePoolSize,
                            int maximumPoolSize,
                            long keepAliveTime,
                            TimeUnit unit,
                            BlockingQueue<Runnable> workQueue) {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
         Executors.defaultThreadFactory(), defaultHandler);
  }
  
  // 构造方法：default rejected execution handler. 
  public ThreadPoolExecutor(int corePoolSize,
                            int maximumPoolSize,
                            long keepAliveTime,
                            TimeUnit unit,
                            BlockingQueue<Runnable> workQueue,
                            ThreadFactory threadFactory) {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
         threadFactory, defaultHandler);
  }
  
  // 构造方法：
  public ThreadPoolExecutor(int corePoolSize,
                            int maximumPoolSize,
                            long keepAliveTime,
                            TimeUnit unit,
                            BlockingQueue<Runnable> workQueue,
                            ThreadFactory threadFactory,
                            RejectedExecutionHandler handler) {
    if (corePoolSize < 0 ||
        maximumPoolSize <= 0 ||
        maximumPoolSize < corePoolSize ||
        keepAliveTime < 0)
        throw new IllegalArgumentException();
    if (workQueue == null || threadFactory == null || handler == null)
        throw new NullPointerException();
    this.acc = System.getSecurityManager() == null ?
            null :
            AccessController.getContext();
    this.corePoolSize = corePoolSize;
    this.maximumPoolSize = maximumPoolSize;
    this.workQueue = workQueue;
    this.keepAliveTime = unit.toNanos(keepAliveTime);
    this.threadFactory = threadFactory;
    this.handler = handler;
  }
  
  // 内部类: AbortPolicy : 当线程池无法处理新任务时，直接抛出异常，从而阻止任务被静默丢弃。
  public static class AbortPolicy implements RejectedExecutionHandler {
      public AbortPolicy() { }
    
      public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
          throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + e.toString());
      }
  }

  // 内部类DiscardPolicy：静默丢弃任务
  public static class DiscardPolicy implements RejectedExecutionHandler {
    public DiscardPolicy() { }

    /**
     * Does nothing, which has the effect of discarding task r.
     */
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
    }
  }
  
  // 内部类DiscardOldestPolicy：丢弃队列中未执行的最老的任务
  public static class DiscardOldestPolicy implements RejectedExecutionHandler {
    public DiscardOldestPolicy() { }

    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        if (!e.isShutdown()) {
            e.getQueue().poll(); // 丢弃
            e.execute(r);
        }
    }
  }
  
  // 内部类CallerRunsPolicy：由**调用线程**执行任务
  public static class CallerRunsPolicy implements RejectedExecutionHandler {
    public CallerRunsPolicy() { }

    /**
     * Executes task r in the caller's thread, unless the executor
     * has been shut down, in which case the task is discarded.
     */
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        if (!e.isShutdown()) {
            r.run();
        }
    }
  }

  // 内部类：Worker。
  // 私有且不可继承的类。
  // 继承自 AbstractQueuedSynchronizer（AQS），用于实现线程的同步机制。
  // 实现了 Runnable 接口，表示它可以被线程执行。
  private final class Worker extends AbstractQueuedSynchronizer implements Runnable{
    private static final long serialVersionUID = 6138294804551838833L;

    // 当前工作线程，如果线程工厂创建失败则为 null。
    final Thread thread;
    
    // 该工作线程启动时要执行的第一个任务，可以为 null。
    Runnable firstTask;
    
    // 该线程完成的任务数，使用 volatile 保证可见性。
    volatile long completedTasks;

    // 构造方法
    Worker(Runnable firstTask) {
        setState(-1); // 防止在工作线程启动前被中断。
        this.firstTask = firstTask;
        this.thread = getThreadFactory().newThread(this);
    }

    // 执行任务循环
    public void run() {
        runWorker(this);
    }
    
    // 判断当前锁是否被独占持有（状态不为 0 表示被持有）。
    protected boolean isHeldExclusively() {
        return getState() != 0;
    }

    // 尝试获取锁，使用 CAS 操作将状态从 0 改为 1，成功则设置当前线程为独占线程。
    protected boolean tryAcquire(int unused) {
        if (compareAndSetState(0, 1)) {
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }
    
    // 释放锁，将状态设为 0，并清除独占线程。
    protected boolean tryRelease(int unused) {
        setExclusiveOwnerThread(null);
        setState(0);
        return true;
    }

    public void lock()        { acquire(1); }
    public boolean tryLock()  { return tryAcquire(1); }
    public void unlock()      { release(1); }
    public boolean isLocked() { return isHeldExclusively(); }
    
    // 如果线程已经启动（状态 >= 0），并且线程未被中断，则尝试中断线程。
    void interruptIfStarted() {
        Thread t;
        if (getState() >= 0 && (t = thread) != null && !t.isInterrupted()) {
            try {
                t.interrupt();
            } catch (SecurityException ignore) {
            }
        }
    }
  }

  /**
    * runWorker 是线程池中每个工作线程（Worker）的主执行方法。它的主要职责是：
    * 从任务队列中获取任务；
    * 执行任务；
    * 处理任务执行过程中的异常；
    * 管理线程的中断和生命周期。
    */
  final void runWorker(Worker w) {
    Thread wt = Thread.currentThread();
    Runnable task = w.firstTask;
    w.firstTask = null;
    w.unlock(); // allow interrupts
    boolean completedAbruptly = true;
    try {
        while (task != null || (task = getTask()) != null) {
            w.lock();
            // 如果线程池状态为 STOP 或更高（如 TERMINATE），并且当前线程未被中断，则主动中断线程。
            // 这是为了确保线程在池关闭时能及时退出。
            if (
              (
              runStateAtLeast(ctl.get(), STOP) || (Thread.interrupted() && runStateAtLeast(ctl.get(), STOP))
              ) 
              && !wt.isInterrupted()){
              wt.interrupt();
            }
            try {
                // 钩子
                beforeExecute(wt, task);
                Throwable thrown = null;
                try {
                    task.run();
                } catch (RuntimeException x) {
                    thrown = x; throw x;
                } catch (Error x) {
                    thrown = x; throw x;
                } catch (Throwable x) {
                    thrown = x; throw new Error(x);
                } finally {
                    // 钩子
                    afterExecute(task, thrown);
                }
            } finally {
                task = null;
                w.completedTasks++;
                w.unlock();
            }
        }
        completedAbruptly = false;
    } finally {
        processWorkerExit(w, completedAbruptly);
    }
  }

    /**
     * 将一个任务提交到线程池中执行。
     * 如果任务无法被提交执行（例如线程池已关闭或线程池容量已满），则任务会由当前的 RejectedExecutionHandler（拒绝执行处理器）进行处理。
     * 
     */
  public void execute(Runnable command) {
      if (command == null)
          throw new NullPointerException();
      /*
       * Proceed in 3 steps:
       *
       * 1. If fewer than corePoolSize threads are running, try to
       * start a new thread with the given command as its first
       * task.  The call to addWorker atomically checks runState and
       * workerCount, and so prevents false alarms that would add
       * threads when it shouldn't, by returning false.
       *
       * 2. If a task can be successfully queued, then we still need
       * to double-check whether we should have added a thread
       * (because existing ones died since last checking) or that
       * the pool shut down since entry into this method. So we
       * recheck state and if necessary roll back the enqueuing if
       * stopped, or start a new thread if there are none.
       *
       * 3. If we cannot queue task, then we try to add a new
       * thread.  If it fails, we know we are shut down or saturated
       * and so reject the task.
       */
      int c = ctl.get();
      if (workerCountOf(c) < corePoolSize) {
          if (addWorker(command, true))
              return;
          c = ctl.get();
      }
      if (isRunning(c) && workQueue.offer(command)) {
          int recheck = ctl.get();
          if (! isRunning(recheck) && remove(command))
              reject(command);
          else if (workerCountOf(recheck) == 0)
              addWorker(null, false);
      }
      else if (!addWorker(command, false))
          reject(command);
  }
  
  // 根据线程池状态和当前线程数，判断是否可以添加新线程；
  // 创建新的 Worker 线程；
  // 将其加入线程池；
  // 启动线程；
  // 如果失败则进行清理。
  // 确保线程池在负载变化时能够灵活地扩展或收缩线程数量。
  private boolean addWorker(Runnable firstTask, boolean core) {
    retry:
    for (;;) {
        int c = ctl.get();
        int rs = runStateOf(c);

        // Check if queue empty only if necessary.
        if (rs >= SHUTDOWN &&
            ! (rs == SHUTDOWN &&
               firstTask == null &&
               ! workQueue.isEmpty()))
            return false;

        for (;;) {
            int wc = workerCountOf(c);
            if (wc >= CAPACITY ||
                wc >= (core ? corePoolSize : maximumPoolSize))
                return false;
            if (compareAndIncrementWorkerCount(c))
                break retry;
            c = ctl.get();  // Re-read ctl
            if (runStateOf(c) != rs)
                continue retry;
            // else CAS failed due to workerCount change; retry inner loop
        }
    }

    boolean workerStarted = false;
    boolean workerAdded = false;
    Worker w = null;
    try {
        w = new Worker(firstTask);
        final Thread t = w.thread;
        if (t != null) {
            final ReentrantLock mainLock = this.mainLock;
            mainLock.lock();
            try {
                // Recheck while holding lock.
                // Back out on ThreadFactory failure or if
                // shut down before lock acquired.
                int rs = runStateOf(ctl.get());

                if (rs < SHUTDOWN ||
                    (rs == SHUTDOWN && firstTask == null)) {
                    if (t.isAlive()) // precheck that t is startable
                        throw new IllegalThreadStateException();
                    workers.add(w);
                    int s = workers.size();
                    if (s > largestPoolSize)
                        largestPoolSize = s;
                    workerAdded = true;
                }
            } finally {
                mainLock.unlock();
            }
            if (workerAdded) {
                t.start();
                workerStarted = true;
            }
        }
    } finally {
        if (! workerStarted)
            addWorkerFailed(w);
    }
    return workerStarted;
}
  
}

```
### 4.1.2 BlockingQueue
ArrayBlockingQueue	有界队列，容量固定	有界  
LinkedBlockingQueue	无界队列，默认容量为 Integer.MAX_VALUE	无界  
SynchronousQueue	不存储任务，直接传递给线程	无界（逻辑上）  
PriorityBlockingQueue	支持优先级排序的任务队列	无界  
DelayQueue	支持延迟执行的任务队列	无界  
### 4.1.3 Executors
Executors 的主要作用是简化线程池的创建，避免手动实现线程池的复杂逻辑。通过它提供的静态方法，我们可以快速创建出适合不同场景的线程池。  
|方法名 | 作用 |
| - | - |
| newFixedThreadPool(int nThreads) | 创建一个固定大小的线程池，线程数量固定，适合负载较重的服务器应用。 |
| newCachedThreadPool() | 创建一个可缓存的线程池，线程数量不固定，会根据需要自动创建或回收线程。 |
| newSingleThreadExecutor() | 创建一个只有一个线程的线程池，保证所有任务按顺序执行。 |
| newScheduledThreadPool(int corePoolSize) | 创建一个支持定时及周期性任务执行的线程池。 |
| newWorkStealingPool()	 | 	创建一个使用工作窃取算法的线程池（Java 8+），适合执行大量独立任务。 |
| newSingleThreadScheduledExecutor() | 	创建一个支持定时任务的单线程线程池。 |
### 4.1.4 举例说明线程池的执行逻辑execute
假设：  
core：2  
max:4  
queue:2  
每个任务的执行时间都比较久，再task7加入时均未执行完成  
1）提交task1，task2  
<img width="479" height="205" alt="image" src="https://github.com/user-attachments/assets/1afc5c90-79ed-4900-b607-d192b3b6ba1c" />

2）提交task3，task4  

3）提交task5  

4）提交task6  

5）提交task7  

## 4.2 使用方式-使用原始方式创建一个线程
**case1:**
```java
public class Client {
    public static void main(String[] args) throws InterruptedException {
        // 主线程
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + "主线程 generate : " + (char) ('A' + i));
        }

        // 使用默认方式创建一个线程池
        ExecutorService executorService1 = new ThreadPoolExecutor(
                2,
                4,
                10,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(2),
                new ThreadPoolExecutor.DiscardPolicy()
        );
        for (int i = 0; i < 8; i++) {
            Thread.sleep(1);
            executorService1.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " generate : 1");
            });
        }
        executorService1.shutdown();
    }
}
```
执行结果
```text
main主线程 generate : A
main主线程 generate : B
main主线程 generate : C
pool-1-thread-1 generate : 1
pool-1-thread-2 generate : 1
pool-1-thread-1 generate : 1
pool-1-thread-2 generate : 1
pool-1-thread-1 generate : 1
pool-1-thread-2 generate : 1
pool-1-thread-1 generate : 1
pool-1-thread-2 generate : 1
```
注意：  
这里是先等主线程执行完之后才会执行线程提交任务这里。执行多少次都是主线程先执行完再执行线程池中的线程中的任务。  
**case2:**
```java
public class Client {
    public static void main(String[] args) throws InterruptedException {
        // 使用默认方式创建一个线程池
        ExecutorService executorService1 = new ThreadPoolExecutor(
                2,
                4,
                10,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(2),
                new ThreadPoolExecutor.DiscardPolicy()
        );
        for (int i = 0; i < 8; i++) {
            Thread.sleep(1);
            executorService1.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " generate : 1");
            });
        }

        // 主线程
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + "主线程 generate : " + (char) ('A' + i));
        }

        // 关闭线程池
        executorService1.shutdown();
    }
}
```
执行结果：
```text
pool-1-thread-1 generate : 1
pool-1-thread-2 generate : 1
pool-1-thread-1 generate : 1
pool-1-thread-2 generate : 1
pool-1-thread-1 generate : 1
pool-1-thread-2 generate : 1
pool-1-thread-1 generate : 1
main主线程 generate : A
pool-1-thread-2 generate : 1
main主线程 generate : B
main主线程 generate : C
```
注意：  
这里结果就会出现随机性，如果关闭线程池的操作在主线程之前，则也不会出现随机性，必须要等线程池中线程全部执行完，再执行主线程的操作。  
## 4.3 使用方式-Executors内置的线程池创建方式
```java
public class Client {
    public static void main(String[] args) throws InterruptedException {
        // 使用java内置的一些线程池简化创建方式
        ExecutorService executorService2 = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 8; i++) {
            executorService2.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " generate : 2");
            });
        }

        // 主线程
        for (int i = 0; i < 8; i++) {
            System.out.println(Thread.currentThread().getName() + "主线程 generate : " + (char) ('A' + i));
        }

        // 关闭线程池2
        executorService2.shutdown();
    }
}
```
执行结果：  
随机  
```text
main主线程 generate : A
main主线程 generate : B
main主线程 generate : C
main主线程 generate : D
main主线程 generate : E
main主线程 generate : F
pool-1-thread-1 generate : 2
main主线程 generate : G
main主线程 generate : H
pool-1-thread-1 generate : 2
pool-1-thread-2 generate : 2
pool-1-thread-1 generate : 2
pool-1-thread-2 generate : 2
pool-1-thread-1 generate : 2
pool-1-thread-1 generate : 2
pool-1-thread-2 generate : 2
```
# 5. CompletableFuture
## 5.1 定义what?
异步编程工具  
主要用于处理异步任务和组合多个异步操作   

## 5.2 使用方式-异步任务-无返回值
**不等待异步线程的场景**  
```java
public class Client {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 异步任务-无返回值
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 20; i++) {
                System.out.println(Thread.currentThread().getName() + " is running ! " + i);
            }
        });
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " : " + i);
        }
    }
}
```
执行结果  
从结果可以看出，第一个线程任务并没有完全打印完就结束了。
原因就在于他是异步执行的，当调用runAsync(...) 后会立即返回一个new CompletableFuture<Void>()对象。
```text
main : 0
main : 1
main : 2
main : 3
main : 4
main : 5
main : 6
main : 7
ForkJoinPool.commonPool-worker-1 is running ! 0
main : 8
ForkJoinPool.commonPool-worker-1 is running ! 1
ForkJoinPool.commonPool-worker-1 is running ! 2
main : 9
ForkJoinPool.commonPool-worker-1 is running ! 3
ForkJoinPool.commonPool-worker-1 is running ! 4
ForkJoinPool.commonPool-worker-1 is running ! 5
ForkJoinPool.commonPool-worker-1 is running ! 6
ForkJoinPool.commonPool-worker-1 is running ! 7
ForkJoinPool.commonPool-worker-1 is running ! 8
ForkJoinPool.commonPool-worker-1 is running ! 9
ForkJoinPool.commonPool-worker-1 is running ! 10
ForkJoinPool.commonPool-worker-1 is running ! 11
ForkJoinPool.commonPool-worker-1 is running ! 12
ForkJoinPool.commonPool-worker-1 is running ! 13
ForkJoinPool.commonPool-worker-1 is running ! 14
ForkJoinPool.commonPool-worker-1 is running ! 15
ForkJoinPool.commonPool-worker-1 is running ! 16
```
**等待异步线程的场景**
```java
public class Client {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 异步任务-无返回值-等待
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + " is running ! " + i);
            }
        });
        for (int i = 0; i < 10; i++) {
            if(i==5){
                future1.join();
            }
            System.out.println(Thread.currentThread().getName() + " : " + i);
        }
    }
}
```
执行结果  
在主线程打印完4之后，等待异步任务全部执行完，再重新执行main主线程
```text
ForkJoinPool.commonPool-worker-1 is running ! 0
ForkJoinPool.commonPool-worker-1 is running ! 1
main : 0
ForkJoinPool.commonPool-worker-1 is running ! 2
main : 1
ForkJoinPool.commonPool-worker-1 is running ! 3
main : 2
ForkJoinPool.commonPool-worker-1 is running ! 4
main : 3
ForkJoinPool.commonPool-worker-1 is running ! 5
main : 4
ForkJoinPool.commonPool-worker-1 is running ! 6
ForkJoinPool.commonPool-worker-1 is running ! 7
ForkJoinPool.commonPool-worker-1 is running ! 8
ForkJoinPool.commonPool-worker-1 is running ! 9
main : 5
main : 6
main : 7
main : 8
main : 9
```
## 5.3 使用方式-异步任务-有返回值
```java
public class Client {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 异步任务-有返回值
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            int sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += i;
            }
            return Thread.currentThread().getName() + " sum is : " + sum;
        });
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                String result = future2.get();
                System.out.println(result);
            }
            System.out.println(Thread.currentThread().getName() + " : " + i);
        }
    }
}
```
执行结果
```text
main : 0
main : 1
main : 2
main : 3
main : 4
ForkJoinPool.commonPool-worker-1 sum is : 45
main : 5
main : 6
main : 7
main : 8
main : 9
```
## 5.4 使用方式-链式调用:针对前一个异步任务结果进行消费和返回
thenApply(Function<? super T,? extends U> fn)  

```java
public class Client {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
      // 链式调用:针对第一个异步任务结果进行消费和返回thenApply(Function<? super T,? extends U> fn)
      CompletableFuture<String> future3 = CompletableFuture
        .supplyAsync(() -> {
          int sum = 0;
          for (int i = 0; i < 10; i++) {
            sum += i;
            }
          System.out.println(" -> 原始数据(sumOf(0-9)): " + sum);
          return sum;
          })
        .thenApply(data -> " -> 转换后的数据(+10)" + (data + 10));
      String result3 = future3.join();
      System.out.println(result3);
    }
}
```
执行结果
```text
 -> 原始数据(sumOf(0-9)): 45
 -> 转换后的数据(+10)55
```
## 5.5 使用方式-链式调用：针对第一个异步任务结果只消费不返回
thenAccept(Consumer<? super T> action)  
```java
public class Client {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
      // 链式调用:针对第一个异步任务结果只消费不返回: thenAccept(Consumer<? super T> action)
      CompletableFuture<Void> future4 = CompletableFuture
        .supplyAsync(() -> {
          int sum = 0;
          for (int i = 0; i < 10; i++) {
              sum += i;
          }
          System.out.println("-> 原始数据(sumOf(0-9)): " + sum);
          return sum;
          })
        .thenAccept(data -> {
          System.out.println("-> 转换后的数据(+10):" + (data + 10));
          });
      future4.join();
    }
}
```
执行结果
```text
-> 原始数据(sumOf(0-9)): 45
-> 转换后的数据(+10):55
```
## 5.6 使用方式-链式调用：针对第前一个任务结果不消费不返回
**thenRun(Runnable action)**  
```java
public class Client {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
      // 链式调用:针对第一个异步任务结果不消费不返回: thenRun(Runnable action)
      CompletableFuture<Void> future5 = CompletableFuture
        .runAsync(() -> {
          int sum = 0;
          for (int i = 0; i < 10; i++) {
              sum += i;
          }
          System.out.println("第一个任务完成：" + sum);
          })
        .thenRun(() -> {
          System.out.println("第二个任务完成：void");
          });
      future5.join();
  }
}
```
执行结果：
```text
第一个任务完成：45
第二个任务完成：void
```
## 5.7 使用方式-多个任务组合，等待所有任务完成后一起执行。
**CompletableFuture.allOf(CompletableFuture<?>... cfs)**
```java
public class Client {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
      CompletableFuture<Void> future6 = CompletableFuture.runAsync(() -> {
        System.out.println("任务6执行");
        });
      CompletableFuture<Void> future7 = CompletableFuture.runAsync(() -> {
        System.out.println("任务7执行");
        });
      CompletableFuture<Void> allFutures = CompletableFuture.allOf(future6, future7);
        allFutures.get();
        System.out.println("所有任务执行完成");
    }
}
```
执行结果
```text
任务6执行
任务7执行
所有任务执行完成
```
## 5.8 多个任务组合，只要有一个任务完成就执行
```java
public class Client {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    // 多个任务组合，只要有一个任务完成就执行
    CompletableFuture<String> future8 = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
      }
      return "任务8完成";
      });
    CompletableFuture<String> future9 = CompletableFuture.supplyAsync(() -> {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
      }
      return "任务9完成";
      });

    CompletableFuture<Object> anyFuture = CompletableFuture.anyOf(future8, future9);
    Object result = anyFuture.join();
    System.out.println("第一个完成的任务结果：" + result);
  }
}
```
执行结果：
```text
第一个完成的任务结果：任务9完成
```
## 5.9 处理异步任务中的异常（exceptionally）
```java
public class Client2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() > 0.5) {
                throw new RuntimeException("发生错误");
            }
            return "成功";
        });

        future.exceptionally(ex -> {
            System.out.println("发生异常：" + ex.getMessage());
            return "默认值";
        }).thenAccept(result -> System.out.println("结果：" + result));
    }
}
```
执行结果
```text
发生异常：java.lang.RuntimeException: 发生错误
结果：默认值
```
## 6.0 使用方式-调用多个接口并统一处理
```java
public class Client3 {
    public static void main(String[] args) {
      CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() -> callUserService());
      CompletableFuture<String> orderFuture = CompletableFuture.supplyAsync(() -> callOrderService());
  
      CompletableFuture<Void> allFutures = CompletableFuture.allOf(userFuture, orderFuture);
      
      allFutures.thenRun(() -> {
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        String user = userFuture.join();
        String order = orderFuture.join();
        System.out.println("用户信息：" + user);
        System.out.println("订单信息：" + order);
      });
      System.out.println("end");
    }

    private static String callOrderService() {
      return "phone (1)";
    }

    private static String callUserService() {
      return "张三";
    }
}
```
执行结果
```text
用户信息：张三
订单信息：phone (1)
end
```
**问题：为什么end会最后输出？？？**  
从源码看，他执行了return uniRunNow(r, e, f);  没有直接return CompletableFuture<Void>
为什么 result有值？

```java
    private CompletableFuture<Void> uniRunStage(Executor e, Runnable f) {
        if (f == null) throw new NullPointerException();
        Object r;
        if ((r = result) != null)
            return uniRunNow(r, e, f);
        CompletableFuture<Void> d = newIncompleteFuture();
        unipush(new UniRun<T>(e, d, this, f));
        return d;
    }
```
