def call(name) {
    def runningJobs = []
    jenkins.model.Jenkins.instance.nodes.each { node ->
        if (node.name == name) {
            runningJobs = node.computer.executors.findAll { it.isBusy() }
        }
    }
    
    // Gib die Anzahl der laufenden Jobs auf diesem Knoten aus
    echo "Es laufen ${runningJobs.size()} Jobs auf dem Knoten ${name}."
}
