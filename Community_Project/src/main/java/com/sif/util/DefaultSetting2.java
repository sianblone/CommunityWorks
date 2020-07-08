package com.sif.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class DefaultSetting2 {
	
	public static void main(String[] args) throws IOException {
		BufferedReader keyInput = new BufferedReader(new InputStreamReader(System.in));
		
		FileReader fr = null;
		BufferedReader readFile = null;
		String encryptKey = null;
		
		try {
			fr = new FileReader("./src/main/webapp/WEB-INF/spring/properties/encrypt-key.properties");
			readFile = new BufferedReader(fr);
			encryptKey = readFile.readLine().split("encrypt-key=")[1];
		} catch (Exception e) {
			// 파일이 없는 경우 encryptKey는 null
		}
		
		while(true) {
			// 메뉴 표시
			System.out.println("---------- 기본 설정 -----------");
			System.out.println("1. 암호화 Key 설정");
			System.out.println("2. 암호화 Key 보기");
			System.out.println("3. DB 유저,비밀번호 세팅");
			System.out.println("4. Gmail ID,앱비밀번호 세팅");
			System.out.println("0. 종료");
			System.out.println("--------------------------------");
			System.out.print("메뉴를 선택하세요(숫자 입력) >> ");
			String strMenu = keyInput.readLine();
			
			int intMenu = 0;
			try {
				intMenu = Integer.parseInt(strMenu);
			} catch (Exception e) {
				continue;
			}
			
			if(intMenu == 1) {
				// 파일 저장하기
				String saveFile = "./src/main/webapp/WEB-INF/spring/properties/encrypt-key.properties";
				System.out.print("암호화에 사용할 Key값을 입력하세요 >> ");
				String newEncryptKey = keyInput.readLine();
				
				PrintWriter pw = new PrintWriter(saveFile);
				pw.print(String.format("encrypt-key=%s", newEncryptKey));
				pw.flush();
				pw.close();
				
				encryptKey = newEncryptKey;
				System.out.println("encrypt-key.properties 저장 완료!");
			} else if(intMenu == 2) {
				if(encryptKey == null || encryptKey.isEmpty()) System.out.println("암호화 Key가 없습니다.");
				else System.out.println("암호화 Key : " + encryptKey);
			} else if(intMenu == 3) {
				if(encryptKey == null || encryptKey.isEmpty()) System.out.println("암호화 Key가 없습니다.");
				else encrypt(keyInput, encryptKey, "Mysql");
			} else if(intMenu == 4) {
				if(encryptKey == null || encryptKey.isEmpty()) System.out.println("암호화 Key가 없습니다.");
				else encrypt(keyInput, encryptKey, "Gmail");
			} else if(intMenu == 0) {
				System.out.println("설정을 종료합니다.");
				keyInput.close();
				readFile.close();
				fr.close();
				break;
			}
		}
	}
	
	public static void encrypt(BufferedReader keyInput, String encryptKey, String menuName) throws IOException {
		StandardPBEStringEncryptor pbe = new StandardPBEStringEncryptor();
		pbe.setAlgorithm("PBEWithMD5AndDES");
		pbe.setPassword(encryptKey);
		
		System.out.print(menuName + " Username >> ");
		String username = keyInput.readLine();
		
		System.out.print(menuName + " Password >> ");
		String password = keyInput.readLine();
		
		String encUserName = pbe.encrypt(username);
		String encPassword = pbe.encrypt(password);
		
		String prefix = "";
		if(menuName.equals("Mysql")) prefix = "db";
		else if(menuName.equals("Gmail")) prefix = "gmail";
		
		String saveFile = "./src/main/webapp/WEB-INF/spring/properties/" + prefix + ".connection.properties";
		
		String saveUserName = String.format("%s.username=ENC(%s)", menuName.toLowerCase(), encUserName);
		String savePassword = String.format("%s.password=ENC(%s)", menuName.toLowerCase(), encPassword);
		
		PrintWriter pw = new PrintWriter(saveFile);
		pw.println(saveUserName);
		pw.println(savePassword);
		pw.flush();
		pw.close();
		
		System.out.println(menuName + ".connection.properties 저장 완료!");
	}

}
