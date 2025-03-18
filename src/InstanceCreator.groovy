class InstanceCreator {
    public InstanceCreator() {

    }

    public String executeGCloudCommand(String command) {
        def sout = new StringBuilder(), serr = new StringBuilder()
        def proc = "/var/lib/jenkins/google-cloud-sdk/bin/gcloud ${command}".execute()
        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(100000)
        return "$sout$serr"
    }

    public String terraformInitApply(String path) {
        def sout_init = new StringBuilder(), serr_init = new StringBuilder(), sout_apply = new StringBuilder(), serr_apply = new StringBuilder()

        def proc_init = "terraform -chdir=./${path} init".execute()
        proc_init.consumeProcessOutput(sout_init, serr_init)
        proc_init.waitForOrKill(1000000)

        def proc_apply = "terraform -chdir=./${path} apply -var='instance_name=my_custom_vm' -auto-approve".execute()
        proc_apply.consumeProcessOutput(sout_apply, serr_apply)
        proc_apply.waitForOrKill(1000000)
        return "$sout_init$serr_init\n$sout_apply$sout_apply"
    }

    public String createInstance(String name) {
        return this.terraformInitApply("workspace/createNewVM/jenkins-gcloud-infra/jenkins/slave")
    }
}