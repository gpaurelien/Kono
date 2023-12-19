public class Pion {
    private int mvt = 1;
    private int mvtPrise = 2;
    private CouleurPion couleur;    

    public Pion(CouleurPion couleur) {
        this.couleur = couleur;
    }
    
    public int getMvt() {
        return mvt;
    }
    
    public int getMvtP() {
        return mvtPrise;
    }

    public CouleurPion getColor() {
        return couleur;
    }

    public String toString() {
        return "Pion {" +
        "couleur = " + couleur +
        ", amplitude = " + mvt +
        ", amplitude (prise) = " + mvtPrise +
        '}';
    }

    public static void main(String[] args) {
        Pion pNoir = new Pion(CouleurPion.NOIR);

        System.out.println(pNoir.toString());
    }
}