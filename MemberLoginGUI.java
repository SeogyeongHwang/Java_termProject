/*package project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MemberLoginGUI {
    private JFrame frame;  // 메인 프레임
    private JTextField nameField;  // 이름 입력 필드
    private JTextField phoneField;  // 전화번호 입력 필드
    private JRadioButton buyerButton;  // 구매자 선택 라디오 버튼
    private JRadioButton sellerButton;  // 판매자 선택 라디오 버튼
    private CardLayout cardLayout;  // 카드 레이아웃
    private JPanel mainPanel;  // 메인 패널
    private SellerPanel sellerPanel;  // 판매자 패널

    public static void main(String[] args) {
        // MemberLoginGUI의 인스턴스 생성 후 frame 보이기 //프로그램의 진입점
        MemberLoginGUI window = new MemberLoginGUI();
        window.frame.setVisible(true);
    }

    public MemberLoginGUI() {
        // GUI 초기화  initialize메서드 호출
        initialize();
    }
    private void initialize() {
        // 메인 프레임 설정
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 400);  //
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // 카드 레이아웃 및 메인 패널 설정 -> 패널간의 전환 
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 로그인 패널 생성 및 구성 
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);  //null로 setBounds메소드호출 후 수동으로 지정 가능

        // 컴포넌트 위치, 크기 설정 및 패널에 추가 
        JLabel loginTitleLabel = new JLabel("회원 정보 등록");
        loginTitleLabel.setBounds(120, 10, 150, 25);
        loginPanel.add(loginTitleLabel);

        // 구매자 라디오 버튼 설정
        buyerButton = new JRadioButton("구매");
        buyerButton.setBounds(100, 50, 80, 25);
        buyerButton.setSelected(true);  // 기본 설정
        loginPanel.add(buyerButton);

        // 판매자 라디오 버튼 설정
        sellerButton = new JRadioButton("판매");
        sellerButton.setBounds(200, 50, 80, 25);
        loginPanel.add(sellerButton);

        // 라디오 버튼 그룹 설정   -한번에 하나의 옵션만 선택하도록.
        ButtonGroup group = new ButtonGroup();
        group.add(buyerButton);   
        group.add(sellerButton);

        // 이름 레이블 및 입력 필드 설정
        JLabel nameLabel = new JLabel("이름");
        nameLabel.setBounds(50, 100, 80, 25);
        loginPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(140, 100, 200, 25);
        loginPanel.add(nameField);

        // 전화번호 레이블 및 입력 필드 설정
        JLabel phoneLabel = new JLabel("전화번호");
        phoneLabel.setBounds(50, 150, 80, 25);
        loginPanel.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(140, 150, 200, 25);
        loginPanel.add(phoneField);

        // 등록 버튼 설정 및 이벤트 리스너 추가
        JButton registerButton = new JButton("등록");
        registerButton.setBounds(150, 200, 100, 30);
        loginPanel.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 구매자/판매자 선택에 따라 화면 전환
                if (buyerButton.isSelected()) {
                    String name = nameField.getText();  //읽은 이름 저장
                    String phone = phoneField.getText();  //읽은 전화번호 저장 
                    cardLayout.show(mainPanel, "buyerPanel");  //구매자화면으로 전환
                } else if (sellerButton.isSelected()) {
                    String name = nameField.getText();   //이름 저장
                    String phone = phoneField.getText();  //전화번호 저장 
                    cardLayout.show(mainPanel, "sellerPanel");   //소비자 만들고 서버로 정보 보내기 
                }
            }
        });

        // 구매자 및 판매자 패널 생성
        //JPanel buyerPanel = createBuyerPanel();
        sellerPanel = new SellerPanel(frame);  // 부모 프레임을 전달

        // 메인 패널에 패널들 추가
        mainPanel.add(loginPanel, "loginPanel");
        //mainPanel.add(buyerPanel, "buyerPanel");
        mainPanel.add(sellerPanel, "sellerPanel");

        // 프레임에 메인 패널 추가
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "loginPanel");
        
        
    }
}*/

