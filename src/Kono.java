import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public class Kono {
    public static int abs(int x) {
        return (x < 0 ? -x : x);
    }

    public static int etat; // 0 au tour du joueur de selectionner sa pièce,  1 en attente de la destination
    public static CouleurPion joueur ; // CouleurPion.BLANC si c'est le tour du joueur blanc, CouleurPion.NOIR si c'est celui du joueur noir
    
    public static Case caseDep;
    public static Case caseArr;
   
    public static int nbPionBlanc = 8;
    public static int nbPionNoir = 8;
    public static int nbCoupSansPrise = 0;

    public static Plateau unPlateau;
    public static StockPion stockBlanc;
    public static StockPion stockNoir;

    public static Fenetre fenetrePrincipale;
    public static JLabel scoreBlanc;
    public static JLabel scoreNoir;
    public static Border redline = BorderFactory.createLineBorder(Color.red, 10, false);
    public static Border greenline = BorderFactory.createLineBorder(Color.green, 10, false);
    public static Border magentaline = BorderFactory.createLineBorder(Color.magenta,10, false);
    public static Border cyanline = BorderFactory.createLineBorder(Color.cyan,10, false);
    public static Border empty = BorderFactory.createEmptyBorder();

    public static void main(String[] args) {
        fenetrePrincipale = new Fenetre("Kono");
        fenetrePrincipale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Window components
        fenetrePrincipale.ajouterComposants(fenetrePrincipale.getContentPane());

        // Show window
        fenetrePrincipale.pack();
        fenetrePrincipale.setVisible(true);
        joueur = CouleurPion.BLANC;
        etat = 0;
    }
}