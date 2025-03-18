class InstanceCreator {
    public InstanceCreator() {

    }

    public String createInstance() {
        def sout = new StringBuilder(), serr = new StringBuilder()
        def proc = 'gcloud compute instances list'.execute()
        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(1000)
        return "$sout"
    }
}