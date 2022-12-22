//Wanting Teng 20179470
//Yuxiang Lin  20172116

//2022/12/17

//C'est un système de recherche d’information (engin de recherche) simplifié.
//Ce système doit indexer un ensemble de textes (documents), construire des structures
//d’index et les utiliser pour retrouver les textes correspondant à une requête de l’utilisateur.


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Engin extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new Engin();
    }

    JButton b_indexer;
    JButton b_inverser;
    JButton b_afficher;
    JButton b_recherche;
    JTextArea textarea;
    JTextField textfield;
    JScrollPane jsp;

   public Engin(){
       JFrame jf=new JFrame();
       jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       jf.setLayout(null);
       jf.setSize(900,700);

       b_indexer = new JButton("indexer");
       b_inverser = new JButton("inverser");
       b_afficher = new JButton("afficher");
       b_recherche = new JButton("Recherche");

       b_indexer.setBounds(100,100,100,50);
       b_inverser.setBounds(300,100,100,50);
       b_afficher.setBounds(500,100,100,50);
       b_recherche.setBounds(100,200,100,50);

       textarea = new JTextArea(30, 40);
       textfield=new JTextField(15);
       jsp=new JScrollPane(textarea);
       jsp.setBounds(150,260,500,300);
       textfield.setBounds(270,210,200,30);

       jf.add(b_indexer);
       jf.add(b_inverser);
       jf.add(b_afficher);
       jf.add(b_recherche);
       jf.add(jsp);
       jf.add(textfield);
       jf.setVisible(true);

       b_indexer.addActionListener(this);
       b_inverser.addActionListener(this);
       b_afficher.addActionListener(this);
       b_recherche.addActionListener(this);
       textfield.addActionListener(this);

   }

   //Trier le tableau alphabétiquement par mot
    public String[] bubbleSort(String[] arr) {

        for (int i = 1; i < arr.length; i++) {

            for (int j = 0; j <= arr.length -1 - i; j++) {
                if (arr[j].compareTo(arr[j + 1])>0) {
                    String temp;
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        return arr;

    }

    //Utilisé conjointement avec la méthode convertirTab
    // pour obtenir le nombre de lignes du tableau à deux dimensions initialisé
    public int getRowNum(String[] arr){
        int temp=0;
        int m=0;
        int repeat=0;
        int row=0;
        for(int i=0;i<arr.length;i=i+temp){

            int frequence=1;
            for(int j=i+1;j<arr.length;j++){
                if(arr[i].equals(arr[j])){

                    frequence=frequence+1;
                }
            }
            if(frequence!=1){
                repeat=repeat+frequence-1;
            }
            m=m+1;
            temp=frequence;
        }
        row=arr.length-repeat;
        return row;
   }


        String[] texteTraiteSansSort;

        String[][] motEtFrequence;


        //convertir le tableau trié en un autre tableau avec le mot et la fréquence
        //associée (il n’y a plus de répétition de mots dans ce tableau : par exemple [(A,2), (B,1)]);
        public String[][] convertirTab(String txt){

            // split en mots et les stocker dans un tableau
            texteTraiteSansSort =txt.toLowerCase().replaceAll("\\p{Punct}", " ").split("\\s+");


            String[] texteTraiteComplete=bubbleSort(texteTraiteSansSort);

            int temp=0;
            int m=0;
            int row= getRowNum(texteTraiteComplete);
            motEtFrequence = new String[row][2];      //initialiser le tableau 2D
            for(int i=0;i<texteTraiteComplete.length;i=i+temp){

                int frequence=1;
                for(int j=i+1;j<texteTraiteComplete.length;j++){

                    //Si le même mot apparaît, la fréquence augmentera automatiquement
                    if(texteTraiteComplete[i].equals(texteTraiteComplete[j])){
                        frequence=frequence+1;
                    }

                }
                motEtFrequence[m][0]=texteTraiteComplete[i];
                motEtFrequence[m][1]=String.valueOf(frequence);
                m=m+1;
                temp=frequence;
            }


           return  motEtFrequence;

        }


        public void afficher() {
             textarea.append("sturcture1: ");
             textarea.append(listStructure1.printStructure());

             textarea.append("structure2: ");

             textarea.append(listStructure2.printStructure());

     }



        Liste listStructure1 =new Liste(null,null);
        Liste listeCourant1;

        public void indexer(String source) {
            File file = new File(source);

            if(file.isDirectory()){                 //open a directory
                files=file.listFiles();
                fileName=file.list();               //Obtenir une liste de noms de fichiers

                for(int i=0;i<files.length;i++){
                    try(BufferedReader br = new BufferedReader(new FileReader(files[i]))) {

                        String line;
                        String text = "";
                        while((line=br.readLine())!=null){
                            text+=line+'\n';
                        }
                        br.close();

                        //commencer à étendre la liste verticale
                        String[][] nodes = convertirTab(text);
                        listStructure1.ajoutFinListe(fileName[i]);

                        for(String[] node:nodes){

                  //Chaque fois qu'il est étendu horizontalement à la liste la plus basse de la liste verticale
                            listeCourant1 = listStructure1.trouverFin();

                            //commencer à ajouter des nœuds dans chaque liste horizontale
                            //Chaque nœud contient un mot et sa fréquence
                            listeCourant1.ajoutFinNode(new NodeTete(node[0],node[1]));

                        }

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }

            }else{                              //open a file
                try ( BufferedReader br = new BufferedReader(new FileReader(file))){
                    String line= null;
                    String text = "";
                    while((line=br.readLine())!=null){
                        text+=line+'\n';
                    }

                    //same as open a directory
                    String[][] nodes = convertirTab(text);
                    listStructure1.ajoutFinListe(file.getName());
                    for(String[] node:nodes){
                        listeCourant1 = listStructure1.trouverFin();
                        listeCourant1.ajoutFinNode(new NodeTete(node[0],node[1]));

                    }

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }



        }//inderxer

    Liste listStructure2 =new Liste(null,null);
    Liste listeCourant2;
    public void indexerInv(){

        Liste l1=listStructure1.premierListe;

            while(l1!=null){
                    Node element=l1.first;
                    while(element!=null){

                        //Si le mot n’est pas trouvé, on insère un nouveau nœud dans la liste
                        //verticale dans l’ordre, et on crée un nœud dans sa liste contenant le
                        //document et la fréquence du mot dans ce document

                        if(listStructure2.trouverListe(element.obj.mots)==null){
                            listStructure2.ajoutFinListe(element.obj.mots);
                            listeCourant2= listStructure2.trouverFin();
                            listeCourant2.ajoutFinNode(new NodeTete(l1.listName,element.obj.frequence));

                        }else {

                            //Si le mot est trouvé, on insère un nouveau nœud à la fin de sa liste
                            //chainée (horizontale), contenant le document et la fréquence du mot
                            //dans ce document;

                            Liste found = listStructure2.trouverListe(element.obj.mots);
                            found.ajoutFinNode(new NodeTete(l1.listName,element.obj.frequence));
                        }

                        element=element.next;
                    }

                l1=l1.suite;
                }



            }


      //Enregistrez combien de fois le mot interrogé apparaît dans quel fichier
      // pour former une nouvelle liste chainee
    public Liste extraiter(String requete){
        Liste listeExtrait=new Liste();

        String[] requeteTab=requete.toLowerCase().replaceAll("\\p{Punct}", " ").split("\\s+");

        for(int i=0;i<requeteTab.length;i++){
            if(listStructure2.trouverListe(requeteTab[i])!=null){
                listeExtrait.ajoutFinListe(requeteTab[i]);
                Node elemente=listStructure2.trouverListe(requeteTab[i]).first;
                while(elemente!=null){
                    listeExtrait.trouverFin().ajoutFinNode(elemente.obj);
                    elemente=elemente.next;
                }
            }
        }
        return listeExtrait;
    }

    ListeReponse lr;
    ListeReponse listeFinale;
    Liste l;
    public ListeReponse rechercher(String requete){
        lr=new ListeReponse();
        listeFinale=new ListeReponse();

       l=extraiter(requete).premierListe;

       //Obtenez La liste de document avec fréquence globale
        while(l!=null){
            Node n=l.first;
            while(n!=null){
                //Si le mot n'est pas trouvé, créez un nouveau nœud
                if(lr.trouverMot(n.obj.mots)==null){
                    lr.ajoutFinNode(n.obj);
                }else{
                    //Si le mot existe déjà dans la liste, cumulez sa fréquence
                    Node trouve=lr.trouverMot(n.obj.mots);
                    int numOriginal=Integer.parseInt(trouve.obj.frequence);
                    int numTrouve=Integer.parseInt(n.obj.frequence);
                    int sum=numOriginal+numTrouve;
                    trouve.obj.frequence=sum+"";
                }
                n=n.next;
            }
            l=l.suite;
        }

        //Obtenez la liste de réponses triée
        //Utilisez la fréquence dans le premier nœud de la liste lr comme valeur maximale,
        // parcourez toute la liste et remplacez la valeur maximale par ce nombre lorsque la fréquence
        // dans les nœuds restants est supérieure
        while(lr.one!=null) {
            Node nodeCurrent = lr.one;
            int maxNum = Integer.parseInt(lr.one.obj.frequence);
            while (nodeCurrent != null) {
                if (Integer.parseInt(nodeCurrent.obj.frequence) >= maxNum) {
                    maxNum = Integer.parseInt(nodeCurrent.obj.frequence);
                }

                nodeCurrent = nodeCurrent.next;

            }

            //Ajouter le nœud contenant la fréquence maximale à la liste finale
            listeFinale.ajoutFinNode(lr.trouverFrequence(maxNum).obj);

            lr.enlever(maxNum);//Après la traversée, supprimez le nœud
        }

        return listeFinale;

    }

    File[] files; String[] fileName;

    @Override
    public void actionPerformed(ActionEvent e){
       if(e.getSource()==b_indexer){

           JFileChooser directory = new JFileChooser();
           directory.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
           directory.showOpenDialog(this);
           File file = directory.getSelectedFile();//Le chemin absolu où se trouve le fichier
           indexer(file.getAbsolutePath());

       }
       if(e.getSource()==b_inverser){
           indexerInv();

       }
       if(e.getSource()==b_afficher){
           afficher();
       }
       if(e.getSource()==b_recherche){

           textarea.setText("");             //reset textarea
          rechercher(textfield.getText());
          textarea.setLineWrap(true);       //wrap lines automatically
          textarea.append(listeFinale.printListeReponse());

       }

    }


}
