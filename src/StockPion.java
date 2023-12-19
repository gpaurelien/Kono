import javax.swing.*;
import java.awt.*;

public class StockPion extends JPanel {
    public Case[][] monStockPion;
    private CouleurPion couleur;
    private int indi, indj;

    public StockPion(CouleurPion couleur) {
        this.couleur = couleur;
        indi = 0;
        indj = 0;
        monStockPion = new Case[4][2];

        for (int i = 0; i <  4; i++) {
            for (int j = 0; j < 2; j++) {
                monStockPion[i][j] = new Case(Color.GRAY, i, j, 1);
            }
        }

        this.setLayout(new GridLayout(4, 2));
    }

    public CouleurPion getColor() {
        return couleur;
    }


    public void ajouterPion(Pion p) {
        if (indi < 4 && indj < 2) {
            monStockPion[indi][indj].setPion(p);
            indj++;
            if (indj == 2) {
                indi++;
                indj = 0;
            }
        } else { System.out.println("Le stock est plein. Impossible d'ajouter plus de pions."); }
    }

    public Pion enleverPion() {
        if (indj == 0) {
            indi--;
            indj = monStockPion[0].length - 1;
        } else { indj--; }

        Pion p = monStockPion[indi][indj].getPion();
        System.out.println(monStockPion[indi][indj].getPion().toString());
        monStockPion[indi][indj].setPion(null);

        return p;
    }

    public static void main(String[] args) {
        StockPion s = new StockPion(CouleurPion.NOIR);

        Pion p1 = new Pion(CouleurPion.NOIR);
        Pion p2 = new Pion(CouleurPion.NOIR);
        Pion p3 = new Pion(CouleurPion.NOIR);
        Pion p4 = new Pion(CouleurPion.NOIR);
        Pion p5 = new Pion(CouleurPion.NOIR);
        Pion p6 = new Pion(CouleurPion.NOIR);

        s.ajouterPion(p1);
        s.ajouterPion(p2);
        s.ajouterPion(p3);
        s.ajouterPion(p4);
        s.ajouterPion(p5);
        s.ajouterPion(p6);

        Case c = s.monStockPion[0][0];

        System.out.println("c := " + c.getPion().toString());
    }
}