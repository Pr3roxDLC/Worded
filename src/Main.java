import javax.swing.*;

public class Main extends JFrame implements Runnable {


    JMenuBar menuBar = new JMenuBar();
    JMenu files = new JMenu("Files");
    JMenu load = new JMenu("Load WordList");

    @Override
    public void run() {

    }


    public Main() {
        setVisible(true);
        setSize(800, 600);
        setLayout(null);

        files.add(load);

        menuBar.add(files);



        setJMenuBar(menuBar);
    }




    public static void main(String[] args) {
        Thread f = new Thread(new Main());
        f.start();
    }

}
