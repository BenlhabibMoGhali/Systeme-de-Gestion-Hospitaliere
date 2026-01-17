package model;

import java.util.Objects;

public class Prescription {

    private final String medicament;
    private final String dosage;

    public Prescription(String medicament, String dosage) {
        this.medicament = medicament;
        this.dosage = dosage;
    }

    public String getMedicament() {
        return medicament;
    }

    public String getDosage() {
        return dosage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prescription)) return false;
        Prescription that = (Prescription) o;
        return Objects.equals(medicament, that.medicament)
                && Objects.equals(dosage, that.dosage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicament, dosage);
    }
}
