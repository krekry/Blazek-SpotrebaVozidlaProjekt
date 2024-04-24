import java.time.LocalDate;

public class JizdaVozidla {
    private LocalDate datum;
    private double spotrebaBenzinu;
    private String ridic;
    private int pocetUjetychKilometru;
    private TypJizdy typ;

    public JizdaVozidla(LocalDate datum, double spotrebaBenzinu, String ridic, int pocetUjetychKilometru, TypJizdy typ) {
        this.datum = datum;
        this.spotrebaBenzinu = spotrebaBenzinu;
        this.ridic = ridic;
        this.pocetUjetychKilometru = pocetUjetychKilometru;
        this.typ = typ;
    }
    public LocalDate getDatum() {
        return datum;
    }

    public double getSpotrebaBenzinu() {
        return spotrebaBenzinu;
    }

    public String getRidic() {
        return ridic;
    }

    public int getPocetUjetychKilometru() {
        return pocetUjetychKilometru;
    }

    public TypJizdy getTyp() {
        return typ;
    }

    // Definice enum TypJizdy
    public enum TypJizdy {
        DALNICE,
        MESTO,
        MIMO_MESTO
    }
}