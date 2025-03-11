class DBSServer {
    private ArrayList<DynamicNode> nodes
    private String name

    public DBSServer(String name) {
        this.name = name
        this.nodes = new ArrayList<DynamicNode>();
    }

    public ArrayList<DynamicNode> getNodes() {
        return nodes
    }
    public void addNode(DynamicNode newNode) {
        this.nodes.add(newNode)
    }
    public void removeNode(String name) {
        for(int i = 0; i < this.nodes.size(); i++) {
            if(nodes[i].getName().equalsIgnoreCase(name)) {
                this.nodes.remove(i)
                break
            }
        }
    }

    public getName() {
        return name
    }
}