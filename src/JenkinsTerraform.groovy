public class JenkinsTerraform {
    private String path;
    public JenkinsTerraform(String path) {
        this.path = path
    }

    public String init() {
        def sout = new StringBuilder(), serr = new StringBuilder()

        def proc = "terraform -chdir=./${path} -no-color init".execute()
        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(1000000)

        return "$sout$serr"
    }

    public String apply(String vars) {
        def sout = new StringBuilder(), serr = new StringBuilder()

        def proc = "terraform -chdir=./${path} -no-color apply ${vars} -auto-approve".execute()
        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(1000000)

        return "$sout$serr"
    }
}