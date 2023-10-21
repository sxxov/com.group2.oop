package com.group2.oop.form;

import java.io.*;
import com.group2.oop.service.Service;
import com.group2.oop.home.HomeService;
import com.group2.oop.dependency.D;
import com.group2.oop.service.Engine;
import java.util.Scanner;

public class submitImage implements Service  {

  private final Scanner scanner = D.get(Scanner.class);

  private final Service next;
  
  public submitImage()  {
       this.next = new HomeService();
  }

  public submitImage(Service next) {
    this.next = next;
  }

  storeImage store = new storeImage();

  @Override
  public void init(Engine engine) {
     System.out.println("Enter the image filepath: ");
    
    for (;;) {
      System.out.print("> ");

      String filepath = scanner.nextLine();

      try {
        store.storeSubmit(filepath);
      } catch (IOException | ClassNotFoundException e) {
        System.out.println("Error storing the image.");
        e.printStackTrace();
      }
      
      break;
  }

    engine.swap(next);
  }

  @Override
  public void exit() {}
  
}
