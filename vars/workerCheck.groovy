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
    sh(script: "/home/jenkins/google-cloud-sdk/bin/gcloud compute instances start ${name} --zone ${zone}", returnStdout: true).trim()
}

def call(input, zone) {
    def inputSplit = input.split("-")
    def name = inputSplit[1]

    if (!checkIfUp(name, zone)) {
        echo "Starting ${name} now..."
        startInstance(name, zone)
        echo "Started worker ${name} in ${zone} using gcloud CLI"
        echo "Waiting for jenkins-agent to come up..."
        while(!checkIfJenkinsUp(input)) {
            sleep 10
        }
        return true
    }

    if(checkIfJenkinsUp(input)) {
        echo "Worker ${name} is already running and jenkins started. Nothing to do!"
    }
    else {
        echo "Worker ${name} is running but jenkins-agent is not started! CANNOT provision job!"
    }
    return false   
}