package com.terra.pp.decorator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.LineNumberReader;

public class Main {

	public static void main(String[] args) throws Exception {
		
	
		
		LineNumberReader fis = new LineNumberReader(
				new BufferedReader(
				new FileReader("alunos.txt"),1024));
		
		while (fis.ready()){
			System.out.println(fis.readLine());
		}
		
		fis.close();
	}
	
	
}
