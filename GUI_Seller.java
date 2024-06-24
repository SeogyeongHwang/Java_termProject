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
    private JPanel productListPanel;	//item list panel
    private JFrame parentFrame;			//parent frame???

    public GUI_Seller(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        // set layout=null => use setBounds
        setLayout(null);

        // setting item register panel
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(null);
        registerPanel.setBounds(10, 10, 300, 250);
        registerPanel.setBorder(BorderFactory.createTitledBorder("register product"));

        // setting item name label & field
        JLabel itemNameLabel = new JLabel("product name:");
        itemNameLabel.setBounds(10, 20, 80, 25);
        registerPanel.add(itemNameLabel);

        JTextField itemNameField = new JTextField();
        itemNameField.setBounds(100, 20, 180, 25);
        registerPanel.add(itemNameField);

        // setting start price label & field
        JLabel startPriceLabel = new JLabel("starting price:");
        startPriceLabel.setBounds(10, 60, 80, 25);
        registerPanel.add(startPriceLabel);

        JTextField startPriceField = new JTextField();
        startPriceField.setBounds(100, 60, 180, 25);
        registerPanel.add(startPriceField);

        // setting picture label & field
        JLabel imageLabel = new JLabel("picture:");
        imageLabel.setBounds(10, 100, 80, 25);
        registerPanel.add(imageLabel);

        JTextField imageField = new JTextField();
        imageField.setBounds(100, 100, 180, 25);
        registerPanel.add(imageField);

        // setting register button
        JButton addButton = new JButton("register");
        addButton.setBounds(100, 140, 80, 30);
        registerPanel.add(addButton);

        // setting product list panel
        productListPanel = new JPanel();
        productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.Y_AXIS));
        JScrollPane listScrollPane = new JScrollPane(productListPanel);
        listScrollPane.setBounds(320, 10, 200, 250);

        // add component in main panel
        add(registerPanel);
        add(listScrollPane);

        // setting register button with listener
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

    // Method to add product
    public void setSellerInfo(String name, String phone) {
        this.name = name;
        this.phone = phone;
        System.out.println("�Ǹ��� ���� - �̸�: " + name + ", ��ȭ��ȣ: " + phone);
    }

    // product list button
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

    // Method to show auction status - from server
    private void showAuctionStatus(String itemName) {
        try {
            String result = getAuctionResultFromServer(itemName);
            showAuctionResult(result);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "There was a problem communicating with the server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to receive auction results from the server
    private String getAuctionResultFromServer(String itemName) throws IOException {
        // Need to write code to communicate with server

        String urlString = "http://example.com/auction/result?item=" + itemName; // change server url ???????????????????????????????????????????????
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) { // normal response
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            return response.toString();
        } else {
            throw new IOException("server response code: " + responseCode);
        }
    }

    // Method to display auction result
    private void showAuctionResult(String result) {
        JDialog resultDialog = new JDialog(parentFrame, "Check the result", true);
        resultDialog.setLayout(new BorderLayout());

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        JLabel resultLabel = new JLabel("successful bidder: " + result);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(resultLabel);
        
        // comment

        JButton backButton = new JButton("Go back");
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
    
    private void sendDataToServer(String itemName, int startPrice, String image) {
        try {
        	Socket socket = new Socket("localhost", 5000);
        	ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(new Item_info(itemName, startPrice, image));
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}