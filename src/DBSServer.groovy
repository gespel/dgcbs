class DBSServer {
    private ArrayList<DynamicNode> nodes
    private String name

    public DBSServer(String name) {
        this.name = name
    }

    public getNodes() {
        return nodes
    }
    public addNode(DynamicNode new) {
        this.nodes.add(new)
    }
    public removeNode(String name) {
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