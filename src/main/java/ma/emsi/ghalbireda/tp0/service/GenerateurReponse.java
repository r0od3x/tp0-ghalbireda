package ma.emsi.ghalbireda.tp0.service;

import jakarta.enterprise.context.Dependent;

import java.io.Serializable;

/**
 * Classe de service qui génère la réponse à une question.
 * <p>
 * Traitement personnel (exercice bonus du TP 0) : la question est chiffrée avec
 * un chiffre de César dont le décalage est égal au numéro de la question dans la
 * conversation. La première question est décalée de 1 lettre, la deuxième de 2,
 * et ainsi de suite. Le même texte donne donc une réponse différente selon le
 * moment où il est posé, ce qui montre que le service garde un état pendant
 * toute la durée de la conversation.
 * <p>
 * L'état (le compteur de questions) peut être gardé ici parce que la portée
 * Dependent fait vivre cette instance aussi longtemps que le backing bean qui
 * l'injecte, dont la portée est view. Le bouton "Nouveau chat" change de vue,
 * donc une nouvelle instance est créée et le compteur repart à zéro.
 */
@Dependent // Portée CDI pour rendre la classe injectable par CDI dans les autres classes.
// Portée Dependent : l'instance sera supprimée quand la classe qui l'a injectée sera supprimée.
public class GenerateurReponse implements Serializable { // Serializable car CDI peut mettre l'instance en mémoire secondaire.

    /**
     * Nombre de questions déjà posées dans la conversation en cours.
     * Sert aussi de décalage pour le chiffre de César.
     */
    private int numeroQuestion = 0;

    /**
     * Nombre de lettres de l'alphabet, pour le chiffre de César.
     */
    private static final int NB_LETTRES = 26;

    /**
     * Génère une réponse à une question en la chiffrant avec un chiffre de César.
     *
     * @param question    La question à chiffrer.
     * @param roleSysteme Le rôle système choisi par l'utilisateur. Ce traitement ne
     *                    l'utilise pas, mais il fait partie de la signature imposée
     *                    par le backing bean fourni dans l'énoncé.
     * @return Le numéro de la question, le décalage appliqué, puis la question chiffrée.
     */
    public String genererReponse(String question, String roleSysteme) {
        this.numeroQuestion++;
        int decalage = this.numeroQuestion;
        return "Question n°" + this.numeroQuestion
                + " : chiffre de César avec un décalage de " + decalage + "\n"
                + chiffrer(question, decalage);
    }

    /**
     * Chiffre un texte avec un chiffre de César.
     * Seules les lettres non accentuées de a à z et de A à Z sont décalées ;
     * les autres caractères (espaces, chiffres, ponctuation, lettres accentuées)
     * sont recopiés tels quels.
     *
     * @param texte    Le texte à chiffrer.
     * @param decalage Le nombre de lettres du décalage.
     * @return Le texte chiffré.
     */
    private String chiffrer(String texte, int decalage) {
        // Ramène le décalage entre 0 et 25, même s'il dépasse la taille de l'alphabet.
        int d = decalage % NB_LETTRES;
        StringBuilder resultat = new StringBuilder(texte.length());
        for (char caractere : texte.toCharArray()) {
            if (caractere >= 'a' && caractere <= 'z') {
                resultat.append((char) ('a' + (caractere - 'a' + d) % NB_LETTRES));
            } else if (caractere >= 'A' && caractere <= 'Z') {
                resultat.append((char) ('A' + (caractere - 'A' + d) % NB_LETTRES));
            } else {
                resultat.append(caractere);
            }
        }
        return resultat.toString();
    }
}