package project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MemberLoginGUI {
    private JFrame frame;  // 메인 프레임
    private JTextField nameField;  // 이름 입력 필드
    private JTextField phoneField;  // 전화번호 입력 필드
    private JRadioButton buyerButton;  // 구매자 선택 라디오 버튼
    private JRadioButton sellerButton;  // 판매자 선택 라디오 버튼
    private CardLayout cardLayout;  // 카드 레이아웃
    private JPanel mainPanel;  // 메인 패널
    private SellerPanel sellerPanel;  // 판매자 패널

    public static void main(String[] args) {
        // MemberLoginGUI의 인스턴스 생성 후 frame 보이기 //프로그램의 진입점
        MemberLoginGUI window = new MemberLoginGUI();
        window.frame.setVisible(true);
    }

    public MemberLoginGUI() {
        // GUI 초기화  initialize메서드 호출
        initialize();
    }
    private void initialize() {
        // 메인 프레임 설정
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 400);  //
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // 카드 레이아웃 및 메인 패널 설정 -> 패널간의 전환 
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 로그인 패널 생성 및 구성 
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);  //null로 setBounds메소드호출 후 수동으로 지정 가능

        // 컴포넌트 위치, 크기 설정 및 패널에 추가 
        JLabel loginTitleLabel = new JLabel("회원 정보 등록");
        loginTitleLabel.setBounds(120, 10, 150, 25);
        loginPanel.add(loginTitleLabel);

        // 구매자 라디오 버튼 설정
        buyerButton = new JRadioButton("구매");
        buyerButton.setBounds(100, 50, 80, 25);
        buyerButton.setSelected(true);  // 기본 설정
        loginPanel.add(buyerButton);

        // 판매자 라디오 버튼 설정
        sellerButton = new JRadioButton("판매");
        sellerButton.setBounds(200, 50, 80, 25);
        loginPanel.add(sellerButton);

        // 라디오 버튼 그룹 설정   -한번에 하나의 옵션만 선택하도록.
        ButtonGroup group = new ButtonGroup();
        group.add(buyerButton);   
        group.add(sellerButton);

        // 이름 레이블 및 입력 필드 설정
        JLabel nameLabel = new JLabel("이름");
        nameLabel.setBounds(50, 100, 80, 25);
        loginPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(140, 100, 200, 25);
        loginPanel.add(nameField);

        // 전화번호 레이블 및 입력 필드 설정
        JLabel phoneLabel = new JLabel("전화번호");
        phoneLabel.setBounds(50, 150, 80, 25);
        loginPanel.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(140, 150, 200, 25);
        loginPanel.add(phoneField);

        // 등록 버튼 설정 및 이벤트 리스너 추가
        JButton registerButton = new JButton("등록");
        registerButton.setBounds(150, 200, 100, 30);
        loginPanel.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 구매자/판매자 선택에 따라 화면 전환
                if (buyerButton.isSelected()) {  
                    cardLayout.show(mainPanel, "buyerPanel");  //구매자화면으로 전환
                } else if (sellerButton.isSelected()) {
                    cardLayout.show(mainPanel, "sellerPanel");   //소비자 만들고 서버로 정보 보내기 
                }
            }
        });

        // 구매자 및 판매자 패널 생성
        //JPanel buyerPanel = createBuyerPanel();
        sellerPanel = new SellerPanel(frame);  // 부모 프레임을 전달

        // 메인 패널에 패널들 추가
        mainPanel.add(loginPanel, "loginPanel");
        //mainPanel.add(buyerPanel, "buyerPanel");
        mainPanel.add(sellerPanel, "sellerPanel");

        // 프레임에 메인 패널 추가
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "loginPanel");
    }
    
    private void sendDataToServer(boolean type, String name, String phone) {
        try (Socket socket = new Socket("localhost", 5000);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            System.out.println("ObjectOutputStream created on client");

            out.writeObject(type);
            out.writeObject(new LoginInfo(name, phone));
            out.flush();
            System.out.println("Data sent to server: " + name + ", " + phone);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class LoginInfo implements java.io.Serializable {
        private String name;
        private String phone;

        public LoginInfo(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }
    }
}
