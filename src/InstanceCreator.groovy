import JenkinsTerraform

class InstanceCreator {
    private String cwd;
    public InstanceCreator(String cwd) {
        this.cwd = cwd
    }

    public String createInstance(String name) {
        def jtf = new JenkinsTerraform("${this.cwd}/jenkins-gcloud-infra/jenkins/slave")

        return jtf.init() + "\n" + jtf.apply("-var=instance_name=my-test-instance")
    }
}