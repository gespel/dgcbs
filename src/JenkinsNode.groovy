class JenkinsNode {
    private String type;
    private String serverName;
    private String containerName;

    public JenkinsNode(String type, String serverName, String containerName) {
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