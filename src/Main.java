import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main extends JFrame implements Runnable {


    private JLabel testLabel = new JLabel();

    private JTextField inPath = new JTextField();

    private JTextField startingIndex = new JTextField();

    private JButton goButton = new JButton();
    private JButton stopButton = new JButton();
    private  JButton saveButton = new JButton();

    private  JLabel startFromLastIndexLabel = new JLabel();
    private  JCheckBox startFromLastIndex = new JCheckBox();


    private JTextPane hitlist = new JTextPane();

    private List<String> hits = new ArrayList<>();

    public enum ProgramState {STARTED, INITIALIZING_RUN, INITIALIZED_RUN, RUNNING, FINISHED}

    ProgramState programState = ProgramState.STARTED;

    File wordList = null;
    Scanner scanner = null;

    int startingIndexInt = 0;
    int currentIndex = 0;
    int savedIndex = 0;
    boolean updateVisual = true;
    boolean isAvailable = false;

    @Override
    public void run() {


        while (true) {

            if(programState == ProgramState.INITIALIZED_RUN){
                goButton.setVisible(false);
                stopButton.setVisible(true);
                saveButton.setVisible(true);
            }
            if(programState == ProgramState.STARTED){


                goButton.setVisible(true);
                stopButton.setVisible(false);
                saveButton.setVisible(false);
            }


            if (updateVisual) {

                if (isAvailable) {
                    testLabel.setForeground(Color.GREEN);
                } else {
                    testLabel.setForeground(Color.RED);
                }

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {

                testLabel.setForeground(Color.WHITE);

                if (programState == ProgramState.INITIALIZING_RUN) {

                    try {startingIndexInt = Integer.parseInt(startingIndex.getText());}catch (NumberFormatException e){startingIndexInt = 0;}
                    currentIndex = 0;


                    try {
                        wordList = new File(inPath.getText());
                        scanner = new Scanner(wordList);
                        programState = ProgramState.INITIALIZED_RUN;
                    } catch (FileNotFoundException e) {
                    //    System.exit(420);
                        programState = ProgramState.STARTED;
                    }

                }
                if (programState == ProgramState.INITIALIZED_RUN) {

                    if (scanner.hasNextLine()) {

                        try {
                            String data = scanner.nextLine();

                            currentIndex++;
                            if (data.length() <= 3 || !isOnlyChars(data) || currentIndex < startingIndexInt) continue;

                            int h =  testLabel.getFontMetrics(testLabel.getFont()).stringWidth(data);
                            testLabel.setLocation(400 + (400-h)/2, 40);
                            testLabel.setText(data);
                            URL oracle = new URL("https://api.mojang.com/users/profiles/minecraft/" + data);
                            URLConnection yc = oracle.openConnection();
                            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                            String inputLine;
                            StringBuilder str = new StringBuilder();
                            while ((inputLine = in.readLine()) != null) {
                                str.append(inputLine);
                            }
                            if (str.toString().equalsIgnoreCase("")) {
                                System.out.println("AVAILABLE: " + data);
                                hits.add(data);
                                hitlist.setText(hitlist.getText() + data.toUpperCase() + System.lineSeparator());
                                isAvailable = true;
                            } else {
                                isAvailable = false;
                                System.out.print(".");
                            }
                            in.close();
                            //MojangAPI Rate Limit 600 / 10Mins, Bypass with Proxies?
                            Thread.sleep(750);
                        } catch (IOException | InterruptedException e) {

                            e.printStackTrace();

                        }
                    }

                }
            }

            updateVisual = !updateVisual;

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


    public boolean isOnlyChars(String name) {
        return name.matches("[a-zA-Z]+");
    }

    public Main() {
        setVisible(true);
        setSize(800, 600);
        setLayout(null);
        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(null);
        panel.setBackground(Color.DARK_GRAY);


        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                programState = ProgramState.INITIALIZING_RUN;

                if(startFromLastIndex.isEnabled()){

                    startingIndexInt = savedIndex;

                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                programState = ProgramState.STARTED;
                savedIndex = currentIndex;
                startingIndexInt = 0;
            }
        });



        goButton.setBounds(25, 450, 350, 100);
        goButton.setVisible(true);
        goButton.setBackground(Color.GREEN);
        goButton.setText("RUN");
        goButton.setFont(new Font("Consolas", Font.PLAIN, 40));
        goButton.setBorder(null);

        stopButton.setBounds(25, 450, 170, 100);
        stopButton.setVisible(false);
        stopButton.setBackground(Color.RED);
        stopButton.setFont(new Font("Consolas", Font.PLAIN, 40));
        stopButton.setText("STOP");
        stopButton.setBorder(null);

        saveButton.setBounds( 205, 450,170, 100);
        saveButton.setVisible(true);
        saveButton.setBackground(Color.LIGHT_GRAY);
        saveButton.setFont(new Font("Consolas", Font.PLAIN, 40));
        saveButton.setText("SAVE");



        JLabel inPathLabel = new JLabel();
        inPathLabel.setBounds(100, 120, 100, 20);
        inPathLabel.setText("WorldList Path");
        inPathLabel.setFont(new Font("Consolas", Font.PLAIN, inPath.getFont().getSize()));
        inPathLabel.setForeground(Color.WHITE);
        inPathLabel.setVisible(true);
        inPath.setBounds(100, 140, 200, 20);
        inPath.setVisible(true);
        inPath.setBackground(Color.LIGHT_GRAY);
        inPath.setBorder(null);
        inPath.setFont(new Font("Consolas", Font.PLAIN, inPath.getFont().getSize()));






        JLabel startingIndexLabel = new JLabel();
        startingIndexLabel.setBounds(100, 160, 100, 20);
        startingIndexLabel.setText("Starting Index");
        startingIndexLabel.setFont(new Font("Consolas", Font.PLAIN, startingIndex.getFont().getSize()));
        startingIndexLabel.setForeground(Color.WHITE);
        startingIndexLabel.setVisible(true);
        startingIndex.setBounds(100, 180, 200, 20);
        startingIndex.setVisible(true);
        startingIndex.setBackground(Color.LIGHT_GRAY);
        startingIndex.setBorder(null);
        startingIndex.setFont(new Font("Consolas", Font.PLAIN, startingIndex.getFont().getSize()));


        startFromLastIndex.setBounds(100, 220, 200, 20);
        startFromLastIndex.setText("Start from last Index");
        startFromLastIndex.setBackground(Color.DARK_GRAY);
        startFromLastIndex.setForeground(Color.WHITE);
        startFromLastIndex.setFont(new Font("Consolas", Font.PLAIN, startingIndex.getFont().getSize()));
        startFromLastIndex.setBorder(null);
        startFromLastIndex.setVisible(true);




        testLabel.setBounds(400, 40, 400, 50);
        testLabel.setForeground(Color.WHITE);
        testLabel.setFont(new Font("Consolas", Font.PLAIN, 40));
        testLabel.setText("");
        testLabel.setVisible(true);




        hitlist.setBounds(400,120,375,430);
        hitlist.setVisible(true);
        hitlist.setBackground(Color.LIGHT_GRAY);
        hitlist.setFont(new Font("Consolas", Font.PLAIN, inPath.getFont().getSize()));


        panel.add(goButton);
        panel.add(inPathLabel);
        panel.add(inPath);
        panel.add(startingIndexLabel);
        panel.add(startingIndex);
        panel.add(testLabel);
        panel.add(hitlist);
        panel.add(stopButton);
        panel.add(saveButton);
        panel.add(startFromLastIndex);
        //Put this at the Very end or the list will dissapear for some reason
        inPath.setToolTipText("Path to your Input File");
        hitlist.setText("Word:                  Index" + System.lineSeparator());

    }


    public static void main(String[] args) {
        Thread f = new Thread(new Main());
        f.start();
    }

}
