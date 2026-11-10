package CatanSpring.model;

import CatanSpring.model.*;
import CatanSpring.model.enums.*;
import CatanSpring.model.gestioFitxers.ImportarFitxer;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;
import java.util.*;


public class Main {
	
	private static Scanner scanner=new Scanner(System.in);;

	public static void main(String args[]) {

	}

	//============================
	//          Scanners
	//============================
	public static int getInt() {
		int i=0;
		while(true) {
			try {
				i=Integer.parseInt(scanner.nextLine());
				return i;
			}catch(NumberFormatException e){
				System.out.print("Valor invàlid, torna a introduir un nombre: ");
			}
		}
	}

	public static int getInt(int min,int max) {
		int i=0;
		while(true) {
			try {
				i=Integer.parseInt(scanner.nextLine());
				if(i<min || i>max) {
					System.out.print("Valor invàlid, torna a introduir un nombre: ");
				}else {
					return i;
				}
			}catch(NumberFormatException e){
				System.out.print("Valor invàlid, torna a introduir un nombre: ");
			}
		}
	}
	
	public static double getDouble() {
		double d=0;
		while(true) {
			try {
				d=Double.parseDouble(scanner.nextLine());
				return d;
			}catch(NumberFormatException e){
				System.out.print("Valor invàlid, torna a introduir un nombre: ");
			}
		}
	}

	public static double getDouble(double min,double max) {
		double i=0;
		while(true) {
			try {
				i=Double.parseDouble(scanner.nextLine());
				if(i<min || i>max) {
					System.out.print("Valor invàlid, torna a introduir un nombre: ");
				}else {
					return i;
				}
			}catch(NumberFormatException e){
				System.out.print("Valor invàlid, torna a introduir un nombre: ");
			}
		}
	}

	public static boolean getBoolean(){
		String inpt;
		while(true){
			inpt=scanner.nextLine().toUpperCase();
			if(inpt.equals("SI") || inpt.equals("SÍ") || inpt.equals("S") || inpt.equals("YES") ||inpt.equals("Y") || inpt.equals("TRUE")){
				return true;
			} else if (inpt.equals("NO") || inpt.equals("N") || inpt.equals("FALSE")){
				return false;
			}else{
				System.out.print("Valor invàlid, torna a introduir una opció (S/N): ");
			}
		}

	}

	public static String getString() {
		String s="";
		while(true) {
			s=scanner.nextLine();
			if(s.equals("")){
				System.out.print("Valor invàlid, torna a intentar: ");
			}else{
				return s;
			}
		}
	}
}