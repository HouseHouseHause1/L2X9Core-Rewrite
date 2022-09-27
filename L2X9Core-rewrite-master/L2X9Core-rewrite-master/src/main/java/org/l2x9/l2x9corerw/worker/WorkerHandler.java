package org.l2x9.l2x9corerw.worker;

import org.l2x9.l2x9corerw.worker.workers.EntityWorker;
import org.l2x9.l2x9corerw.worker.workers.ViolationWorker;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class WorkerHandler {
    private final ArrayList<Worker> workers = new ArrayList<>();
    private Timer timer;

    public void startWorkers() {
        timer = new Timer();
        addWorker(new ViolationWorker());
        addWorker(new EntityWorker());
        
    }
    private void addWorker(Worker worker) {
        if (worker.isEnabled()) {
            workers.add(worker);
            System.out.println(worker.getName());
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    worker.execute();
                }
            }, 0, worker.getTime());
        }
    }
    public void stopAllWorkers() {
        timer.cancel();
        timer = null;
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }
}
