import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

//twt
public class Dictionnaire extends Component {


    ArrayList<String> mots = new ArrayList<>();
    // contenir une structure de données, contenant l’ensemble de mots connus.


    //La méthode qui lit le mot dans le dictionnaire, qui est stockée ici
    public void lireMethode_dic() {
        JFileChooser directory = new JFileChooser();
        directory.showOpenDialog(this);
        File myObj = directory.getSelectedFile();//Le chemin absolu où se trouve le fichier
        BufferedReader reader;

        try {

            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                mots.add(data);
            }

            myReader.close();
        } catch (FileNotFoundException m) {
            System.out.println("An error occurred.");
            m.printStackTrace();
        }
    }

    //Détecter la distance entre deux mots
    public  int distance(String s1, String s2){
        int edits[][]=new int[s1.length()+1][s2.length()+1];
        for(int i=0;i<=s1.length();i++)
            edits[i][0]=i;
        for(int j=1;j<=s2.length();j++)
            edits[0][j]=j;
        for(int i=1;i<=s1.length();i++){
            for(int j=1;j<=s2.length();j++){
                int u=(s1.charAt(i-1)==s2.charAt(j-1)?0:1);
                edits[i][j]=Math.min(
                        edits[i-1][j]+1,
                        Math.min(
                                edits[i][j-1]+1,
                                edits[i-1][j-1]+u
                        )
                );
            }
        }
        return edits[s1.length()][s2.length()];
    }




}
