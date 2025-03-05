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

    public boolean checkIfWorkerIsOnline(name) {
        jenkins.model.Jenkins.instance.nodes.each { node ->
            if (node.name == name) {
                if(node.toComputer().isOnline()) {
                    return true
                }
            }
        }
        return false
    }
}