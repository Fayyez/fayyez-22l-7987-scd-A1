import models.Bill;
import utils.BillManager;
import utils.DateBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;

class SplitPaneExample extends JFrame {

    public static void main(String[] args) throws FileNotFoundException {
        Bill newbill = new Bill(1, 1, 1, 1, new Date(), 1, 1, 1, 1, DateBuilder.add7Days(new Date()), false, null);
        ArrayList<Bill> bills =new ArrayList<>();
        try {
            BillManager.getListOfBills(bills);
        } catch (Exception e) {
            System.out.println("Error reading bills file");
        }
        for(Bill bill: bills) {
            System.out.println(bill);
        }
    }
}

