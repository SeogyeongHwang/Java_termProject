package term_project;

import java.io.*;
import java.net.*;
import java.util.*;

class Users {
	   private String name;
	   private String phoneNumber;
	   
	   public Users(String name, String phoneNumber) {
	      this.name = name;
	      this.phoneNumber = phoneNumber;
	   }
	   
	   public String getName() {
	      return name;
	   }
	   
	   public String getPhoneNumber() {
	      return phoneNumber;
	   }
	   
	   public boolean equals(Object data) {
	      Users user = (Users) data;
	      
	      return name.equals(user.name) && phoneNumber.equals(user.phoneNumber); 
	   }
}

class login extends Thread {
	private Socket incoming;
	private Auction auction;
	   
	login(Socket incoming, Auction auction) {
		this.incoming = incoming;
	    this.auction = auction;
	}
	   
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
	        PrintWriter out = new PrintWriter(incoming.getOutputStream(), true);
	         
	        String role = in.readLine();
	        String name = in.readLine();
	        String phonenumber = in.readLine();
	         
	        if (role != null && name != null && phonenumber != null) {
	        	Users user = new Users(name, phonenumber);
	            if (role.equals("buyer")) {
	               auction.addBuyer(user);
	               
	               String price = in.readLine();
	               if (price != null) {
	                  try {
	                     int Price = Integer.parseInt(price);
	                     // 가격과 User 정보 저장
	                  }
	               }
	            }
	            else if (role.equals("seller")) {
	            	auction.addSeller(user);
	        }
	      }
	      incoming.close();
	      } catch (Exception e) { 
	         System.out.println(e); 
	      }
	   }
}

public class AuctionServer {
	public static void main(String[] args) {
		Auction auction = new Auction();
	      
	    try {
	    	ServerSocket s = new ServerSocket(5000);
	         
	        while (true) {
	        	Socket incoming = s.accept();
	            new login(incoming, auction).start();
	        }
	    } catch (IOException e) {
	    	System.out.println(e);
	    }
	}
}

class Auction {
	private ArrayList<Users> buyers = new ArrayList<>();
	private ArrayList<Users> sellers = new ArrayList<>();
	   
	public synchronized void addBuyer(Users buyer) {
		buyers.add(buyer);
	}
	   
	public synchronized void addSeller(Users seller) {
	    sellers.add(seller);
	}
	   
	public synchronized ArrayList<Users> getBuyers() {
	    return buyers;
	}
	   
	public synchronized ArrayList<Users> getSellers() {
	    return sellers;
	}
	   
	public synchronized boolean same(Users user1, Users user2) {
	    return user1.equals(user2);
	}
}