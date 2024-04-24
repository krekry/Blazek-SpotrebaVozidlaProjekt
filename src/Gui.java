import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Gui extends JFrame {

    private JTextField textFieldDatum;
    private JTextField textFieldSpotrebaBenzinu;
    private JTextField textFieldRidic;
    private JTextField textFieldPocetUjetychKilometru;
    private JRadioButton radioButtonDalnice;
    private JRadioButton radioButtonMesto;
    private JRadioButton radioButtonMimoMesto;

    private ArrayList<JizdaVozidla> seznamJizd;

    private JPanel panel;
    private JButton uložitNovýZáznamButton;

    public Gui() {
        setTitle("Aplikace pro zaznamenání jízdy vozidla");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300); // Zvětšení velikosti okna

        seznamJizd = new ArrayList<>();

        panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2)); // Změna počtu řádků na 7

        panel.add(new JLabel("Datum (RRRR-MM-DD):"));
        textFieldDatum = new JTextField();
        panel.add(textFieldDatum);

        panel.add(new JLabel("Spotřeba benzinu (l):"));
        textFieldSpotrebaBenzinu = new JTextField();
        panel.add(textFieldSpotrebaBenzinu);

        panel.add(new JLabel("Řidič:"));
        textFieldRidic = new JTextField();
        panel.add(textFieldRidic);

        panel.add(new JLabel("Počet ujetých kilometrů:"));
        textFieldPocetUjetychKilometru = new JTextField();
        panel.add(textFieldPocetUjetychKilometru);

        panel.add(new JLabel("Typ jízdy:"));
        ButtonGroup typJizdyGroup = new ButtonGroup();
        radioButtonDalnice = new JRadioButton("Dálnice");
        radioButtonMesto = new JRadioButton("Město");
        radioButtonMimoMesto = new JRadioButton("Mimo město");
        typJizdyGroup.add(radioButtonDalnice);
        typJizdyGroup.add(radioButtonMesto);
        typJizdyGroup.add(radioButtonMimoMesto);
        panel.add(radioButtonDalnice);
        panel.add(radioButtonMesto);
        panel.add(radioButtonMimoMesto);

        JButton buttonUlozit = new JButton("Ulož nový záznam");
        buttonUlozit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ulozitJizdu();
            }
        });
        panel.add(buttonUlozit);

        JButton buttonUlozitDoSouboru = new JButton("Uložit do souboru");
        buttonUlozitDoSouboru.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ulozitDoSouboru();
            }
        });
        panel.add(buttonUlozitDoSouboru);

        add(panel);
        setJMenuBar(createMenuBar());
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuNastroje = new JMenu("Nástroje");

        JMenuItem menuItemZobrazPoctKilometru = new JMenuItem("Zobraz celkový počet kilometrů");
        menuItemZobrazPoctKilometru.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zobrazCelkovyPocetKilometru();
            }
        });
        menuNastroje.add(menuItemZobrazPoctKilometru);

        JMenuItem menuItemZobrazPrumerSpotrebu = new JMenuItem("Zobraz průměrnou spotřebu");
        menuItemZobrazPrumerSpotrebu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zobrazPrumernouSpotrebu();
            }
        });
        menuNastroje.add(menuItemZobrazPrumerSpotrebu);

        menuBar.add(menuNastroje);

        return menuBar;
    }

    private void ulozitJizdu() {
        try {
            LocalDate datum = LocalDate.parse(textFieldDatum.getText());
            double spotrebaBenzinu = Double.parseDouble(textFieldSpotrebaBenzinu.getText());
            String ridic = textFieldRidic.getText();
            int pocetUjetychKilometru = Integer.parseInt(textFieldPocetUjetychKilometru.getText());
            JizdaVozidla.TypJizdy typJizdy = null;
            if (radioButtonDalnice.isSelected()) {
                typJizdy = JizdaVozidla.TypJizdy.DALNICE;
            } else if (radioButtonMesto.isSelected()) {
                typJizdy = JizdaVozidla.TypJizdy.MESTO;
            } else if (radioButtonMimoMesto.isSelected()) {
                typJizdy = JizdaVozidla.TypJizdy.MIMO_MESTO;
            }

            JizdaVozidla novaJizda = new JizdaVozidla(datum, spotrebaBenzinu, ridic, pocetUjetychKilometru, typJizdy);
            seznamJizd.add(novaJizda);
            System.out.println("Nový záznam: " + novaJizda.toString());
            JOptionPane.showMessageDialog(this, "Nový záznam byl úspěšně uložen.", "Úspěch", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Chyba při ukládání záznamu: " + ex.getMessage(), "Chyba", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ulozitDoSouboru() {
        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()));

                for (JizdaVozidla jizda : seznamJizd) {
                    String typJizdyText;
                    switch (jizda.getTyp()) {
                        case DALNICE:
                            typJizdyText = "dálnice";
                            break;
                        case MESTO:
                            typJizdyText = "město";
                            break;
                        case MIMO_MESTO:
                            typJizdyText = "mimo město";
                            break;
                        default:
                            typJizdyText = "neznámý";
                    }

                    String line = jizda.getDatum() + ";" + jizda.getSpotrebaBenzinu() + ";" +
                            "\"" + jizda.getRidic() + "\"" + ";" + jizda.getPocetUjetychKilometru() + ";" +
                            typJizdyText;
                    writer.write(line);
                    writer.newLine();
                }

                writer.close();
                JOptionPane.showMessageDialog(this, "Záznamy byly úspěšně uloženy do souboru.", "Úspěch", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Chyba při ukládání záznamů do souboru: " + ex.getMessage(), "Chyba", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void zobrazCelkovyPocetKilometru() {
        int celkemKilometru = 0;
        for (JizdaVozidla jizda : seznamJizd) {
            celkemKilometru += jizda.getPocetUjetychKilometru();
        }
        JOptionPane.showMessageDialog(this, "Celkový počet ujetých kilometrů: " + celkemKilometru, "Celkový počet kilometrů", JOptionPane.INFORMATION_MESSAGE);
    }

    private void zobrazPrumernouSpotrebu() {
        double celkovaSpotreba = 0;
        int celkemKilometru = 0;
        for (JizdaVozidla jizda : seznamJizd) {
            celkovaSpotreba += jizda.getSpotrebaBenzinu();
            celkemKilometru += jizda.getPocetUjetychKilometru();
        }
        double prumernaSpotreba = (celkovaSpotreba / celkemKilometru) * 100;
        JOptionPane.showMessageDialog(this, "Průměrná spotřeba: " + prumernaSpotreba + " l/100km", "Průměrná spotřeba", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Gui();
            }
        });
    }
}
