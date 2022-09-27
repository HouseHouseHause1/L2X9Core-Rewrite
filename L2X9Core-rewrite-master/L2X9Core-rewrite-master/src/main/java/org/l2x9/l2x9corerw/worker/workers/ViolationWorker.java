package org.l2x9.l2x9corerw.worker.workers;

import org.l2x9.l2x9corerw.worker.Worker;

public class ViolationWorker extends Worker {
    public ViolationWorker() {
        super(1000);
    }

    @Override
    public void execute() {
        plugin.getViolationMaps().forEach(map -> map.forEach(((key, vls) -> {
            if (vls > 0) {
                map.replace(key, vls - 1);
            } else if (vls == 0) {
                map.remove(key);
            }
        })));
    }
}
