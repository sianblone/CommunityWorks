package com.sif.community.service.board;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

/*
 * summernote에서 dragAnddrop으로 이미지파일을 업로드 하면
 * 일단 서버에 파일을 업로드 하고
 * 파일 이름을 다시 내려보내서 
 * 		base64로 encoding된 파일 정보를
 * 		img src="저장된경로/파일이름" 형식으로 변경한다.
 * 
 */

@RequiredArgsConstructor
@Service
public class FileService {
	
	// servlet-context.xml에 설정된 파일 저장경로 정보를
	// 가져와서 사용하기
	
	private final String filePath;
	
	/*
	 * 브라우저에서 파일이 전송되어 오면
	 * 원래 파일이름을 UUID 부착된 파일이름을 변경하고
	 * 변경된 이름으로 서버의 filePath에 저장하고
	 * 변경된 파일이름을 return
	 */
	public String fileUp(MultipartFile upFile) {
		
//		1. 폴더 생성
		File dir = new File(filePath);
		// upload할 filePath가 있는지 확인 후 없으면 폴더 생성
		if(!dir.exists()) {
			//dir.mkdir();// filePath = c:/bizwork/upload를 만들때 upload 폴더만 만들기
			dir.mkdirs();//  filePath = c:/bizwork/upload를 만들때 앞까지 전부 만들기(만약 앞이 전부 있다면 생략하고 upload만 만들기)
		}
		
//		2. 같은 파일명으로 공격 등을 방지하기 위해 UUID를 붙인 새로운 파일명 만들기
		// MultipartFile에서 파일명 추출(그림.jpg)
		String originalFileName = upFile.getOriginalFilename();
		
		// UUID가 부착된 새로운 파일명 생성
		String uuid = UUID.randomUUID().toString();
		String saveName = uuid + "_" + originalFileName; // (UUID_그림.jpg)
		
		// filePath와 새로운 파일명을 결합하여 File 객체 생성(serverFile)
		File serverFile = new File(filePath, saveName);
		
		try {
			// File.transferTo() : 실제 파일을 복사하는 메소드
			// file 객체의 transferTo() 메소드를 사용하여 컨트롤러에서 받은 upFile을 serverFile로 복사하기
			upFile.transferTo(serverFile);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// try-catch에 의해 파일복사 실패 시
			return "fail";
		}
		
		return saveName;
	}
	
	
	
}
