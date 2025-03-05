class DGCBSDaemon {
    public DGCBSDaemon() {
        println("Creating a new dynamic gcloud build system daemon")
    }

    public int getNumJobsOfWorker(name) {
        def runningJobs = []
        jenkins.model.Jenkins.instance.nodes.each { node ->
            if (node.name == name) {
                runningJobs = node.computer.executors.findAll { 
                    it.isBusy() 
                }
            }
        }
        
        return runningJobs.size()
    }

    public boolean checkIfWorkerIsOnline(String name) {
        for (node in jenkins.model.Jenkins.instance.nodes) {
            if (node.name == name) {
                if (node.toComputer()?.isOnline()) {
                    return true
                }
            }
        }
        return false
    }

    public int getNumOfOnlineWorker() {
        def count = 0
        for (node in jenkins.model.Jenkins.instance.nodes) {
            if(node.toComputer()?.isOnline()) {
                count += 1
            }
        }
        return count
    }
}