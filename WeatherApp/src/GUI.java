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

        JLabel weatherImage = new JLabel(getImage("src/images/clouds.png"));
        weatherImage.setBounds(0,125, 450, 217);

        add(weatherImage);

        JLabel temperatureText = new JLabel("20 C");
        temperatureText.setBounds(0,350, 450, 54);
        temperatureText.setFont(new Font("SansSerif", Font.BOLD, 40));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);

        add(temperatureText);

        JLabel weatherCondition = new JLabel("Cloudy");
        weatherCondition.setBounds(0, 405, 450, 36);
        weatherCondition.setFont(new Font("SansSerif", Font.BOLD, 30));
        weatherCondition.setHorizontalAlignment(SwingConstants.CENTER);

        add(weatherCondition);

        JLabel humidityImage = new JLabel(getImage("src/images/humidity.png"));
        humidityImage.setBounds(15, 500, 74, 66);

        add(humidityImage);

        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90, 500, 80, 55);
        humidityText.setFont(new Font("SansSerif", Font.BOLD, 16));

        add(humidityText);

        JLabel windSpeedImage = new JLabel(getImage("src/images/wind.png"));
        windSpeedImage.setBounds(240, 500, 74, 66);

        add(windSpeedImage);

        JLabel windSpeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windSpeedText.setBounds(320, 500, 97, 55);
        windSpeedText.setFont(new Font("SansSerif", Font.BOLD, 16));

        add(windSpeedText);
    }
}
