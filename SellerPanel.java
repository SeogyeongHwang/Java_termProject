package project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SellerPanel extends JPanel {
    private String name;  // 이름
    private String phone;  // 전화번호
    private JPanel productListPanel;  // 상품 목록 패널
    private JFrame parentFrame;  // 부모 프레임

    public SellerPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        // 레이아웃을 null로 설정하여 setBounds를 사용할 수 있도록 함
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

        // 사진 레이블 및 입력 필드 설정
        JLabel imageLabel = new JLabel("사진:");
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

        // 등록 버튼 이벤트 리스너
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String itemName = itemNameField.getText();
                String startPrice = startPriceField.getText();
                String image = imageField.getText();
                if (!itemName.isEmpty() && !startPrice.isEmpty() && !image.isEmpty()) {
                    addProduct(itemName, startPrice, image);
                    itemNameField.setText("");
                    startPriceField.setText("");
                    imageField.setText("");
                    
                 // 서버로 상품 정보 전송
                    /*try {
                        sendProductToServer(itemName, startPrice, image);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(parentFrame, "상품 정보를 서버에 전송하는 중 오류가 발생했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
                    }*/
                }
            }
        });
    }
    
    // 상품 추가 메서드
    private void addProduct(String itemName, String startPrice, String image) {
        JButton productButton = new JButton(itemName);
        //상품 목록 버튼 
        productButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));  
        productButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        productButton.setHorizontalAlignment(SwingConstants.LEFT);
        productButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAuctionStatus(itemName);
            }
        });
        productListPanel.add(productButton);
        productListPanel.revalidate();  //패널 업데이트 
        productListPanel.repaint();
    }

    // 경매 상태 표시 메서드 - 경매상태 서버한테 받아 
    private void showAuctionStatus(String itemName) {
        try {
            String result = getAuctionResultFromServer(itemName);
            showAuctionResult(result);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "서버와의 통신에 문제가 발생했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 서버로부터 경매 결과를 받아오는 메서드
    private String getAuctionResultFromServer(String itemName) throws IOException {
        // 실제 서버와 통신하는 코드 작성 필요
    	
        // 수정해야할듯 
        String urlString = "http://example.com/auction/result?item=" + itemName; // 서버 URL을 적절히 변경하세요
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) { // 정상 응답
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            return response.toString();
        } else {
            throw new IOException("서버 응답 코드: " + responseCode);
        }
    }

    // 경매 결과 표시 메서드
    private void showAuctionResult(String result) {
        JFrame resultFrame = new JFrame("결과 확인");
        resultFrame.setLayout(new BorderLayout());

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        JLabel resultLabel = new JLabel("낙찰자: " + result);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(resultLabel);


        /*JButton backButton = new JButton("뒤로 가기");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resultFrame.dispose();
            }
        });
        resultPanel.add(backButton);*/

        resultFrame.add(resultPanel, BorderLayout.CENTER);
        resultFrame.setSize(300, 200);
        resultFrame.setLocationRelativeTo(parentFrame);
        resultFrame.setVisible(true);
    }

}

