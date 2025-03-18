public class JenkinsTerraform {
    private String path;
    public JenkinsTerraform(String path) {
        this.path = path
    }

    public String init() {
        def sout = new StringBuilder(), serr = new StringBuilder()

        def proc = "terraform -chdir=${path} init -no-color".execute()
        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(1000000)

        return "$sout$serr"
    }

    public String apply(String vars) {
        def sout = new StringBuilder(), serr = new StringBuilder()

        def proc = "terraform -chdir=${path} apply ${vars} -auto-approve -no-color".execute()
        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(1000000)

        return "$sout$serr"
    }
}