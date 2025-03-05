def call(name, zone) {
    echo "Starting worker ${name} in zone ${zone}"
    def status = sh(script: "", returnStdout: true).trim()
    echo "Status is: " + status
    return status
}
