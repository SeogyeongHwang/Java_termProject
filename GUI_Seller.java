package Auction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class GUI_Seller extends JPanel {
    private String name; 
    private String phone;
    private JPanel productListPanel;    // ��ǰ ��� �г�
    private JFrame parentFrame;         // �θ� ������

    public GUI_Seller(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        // ���̾ƿ� ����
        setLayout(null);

        // ��ǰ ��� �г� ����
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(null);
        registerPanel.setBounds(10, 10, 300, 250);
        registerPanel.setBorder(BorderFactory.createTitledBorder("��ǰ ���"));

        // ��ǰ�� ���̺� �� �Է� �ʵ� ����
        JLabel itemNameLabel = new JLabel("��ǰ��:");
        itemNameLabel.setBounds(10, 20, 80, 25);
        registerPanel.add(itemNameLabel);

        JTextField itemNameField = new JTextField();
        itemNameField.setBounds(100, 20, 180, 25);
        registerPanel.add(itemNameField);

        // ���� ���� ���̺� �� �Է� �ʵ� ����
        JLabel startPriceLabel = new JLabel("���� ����:");
        startPriceLabel.setBounds(10, 60, 80, 25);
        registerPanel.add(startPriceLabel);

        JTextField startPriceField = new JTextField();
        startPriceField.setBounds(100, 60, 180, 25);
        registerPanel.add(startPriceField);

        // �̹��� ��� ���̺� �� �Է� �ʵ� ����
        JLabel imageLabel = new JLabel("�̹��� ���:");
        imageLabel.setBounds(10, 100, 80, 25);
        registerPanel.add(imageLabel);

        JTextField imageField = new JTextField();
        imageField.setBounds(100, 100, 180, 25);
        registerPanel.add(imageField);

        // ��� ��ư ����
        JButton addButton = new JButton("���");
        addButton.setBounds(100, 140, 80, 30);
        registerPanel.add(addButton);

        // ��ǰ ��� �г� ����
        productListPanel = new JPanel();
        productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.Y_AXIS));
        JScrollPane listScrollPane = new JScrollPane(productListPanel);
        listScrollPane.setBounds(320, 10, 200, 250);

        // ���� �гο� ������Ʈ �߰�
        add(registerPanel);
        add(listScrollPane);

        // ��� ��ư�� �̺�Ʈ ������ �߰�
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String itemName = itemNameField.getText();
                String startPrice = startPriceField.getText();
                String image = imageField.getText();
                if (!itemName.isEmpty() && !startPrice.isEmpty() && !image.isEmpty()) {
                    sendDataToServer(itemName, Integer.parseInt(startPrice), image);

                    addProduct(itemName, startPrice, image);
                    itemNameField.setText("");
                    startPriceField.setText("");
                    imageField.setText("");
                }
            }
        });
    }

    // �Ǹ��� ���� ����
    public void setSellerInfo(String name, String phone) {
        this.name = name;
        this.phone = phone;
        System.out.println("�Ǹ��� ���� - �̸�: " + name + ", ��ȭ��ȣ: " + phone);
    }

    // ��ǰ ��Ͽ� ��ǰ �߰�
    private void addProduct(String itemName, String startPrice, String image) {
        JButton productButton = new JButton(itemName);
        productButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        productButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        productButton.setHorizontalAlignment(SwingConstants.LEFT);
        productButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAuctionStatus(itemName);
            }
        });
        productListPanel.add(productButton);
        productListPanel.revalidate();
        productListPanel.repaint();
    }

    // �������� ��� ���¸� �����ͼ� ������
    private void showAuctionStatus(String itemName) {
        try {
            String result = getAuctionResultFromServer(itemName);
            showAuctionResult(result);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "�������� ��ſ� ������ �߻��߽��ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // �������� ��� ����� �������� �޼ҵ�
    private String getAuctionResultFromServer(String itemName) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket("localhost", 5000);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            out.writeObject("GET_RESULT");
            out.writeObject(itemName);
            out.flush();
            return (String) in.readObject();
        }
    }

    // ��� ����� �����ִ� �޼ҵ�
    private void showAuctionResult(String result) {
        JDialog resultDialog = new JDialog(parentFrame, "��� Ȯ��", true);
        resultDialog.setLayout(new BorderLayout());

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        JLabel resultLabel = new JLabel("������: " + result);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(resultLabel);

        JButton backButton = new JButton("���ư���");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resultDialog.dispose();
            }
        });
        resultPanel.add(backButton);

        resultDialog.add(resultPanel, BorderLayout.CENTER);
        resultDialog.setSize(300, 200);
        resultDialog.setLocationRelativeTo(parentFrame);
        resultDialog.setVisible(true);
    }

    // ������ �����͸� �����ϴ� �޼ҵ�
    private void sendDataToServer(String itemName, int startPrice, String image) {
        try {
            Socket socket = new Socket("localhost", 5000);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(new Item_info(itemName, startPrice, image));
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
