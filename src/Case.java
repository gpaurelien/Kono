import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.String.valueOf;

public class Case extends JButton implements ActionListener {
    private int typeCase;  // si typeCase == 0, case du plateau, si typeCase == 1 case du stock
    private ImageIcon imagePion;
    private Pion pion;
    private Color couleurFond;
    private boolean occupe;
    private int abscisse;
    private int ordonnee;


    public Case(Color couleur, int ord, int abs, int typeCase) {
		couleurFond = couleur;
		this.setBackground(couleur);
		this.setPreferredSize(new Dimension(100, 100));
		abscisse = abs;
		ordonnee = ord;
		this.typeCase = typeCase;

		// ?
		occupe = true;

		addActionListener(this);
    }

	/* private void loadImage() {
		try {
			String imagePath = "./img/guepard.png";

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
	} */

    public Pion getPion() {
		return pion;
    }

    public void setPion(Pion p) {
		pion = p;
		occupe = true;

		// Interface logic
		if (p != null) {
			if (p.getColor() == CouleurPion.BLANC) {
				imagePion = new ImageIcon("./img/guepard.png");
				this.setIcon(imagePion);
			} else {
				imagePion = new ImageIcon("./img/zebre.png");
				this.setIcon(imagePion);
			}
		} else {
			this.pion = null;
			this.occupe = false;
			imagePion = null;
			this.setIcon(imagePion);
		}
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
		return occupe;
    }

	public void actionPerformed(ActionEvent e) {
		if (Kono.etat == 0) {
			Fenetre.boutonAnnuler.setEnabled(false);

			System.out.println("Nombre de coup(s) sans prise : " + Kono.nbCoupSansPrise);

			Kono.caseDep = ((Case) e.getSource());

			System.out.println("caseDep coords: ord = " + Kono.caseDep.getAbscisse() + " abs = " + Kono.caseDep.getOrdonnee());

			// Is the case occuped?
			if (Kono.caseDep.isOccupe()) {
				if (Kono.caseDep.getPion().getColor() == Kono.joueur) { // We check that the current player color == 'Pion' color
					Kono.caseDep.setBorder(Kono.redline);
					Kono.etat = 1;		
				}
			}
		} else {
			if (Kono.etat == 1) {
				Kono.caseArr = ((Case) e.getSource());
				Kono.caseDep.setBorder(Kono.redline);
				Kono.caseDep.setBorder(Kono.empty);

				System.out.println("caseArr coords: ord = " + Kono.caseArr.getAbscisse() + " abs = " + Kono.caseArr.getOrdonnee());

				boolean finPartieBloque = false;
				boolean finPartieNbPion = false;
				boolean finPartieSansPrise = false;

				if (Kono.nbPionBlanc == 1 || Kono.nbPionNoir == 1) {
					finPartieNbPion = true;
				}
	
				if (Kono.caseDep.getTypeCase() == 0) {
					if (Kono.unPlateau.coupValide(Kono.caseDep, Kono.caseArr) == 1) {
						// Si c'est un déplacement simple
						Kono.caseDep.setBorder(null);
						// 1. On déplace le pion
						Kono.unPlateau.jouerCoup(Kono.caseDep, Kono.caseArr);

						if (Kono.nbCoupSansPrise == 25) {
		    				finPartieSansPrise = true;
						}

						// 3. On vérifie que le prochain joueur pourra jouer
						if (Kono.unPlateau.testDeplacement()) {
							finPartieBloque = true;
						}
					} else {
						if (Kono.unPlateau.coupValide(Kono.caseDep, Kono.caseArr) == 2) {
							// Si c'est un déplacement avec prise
							Kono.caseDep.setBorder(null);
							// 1. On déplace le pion
							Kono.unPlateau.jouerCoupPrise(Kono.caseDep, Kono.caseArr); // -> The game state: 0

							// 2. On vérifie que le prochain joueur a plus d'un pion
							if (Kono.nbPionNoir <= 1 || Kono.nbPionBlanc <= 1) {
								finPartieNbPion = true;
							}

							// 3. On vérifie que le prochain joueur pourra jouer
							/* if (Kono.unPlateau.testDeplacement() == false) {
								finPartieBloque = true;
							} */

							Kono.joueur = (Kono.joueur == CouleurPion.BLANC) ? CouleurPion.NOIR : CouleurPion.BLANC;
						} else {
							// La case d'arrivée sélectionnée n'est pas valide
							if (Kono.caseArr.getPion() != null) {
								// Si elle contient un pion de la couleur du joueur, elle devient la case d'arrivée
								Kono.caseDep = null;
								Kono.etat = 0;
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
}