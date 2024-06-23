/*package project1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MemberLoginGUI {
    private JFrame frame;  // ���� ������
    private JTextField nameField;  // �̸� �Է� �ʵ�
    private JTextField phoneField;  // ��ȭ��ȣ �Է� �ʵ�
    private JRadioButton buyerButton;  // ������ ���� ���� ��ư
    private JRadioButton sellerButton;  // �Ǹ��� ���� ���� ��ư
    private CardLayout cardLayout;  // ī�� ���̾ƿ�
    private JPanel mainPanel;  // ���� �г�
    private SellerPanel sellerPanel;  // �Ǹ��� �г�

    public static void main(String[] args) {
        // MemberLoginGUI�� �ν��Ͻ� ���� �� frame ���̱� //���α׷��� ������
        MemberLoginGUI window = new MemberLoginGUI();
        window.frame.setVisible(true);
    }

    public MemberLoginGUI() {
        // GUI �ʱ�ȭ  initialize�޼��� ȣ��
        initialize();
    }
    private void initialize() {
        // ���� ������ ����
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 400);  //
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // ī�� ���̾ƿ� �� ���� �г� ���� -> �гΰ��� ��ȯ 
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // �α��� �г� ���� �� ���� 
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);  //null�� setBounds�޼ҵ�ȣ�� �� �������� ���� ����

        // ������Ʈ ��ġ, ũ�� ���� �� �гο� �߰� 
        JLabel loginTitleLabel = new JLabel("ȸ�� ���� ���");
        loginTitleLabel.setBounds(120, 10, 150, 25);
        loginPanel.add(loginTitleLabel);

        // ������ ���� ��ư ����
        buyerButton = new JRadioButton("����");
        buyerButton.setBounds(100, 50, 80, 25);
        buyerButton.setSelected(true);  // �⺻ ����
        loginPanel.add(buyerButton);

        // �Ǹ��� ���� ��ư ����
        sellerButton = new JRadioButton("�Ǹ�");
        sellerButton.setBounds(200, 50, 80, 25);
        loginPanel.add(sellerButton);

        // ���� ��ư �׷� ����   -�ѹ��� �ϳ��� �ɼǸ� �����ϵ���.
        ButtonGroup group = new ButtonGroup();
        group.add(buyerButton);   
        group.add(sellerButton);

        // �̸� ���̺� �� �Է� �ʵ� ����
        JLabel nameLabel = new JLabel("�̸�");
        nameLabel.setBounds(50, 100, 80, 25);
        loginPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(140, 100, 200, 25);
        loginPanel.add(nameField);

        // ��ȭ��ȣ ���̺� �� �Է� �ʵ� ����
        JLabel phoneLabel = new JLabel("��ȭ��ȣ");
        phoneLabel.setBounds(50, 150, 80, 25);
        loginPanel.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(140, 150, 200, 25);
        loginPanel.add(phoneField);

        // ��� ��ư ���� �� �̺�Ʈ ������ �߰�
        JButton registerButton = new JButton("���");
        registerButton.setBounds(150, 200, 100, 30);
        loginPanel.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ������/�Ǹ��� ���ÿ� ���� ȭ�� ��ȯ
                if (buyerButton.isSelected()) {
                    String name = nameField.getText();  //���� �̸� ����
                    String phone = phoneField.getText();  //���� ��ȭ��ȣ ���� 
                    cardLayout.show(mainPanel, "buyerPanel");  //������ȭ������ ��ȯ
                } else if (sellerButton.isSelected()) {
                    String name = nameField.getText();   //�̸� ����
                    String phone = phoneField.getText();  //��ȭ��ȣ ���� 
                    cardLayout.show(mainPanel, "sellerPanel");   //�Һ��� ����� ������ ���� ������ 
                }
            }
        });

        // ������ �� �Ǹ��� �г� ����
        //JPanel buyerPanel = createBuyerPanel();
        sellerPanel = new SellerPanel(frame);  // �θ� �������� ����

        // ���� �гο� �гε� �߰�
        mainPanel.add(loginPanel, "loginPanel");
        //mainPanel.add(buyerPanel, "buyerPanel");
        mainPanel.add(sellerPanel, "sellerPanel");

        // �����ӿ� ���� �г� �߰�
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
    private JFrame frame;  // ���� ������
    private JTextField nameField;  // �̸� �Է� �ʵ�
    private JTextField phoneField;  // ��ȭ��ȣ �Է� �ʵ�
    private JRadioButton buyerButton;  // ������ ���� ���� ��ư
    private JRadioButton sellerButton;  // �Ǹ��� ���� ���� ��ư
    private CardLayout cardLayout;  // ī�� ���̾ƿ�
    private JPanel mainPanel;  // ���� �г�
    private SellerPanel sellerPanel;  // �Ǹ��� �г�

    public static void main(String[] args) {
        // MemberLoginGUI�� �ν��Ͻ� ���� �� frame ���̱� //���α׷��� ������
        MemberLoginGUI window = new MemberLoginGUI();
        window.frame.setVisible(true);
    }

    public MemberLoginGUI() {
        // GUI �ʱ�ȭ  initialize�޼��� ȣ��
        initialize();
    }
    private void initialize() {
        // ���� ������ ����
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 400);  //
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // ī�� ���̾ƿ� �� ���� �г� ���� -> �гΰ��� ��ȯ 
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // �α��� �г� ���� �� ���� 
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);  //null�� setBounds�޼ҵ�ȣ�� �� �������� ���� ����

        // ������Ʈ ��ġ, ũ�� ���� �� �гο� �߰� 
        JLabel loginTitleLabel = new JLabel("ȸ�� ���� ���");
        loginTitleLabel.setBounds(120, 10, 150, 25);
        loginPanel.add(loginTitleLabel);

        // ������ ���� ��ư ����
        buyerButton = new JRadioButton("����");
        buyerButton.setBounds(100, 50, 80, 25);
        buyerButton.setSelected(true);  // �⺻ ����
        loginPanel.add(buyerButton);

        // �Ǹ��� ���� ��ư ����
        sellerButton = new JRadioButton("�Ǹ�");
        sellerButton.setBounds(200, 50, 80, 25);
        loginPanel.add(sellerButton);

        // ���� ��ư �׷� ����   -�ѹ��� �ϳ��� �ɼǸ� �����ϵ���.
        ButtonGroup group = new ButtonGroup();
        group.add(buyerButton);   
        group.add(sellerButton);

        // �̸� ���̺� �� �Է� �ʵ� ����
        JLabel nameLabel = new JLabel("�̸�");
        nameLabel.setBounds(50, 100, 80, 25);
        loginPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(140, 100, 200, 25);
        loginPanel.add(nameField);

        // ��ȭ��ȣ ���̺� �� �Է� �ʵ� ����
        JLabel phoneLabel = new JLabel("��ȭ��ȣ");
        phoneLabel.setBounds(50, 150, 80, 25);
        loginPanel.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(140, 150, 200, 25);
        loginPanel.add(phoneField);

        // ��� ��ư ���� �� �̺�Ʈ ������ �߰�
        JButton registerButton = new JButton("���");
        registerButton.setBounds(150, 200, 100, 30);
        loginPanel.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ������/�Ǹ��� ���ÿ� ���� ȭ�� ��ȯ
                if (buyerButton.isSelected()) {  
                    cardLayout.show(mainPanel, "buyerPanel");  //������ȭ������ ��ȯ
                } else if (sellerButton.isSelected()) {
                    cardLayout.show(mainPanel, "sellerPanel");   //�Һ��� ����� ������ ���� ������ 
                }
            }
        });

        // ������ �� �Ǹ��� �г� ����
        //JPanel buyerPanel = createBuyerPanel();
        sellerPanel = new SellerPanel(frame);  // �θ� �������� ����

        // ���� �гο� �гε� �߰�
        mainPanel.add(loginPanel, "loginPanel");
        //mainPanel.add(buyerPanel, "buyerPanel");
        mainPanel.add(sellerPanel, "sellerPanel");

        // �����ӿ� ���� �г� �߰�
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
