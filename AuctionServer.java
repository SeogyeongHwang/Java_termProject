package Auction;

import java.io.*;
import java.net.*;
import java.util.*;

public class AuctionServer {
	public static void main(String[] args) {
		try {
			ServerSocket s = new ServerSocket(5000);
			CountdownTimer timer = new CountdownTimer(10);
			timer.start();
			while(true) {
				Socket incoming = s.accept();
				System.out.println("Client connected");
				ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
				
				boolean type = (boolean) in.readObject();
				System.out.println("client type: "+type);
				
				Login_info user = (Login_info) in.readObject();
				System.out.println("Login info readed.");

				if(type==false) {	//Seller
					Auction_Seller seller = new Auction_Seller(incoming, user, timer);
					seller.start();
					System.out.println("spawning seller thread");
				}
				else if(type== true) {	//Buyer
					new Auction_Buyer(incoming, user, timer).start();					
					System.out.println("spawning buyer thread");
				}
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}


// Auction System for Seller
class Auction_Seller extends Thread{
	Socket socket;
	Login_info user;
	CountdownTimer timer;
	
	Auction_Seller(Socket socket, Login_info user, CountdownTimer timer) {
		this.socket = socket;
		this.user = user;
		this.timer = timer;
	}
	
	public void search() {
		
	}
	
	public void run() {
		System.out.println("Seller thread started");
		try {
			System.out.println("1");
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("2");
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			
			System.out.println("Seller Info: " + user);
			int type;
			Item_info item;
			Data data = new Data(user);
			System.out.println("connected Data server " + data);
			
			//Search and deliver seller's all history
			ArrayList<Item_info> ex_list = new ArrayList<>();
			ex_list = data.getMyItem();
			out.writeObject(ex_list);
			
			//loop
			while(true) {
				type = (int) in.readObject();
				item = (Item_info) in.readObject();
				
				if(type==0) {	// item registration
					data.addItem(item);
					//out.writeObject(true);	//delete this reply
				}
				else if(type==1) {	// Check sales history
					int myitemnum = data.getItemNum(item);
					
					if(myitemnum == -1) {	//-1:Error
						Item_info noitem = new Item_info("Error",0,"Server Error.\nItem not found.");	//Item not found Error.
						out.writeObject(noitem);
					}
					else {
						out.writeObject(data.getItem(myitemnum));
					}
				}
			}
		}
		
		catch(Exception e) {
			e.printStackTrace();
		} 
	}	
}


//Auction System for Buyer
class Auction_Buyer extends Thread{
	Socket socket;
	Login_info user;
	CountdownTimer timer;
	Data data;
	boolean i = false;
	
	Auction_Buyer(Socket socket, Login_info user, CountdownTimer timer) {
		this.socket = socket;
		this.user = user;
		this.timer = timer;
		data = new Data(user);
	}
	
	public void run() {
		// send data
		try{
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			int my_price;
			
			Thread send_data = new Thread(()->{
				try {
					while(true) {
						out.writeObject(i);
						out.writeObject(data.getNowItem());
						out.writeObject(timer.getTime());
						i=false;	// set i=false
					}
				}
				catch(Exception e) {
					System.out.println(e);
				}
			});
			send_data.start();
			// receive data, set i
			try {
				while(true) {
					my_price = in.readInt();
					if(my_price>data.getNowPrice()) {
						data.setNowPrice(my_price);
						i=true;
					}
				}
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}	
}


// Data base (Acts as a Central Server)
class Data{
	static ArrayList<Login_info> sellerList = new ArrayList<>();
	static ArrayList<Item_info> itemList = new ArrayList<>();
	static int num=0;	// Sales item number
	Login_info user;
	
	
	Data(Login_info user){
		this.user = user;
	}
	
	Data(){	// method for CountdownTimer
	}
	
	public synchronized void addItem(Item_info item) {
		sellerList.add(user);
		itemList.add(item);	
		notifyAll();		// to wake timer
	}
	
	public int getSize() {
		return sellerList.size();
	}
	
	public int getNow() {
		return num;
	}
	
	// item info on sale
	public Item_info getNowItem(){
		return itemList.get(num);
	}
	
	public int getNowPrice() {
		return itemList.get(num).getPrice();
	}
	
	public void setNowPrice(int my_price) {
		itemList.get(num).setPrice(my_price, user);
	}
	
	// change to next item
	public boolean nextItem() {
		if(itemList.size() > num) {
			num++;
			return true;
		}
		else {
			return false;
		}
	}
	
	public Login_info getSeller(int n) {
		return sellerList.get(n);
	}
	
	public ArrayList<Item_info> getMyItem() {
		ArrayList<Item_info> myitem = new ArrayList<>();
		int size = sellerList.size();
		
		for(int i=0;i<size;i++) {
			if(sellerList.get(i)==user) {
				myitem.add(itemList.get(i));
			}
		}
		return myitem;
	}
	
	public Item_info getItem(int i) {
		return itemList.get(i);
	}
	
	public int getItemNum(Item_info item) {	// Check seller name + item name
		String itemname = item.getItemname();
		String name = user.getName();
		int size = sellerList.size();
		int match=-1;
		
		for(int i=0;i<size;i++) {
			if(itemList.get(i).getItemname()==itemname) {	//match item name
				match=i;
				if(sellerList.get(match).getName()!=name) {	//mismatch seller name
					match=-1;
				}
			}
			else{	//mismatch item name
				match=-1;
			}
		}
		return match;
	}	
}


// Timer
class CountdownTimer extends Thread{
	int count = 0;	// countdown time initial value
	int init_count = 0;
	Data data = new Data();
	
	CountdownTimer(int count){
		this.init_count=count;
		this.count = count;
	}
	
	public int getTime() {
		return count;
	}
	
	public synchronized void run() {
		try {
			wait();		// start when at least one product is registered
		}
		catch(Exception e) {
			System.out.println(e);
		}
		while(true) {	// STOP when sold out all items /// or wait() until registration new item
			try {
				count = init_count;
				for(int i=count; i>=0;i--) {
					//System.out.println("Time: "+i+"s");
					count = i;
					Thread.sleep(1000);
				}
				//System.out.println("Time's up");
				if(!(data.nextItem())) {	// sell next following item
					wait();
				}
			}
			catch(Exception e) {
				System.out.println(e);
			}
		} 
	}
}

//Item info
class Item_info implements Serializable{
	private static final long serialVersionUID = 1L;
	int value=0;	// For select sale/saled/fail to sale
	String itemName;
	int price;
	String img_path;
	String buyer;
	
	Item_info(String itemName, int price, String img_path){
		this.itemName = itemName;
		this.price = price;
		this.img_path = img_path;
	}
	
	Item_info(String itemName){
		this.itemName = itemName;
	}
	
	public void setValue(int v) {
		value = v;
	}
	
	public synchronized void setPrice(int price, Login_info buyer) {	//synchro
		this.price = price;
		this.buyer = buyer.getName();
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
	
	public String getBuyer() {
		return buyer;
	}
	
}


// Login_info class
class Login_info implements Serializable {
	private static final long serialVersionUID = 1L;
	String name;
	String phonenum;
	
	Login_info(String name, String phonenum){
		this.name = name;
		this.phonenum = phonenum;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPhonenum() {
		return phonenum;
	}

	@Override
	public String toString() {
		return name + "," + phonenum;
	}
}
