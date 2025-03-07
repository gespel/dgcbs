class DGCBSDaemon {
    public DGCBSDaemon() {
        println("Creating a new dynamic gcloud build system daemon")
    }

    public void checkWorkers() {
        if(this.getNumJobsOfWorker() == 0 && this.checkIfWorkerIsOnline()) {
            sleep 30
            if(this.getNumJobsOfWorker() == 0 && this.checkIfWorkerIsOnline()) {
                stopInstance("jenkins-slave", "europe-west10-a")
            }
        }
    }

    public void stopInstance(name, zone) {
        sh(script: "/home/jenkins/google-cloud-sdk/bin/gcloud compute instances start ${name} --zone ${zone}", returnStdout: true)
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