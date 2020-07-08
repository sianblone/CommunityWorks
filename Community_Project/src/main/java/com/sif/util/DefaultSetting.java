package com.sif.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class DefaultSetting {
	
	public static void main(String[] args) throws IOException {
		BufferedReader keyInput = new BufferedReader(new InputStreamReader(System.in));
		
		StandardPBEStringEncryptor pbe = new StandardPBEStringEncryptor();
		pbe.setAlgorithm("PBEWithMD5AndDES");
		String encryptKey = System.getenv("ENV_PASS");
		pbe.setPassword(encryptKey);
		
		while(true) {
			// 메뉴 표시
			System.out.println("---------- 기본 설정 -----------");
			System.out.println("1. 암호화 Key 보기");
			System.out.println("2. DB 유저,비밀번호 세팅");
			System.out.println("3. Gmail ID,앱비밀번호 세팅");
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
				if(encryptKey == null || encryptKey.isEmpty()) System.out.println("암호화 Key가 없습니다.");
				else System.out.println("암호화 Key : " + encryptKey);
			} else if(intMenu == 2) {
				if(encryptKey == null || encryptKey.isEmpty()) System.out.println("암호화 Key가 없습니다.");
				else encrypt(keyInput, pbe, "Mysql");
			} else if(intMenu == 3) {
				if(encryptKey == null || encryptKey.isEmpty()) System.out.println("암호화 Key가 없습니다.");
				else encrypt(keyInput, pbe, "Gmail");
			} else if(intMenu == 0) {
				System.out.println("설정을 종료합니다.");
				keyInput.close();
				break;
			}
		}
	}
	
	public static void encrypt(BufferedReader keyInput, StandardPBEStringEncryptor pbe, String menuName) throws IOException {
		String schema = null;
		
		if(menuName.equals("Mysql")) {
			System.out.print(menuName + " Schema(Database) >> ");
			schema = keyInput.readLine();
		}
		
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
		
		if(schema != null)  schema = String.format("%s.schema=%s", menuName.toLowerCase(), schema);
		String saveUserName = String.format("%s.username=ENC(%s)", menuName.toLowerCase(), encUserName);
		String savePassword = String.format("%s.password=ENC(%s)", menuName.toLowerCase(), encPassword);
		
		PrintWriter pw = new PrintWriter(saveFile);
		if(schema != null) pw.println(schema);
		pw.println(saveUserName);
		pw.println(savePassword);
		pw.flush();
		pw.close();
		
		System.out.println(menuName + ".connection.properties 저장 완료!");
	}

}
