def call(name) {
    def runningJobs = []
    jenkins.model.Jenkins.instance.nodes.each { node ->
        if (node.name == name) {
            runningJobs = node.computer.executors.findAll { 
                it.isBusy() 
            }
        }
    }
    
    //echo "Es laufen ${runningJobs.size()} Jobs auf dem Knoten ${name}."
    return runningJobs.size()
}
