package com.group2.oop;

import com.group2.oop.account.*;

class Main {
  public static void main(String[] args) {
    System.out.println("Hello world!");

    var user = new User("username", "firstName", "lastName");

    System.out.println(user.username());
  }
}
