package a;


import java.text.DecimalFormat;
import java.util.*;

public class Vigenere_BeaufortAlgo {

    private static final int ENCRYPT = 1;
    private static final int DE_ENCRYPT = 2;
    private static final int EXIT = 3;
    private static final List<String> ALFABETI_GJUHES_SHQIPE = new ArrayList<>(Arrays.asList(
            "A", "B", "C", "Ç", "D", "E", "Ë", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "X", "Y", "Z",
            "a", "b", "c", "ç", "d", "e", "ë", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
            "p", "q", "r", "s", "t", "u", "v", "x", "y", "z", " "));


    public static void main(String[] args) {
        chooseOptions();
    }


    /**
     *
     */
    public static void chooseOptions() {
        System.out.println("\n");
        System.out.println("-------- Vigenere shiferim (enkondim dhe dekodim)  --------\n");
        Scanner in = new Scanner(System.in);
        String input = "0";

        try {
            while (Integer.parseInt(input) != EXIT) {
                informoPerdorimin();
                input = in.nextLine();
                if (Integer.parseInt(input) == ENCRYPT) {
                    fillojEnkriptimi(in);
                } else if (Integer.parseInt(input) == DE_ENCRYPT) {
                    fillojDekriptimi(in);
                }
            }
        } catch (InputMismatchException i) {
            System.err.println("Ju nuk keni jepur numer ndermejt 1 deri 3.");
            input = "1";
        }
        System.out.println("Kalo sa me bukur!");
        in.close();
    }


    /**
     * @param plaintext
     * @param key
     */
    private static void encrypt(String plaintext, String key) {
        LinkedHashMap<String, Integer> shkronjaPersiturEnkriptimin = new LinkedHashMap<>();
        final int plaintextSize = plaintext.length();
        final int keySize = key.length();

        final StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < plaintextSize; i++) {
            final int plainNR = plaintext.codePointAt(i);

            final int keyNR = key.codePointAt(i % keySize);

            int pozitjaShkrojnes = ALFABETI_GJUHES_SHQIPE.indexOf((char) plainNR + "");
            int pozitjaQelesit = ALFABETI_GJUHES_SHQIPE.indexOf((char) keyNR + "");
            int pozitjaAlfabet = (pozitjaShkrojnes + pozitjaQelesit) % ALFABETI_GJUHES_SHQIPE.size();
            encryptedText.append(ALFABETI_GJUHES_SHQIPE.get(pozitjaAlfabet));
            analizoFrekuencore(pozitjaShkrojnes, shkronjaPersiturEnkriptimin);

        }

        System.out.println("Mesazhi i enkriptuar : " + encryptedText);
        printoFrekuencoretShkronave("Frekuencore e shkrovae ne enkremtpim", shkronjaPersiturEnkriptimin);
    }

    /**
     * @param encryptedText
     * @param key
     */
    private static void decrypt(String encryptedText, String key) {
        LinkedHashMap<String, Integer> shkronjaPersiturDekriptimin = new LinkedHashMap<>();
        final int encryptedTextSize = encryptedText.length();
        final int keySize = key.length();

        final StringBuilder decryptedText = new StringBuilder(encryptedTextSize);
        for (int i = 0; i < encryptedTextSize; i++) {
            final int plainNR = encryptedText.codePointAt(i);
            final int keyNR = key.codePointAt(i % keySize);


            int pozitjaShkrojnes = ALFABETI_GJUHES_SHQIPE.indexOf((char) plainNR + "");
            int pozitjaQelesit = ALFABETI_GJUHES_SHQIPE.indexOf((char) keyNR + "");
            int pozitjaAlfabet = (pozitjaShkrojnes - pozitjaQelesit + ALFABETI_GJUHES_SHQIPE.size()) % ALFABETI_GJUHES_SHQIPE.size();

            decryptedText.append(ALFABETI_GJUHES_SHQIPE.get(pozitjaAlfabet));
            analizoFrekuencore(pozitjaShkrojnes, shkronjaPersiturDekriptimin);
        }
        System.out.println("Mesazhi i dekriptuar: " + decryptedText);
        printoFrekuencoretShkronave("Frekuencore e shkrovae ne dekremtpim", shkronjaPersiturDekriptimin);
    }


    /**
     * @param pozitaShkrojnes
     * @param map
     */
    private static void analizoFrekuencore(Integer pozitaShkrojnes, LinkedHashMap<String, Integer> map) {
        String shkronja = ALFABETI_GJUHES_SHQIPE.get(pozitaShkrojnes);

        if (map.containsKey(shkronja)) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getKey().equals(shkronja)) {
                    entry.setValue(entry.getValue() + 1);
                }
            }
        } else {
            map.put(shkronja, 1);
        }

    }

    /**
     * @param info
     * @param map
     */
    private static void printoFrekuencoretShkronave(String info, LinkedHashMap<String, Integer> map) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("================================================\n");
        stringBuilder.append("\t" + info + "\n");
        double shumaTotale = kalkuloShumen(map);
        double perqindjeTotale = 0.0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            double perqindjaVleres = entry.getValue() / shumaTotale;
            perqindjeTotale += perqindjaVleres;
            stringBuilder.append("\tShkronja: " + entry.getKey() + " ka frekuencen: " + decimalFormat.format(perqindjaVleres * 100) + "% dhe quanitetin " + entry.getValue() + "\n");
        }
        stringBuilder.append("------------------------------------------------\n");
        stringBuilder.append("\t\t\t\t\t\tShuma: " + decimalFormat.format(perqindjeTotale * 100) + "% \t\t\t" + shumaTotale + "\n");
        stringBuilder.append("================================================\n");
        System.out.println(stringBuilder);
    }

    /**
     * @param map
     * @return
     */
    private static double kalkuloShumen(LinkedHashMap<String, Integer> map) {
        double numero = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            numero += entry.getValue();
        }
        return numero;
    }

    /**
     * @param in
     */
    private static void fillojDekriptimi(Scanner in) {
        System.out.println("Filloj dekriptimi");
        System.out.print("Shkruaj celsin ne  UPPER Case: ");
        String key = in.nextLine();
        System.out.print("Shkruani mesazhin e dekriptuar nga Vigenere cipher:  ");
        String DMessage = in.nextLine();
        decrypt(DMessage, key);
    }


    /**
     * @param in
     */
    private static void fillojEnkriptimi(Scanner in) {
        System.out.println("Filloj enkriptimi");
        System.out.print("Shkruaj celsin ne UPPER Case: ");
        String key = in.nextLine();
        System.out.print("Shkruani mesazhin e enkriptuar nga Vigenere cipher: ");
        String EMessage = in.nextLine();
        encrypt(EMessage, key);
    }

    /**
     *
     */
    private static void informoPerdorimin() {
        StringBuilder informacion = new StringBuilder();
        informacion.append("\n");
        informacion.append("++++++++++++++++++++++++++++++++++++\n");
        informacion.append("\tZgjedh 1 per enkriptim\n");
        informacion.append("\tZgjedh 2 per dekriptim\n");
        informacion.append("\tZgjedh 3 per mbarim\n");
        informacion.append("\tJepni shkronja e alfabeti shqip:\n");
        informacion.append("\t" + ALFABETI_GJUHES_SHQIPE + "\n");
        informacion.append("++++++++++++++++++++++++++++++++++++\n\n");
        System.out.println(informacion);
    }


}
