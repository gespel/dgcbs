class JenkinsNode {
    private String type;
    private String serverName;
    private String containerName;
    private String id;

    public JenkinsNode(String type, String serverName, String containerName) {
        this.type = type;
        this.serverName = serverName;
        this.containerName = containerName;
        this.id = type + serverName + containerName
    }

    public String getServerName() {
        return this.serverName;
    }

    public String getContainerName() {
        return this.containerName;
    }

    public int getNumJobs() {
        def runningJobs = []
        jenkins.model.Jenkins.instance.nodes.each { node ->
            if (node.name == this.id) {
                runningJobs = node.computer.executors.findAll { 
                    it.isBusy() 
                }
            }
        }
        
        return runningJobs.size()
    }
}