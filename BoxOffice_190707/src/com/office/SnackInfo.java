package com.office;

public class SnackInfo {
   private String name = null;
   private int price = 0;
   
   public SnackInfo(String name,int price) {
   
	   this.name = name;
	   this.price = price;
      
   }
   
   public String getSnackName() {return name;}
   public int getSnackPrice() {return price;}
}