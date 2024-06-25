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
    private JPanel productListPanel;    // 상품 목록 패널
    private JFrame parentFrame;         // 부모 프레임

    public GUI_Seller(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        // 레이아웃 설정
        setLayout(null);

        // 상품 등록 패널 설정
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(null);
        registerPanel.setBounds(10, 10, 300, 250);
        registerPanel.setBorder(BorderFactory.createTitledBorder("상품 등록"));

        // 상품명 레이블 및 입력 필드 설정
        JLabel itemNameLabel = new JLabel("상품명:");
        itemNameLabel.setBounds(10, 20, 80, 25);
        registerPanel.add(itemNameLabel);

        JTextField itemNameField = new JTextField();
        itemNameField.setBounds(100, 20, 180, 25);
        registerPanel.add(itemNameField);

        // 시작 가격 레이블 및 입력 필드 설정
        JLabel startPriceLabel = new JLabel("시작 가격:");
        startPriceLabel.setBounds(10, 60, 80, 25);
        registerPanel.add(startPriceLabel);

        JTextField startPriceField = new JTextField();
        startPriceField.setBounds(100, 60, 180, 25);
        registerPanel.add(startPriceField);

        // 이미지 경로 레이블 및 입력 필드 설정
        JLabel imageLabel = new JLabel("이미지 경로:");
        imageLabel.setBounds(10, 100, 80, 25);
        registerPanel.add(imageLabel);

        JTextField imageField = new JTextField();
        imageField.setBounds(100, 100, 180, 25);
        registerPanel.add(imageField);

        // 등록 버튼 설정
        JButton addButton = new JButton("등록");
        addButton.setBounds(100, 140, 80, 30);
        registerPanel.add(addButton);

        // 상품 목록 패널 설정
        productListPanel = new JPanel();
        productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.Y_AXIS));
        JScrollPane listScrollPane = new JScrollPane(productListPanel);
        listScrollPane.setBounds(320, 10, 200, 250);

        // 메인 패널에 컴포넌트 추가
        add(registerPanel);
        add(listScrollPane);

        // 등록 버튼에 이벤트 리스너 추가
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

    // 판매자 정보 설정
    public void setSellerInfo(String name, String phone) {
        this.name = name;
        this.phone = phone;
        System.out.println("판매자 정보 - 이름: " + name + ", 전화번호: " + phone);
    }

    // 상품 목록에 상품 추가
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

    // 서버에서 경매 상태를 가져와서 보여줌
    private void showAuctionStatus(String itemName) {
        try {
            String result = getAuctionResultFromServer(itemName);
            showAuctionResult(result);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "서버와의 통신에 문제가 발생했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 서버에서 경매 결과를 가져오는 메소드
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

    // 경매 결과를 보여주는 메소드
    private void showAuctionResult(String result) {
        JDialog resultDialog = new JDialog(parentFrame, "결과 확인", true);
        resultDialog.setLayout(new BorderLayout());

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        JLabel resultLabel = new JLabel("낙찰자: " + result);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(resultLabel);

        JButton backButton = new JButton("돌아가기");
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

    // 서버에 데이터를 전송하는 메소드
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
