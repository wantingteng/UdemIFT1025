import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MyFrame extends JFrame implements ActionListener,MouseListener{

    public static void main(String[] args) {
        new MyFrame();
    }
    //declarer des variables
    JButton button1;
    JButton button2;
    JButton button3;
    JButton button4;
    JButton button5;
    JTextArea text;


    //Initialisation de l'affichage de la page fenêtre
    public MyFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        JPanel jp1=new JPanel();
        JPanel jp2=new JPanel();
        add(jp1);
        add(jp2);


        button1 = new JButton("Dictionnaire");
        button2 = new JButton("Fichier");
        button3 = new JButton("Verifier");
        button4 = new JButton("ouvrir");
        button5 = new JButton("enregistrer");

        button4.setVisible(false);
        button5.setVisible(false);

        jp2.add(menu);
        jp1.add(button1);
        jp1.add(button2);
        jp1.add(button3);
        jp1.add(button4);
        jp1.add(button5);

        text = new JTextArea(30, 30);
        jp2.add(text);

        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
        button5.addActionListener(this);
        text.addMouseListener(this);

        pack();
        setVisible(true);

    }

    //Créer un objet, afin d'utiliser les méthodes de la classe dans laquelle l'objet réside
    Dictionnaire dictionnaire=new Dictionnaire();

    //Ouvrez le fichier et transférez le contenu dans la zone de texte
    public void lireMethode_ouv() {

        JFileChooser directory = new JFileChooser();
        directory.showOpenDialog(this);
        File file = directory.getSelectedFile();
        FileReader filereader; //flow de donnees

        try {
            filereader = new FileReader(file);
            BufferedReader bufferreader = new BufferedReader(filereader);
            String aline;

            while ((aline = bufferreader.readLine()) != null) {
                //Lire le texte ligne par ligne, afficher dans TEXTAREA
                text.append(aline + "\r\n");

            }
            filereader.close();
            bufferreader.close();

        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            dictionnaire.lireMethode_dic();// button-dictionnaire La méthode qui lit le mot dans le dictionnaire

        } else if (e.getSource() == button2) {//En cliquant sur ce bouton,les boutons "Ouvrir"et"Enregistrer"s'affichent

            button4.setVisible(true);
            button5.setVisible(true);

        } else if (e.getSource() == button4) { //ouvrir le fichier

            lireMethode_ouv();

        } else if (e.getSource() == button3) { //verification
            String t = text.getText();
            String result = t.replaceAll("\\p{Punct}", " ");  //supprimer toute ponctuation

            String[] messageArr = result.split("\\s+");//supprimer tous les espaces


            for (int i = 0; i < messageArr.length; i++) {
                int tempNum=0;
                for (int j = 0; j < dictionnaire.mots.size(); j++) {
                    if (!messageArr[i].equals(dictionnaire.mots.get(j))){
                        //Si le mot n'est égal à aucun des mots connus du dictionnaire, le nombre est incrémenté d'un
                        tempNum+=1;
                    }

                }
                //Si le nombre est égal au nombre de mots dans le dictionnaire,
                // cela prouve que le mot est bien une faute de frappe-->highlight
                if(tempNum==dictionnaire.mots.size()){
                    try {
                        highlight(messageArr[i]);
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        }//button3

       if(e.getSource()==item0 || e.getSource()==item1 || e.getSource()==item2 || e.getSource()==item3||
               e.getSource()==item4){
              word=e.getActionCommand();//word's name = name of menuitem that you click
           text.replaceRange(word,start,end);//replace wrong words
        }

       if(e.getSource()==button5){
           saveFile();

       }


    }

   //add highlight on wrong words --red color
    public void highlight(String pattern) throws BadLocationException {
        Highlighter hilite = text.getHighlighter();
        Document doc = text.getDocument();
        String docText = doc.getText(0, doc.getLength());

        hilite.addHighlight(docText.indexOf(pattern), docText.indexOf(pattern)+pattern.length(), myHighlightPainter);
    }

    Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.red);
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }

    //declarer des variables
    ArrayList<String> distanceMots =new ArrayList<>();//pour stocker 5 mots le plus proche sur ici
    JPopupMenu menu=new JPopupMenu();
    JMenuItem item0;
    JMenuItem item1;
    JMenuItem item2;
    JMenuItem item3;
    JMenuItem item4;

    String word; //le mot qu'on clique
    int start;
    int end;

    @Override
    public void mouseClicked(MouseEvent e) {

        int offset = text.viewToModel(e.getPoint());

        try {
            start = Utilities.getWordStart(text, offset);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }

        try {
            end = Utilities.getWordEnd(text, offset);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }

        try {
            word = text.getDocument().getText(start, end - start);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }


        //événement déclencheur de clic droit
        if (e.getButton() == MouseEvent.BUTTON3) {
            int num = 0;
            for (int k = 0; k < dictionnaire.mots.size(); k++) {
                if (!word.equals(dictionnaire.mots.get(k))) {
                    num += 1;
                }
            }

            //si ce sont bien les mots inconnus, jpopupmenu va apparaitre

            if (num == dictionnaire.mots.size() && !word.trim().isEmpty()) {

                for (int diff = 0; diff < 3; diff++) {      //La distance entre le mot cliqué et le mot du dictionnaire
                    for (int i = 0; i < dictionnaire.mots.size(); i++) {
                        if (dictionnaire.distance(word, dictionnaire.mots.get(i)) <= diff + 1) {
                            distanceMots.add(dictionnaire.mots.get(i));
                        }
                    }
                    if (distanceMots.size() == 5) {//Si après la première boucle, il y a exactement cinq mots, parfait
                        break;
                    } else if (distanceMots.size() > 5) {

                        //S'il y a plus de cinq mots après la première boucle, supprimez la partie en excès

                        int initial = distanceMots.size();
                        for (int delete = 0; delete < initial - 5; delete++) {
                            distanceMots.remove(distanceMots.size() - 1);
                        }
                        break;
                    } else {
                        //Si le nombre de mots obtenus après la première boucle est inférieur à cinq,
                        // entrez dans la deuxième boucle pour trouver plus de mots avec la distance la plus proche
                        distanceMots.clear();
                    }
                }

                menu.removeAll();// refresh

                item0=new JMenuItem(distanceMots.get(0));
                item0.addActionListener(this);
                menu.add(item0);

                item1=new JMenuItem(distanceMots.get(1));
                item1.addActionListener(this);
                menu.add(item1);

                item2=new JMenuItem(distanceMots.get(2));
                item2.addActionListener(this);
                menu.add(item2);

                item3=new JMenuItem(distanceMots.get(3));
                item3.addActionListener(this);
                menu.add(item3);

                item4=new JMenuItem(distanceMots.get(4));
                item4.addActionListener(this);
                menu.add(item4);

                if(word!=null){

                menu.show(text,e.getX(),e.getY());}
                distanceMots.clear();//refresh

            }
        }

    }

    //la methode pour enregistrer le document
    public void saveFile() {
        JFileChooser fileChooser = new JFileChooser();

        int status = fileChooser.showSaveDialog(this);

        if(status == 0){
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()))){
                if(text.getText()!=null){
                    bw.write(text.getText());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }



}

