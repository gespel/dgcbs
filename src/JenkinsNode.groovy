class JenkinsNode {
    private String type;
    private String serverName;
    private String containerName;

    public JenkinsNode(type, serverName, containerName) {
        this.type = type;
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