class InstanceCreator {
    public InstanceCreator() {

    }

    public String createInstance() {
        def sout = new StringBuilder(), serr = new StringBuilder()
        def proc = 'ls'.execute()
        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(1000)
        return "out> $sout\nerr> $serr"
    }
}