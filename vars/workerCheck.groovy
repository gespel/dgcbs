def checkIfUp(name, zone) {
    echo "Checking for ${name} in zone ${zone}"
    def status = sh(script: "/home/jenkins/google-cloud-sdk/bin/gcloud compute instances describe ${name} --zone ${zone} --format=\"get(status)\"",
                    returnStdout: true).trim()
    echo "Status is: " + status

    if (status == "RUNNING") {
        return true
    } else {
        return false
    }
}

def checkIfJenkinsUp(name) {
    for (node in jenkins.model.Jenkins.instance.nodes) {
        if (node.name == name) {
            if (node.toComputer()?.isOnline()) {
                return true
            }
        }
    }
    return false
}

def startInstance(name, zone) {

}

def call(name, zone) {
    if (!checkIfUp(name, zone)) {
        echo "Starting ${name} now..."
        def status = sh(script: "/home/jenkins/google-cloud-sdk/bin/gcloud compute instances start ${name} --zone ${zone}",
                    returnStdout: true).trim()
        echo "Started worker ${name} in ${zone} using gcloud CLI"
        echo "Waiting for jenkins-agent to come up..."
        while(!checkIfJenkinsUp("build-slave")) {
            sleep 10
        }
        return true
    }
    else {
        if(checkIfJenkinsUp("build-slave")) {
            echo "Worker ${name} is already running and jenkins started. Nothing to do!"
        }
        else {
            echo "Worker ${name} is running but jenkins-agent is not started! CANNOT provision job!"
        }
        return false
    }
}