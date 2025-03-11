class JenkinsNode {
    private String class;
    private String serverName;
    private String containerName;

    public JenkinsNode(class, serverName, containerName) {
        this.class = class;
        this.serverName = serverName;
        this.containerName = containerName;
    }

    public String getServerName() {
        return this.serverName;
    }

    public String getContainerName() {
        return this.containerName;
    }
}