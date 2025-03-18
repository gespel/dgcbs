class InstanceCreator {
    public InstanceCreator() {

    }

    public String createInstance() {
        def sout = new StringBuilder(), serr = new StringBuilder()
        def proc = '/var/lib/jenkins/google-cloud-sdk/bin/gcloud compute instances list'.execute()
        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(1000)
        return "Out: $sout Error: $serr"
    }
}