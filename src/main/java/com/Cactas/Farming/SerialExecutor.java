package com.Cactas.Farming;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.*;

public class SerialExecutor implements Executor {
    final Queue<Runnable> tasks = new ArrayDeque<Runnable>();
    final ScheduledExecutorService exec;
    Runnable active;
    final ScheduledExecutorService checkDone;
    ScheduledFuture<?> current;
    SerialExecutor(ScheduledExecutorService e){
        this.exec = e;
        checkDone = Executors.newSingleThreadScheduledExecutor();
    }
    public synchronized void execute(final Runnable r){
        tasks.offer(new Runnable(){
           public void run(){
               try {
                   r.run();
               } finally {
                   scheduleNext();
               }
           }
        });
        if(active == null){
            scheduleNext();
        }
    }

    protected synchronized void scheduleNext() {
        if((active = tasks.poll()) != null) {
            if(current == null || current.isCancelled()){
                current = exec.schedule(active, 0, TimeUnit.MILLISECONDS);
            }
            else{
                checkCompleteRunnable ccr = new checkCompleteRunnable();
                ccr.add(checkDone.schedule(ccr, 50, TimeUnit.MILLISECONDS));
            }
        }
    }

    private class checkCompleteRunnable implements Runnable{
        ScheduledFuture<?> s;
        public void run(){
            if(current.isCancelled()){
                s.cancel(true);
            }
        }
        public void add(ScheduledFuture<?> sf){
            s = sf;
        }
    }
}
