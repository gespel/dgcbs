import JenkinsTerraform

class InstanceCreator {
    public InstanceCreator() {

    }

    public String createInstance(String name) {
        def jtf = new JenkinsTerraform("workspace/createNewVM/jenkins-gcloud-infra/jenkins/slave")

        return jtf.init() + "\n" + jtf.apply("-var=instance_name=my-test-instance")
    }
}