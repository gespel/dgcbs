def call(name, zone) {
    def command = "/home/jenkins/google-cloud-sdk/bin/gcloud compute instances describe ${name} --zone ${zone} --format='get(status)'"
    
    def process = command.execute()
    process.waitFor()

    def status = process.text.trim()

    if (status == "RUNNING") {
        return true
    } else {
        return false
    }
}
