import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

public class WelcomeTo extends JFrame {
    private List<List<String>> decks = new ArrayList<>();
    private List<JLabel> topCardLabels = new ArrayList<>();
    private List<Integer> deckIndices = new ArrayList<>();

    private final String cardDirPath = "C:\\Users\\lucasfongemie\\Downloads\\Welcome To Nums";
    private final int cardWidth = 100;
    private final int cardHeight = 150;

    public WelcomeTo() {
        setTitle("Welcome To... Game Board");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            BufferedImage backgroundImage = ImageIO.read(new File("C:\\Users\\lucasfongemie\\Downloads\\output-onlinepngtools.png"));
            JLabel imageLabel = new JLabel(new ImageIcon(backgroundImage));
            imageLabel.setBounds(0, 0, backgroundImage.getWidth(), backgroundImage.getHeight());
            imageLabel.setLayout(null);

            JButton button = new JButton("Add Elements");
            button.setBounds(300, 10, 150, 40);
            imageLabel.add(button);

            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem addTextBoxItem = new JMenuItem("Add Text Box");
            JMenuItem addCrossOff = new JMenuItem("Add Cross Off");
            JMenuItem addCircle = new JMenuItem("Add Circle");
            JMenuItem addFence = new JMenuItem("Add Fence");
            JMenuItem addGroups = new JMenuItem("Add Groups");
            popupMenu.add(addTextBoxItem);
            popupMenu.add(addCrossOff);
            popupMenu.add(addCircle);
            popupMenu.add(addFence);
            popupMenu.add(addGroups);
            button.addActionListener(e -> popupMenu.show(button, 0, button.getHeight()));

            addTextBoxItem.addActionListener(e -> {
                JTextField textField = new JTextField("Type...");
                textField.setBounds(350, 50, 150, 30);
                textField.setOpaque(false);
                textField.setForeground(Color.BLACK);
                textField.setBorder(null);
                textField.setCaretColor(Color.BLACK);
                textField.setFont(new Font("Arial", Font.PLAIN, 30));

                final Point[] clickPoint = {null};
                textField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        clickPoint[0] = e.getPoint();
                    }
                });
                textField.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if (clickPoint[0] != null) {
                            Point parentLoc = textField.getLocation();
                            int newX = parentLoc.x + e.getX() - clickPoint[0].x;
                            int newY = parentLoc.y + e.getY() - clickPoint[0].y;
                            textField.setLocation(newX, newY);
                        }
                    }
                });
                imageLabel.add(textField);
                imageLabel.repaint();
                imageLabel.revalidate();
            });

            addCrossOff.addActionListener(e -> addImage(imageLabel, "C:\\Users\\lucasfongemie\\Downloads\\pngtree-scribble-ink-icon-vector-png-image_6700609.png", 35, 35, 350, 50));
            addCircle.addActionListener(e -> addImage(imageLabel, "C:\\Users\\lucasfongemie\\Downloads\\105-1054647_drawn-circle-hand-drawn-circle-png-transparent-png.png", 40, 40, 350, 50));
            addFence.addActionListener(e -> addImage(imageLabel, "C:\\Users\\lucasfongemie\\Downloads\\verticle line.png", 12, 115, 350, 50));

            addGroups.addActionListener(e -> {
                JPopupMenu groupPopup = new JPopupMenu();
                for (int i = 2; i <= 6; i++) {
                    int groupSize = i;
                    JMenuItem groupItem = new JMenuItem("Group of " + groupSize);
                    groupItem.addActionListener(ev -> {
                        String imagePath = "C:\\Users\\lucasfongemie\\Downloads\\horizontal line.jpg";
                        addImage(imageLabel, imagePath, 65 * groupSize, 12, 350, 50);
                    });
                    groupPopup.add(groupItem);
                }
                groupPopup.show(button, 0, button.getHeight() + popupMenu.getComponentCount() * 25);
            });

            setupDecks(imageLabel);
            addCardControls(imageLabel);

            setContentPane(imageLabel);
            setSize(backgroundImage.getWidth(), backgroundImage.getHeight());
            setLocationRelativeTo(null);
            setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load image.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void setupDecks(JLabel imageLabel) {
        File folder = new File(cardDirPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));
        if (files == null) return;

        List<String> cardPaths = new ArrayList<>();
        for (File file : files) cardPaths.add(file.getAbsolutePath());
        Collections.shuffle(cardPaths);

        decks.clear();
        for (int i = 0; i < 3; i++) decks.add(new ArrayList<>());
        for (int i = 0; i < cardPaths.size(); i++) decks.get(i % 3).add(cardPaths.get(i));

        deckIndices.clear();
        for (int i = 0; i < 3; i++) deckIndices.add(0);

        drawCards(imageLabel);
    }

    private void drawCards(JLabel imageLabel) {
    for (JLabel label : topCardLabels) imageLabel.remove(label);
    topCardLabels.clear();

    try {
        for (int i = 0; i < 3; i++) {
            int x = 100 + i * 150;
            int y = 100;

            int currentIndex = deckIndices.get(i);
            String currentCardPath = decks.get(i).get(currentIndex);
            JLabel faceUp = createCardLabel(currentCardPath, x, y);
            imageLabel.add(faceUp);
            topCardLabels.add(faceUp);

            if (currentIndex > 0) {
    String prevCardPath = decks.get(i).get(currentIndex - 1);
    String backCardName = "B_" + getCardType(prevCardPath) + ".png";
    //String backCardPath = cardDirPath + "\\" + backCardName;
    String backCardPath = "C:\\Users\\lucasfongemie\\Downloads\\Welcome To\\Welcome To\\" + backCardName;

    File backFile = new File(backCardPath);
if (backFile.exists()) {
    JLabel backLabel = createCardLabel(backCardPath, x, y + cardHeight + 10);
    imageLabel.add(backLabel);
    topCardLabels.add(backLabel);
} else {
    System.out.println("Missing back image: " + backCardPath);
}

}
 else {
                JLabel faceDown = new JLabel("?");
                faceDown.setBounds(x, y + cardHeight + 10, cardWidth, cardHeight);
                faceDown.setHorizontalAlignment(SwingConstants.CENTER);
                faceDown.setVerticalAlignment(SwingConstants.CENTER);
                faceDown.setFont(new Font("Arial", Font.BOLD, 40));
                faceDown.setOpaque(true);
                faceDown.setBackground(Color.LIGHT_GRAY);
                faceDown.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                imageLabel.add(faceDown);
                topCardLabels.add(faceDown);
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    imageLabel.repaint();
    imageLabel.revalidate();
}

private String getCardType(String path) {
    String name = new File(path).getName();
    int underscore = name.indexOf('_');
    int dot = name.indexOf('.');
    if (underscore != -1 && dot != -1) {
        return name.substring(underscore + 1, dot);
    }
    return "";
}


    private JLabel createCardLabel(String imagePath, int x, int y) throws Exception {
        BufferedImage img = ImageIO.read(new File(imagePath));
        Image scaledImage = img.getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(scaledImage));
        label.setBounds(x, y, cardWidth, cardHeight);
        return label;
    }

    private void addCardControls(JLabel imageLabel) {
        JButton flipButton = new JButton("Flip Next Card");
        flipButton.setBounds(500, 10, 150, 30);
        imageLabel.add(flipButton);

        JButton shuffleButton = new JButton("Shuffle Decks");
        shuffleButton.setBounds(660, 10, 150, 30);
        imageLabel.add(shuffleButton);

        flipButton.addActionListener(e -> {
            for (int i = 0; i < 3; i++) {
                int nextIndex = deckIndices.get(i) + 1;
                if (nextIndex < decks.get(i).size()) deckIndices.set(i, nextIndex);
            }
            drawCards(imageLabel);
        });

        shuffleButton.addActionListener(e -> setupDecks(imageLabel));
    }

    private void addImage(JLabel imageLabel, String imagePath, int width, int height, int x, int y) {
        try {
            BufferedImage img = ImageIO.read(new File(imagePath));
            Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaledImage));
            imgLabel.setBounds(x, y, width, height);

            final Point[] clickPoint = {null};
            imgLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    clickPoint[0] = e.getPoint();
                }
            });
            imgLabel.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (clickPoint[0] != null) {
                        Point parentLoc = imgLabel.getLocation();
                        int newX = parentLoc.x + e.getX() - clickPoint[0].x;
                        int newY = parentLoc.y + e.getY() - clickPoint[0].y;
                        imgLabel.setLocation(newX, newY);
                    }
                }
            });
            imageLabel.add(imgLabel);
            imageLabel.repaint();
            imageLabel.revalidate();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load image: " + imagePath, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WelcomeTo::new);
    }
}
