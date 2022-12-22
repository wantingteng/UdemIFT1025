public class Liste {
    Node first;
    Liste premierListe;
    Liste suite;
    String listName;

    public Liste(){}
    public Liste(String name,Liste nextlist){
        listName=name;
        suite=nextlist;

    }


    //Ajouter un nœud à la fin de la liste horizontale
    public void ajoutFinNode(NodeTete objet)
    {
        Node n = first;
        if (first == null) first = new Node(objet,null);
        else
        {
            while (n.next != null) n = n.next;
            n.next = new Node(objet,null);
        }
    }


    //Créer des la liste chaînée verticale, ajouter de nouvelles listes verticalement
    public void ajoutFinListe(String name){
        Liste l=premierListe;
        if(premierListe==null)premierListe=new Liste(name,null);
        else{
            while(l.suite!=null)l=l.suite;
            l.suite=new Liste(name,null);
        }


    }


    //Trouvez la liste au bas de la liste chaînée verticale
    public Liste trouverFin(){
        Liste l=premierListe;
        while(l.suite!=null)l=l.suite;
        return l;
    }


    //Détecter si la liste chaînée verticale dans la structure 2 peut trouver le mot en tant que paramètre
    public Liste trouverListe(String name){
        Liste list = premierListe;
        while(list!=null&&!list.listName.equals(name)){
                 list=list.suite;
        }
        return list;
    }



    //pour la methode afficher();
    //Imprimer le contenu de la structure 1 et de la structure 2 dans la console
    public String printStructure() {
        String text="";
        Liste l1 = premierListe;
        while (l1 != null) {
            text += l1.listName+"-->";
            Node element = l1.first;
            while (element != null) {
                text+=element.obj.mots+", "+element.obj.frequence+"  --> ";
                element=element.next;
            }
            text+="null\n";
            l1=l1.suite;
        }

        return text;
    }

}
