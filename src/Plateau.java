import javax.swing.*;
import java.awt.*;

import static java.lang.String.valueOf;

public class Plateau extends JPanel {
    private Case[][] monPlateau;

    public Plateau() {
        this.setLayout(new GridLayout(4, 4));
        monPlateau = new Case[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                monPlateau[i][j] = new Case(Color.GRAY, i, j, 0);

				if(i < 2) {
					monPlateau[i][j].setPion(new Pion(CouleurPion.BLANC));
				} else {
					monPlateau[i][j].setPion(new Pion(CouleurPion.NOIR));
				}

                this.add(monPlateau[i][j]);
            }
        }
    }
    
    public int coupValide(Case dep, Case arr) {
		if (verifieDeplacementHor(dep, arr) || verifieDeplacementVert(dep, arr)) {
			return 1;
		} else if (verifieDeplacementHorPrise(dep, arr) || verifieDeplacementVertPrise(dep, arr)) {
			return 2;
		} else {
			return 0;
		}
    }

    public boolean verifieDeplacementHorPrise(Case dep, Case arr) {
		if (arr.getPion() == null) { return false; } // Early return

		int dir = (arr.getAbscisse() > dep.getAbscisse() ? 1 : -1); // Movement direction
		Case inter = monPlateau[dep.getOrdonnee()][dep.getAbscisse() + dir];

		if (!inter.isOccupe() || inter.getPion().getColor() != Kono.joueur) { return false; }

		return true;
    }

    public boolean verifieDeplacementVertPrise(Case dep, Case arr) {
		if (arr.getPion() == null) { return false; }

		int dir = (arr.getOrdonnee() > dep.getOrdonnee() ? 1 : -1);
		Case inter = monPlateau[dep.getOrdonnee() + dir][dep.getAbscisse()];

		if (!inter.isOccupe() || inter.getPion().getColor() != Kono.joueur) { return false; }

		return true;
    }

    public boolean verifieDeplacementHor(Case dep, Case arr) {
		return (arr.getPion() == null &&
				Math.abs(arr.getAbscisse() - dep.getAbscisse()) == 1 &&
				dep.getOrdonnee() == arr.getOrdonnee()
		);
    }

    public boolean verifieDeplacementVert(Case dep, Case arr) {
		return (arr.getPion() == null &&
				Math.abs(arr.getOrdonnee() - dep.getOrdonnee()) == 1 &&
				dep.getAbscisse() == arr.getAbscisse()
		);
    }
  
    public void jouerCoup(Case dep, Case arr) {
    	if (verifieDeplacementVert(dep,arr) || verifieDeplacementHor(dep,arr)) {
    		arr.setPion(dep.getPion());
    		dep.setPion(null);
    		Kono.etat = 0;
    	}
    	Kono.nbCoupSansPrise++;
        Fenetre.boutonAnnuler.setEnabled(true);
    }
    
    public void jouerCoupPrise(Case dep, Case arr) {
    	if (verifieDeplacementHorPrise(dep, arr) || verifieDeplacementVertPrise(dep, arr)) {
    		if (arr.getPion().getColor() == CouleurPion.BLANC) {
    			Kono.stockNoir.ajouterPion(arr.getPion());
    		} else {
    			Kono.stockBlanc.ajouterPion(arr.getPion());
    		}

    		arr.setPion(dep.getPion());
    		dep.setPion(null);

    		if (Kono.joueur == CouleurPion.BLANC) {
    			Kono.nbPionNoir--;
			} else {
    			Kono.nbPionBlanc--;
			}

    		Kono.etat = 0;
    	}
		Fenetre.boutonAnnuler.setEnabled(true);
    }

	private void changePlayer() {
		Kono.joueur = (Kono.joueur == CouleurPion.BLANC) ? CouleurPion.NOIR : CouleurPion.BLANC;
	}
    
    public boolean testDeplacement() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				
				for (int k = 0; k < 4; k++) {
					for (int l = 0; l < 4; l++) {

						if (coupValide(monPlateau[i][j], monPlateau[k][l]) != 0) {
							return false; // The current player can move
						}

					}
				}
			}
		}

		return true;
    }
    
	public void annulerCoup(Case dep, Case arr) {
		Pion temp = null;
		int nb_colonne = Kono.abs(dep.getAbscisse() - arr.getAbscisse());
		int nb_ligne = Kono.abs(dep.getOrdonnee() - arr.getOrdonnee());
	
		if ((nb_ligne == 0 && nb_colonne == 1) || (nb_ligne == 1 && nb_colonne == 0)) {
			// Dernier coup simple
			System.out.println("Coup simple");
			temp = dep.getPion();
			dep.setPion(arr.getPion());
			arr.setPion(temp);
		}
	
		if ((nb_ligne == 0 && nb_colonne == 2) || (nb_ligne == 2 && nb_colonne == 0)) {
			System.out.println("Coup prise");
			if (Kono.joueur == CouleurPion.BLANC) {
				System.out.println("On annule le coup du joueur noir");
				dep.setPion(arr.getPion());
				arr.setPion(Kono.stockNoir.enleverPion());
				Kono.nbPionBlanc++;
			} else {
				System.out.println("On annule le coup du joueur blanc");
				dep.setPion(arr.getPion());
				arr.setPion(Kono.stockBlanc.enleverPion());
				Kono.nbPionNoir++;
			}
		}
	
		Kono.etat = 0;
	
		if (Kono.joueur == CouleurPion.BLANC) {
			Kono.joueur = CouleurPion.NOIR;
		} else {
			Kono.joueur = CouleurPion.BLANC;
		}
	
		Kono.scoreBlanc.setText("Score Joueur BLANC \n " + valueOf(Kono.nbPionBlanc));
		Kono.scoreNoir.setText("Score Joueur NOIR \n" + valueOf(Kono.nbPionNoir));
	
		Kono.fenetrePrincipale.repaint();
		Kono.fenetrePrincipale.validate();
	
		Fenetre.boutonAnnuler.setEnabled(false);
	}	

    public void reinitialiser() {
		System.out.println("Recommencer");
		Kono.fenetrePrincipale.dispose();

		Kono.fenetrePrincipale = new Fenetre("Kono");

		Kono.fenetrePrincipale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Kono.fenetrePrincipale.ajouterComposants(Kono.fenetrePrincipale.getContentPane());
		
		Kono.joueur = CouleurPion.BLANC;
		Kono.etat = 0;
		Kono.nbPionBlanc = 8;
		Kono.nbPionNoir = 8;
		Kono.nbCoupSansPrise = 0;

		Kono.scoreBlanc.setText("Score joueur BLANC \n "+ valueOf(Kono.nbPionBlanc));
		Kono.scoreNoir.setText("Score joueur NOIR \n" + valueOf(Kono.nbPionNoir));

		Kono.fenetrePrincipale.pack();
		Kono.fenetrePrincipale.setVisible(true);
		Kono.fenetrePrincipale.repaint();
		Kono.fenetrePrincipale.validate();

		Fenetre.boutonAnnuler.setEnabled(false);
    }
}