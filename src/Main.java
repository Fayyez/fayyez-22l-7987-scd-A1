import models.*;
import utils.CustomerManager;
import utils.Login;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            Employee c = Login.LoginEmployee();
            System.out.println(c);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}