package service;

import model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

public class PersistenceService {

    private final Path baseDir;

    public PersistenceService(String dossier) {
        this.baseDir = Path.of(dossier);
        try {
            Files.createDirectories(baseDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* =======================
       PATIENTS
       ======================= */

    public void savePatients(List<Patient> patients) {
        Path f = baseDir.resolve("patients.csv");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(f))) {
            pw.println("id,nom,prenom,assurance,mutuelle,groupeSanguin,allergies,antecedents,contactNom,contactTel");
            for (Patient p : patients) {
                pw.println(csv(
                        String.valueOf(p.getId()),
                        p.getNom(),
                        p.getPrenom(),
                        p.getAssuranceMaladie(),
                        nullToEmpty(p.getMutuelle()),
                        nullToEmpty(p.getGroupeSanguin()),
                        nullToEmpty(p.getAllergies()),
                        nullToEmpty(p.getAntecedents()),
                        nullToEmpty(p.getContactUrgenceNom()),
                        nullToEmpty(p.getContactUrgenceTel())
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Patient> loadPatients() {
        Path f = baseDir.resolve("patients.csv");
        if (!Files.exists(f)) return new ArrayList<>();

        List<Patient> out = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(f)) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] c = parseCsv(line);
                Patient p = new Patient(
                        Integer.parseInt(c[0]),
                        c[1],
                        c[2],
                        c[3]
                );
                if (!c[4].isBlank()) p.setMutuelle(c[4]);
                if (!c[5].isBlank()) p.setGroupeSanguin(c[5]);
                if (!c[6].isBlank()) p.setAllergies(c[6]);
                if (!c[7].isBlank()) p.setAntecedents(c[7]);
                if (!c[8].isBlank()) p.setContactUrgenceNom(c[8]);
                if (!c[9].isBlank()) p.setContactUrgenceTel(c[9]);
                out.add(p);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out;
    }

    /* =======================
       MEDICAMENTS (STOCK)
       ======================= */

    public void saveMedicaments(Collection<Medicament> meds) {
        Path f = baseDir.resolve("medicaments.csv");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(f))) {
            pw.println("nom,code,quantite,dateExpiration,controle");
            for (Medicament m : meds) {
                pw.println(csv(
                        m.getNom(),
                        m.getCode(),
                        String.valueOf(m.getQuantite()),
                        String.valueOf(m.getDateExpiration()),
                        String.valueOf(m.estControle())
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Medicament> loadMedicaments() {
        Path f = baseDir.resolve("medicaments.csv");
        if (!Files.exists(f)) return new ArrayList<>();

        List<Medicament> out = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(f)) {
            br.readLine(); // header
            String line;
            while ((line = br.readLine()) != null) {
                String[] c = parseCsv(line);
                out.add(new Medicament(
                        c[0],
                        c[1],
                        Integer.parseInt(c[2]),
                        LocalDate.parse(c[3]),
                        Boolean.parseBoolean(c[4])
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out;
    }

    /* =======================
       CHAMBRES
       ======================= */

    public void saveChambres(List<Chambre> chambres) {
        Path f = baseDir.resolve("chambres.csv");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(f))) {
            pw.println("numero,type,nbLits,enNettoyage");
            for (Chambre c : chambres) {
                pw.println(csv(
                        String.valueOf(c.getNumero()),
                        c.getType().name(),
                        String.valueOf(c.getLits().size()),
                        String.valueOf(c.estEnNettoyage())
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Chambre> loadChambres() {
        Path f = baseDir.resolve("chambres.csv");
        if (!Files.exists(f)) return new ArrayList<>();

        List<Chambre> out = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(f)) {
            br.readLine(); // header
            String line;
            while ((line = br.readLine()) != null) {
                String[] c = parseCsv(line);
                Chambre ch = new Chambre(
                        Integer.parseInt(c[0]),
                        ChambreType.valueOf(c[1]),
                        Integer.parseInt(c[2])
                );
                ch.setEnNettoyage(Boolean.parseBoolean(c[3]));
                out.add(ch);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out;
    }

    /* =======================
       UTIL CSV
       ======================= */

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    private String csv(String... cols) {
        // encodage simple avec guillemets si besoin
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cols.length; i++) {
            String v = cols[i] == null ? "" : cols[i];
            if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
                v = "\"" + v.replace("\"", "\"\"") + "\"";
            }
            sb.append(v);
            if (i < cols.length - 1) sb.append(",");
        }
        return sb.toString();
    }

    private String[] parseCsv(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (inQuotes) {
                if (ch == '"' && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"');
                    i++;
                } else if (ch == '"') {
                    inQuotes = false;
                } else {
                    cur.append(ch);
                }
            } else {
                if (ch == '"') inQuotes = true;
                else if (ch == ',') {
                    out.add(cur.toString());
                    cur.setLength(0);
                } else cur.append(ch);
            }
        }
        out.add(cur.toString());
        return out.toArray(new String[0]);
    }
}
