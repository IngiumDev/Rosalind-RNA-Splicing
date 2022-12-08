import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RNASplicing {
    public static void main(String[] args) {
        String fastaFileName = "src/rosalind_splc.txt";
        String rNACodonsFileName = "src/RNA-Codons.txt";
        ArrayList<String> fastaFile = parseDNAIntronFASTA(fastaFileName);
        HashMap<String, String> RNAcodons = getRNAtoProteinTable(rNACodonsFileName);
        System.out.println(createProteinStringFromIntrons(fastaFile, RNAcodons)); // MTYPGNRPIDFTTWNIHDQSSASSSVLCRRCPLNCHLIRTAEGSTFALNSFLAWKLCCRYSLELLKSSTATPATTPTYAHSCRASIEAVLTYRHSSDGCLFFIAAPDGLLPCCRISRLFIYRQGQEWREHSCLGTAVLHSGILNSHSYTTERRSLVMPPSWRCQRPPSTQSQRYV
    }

    /**
     * @param DNAIntrons ArrayList of DNA strings with the first string being the DNA string to be spliced and the rest being introns to be removed
     * @param proteinMap HashMap of RNA codons to their corresponding amino acids (in single letter format)
     * @return String of amino acids
     */
    public static String createProteinStringFromIntrons(ArrayList<String> DNAIntrons, HashMap<String, String> proteinMap) {
        // Remove introns from DNA
        String DNA = DNAIntrons.get(0);
        for (int i = 1; i < DNAIntrons.size(); i++) {
            DNA = DNA.replace(DNAIntrons.get(i), "");
        }
        // Convert from DNA to RNA
        String RNA = convertDNAtoRNA(DNA);
        // Convert from RNA to protein
        StringBuilder protein = new StringBuilder();
        for (int i = 0; i < RNA.length(); i += 3) {
            String codon = RNA.substring(i, i + 3);
            if (proteinMap.containsKey(codon)) {
                // If it's a stop codon, stop
                if (proteinMap.get(codon).equals("Z")) {
                    break;
                }
                protein.append(proteinMap.get(codon));
            }
        }
        return protein.toString();
    }

    /**
     * @param DNA String of DNA
     * @return String of RNA
     */
    public static String convertDNAtoRNA(String DNA) {
        // This is just not really how it works but it works for this problem
        DNA = DNA.replace('T', 'U');
        return DNA;
    }

    /**
     * Parse the FASTA file into an ArrayList of Strings with the first string being the DNA sequence and the rest being the introns
     *
     * @param filename the name of the file to parse
     * @return an ArrayList of Strings
     */
    public static ArrayList<String> parseDNAIntronFASTA(String filename) {
        ArrayList<String> dnaIntronList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            StringBuilder DNAtoAdd = new StringBuilder();
            while ((line = br.readLine()) != null) {
                // If it's a header, add the DNA to the list and start a new DNA string
                if (line.startsWith(">")) {
                    if (DNAtoAdd.length() > 0) {
                        dnaIntronList.add(DNAtoAdd.toString());
                        DNAtoAdd = new StringBuilder();
                    }
                } else {
                    DNAtoAdd.append(line);
                }
            }
            dnaIntronList.add(DNAtoAdd.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dnaIntronList;
    }

    /**
     * @param filename - file containing codon table
     * @return HashMap containing codon table
     */
    public static HashMap<String, String> getRNAtoProteinTable(String filename) {
        HashMap<String, String> RNAtoProteinTable = new HashMap<String, String>();
        // Create Arraylist for the lines of the input
        ArrayList<String> lines = new ArrayList<String>();
        // Read the codon table from the file
        String nextline = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((nextline = br.readLine()) != null) {
                lines.add(nextline);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Parse the lines into the HashMap
        for (String line : lines) {
            String[] codon = line.split(" ");
            RNAtoProteinTable.put(codon[0], codon[1]);
            RNAtoProteinTable.put(codon[7], codon[8]);
            RNAtoProteinTable.put(codon[14], codon[15]);
            RNAtoProteinTable.put(codon[21], codon[22]);
        }

        return RNAtoProteinTable;
    }
}
