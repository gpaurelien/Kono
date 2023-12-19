import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import static java.lang.String.valueOf;

public class Case extends JButton implements ActionListener {
    private int typeCase;  // si typeCase == 0, case du plateau, si typeCase == 1 case du stock
    private ImageIcon imagePion;
    private Pion pion;
    private Color couleurFond;
    private boolean occupe;
    private int abscisse;
    private int ordonnee;


    public Case(Color couleur, int abs, int ord, int typeCase) {
		couleurFond = couleur;
		this.setBackground(couleur);
		this.setPreferredSize(new Dimension(100, 100));
		abscisse = abs;
		ordonnee = ord;
		this.typeCase = typeCase;

		// ?
		occupe = false;
		pion = null;

		addActionListener(this);
    }

	private void loadImage() {
		try {
			String imagePath = "img/guepard.png";

			File imageFile = new File(imagePath);

			if (!imageFile.exists()) {
				throw new IOException("L'image n'existe pas : " + imagePath);
			}

			imagePion = new ImageIcon(imagePath);

			setIcon(imagePion);
		} catch (IOException e) {
		e.printStackTrace();
			System.out.println("Erreur lors du chargement");
		}
	}

    public Pion getPion() {
		return pion;
    }

    public void setPion(Pion p) {
		pion = p;
    }

    public int getAbscisse() {
		return abscisse;
    }

    public int getTypeCase() {
		return typeCase;
    }

    public int getOrdonnee() {
		return ordonnee;
    }

    public boolean isOccupe() {
		return (pion != null);
    }

	public void actionPerformed(ActionEvent e) {
		// To complete
		if (Kono.etat == 0) {
			// Réactiver le bouton annuler
			Fenetre.boutonAnnuler.setEnabled(false);
	
			// Si on n'a pas encore sélectionné de case de départ
			Kono.caseDep = ((Case) e.getSource());
			Kono.caseDep.setBorder(Kono.redline);
		} else {
			if (Kono.etat == 1) {
				// Identification de la case possible d'arrivée
				Kono.caseArr = ((Case) e.getSource());
	
				boolean finPartieBloque = false;
				boolean finPartieNbPion = false;
				boolean finPartieSansPrise = false;
	
				if (Kono.caseDep.getTypeCase() == 0) {
					if (Kono.unPlateau.coupValide(Kono.caseDep, Kono.caseArr) == 1) {
						// Si c'est un déplacement simple
						// 1. On déplace le pion
						// 2. On vérifie que le nombre de coups sans prise est inférieur à 25
						// 3. On vérifie que le prochain joueur pourra jouer
					} else {
						if (Kono.unPlateau.coupValide(Kono.caseDep, Kono.caseArr) == 2) {
							// Si c'est un déplacement avec prise
							// 1. On déplace le pion
							// 2. On vérifie que le prochain joueur a plus d'un pion
							// 3. On vérifie que le prochain joueur pourra jouer
						} else {
							// La case d'arrivée sélectionnée n'est pas valide
							if (Kono.caseArr.getPion() != null) {
								// Si elle contient un pion de la couleur du joueur, elle devient la case d'arrivée
							}
						}
					}
				}

				Kono.scoreBlanc.setText("Score joueur BLANC \n " + valueOf(Kono.nbPionBlanc));
				Kono.scoreNoir.setText("Score joueur NOIR \n" + valueOf(Kono.nbPionNoir));
				Kono.fenetrePrincipale.repaint();
				Kono.fenetrePrincipale.validate();

				Fenetre.boutonAnnuler.setEnabled(true);
	
				if (finPartieNbPion || finPartieBloque || finPartieSansPrise) {
					// Si la partie est finie pour une des raisons de fin de partie
					// Ouvrir une boîte de dialogue pour signifier que le partie est finie :
					// 2 choix : fermer le jeu ou recommencer
					JOptionPane d = new JOptionPane();

					String lesTextes[] = {
						"Recommencer",
						"Fermer le jeu"
					};
					// Indice du bouton qui a été cliqué ou CLOSED_OPTION
					int retour = 0;
					if (finPartieSansPrise) {
						if (Kono.nbPionBlanc == Kono.nbPionNoir)
							retour = d.showOptionDialog(this, "Partie terminée car plus de 25 coups sans prise. Les joueurs sont à égalité", "Fin de jeu", 1, 1, new ImageIcon(), lesTextes, lesTextes[0]);
						else {
							if (Kono.nbPionBlanc > Kono.nbPionNoir)
								Kono.joueur = CouleurPion.NOIR;
							else
								Kono.joueur = CouleurPion.BLANC;
							retour = d.showOptionDialog(this, "Partie terminée car plus de 25 coups sans prise. Le joueur " + Kono.joueur + " a perdu !", "Fin de jeu", 1, 1, new ImageIcon(), lesTextes, lesTextes[0]);
						}
					} else {
						if (finPartieNbPion) {
							retour = d.showOptionDialog(this, "Partie terminée car le joueur " + Kono.joueur + " n'a plus qu'un pion. Il a perdu !", "Fin de jeu", 1, 1, new ImageIcon(), lesTextes, lesTextes[0]);
						} else {
							if (finPartieBloque) {
								retour = d.showOptionDialog(this, "Partie terminée car le joueur " + Kono.joueur + " est bloqué. Il a perdu !", "Fin de jeu", 1, 1, new ImageIcon(), lesTextes, lesTextes[0]);
							}
						}
					}

					if (retour == 0) {
						Kono.unPlateau.reinitialiser();
					} else {
						Kono.fenetrePrincipale.dispose();
					}
				}
			}
		}
	}
	
    public String toString() {
        return "";
    }

	public static void main(String[] args) {

	}
}