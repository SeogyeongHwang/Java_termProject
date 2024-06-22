package Auction;

import java.io.*;
import java.net.*;

public class AuctionServer {
	public static void main(String[] args) {
		try {
			ServerSocket s = new ServerSocket(5000);
			while(true) {
				Socket incoming = s.accept();
				ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
				int type = (int) in.readObject();
				
				if(type==0) {	//Seller
					new Auction_Seller(in).start();					
					System.out.println("spawning seller thread");
				}
				else if(type==1) {	//Buyer
					new Auction_Buyer(in).start();					
					System.out.println("spawning buyer thread");
				}
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}

class Auction_Seller extends Thread{
	ObjectInputStream in;
	
	Auction_Seller(ObjectInputStream in) {
		this.in = in;
	}
	
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(in.getInputStream()));
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
}

// Item info
class Item_info{
	int value=0;	//유찰 낙찰 판단용
	String itemName;
	int price;
	String img_path;
	int time;
	
	Item_info(String itemName, int price, String img_path, int time){
		this.itemName = itemName;
		this.price = price;
		this.img_path = img_path;
		this.time = time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}

	public String getItemname() {
		return itemName;
	}
	
	public int getPrice() {
		return price;
	}
	
	public String getImgpath() {
		return img_path;
	}
	
	public int getTime() {
		return time;
	}
	
}


// Seller class
class Seller{
	Login_info user;
	int click;	//물품등록 클릭=0, 판매내역 클릭=1
	String itemName;
	int start_price;
	String img_path;
	
	Seller(Login_info user,int click,String itemName,int start_price, String img_path){
		this.user = user;
		this.click = click;
		this.itemName = itemName;
		this.start_price = start_price;
		this.img_path = img_path;
	}
	
	public Login_info getLogininfo() {
		return user;
	}
	
	public int getClick() {
		return click;
	}
	
	public String getItemname() {
		return itemName;
	}
	
	public int getStartprice() {
		return start_price;
	}
	
	public String getImgpath() {
		return img_path;
	}
}


// Buyer class
class Buyer{
	Login_info user;
	int price;
	
	Buyer(Login_info user, int price){
		this.user = user;
		this.price = price;
	}
	
	public Login_info getLogininfo() {
		return user;
	}
	
	public int getPrice() {
		return price;
	}
}


// Login_info class
class Login_info{
	String name;
	String phone_num;
	
	Login_info(String name, String phone_num){
		this.name = name;
		this.phone_num=phone_num;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPhonenum() {
		return phone_num;
	}
}