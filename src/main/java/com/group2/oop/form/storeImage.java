package com.group2.oop.form;

import java.util.ArrayList;
import java.io.*;

public class storeImage implements java.io.Serializable {

    public ArrayList<String> submittedImage = new ArrayList<String>(); 
    public ArrayList<String> approvedImage = new ArrayList<String>();

    public void storeSubmit(String filepath) throws IOException, ClassNotFoundException {
        submittedImage.add(filepath);
        try (FileOutputStream fileOut = new FileOutputStream("submit.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(submittedImage);
        }
        System.out.println("You have successfully submitted: " + filepath);
    }

    public void storeApproved(int input) throws IOException, ClassNotFoundException {
        approvedImage.add(submittedImage.get(input));
        submittedImage.remove(submittedImage.get(input));
        try (FileOutputStream fileOut = new FileOutputStream("approve.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(approvedImage);
        }
        try (FileOutputStream fileOut2 = new FileOutputStream("submit.ser");
             ObjectOutputStream out2 = new ObjectOutputStream(fileOut2)) {
            out2.writeObject(submittedImage);
        }
        System.out.println("Image is approved!");
    }

    public void showSubmit() throws IOException, ClassNotFoundException {
        ArrayList<String> read = submittedImage;
        try (FileInputStream fileIn = new FileInputStream("submit.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
             read = (ArrayList) in.readObject();
        }
        for (int i = 0; i < submittedImage.size(); i++) {
            System.out.println(i + " " + submittedImage.get(i));
        }
        if (submittedImage.isEmpty()) {
            System.out.println("No image has been submitted yet");
        }
    }

    public void showApproved() throws IOException, ClassNotFoundException {
        ArrayList<String> read = store.approvedImage;
        try (FileInputStream fileIn = new FileInputStream("approve.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
             read = (ArrayList) in.readObject();
        }
        for (int i = 0; i < approvedImage.size(); i++) {
            System.out.println(i + " " + approvedImage.get(i));
        }
        if (approvedImage.isEmpty()) {
            System.out.println("No image has been approved yet");
        }
    }
}
