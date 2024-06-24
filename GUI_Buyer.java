package Auction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI_Buyer {
    private static JLabel timerLabel;
    private static JLabel bidPriceLabel;
    private static JLabel resultLabel;
    private static Timer timer;
    private static int timeLeft = 10;  // 타이머 시작 시간 (초)
    private static int bidPrice = 10000;  // 초기 제시 가격

    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Auction Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new BorderLayout());

        // Create background panel
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(new Color(0, 96, 128));  // Blue background
        backgroundPanel.setLayout(new BorderLayout());
        frame.add(backgroundPanel, BorderLayout.CENTER);

        // Create top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 96, 128));  // Blue background
        JLabel itemNameLabel = new JLabel("물품명", SwingConstants.CENTER);
        itemNameLabel.setOpaque(true);
        itemNameLabel.setBackground(Color.WHITE);
        topPanel.add(itemNameLabel, BorderLayout.CENTER);

        // Create middle panel
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBackground(new Color(0, 96, 128));  // Blue background
        JLabel photoLabel = new JLabel();
        photoLabel.setOpaque(true);
        photoLabel.setBackground(Color.WHITE);
        // Load and set the image
        ImageIcon imageIcon = new ImageIcon("이미지 경로");  // 이미지 경로
        photoLabel.setIcon(imageIcon);
        JLabel currentPriceLabel = new JLabel("현재 가격", SwingConstants.CENTER);
        currentPriceLabel.setOpaque(true);
        currentPriceLabel.setBackground(Color.WHITE);
        middlePanel.add(photoLabel, BorderLayout.CENTER);
        middlePanel.add(currentPriceLabel, BorderLayout.SOUTH);

        // Create bottom panel
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(0, 96, 128));  // Blue background
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        timerLabel = new JLabel("남은 시간: " + timeLeft, SwingConstants.CENTER);
        timerLabel.setOpaque(true);
        timerLabel.setBackground(Color.WHITE);
        timerLabel.setPreferredSize(new Dimension(100, 50));
        c.gridx = 0;
        c.gridy = 0;
        bottomPanel.add(timerLabel, c);

        bidPriceLabel = new JLabel("제시 가격: " + bidPrice + "원", SwingConstants.CENTER);
        bidPriceLabel.setOpaque(true);
        bidPriceLabel.setBackground(Color.WHITE);
        bidPriceLabel.setPreferredSize(new Dimension(150, 50));
        c.gridx = 1;
        c.gridy = 0;
        bottomPanel.add(bidPriceLabel, c);

        JButton bidButton = new JButton("입찰") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.ORANGE);
                g.fillOval(0, 0, getWidth(), getHeight());
                g.setColor(Color.BLACK);
                g.drawString("     입찰", getWidth() / 2 - 30, getHeight() / 2 + 5);
            }
        };
        bidButton.setPreferredSize(new Dimension(100, 50));
        bidButton.setFocusPainted(false);
        bidButton.setContentAreaFilled(false);
        bidButton.setBorderPainted(false);
        c.gridx = 2;
        c.gridy = 0;
        bottomPanel.add(bidButton, c);

        // Result label
        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setOpaque(true);
        resultLabel.setBackground(new Color(0, 96, 128));  // Blue background
        resultLabel.setForeground(Color.WHITE);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        bottomPanel.add(resultLabel, c);

        // Add all panels to main panel
        backgroundPanel.add(topPanel, BorderLayout.NORTH);
        backgroundPanel.add(middlePanel, BorderLayout.CENTER);
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Display the frame
        frame.setVisible(true);

        // Start the timer
        startTimer();

        // Bid button action listener
        bidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTimer();
                increaseBidPrice();
            }
        });
    }

    private static void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    timerLabel.setText("남은 시간: " + timeLeft);
                } else {
                    timer.stop();
                    resultLabel.setText("입찰종료 : XXX님이 입찰에 성공하셨습니다!");
                }
            }
        });
        timer.start();
    }

    private static void resetTimer() {
        timeLeft = 10;
        timerLabel.setText("남은 시간: " + timeLeft);
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    private static void increaseBidPrice() {
        bidPrice += 1000;
        bidPriceLabel.setText("제시 가격: " + bidPrice + "원");
        resultLabel.setText("");  // Clear the result message
    }
}
