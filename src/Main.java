import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Main extends JFrame implements Runnable {


    JLabel testLabel = new JLabel();
    JTextField inPath = new JTextField();
    JButton goButton = new JButton();


    JPanel panel = new JPanel();

    public enum ProgramState {STARTED, INITIALIZING_RUN, INITIALIZED_RUN, RUNNING, FINISHED}

    ProgramState programState = ProgramState.STARTED;

    File wordList = null;
    Scanner scanner = null;
    boolean updateVisual = true;
    boolean isAvailable = false;

    @Override
    public void run() {


        while (true) {

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
                    wordList = new File("C:\\Users\\Tim\\Documents\\wordlist.txt");
                    try {
                        scanner = new Scanner(wordList);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    programState = ProgramState.INITIALIZED_RUN;
                }
                if (programState == ProgramState.INITIALIZED_RUN) {

                    if (scanner.hasNextLine()) {

                        try {
                            String data = scanner.nextLine();

                            if (data.length() <= 3) continue;

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


    public Main() {
        setVisible(true);
        setSize(800, 600);
        setLayout(null);
        setContentPane(panel);
        panel.setLayout(null);
        panel.setBackground(Color.DARK_GRAY);


        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                programState = ProgramState.INITIALIZING_RUN;

            }
        });

        goButton.setBounds(100, 100, 50, 20);
        goButton.setVisible(true);
        panel.add(goButton);


        testLabel.setBounds(400, 40, 400, 50);
        testLabel.setForeground(Color.WHITE);
        testLabel.setFont(new Font("Consolas", Font.PLAIN, 40));
        testLabel.setText("TestTestTest");
        testLabel.setVisible(true);
        panel.add(testLabel);

        inPath.setBounds(0, 100, 50, 20);
        inPath.setVisible(true);
        panel.add(inPath);


    }

    public static void main(String[] args) {
        Thread f = new Thread(new Main());
        f.start();
    }

}
