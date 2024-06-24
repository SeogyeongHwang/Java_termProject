package project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class BuyerPanel extends JPanel {
    private JLabel itemNameLabel;
    private JLabel startPriceLabel;
    private JLabel currentPriceLabel;
    private JLabel timerLabel;
    private JTextField bidPriceField;
    private JButton bidButton;
    private JLabel imageLabel;
    private String name;
    private String phone;
    private JFrame parentFrame;
    private String currentProductName;
    private int currentPrice;
    private Timer timer;
    private int remainingTime;
    private boolean isRunning;

    public BuyerPanel() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout());

        // 상품명 레이블
        itemNameLabel = new JLabel("상품명");
        topPanel.add(itemNameLabel, BorderLayout.NORTH);

        // 이미지 레이블
        imageLabel = new JLabel("사진");
        centerPanel.add(imageLabel, BorderLayout.CENTER);

        // 시작 가격 레이블
        startPriceLabel = new JLabel("시작 가격");
        centerPanel.add(startPriceLabel, BorderLayout.NORTH);

        // 현재 가격 레이블
        currentPriceLabel = new JLabel("현재 가격");
        centerPanel.add(currentPriceLabel, BorderLayout.SOUTH);

        // 타이머 레이블
        timerLabel = new JLabel("타이머");
        bottomPanel.add(timerLabel);

        // 입찰가 입력 필드
        bidPriceField = new JTextField(10);
        bottomPanel.add(bidPriceField);

        // 입찰 버튼
        bidButton = new JButton("입찰 버튼");
        bottomPanel.add(bidButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // 입찰 버튼 이벤트 리스너
        bidButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int bidPrice = Integer.parseInt(bidPriceField.getText());
                if (bidPrice > currentPrice) {
                    sendBidToServer(bidPrice);
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "입찰가가 현재가보다 낮습니다.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 서버로부터 데이터를 받는 스레드 시작
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 5000);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                while (true) {
                    currentProductName = (String) in.readObject();
                    String imagePath = (String) in.readObject();
                    int startPrice = (int) in.readObject();
                    currentPrice = (int) in.readObject();
                    remainingTime = (int) in.readObject();

                    // UI 업데이트
                    SwingUtilities.invokeLater(() -> {
                        itemNameLabel.setText(currentProductName);
                        // 이미지 업데이트 로직 필요
                        startPriceLabel.setText("시작 가격: " + startPrice);
                        currentPriceLabel.setText("현재 가격: " + currentPrice);
                        updateTimer();
                    });
                }
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    // 서버로 입찰 정보를 보내는 메서드
    private void sendBidToServer(int bidPrice) {
        try (Socket socket = new Socket("localhost", 5000);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(name);
            out.writeObject(phone);
            out.writeObject(currentProductName);
            out.writeObject(bidPrice);
            out.writeObject("bid");
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // 타이머 업데이트 메서드
    private void updateTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        isRunning = true;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (remainingTime > 0) {
                    remainingTime--;
                    timerLabel.setText("타이머: " + remainingTime + "초");
                } else {
                    timer.cancel();
                    isRunning = false;
                }
            }
        }, 0, 1000);
    }
}

