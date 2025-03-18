class InstanceCreator {
    public InstanceCreator() {

    }

    public String executeGCloudCommand(String command) {
        def sout = new StringBuilder(), serr = new StringBuilder()
        def proc = '/var/lib/jenkins/google-cloud-sdk/bin/gcloud $command'.execute()
        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(100000)
        return "$sout $serr"
    }

    public String createInstance() {
        return this.executeGCloudCommand("compute instances list")
    }
}