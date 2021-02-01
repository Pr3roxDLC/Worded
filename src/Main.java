import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Main extends JFrame implements Runnable {


   JLabel testLabel = new JLabel();
   JTextField inPath = new JTextField();
   JButton goButton = new JButton();

   JPanel panel = new JPanel();


    @Override
    public void run() {

        while(true) {

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
                try {
                    go();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        goButton.setBounds(100, 100, 50, 20);
        goButton.setVisible(true);
        panel.add(goButton);

        testLabel.setLocation(50, 50);
        testLabel.setBounds(50, 50, 50, 12);
        testLabel.setText("TestTestTest");
        testLabel.setVisible(true);
        panel.add(testLabel);

        inPath.setBounds(0, 100, 50, 20);
        inPath.setVisible(true);
        panel.add(inPath);


    }


    public void go() throws IOException {

        System.out.println("AAAA");

        URL oracle = new URL("https://api.mojang.com/users/profiles/minecraft/Pr3roxDLC");
        URLConnection yc = oracle.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;
        StringBuilder str = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            str.append(inputLine);
        }
        if(str.toString().equalsIgnoreCase("")){
            System.out.println("AVAILABLE");
        }
        in.close();

    }

    public static void main(String[] args) {
        Thread f = new Thread(new Main());
        f.start();
    }

}
