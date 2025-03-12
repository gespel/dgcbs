class DGCBSDaemon {
    private ArrayList<DBSServer> servers

    public DGCBSDaemon() {
        println("Creating a new dynamic gcloud build system daemon")
        servers = []
    }

    public void checkWorkers() {
        if(this.getNumJobsOfWorker() == 0 && this.checkIfWorkerIsOnline()) {
            sleep 30
            if(this.getNumJobsOfWorker() == 0 && this.checkIfWorkerIsOnline()) {
                stopInstance("jenkins-slave", "europe-west10-a")
            }
        }
    }

    public void stopInstance(String name, String zone) {
        //sh(script: "/var/lib/jenkins/google-cloud-sdk/bin/gcloud compute instances start ${name} --zone ${zone}", returnStdout: true)
        def p = "/var/lib/jenkins/google-cloud-sdk/bin/gcloud compute instances start ${name} --zone ${zone}".execute()
        p.waitFor()

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
            //if(nameParts[0].equalsIgnoreCase("slave")) {
                def nodeClass = nameParts[0]
                def nodeName = nameParts[1]
                def containerName = nameParts[2]

                workerContainers.add([nodeClass, nodeName, containerName, node])
            //}
        }
        return workerContainers
    }

    public void updateServers(ArrayList<ArrayList<String>> workerContainers) {
        for(wc in workerContainers) {
            for(s in this.servers) {
                if(wc[1].equalsIgnoreCase(s.getName())) {
                    s.addNode(new DynamicNode(wc[0], wc[1], wc[2]))
                }
            }
        }
    }

    public ArrayList<String> getOnlineServers(ArrayList<ArrayList<String>> workerContainers) {
        def out = []
        for(node in workerContainers) {
            if(!out.contains(node[1])) {
                out.add(node[1])
            }
        }
        return out
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

        def workerContainers = getWorkerContainers(onlineNodes)

        def onlineServers = getOnlineServers(workerContainers)
        for(server in onlineServers) {
            this.servers.add(new DBSServer(server))
        }

        updateServers(workerContainers)

        for(server in this.servers) {
            if(!isServerBusy(server.getName())) {
                stopInstance(server.getName().toString(), "europe-west10-a")
                changes += " !Shutdown of ${server.getName()}! "
            }
        }

        if(changes.equals("")) {
            changes = " None"
        }
 
        return "Checking of build backends done. Changes:" + changes// + " Debug: "
    }
}