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


def call(name, zone) {
    if (!checkIfUp(name, zone)) {
        echo "Starting ${name} now..."
        def status = sh(script: "/home/jenkins/google-cloud-sdk/bin/gcloud compute instances start ${name} --zone ${zone}",
                    returnStdout: true).trim()
        echo "Status is: " + status
        return true
    }
    else {
        echo "Worker ${name} is already running. Nothing to do!"
        return false
    }
}