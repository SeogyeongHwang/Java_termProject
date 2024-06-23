package Auction;

import java.io.*;
import java.net.*;
import java.util.*;

public class AuctionServer {
	public static void main(String[] args) {
		try {
			ServerSocket s = new ServerSocket(5000);
			while(true) {
				Socket incoming = s.accept();
				ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
				
				int type = (int) in.readObject();					//user type 판단 (판매자or구매자)
				Login_info user = (Login_info) in.readObject();		//login_info 저장
				
				if(type==0) {	//Seller
					new Auction_Seller(incoming, user).start();					
					System.out.println("spawning seller thread");
				}
				else if(type==1) {	//Buyer
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
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());;
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			int type;
			Item_info item;
			Data data = new Data(user);

			//판매자 기존 정보 검색,전달.
			ArrayList<Item_info> ex_list = new ArrayList<>();
			ex_list = data.getMyItem();
			out.writeObject(ex_list);
			
			//loop
			while(true) {
				type = (int) in.readObject();
				item = (Item_info) in.readObject();
				
				if(type==0) {	//물품 등록
					data.addItem(item);
					out.writeObject(1);
				}
				else if(type==1) {	//판매내역 확인
					int myitemnum = data.getItemNum(item);
					
					if(myitemnum == -1) {	//-1: 없음(오류)
						Item_info noitem = new Item_info("Error",0,"");	//3번째 String에 Error이미지 필요.
						out.writeObject(noitem);
					}
					else {
						out.writeObject(data.getItem(myitemnum));
					}
				}
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}


// Auction System for Buyer
class Auction_Buyer extends Thread{
	Socket socket;
	Login_info user;
	
	Auction_Buyer(Socket socket, Login_info user) {
		this.socket = socket;
		this.user = user;
	}
	public void run() {
		try {
			
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}


// Data base (Acts as a Central Server)
class Data{
	private ArrayList<Login_info> sellerList = new ArrayList<>();
	private ArrayList<Item_info> itemList = new ArrayList<>();
	private int num=0;	//판매중인 번호

	Login_info user;
	
	
	Data(Login_info user){
		this.user = user;
	}
	
	public void addItem(Item_info item) {	//serialized 필요할것 같음.
		sellerList.add(user);
		itemList.add(item);	
	}
	
	public int len() {
		return sellerList.size();
	}
	
	public int getNow() {
		return num;
	}
	
	//현재 판매중인 아이템 정보.
	public Item_info getNowItem(){
		return itemList.get(num);
	}
	
	//판매아이템을 다음으로 바꿈.(타이머 끝나고 실행)
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
	
	public int getItemNum(Item_info item) {	//판매자 이름 + 아이템이름 확인
		String itemname = item.getItemname();
		String name = user.getName();
		int size = sellerList.size();
		int match=-1;
		
		for(int i=0;i<size;i++) {
			if(itemList.get(i).getItemname()==itemname) {	//물건 이름 일치
				match=i;
				if(sellerList.get(match).getName()!=name) {	//판매자 이름 불일치.
					match=-1;
				}
			}
			else{	//물건 이름 불일치
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
	
	CountdownTimer(int count){
		this.init_count=count;
	}
	
	public void run() {
		while(true) {	//true가 아니라 조건문으로 바꿔서 물건이 sold out일 경우 멈추도록 할 수 있음.
			try {
				count = init_count;
				for(int i=count; i>=0;i--) {
					System.out.println("Time: "+i+"s");
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


// Item info
class Item_info{
	int value=0;	//진행, 유찰, 낙찰 판단용
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
	
	public void setPrice(int price, Login_info buyer) {	//serialized 필요하다 생각됨. 나머지 코드 완성 후 판단.
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

