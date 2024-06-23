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
    private String name;  // �̸�
    private String phone;  // ��ȭ��ȣ
    private JPanel productListPanel;  // ��ǰ ��� �г�
    private JFrame parentFrame;  // �θ� ������

    public SellerPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        // ���̾ƿ��� null�� �����Ͽ� setBounds�� ����� �� �ֵ��� ��
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

        // ���� ���̺� �� �Է� �ʵ� ����
        JLabel imageLabel = new JLabel("����:");
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

        // ��� ��ư �̺�Ʈ ������
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
                    
                 // ������ ��ǰ ���� ����
                    /*try {
                        sendProductToServer(itemName, startPrice, image);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(parentFrame, "��ǰ ������ ������ �����ϴ� �� ������ �߻��߽��ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
                    }*/
                }
            }
        });
    }
    
    // ��ǰ �߰� �޼���
    private void addProduct(String itemName, String startPrice, String image) {
        JButton productButton = new JButton(itemName);
        //��ǰ ��� ��ư 
        productButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));  
        productButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        productButton.setHorizontalAlignment(SwingConstants.LEFT);
        productButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAuctionStatus(itemName);
            }
        });
        productListPanel.add(productButton);
        productListPanel.revalidate();  //�г� ������Ʈ 
        productListPanel.repaint();
    }

    // ��� ���� ǥ�� �޼��� - ��Ż��� �������� �޾� 
    private void showAuctionStatus(String itemName) {
        try {
            String result = getAuctionResultFromServer(itemName);
            showAuctionResult(result);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "�������� ��ſ� ������ �߻��߽��ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // �����κ��� ��� ����� �޾ƿ��� �޼���
    private String getAuctionResultFromServer(String itemName) throws IOException {
        // ���� ������ ����ϴ� �ڵ� �ۼ� �ʿ�
    	
        // �����ؾ��ҵ� 
        String urlString = "http://example.com/auction/result?item=" + itemName; // ���� URL�� ������ �����ϼ���
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) { // ���� ����
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            return response.toString();
        } else {
            throw new IOException("���� ���� �ڵ�: " + responseCode);
        }
    }

    // ��� ��� ǥ�� �޼���
    private void showAuctionResult(String result) {
        JFrame resultFrame = new JFrame("��� Ȯ��");
        resultFrame.setLayout(new BorderLayout());

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        JLabel resultLabel = new JLabel("������: " + result);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(resultLabel);


        /*JButton backButton = new JButton("�ڷ� ����");
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

