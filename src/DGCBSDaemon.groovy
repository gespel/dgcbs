class DGCBSDaemon {
    private ArrayList<DBSServer> servers

    public DGCBSDaemon() {
        println("Creating a new dynamic gcloud build system daemon")
        servers = []
        servers.add(new DBSServer("srv1"))
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

    public ArrayList<ArrayList<String>> getWorkerContainers(ArrayList<String> onlineNodes) {
        def workerContainers = []
        for(node in onlineNodes) {
            def nameParts = node.split("-")
            if(nameParts[0].equalsIgnoreCase("slave")) {
                def nodeClass = nameParts[0]
                def nodeName = nameParts[1]
                def containerName = nameParts[2]

                workerContainers.add([nodeClass, nodeName, containerName, node])
            }
        }
        return workerContainers
    }

    public void updateServers(ArrayList<ArrayList<String>> workerContainers) {
        //this.servers[0].addNode(new DynamicNode(workerContainers[0][0], workerContainers[0][1], workerContainers[0][2]))
        for(wc in workerContainers) {
            for(s in this.servers) {
                s.addNode(new DynamicNode(wc[0], wc[1], wc[2]))
                if(wc[1].equalsIgnoreCase(s.getName())) {
                    s.addNode(new DynamicNode(wc[0], wc[1], wc[2]))
                }
            }
        }
    }

    public boolean isServerBusy(String name) {
        for(int i = 0; i < this.servers.size(); i++) {
            if(name.equalsIgnoreCase(this.servers[i].getName())) {
                for(container in this.servers[i].getNodes()) {
                    if(container.getNumJobs() > 0) {
                        return true
                    }
                }
            }
        }
        return false
    }

    public String check() {
        def changes = ""
        def onlineNodes = []
        for(node in jenkins.model.Jenkins.instance.nodes) {
            if(node.toComputer()?.isOnline()) {
                onlineNodes.add(node.getNodeName())
            }
        }
        
        updateServers(getWorkerContainers(onlineNodes))

        for(server in this.servers) {
            if(!isServerBusy(server.getName())) {
                //stopInstance("jenkins-slave", "europe-west10-a")
                changes += " !Shutdown of ${server.getName()}! "
            }
        }
 
        return "Checking of build backends done. Changes:" + changes + " Debug: " + getWorkerContainers(onlineNodes).toString()
    }
}