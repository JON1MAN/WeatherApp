import javax.swing.*;

public class Launcher {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new GUI().setVisible(true);
                System.out.println(Application.getLocationData("Tokyo"));
            }
        });
    }
}
