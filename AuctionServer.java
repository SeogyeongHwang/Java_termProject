package Auction_git;

import java.io.*;
import java.net.*;
import java.util.*;

public class AuctionServer {
	public static void main(String[] args) {
		try {
			ServerSocket s = new ServerSocket(5000);
			while(true) {
				Socket incoming = s.accept();
				System.out.println("Client connected");
				ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
				
				boolean type = (boolean) in.readObject();
				System.out.println("client type: "+type);
				
				Login_info user = (Login_info) in.readObject();
				System.out.println("Login info readed.");

				if(type==false) {	//Seller
					Auction_Seller seller = new Auction_Seller(incoming, user);
					seller.start();
					System.out.println("spawning seller thread");
				}
				else if(type== true) {	//Buyer
					new Auction_Buyer(incoming, user).start();					
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
	
	Auction_Seller(Socket socket, Login_info user) {
		this.socket = socket;
		this.user = user;
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
			
			//Search and deliver seller's history
			ArrayList<Item_info> ex_list = new ArrayList<>();
			ex_list = data.getMyItem();
			out.writeObject(ex_list);
			
			//loop
			while(true) {
				type = (int) in.readObject();
				item = (Item_info) in.readObject();
				
				if(type==0) {	// item registration
					data.addItem(item);
					out.writeObject(1);
				}
				else if(type==1) {	// Check sales history
					int myitemnum = data.getItemNum(item);
					
					if(myitemnum == -1) {	//-1:Error
						Item_info noitem = new Item_info("Error",0,"");	//3��° String�� Error�̹��� �ʿ�.
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
	Buyer lastbuyer;
	Login_info user;
	ArrayList<ObjectOutputStream> clients = new ArrayList<>();
	Data data;
	CountdownTimer timer;
	
	Auction_Buyer(Socket socket, Login_info user) {
		this.socket = socket;
		this.user = user;
	}
	
	private void Time(int time) {
		for (ObjectOutputStream out : clients) {
			try {
				out.writeObject(time);
				out.flush();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
	
	public void run() {
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			clients.add(out);
			System.out.println(clients);
			Buyer buyer = (Buyer) in.readObject();
			
			if (clients != null && data.getNowItem() != null) {
				timer = new CountdownTimer(5);
				timer.start();
				
				while(true) {
					int remainTime = timer.getTime();
					Time(remainTime);

					if (remainTime == 0) {
						System.out.println("Auction end");
						break;
					}
					boolean button = (boolean) in.readObject();
					if (button == true) {
						int price = (int) in.readObject();
						buyer = (Buyer) in.readObject();
						int currentItemIndex = data.getNow();
						Login_info currentSeller = data.getSeller(currentItemIndex);
							
						if (!currentSeller.getName().equals(buyer.getLogininfo().getName()) || !currentSeller.getPhonenum().equals(buyer.getLogininfo().getPhonenum()))
							lastbuyer = new Buyer(user, price);
						else System.out.println("Seller cannot buy their own item");
						
						timer = new CountdownTimer(5);
						timer.start();
						
					}
				}
			}
			out.writeObject(lastbuyer);
		} catch(Exception e) {
			System.out.println(e);
		}
	}	
}


// Data base (Acts as a Central Server)
class Data{
	private ArrayList<Login_info> sellerList = new ArrayList<>();
	private ArrayList<Item_info> itemList = new ArrayList<>();
	private int num=0;	// Sales item number

	Login_info user;
	
	
	Data(Login_info user){
		this.user = user;
	}
	
	public synchronized void addItem(Item_info item) {
		sellerList.add(user);
		itemList.add(item);	
	}
	
	public int len() {
		return sellerList.size();
	}
	
	public int getNow() {
		return num;
	}
	
	// item info on sale
	public Item_info getNowItem(){
		return itemList.get(num);
	}
	
	// change to next item
	public void nextItem() {
		if(itemList.size() > num) {
			num++;
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
	boolean running = true;
	
	CountdownTimer(int count){
		this.init_count=count;
		this.count = count;
	}
	
	public int getTime() {
		return count;
	}
	
	public void run() {
		while(running) {	// STOP when sold out all items /// or wait() until registration new item
			try {
				count = init_count;
				for(int i=count; i>=0;i--) {
					if (!running) break;
					System.out.println("Time: "+i+"s");
					count = i;
					Thread.sleep(1000);
				}
				System.out.println("Time's up");

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


// Seller class
class Seller{
	Login_info user;
	int click;	//registration item=0 sell item=1
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
