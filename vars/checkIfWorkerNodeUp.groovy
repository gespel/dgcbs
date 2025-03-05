def call(name, zone) {
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
