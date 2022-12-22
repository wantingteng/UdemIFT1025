public class Node {
    Node next;
    NodeTete obj;


    //horizontale
    public Node(NodeTete o, Node p) {
        obj = o;
        next = p;

    }
}

    class NodeTete {
        String mots;
        String frequence;

        public NodeTete() {
        }

        public NodeTete(String mots, String frequence) {
            this.mots = mots;
            this.frequence = frequence;

        }

    }

