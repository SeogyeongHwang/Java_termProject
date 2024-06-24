package project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

//서버한테 상품정보 받기
//서버한테 타이머 정보 받기
//입찰버튼누르면 서버한테 보내기



public class BuyerPanel extends JPanel {
    private static JLabel timerLabel;
    private static JLabel bidPriceLabel;
    private static JLabel resultLabel;
    private static Timer timer;
    private static int timeLeft = 10;  // 타이머 시작 시간 (초)
    private static int bidPrice = 10000;  // 초기 제시 가격

    public BuyerPanel() {
        setLayout(new BorderLayout());

        // Create background panel
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(new Color(0, 96, 128));  // Blue background
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);

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
        bidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bidPrice += 1000;  // Increase the bid price by 1000
                bidPriceLabel.setText("제시 가격: " + bidPrice + "원");
            }
        });
        c.gridx = 2;
        c.gridy = 0;
        bottomPanel.add(bidButton, c);

        resultLabel = new JLabel("결과: ", SwingConstants.CENTER);
        resultLabel.setOpaque(true);
        resultLabel.setBackground(Color.WHITE);
        resultLabel.setPreferredSize(new Dimension(200, 50));
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        bottomPanel.add(resultLabel, c);

        // Add panels to background panel
        backgroundPanel.add(topPanel, BorderLayout.NORTH);
        backgroundPanel.add(middlePanel, BorderLayout.CENTER);
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Start the timer
        startTimer();
    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    timerLabel.setText("남은 시간: " + timeLeft + "초");
                } else {
                    timer.stop();
                    timerLabel.setText("입찰 종료");
                    resultLabel.setText("입찰 결과: " + bidPrice + "원");
                    try {
                        sendToServer("http://example.com", String.valueOf(bidPrice));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        timer.start();
    }

    private void sendToServer(String urlString, String bidPrice) throws IOException {
        URL url = new URL(urlString + "?price=" + bidPrice);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Server returned non-200 response code: " + responseCode);
        }
        Scanner scanner = new Scanner(conn.getInputStream());
        while (scanner.hasNext()) {
            System.out.println(scanner.nextLine());
        }
        scanner.close();
    }
}
