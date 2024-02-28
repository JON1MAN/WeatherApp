import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUI extends JFrame {
    public GUI(){

        super("Weather app");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        addComponents();


    }
    private ImageIcon getImage(String path){
        try{

            BufferedImage image = ImageIO.read(new File(path));

            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("There is no such image");
        return null;
    }

    public void addComponents(){

        JTextField searchField = new JTextField();

        searchField.setBounds(20, 15, 350, 45);
        searchField.setFont(new Font("SansSerif", Font.BOLD, 24));
        searchField.setHorizontalAlignment(JTextField.CENTER);

        add(searchField);

        JButton searchButton = new JButton(getImage("src/images/search.png"));

        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(380, 13, 47, 45);

        add(searchButton);
    }
}
