package project1;

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
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // ī�� ���̾ƿ� �� ���� �г� ���� -> �гΰ��� ��ȯ 
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // �α��� �г� ���� �� ���� 
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);   //

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

        // ���� ��ư �׷� ����   
        ButtonGroup group = new ButtonGroup();
        group.add(buyerButton);   //
        group.add(sellerButton);

        // �̸� ���̺� �� �Է� �ʵ� ����
        JLabel nameLabel = new JLabel("�̸�");
        nameLabel.setBounds(50, 100, 80, 25);
        loginPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(140, 100, 200, 25);
        loginPanel.add(nameField);
        nameField.setColumns(10);   //

        // ��ȭ��ȣ ���̺� �� �Է� �ʵ� ����
        JLabel phoneLabel = new JLabel("��ȭ��ȣ");
        phoneLabel.setBounds(50, 150, 80, 25);
        loginPanel.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(140, 150, 200, 25);
        loginPanel.add(phoneField);
        phoneField.setColumns(10);   //

        // ��� ��ư ���� �� �̺�Ʈ ������ �߰�
        JButton registerButton = new JButton("���");
        registerButton.setBounds(150, 200, 100, 30);
        loginPanel.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ������/�Ǹ��� ���ÿ� ���� ȭ�� ��ȯ
                if (buyerButton.isSelected()) {
                    cardLayout.show(mainPanel, "buyerPanel");
                } else if (sellerButton.isSelected()) {
                    String name = nameField.getText();
                    String phone = phoneField.getText();
                    sellerPanel.setSellerInfo(name, phone);  // �Ǹ��� ���� ����
                    cardLayout.show(mainPanel, "sellerPanel");
                }
            }
        });

        // ������ �� �Ǹ��� �г� ����
        JPanel buyerPanel = createBuyerPanel();
        sellerPanel = new SellerPanel(frame);  // �θ� �������� ����

        // ���� �гο� �гε� �߰�
        mainPanel.add(loginPanel, "loginPanel");
        mainPanel.add(buyerPanel, "buyerPanel");
        mainPanel.add(sellerPanel, "sellerPanel");

        // �����ӿ� ���� �г� �߰�
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "loginPanel");
    }

    private JPanel createBuyerPanel() {
        // ������ �г� ���� �� ����
        JPanel buyerPanel = new JPanel();
        buyerPanel.setLayout(new BorderLayout());
        JLabel label = new JLabel("������ ȭ��");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        buyerPanel.add(label, BorderLayout.CENTER);
        return buyerPanel;
    }
}
