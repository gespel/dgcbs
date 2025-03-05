def call(name, zone) {
    echo "Checking for ${name} in zone ${zone}"
    //def command = "/home/jenkins/google-cloud-sdk/bin/gcloud compute instances describe " + name + " --zone " + zone + " --format=\"get(status)\""
    def commang = "/home/jenkins/google-cloud-sdk/bin/gcloud compute instances list"
    echo command
    def process = command.execute()
    process.waitFor()

    if (process.exitValue() != 0) {
        echo "Fehler beim Ausführen des Befehls: ${process.err.text}"
        return false
    }

    def status = process.text
    echo "Status is: " + status

    if (status == "RUNNING") {
        return true
    } else {
        return false
    }
}
