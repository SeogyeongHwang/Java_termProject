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

        // ��ǰ�� ���̺�
        itemNameLabel = new JLabel("��ǰ��");
        topPanel.add(itemNameLabel, BorderLayout.NORTH);

        // �̹��� ���̺�
        imageLabel = new JLabel("����");
        centerPanel.add(imageLabel, BorderLayout.CENTER);

        // ���� ���� ���̺�
        startPriceLabel = new JLabel("���� ����");
        centerPanel.add(startPriceLabel, BorderLayout.NORTH);

        // ���� ���� ���̺�
        currentPriceLabel = new JLabel("���� ����");
        centerPanel.add(currentPriceLabel, BorderLayout.SOUTH);

        // Ÿ�̸� ���̺�
        timerLabel = new JLabel("Ÿ�̸�");
        bottomPanel.add(timerLabel);

        // ������ �Է� �ʵ�
        bidPriceField = new JTextField(10);
        bottomPanel.add(bidPriceField);

        // ���� ��ư
        bidButton = new JButton("���� ��ư");
        bottomPanel.add(bidButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // ���� ��ư �̺�Ʈ ������
        bidButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int bidPrice = Integer.parseInt(bidPriceField.getText());
                if (bidPrice > currentPrice) {
                    sendBidToServer(bidPrice);
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "�������� ���簡���� �����ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // �����κ��� �����͸� �޴� ������ ����
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

                    // UI ������Ʈ
                    SwingUtilities.invokeLater(() -> {
                        itemNameLabel.setText(currentProductName);
                        // �̹��� ������Ʈ ���� �ʿ�
                        startPriceLabel.setText("���� ����: " + startPrice);
                        currentPriceLabel.setText("���� ����: " + currentPrice);
                        updateTimer();
                    });
                }
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    // ������ ���� ������ ������ �޼���
    private void sendBidToServer(int bidPrice) {
        try (Socket socket = new Socket("localhost", 5000);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(name);
            out.writeObject("��");
            out.writeObject(currentProductName);
            out.writeObject(bidPrice);
            out.writeObject("bid");
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Ÿ�̸� ������Ʈ �޼���
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
                    timerLabel.setText("Ÿ�̸�: " + remainingTime + "��");
                } else {
                    timer.cancel();
                    isRunning = false;
                }
            }
        }, 0, 1000);
    }
}

