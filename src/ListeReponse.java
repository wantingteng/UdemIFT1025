public class ListeReponse {
    Node one;
    public ListeReponse(){}
    public void ajoutFinNode(NodeTete objet)
    {
        Node n = one;
        if (one == null) one = new Node(objet,null);
        else
        {
            while (n.next != null) n = n.next;
            n.next = new Node(objet,null);
        }
    }

    //Vérifiez si le mot peut être trouvé dans listReponse(lr)
    public Node trouverMot(String name){
        Node node=one;
        while(node!=null&&!node.obj.mots.equals(name)){
            node=node.next;
        }
        return node;

    }

    //Trouver le nœud contenant la fréquence
    public Node trouverFrequence(int v){
        Node node=one;
        while(node!=null&&Integer.parseInt(node.obj.frequence)!=v){
            node=node.next;
        }
        return node;

    }

    //Imprimer Structure 1 et Structure 2
    public String printListeReponse(){
        String text="";
        while(one!=null) {
            text=text+" ( "+one.obj.mots+","+one.obj.frequence+")";
            one=one.next;

        }
        return text;
    }


    //supprimer le nœud contenant la fréquence
    public void enlever(int v){
        Node n=one;
        if(one==null)return;
        if(Integer.parseInt(one.obj.frequence)==v){
            one=one.next;
            return;
        }
        while(n.next!=null&&Integer.parseInt(n.next.obj.frequence)!=v)n=n.next;
        if(n.next!=null)n.next=n.next.next;
    }





}
