package com.sif.community.service.board;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
	
	// servlet-context.xml에 설정된 파일 저장경로 정보 가져와서 사용하기
	private final String filePath;
	
	/*
	 * 브라우저에서 파일이 전송되어 오면
	 * 원래 파일이름을 UUID 부착된 파일이름을 변경하고
	 * 변경된 이름으로 서버의 filePath에 저장하고
	 * 변경된 파일이름을 return
	 */
	
	public String fileUp(MultipartFile uploadFile) {
		
		// 1. File 클래스와 업로드 할 filePath 경로를 이용하여 폴더가 이미 생성되어 있는지 확인
		File dir = new File(filePath);
		if(!dir.exists()) {
			// 1-1. 폴더가 없으면 폴더 생성
			//dir.mkdir();// filePath가 "c:/sif/files/" 라면 files 폴더 생성. 상위 디렉토리가 존재하지 않으면 생성 불가
			dir.mkdirs();//	 filePath가 "c:/sif/files/" 라면 files 폴더 생성. 상위 디렉토리가 존재하지 않으면 상위 디렉토리까지 생성
		}
		
		// 2. 같은 파일명으로 공격 등을 방지하기 위해 UUID를 붙인 새로운 파일명 만들기
		// 컨트롤러에서 받은 MultipartFile에서 파일명만 추출(Ex. 파일명.jpg)
		String originalFileName = uploadFile.getOriginalFilename();
		
		// 3. UUID를 붙인 새로운 파일명 생성
		String uuid = UUID.randomUUID().toString();
		String saveFileName = uuid + "_" + originalFileName; // UUID_파일명.jpg
		
		// 4. filePath 경로와 UUID를 붙인 파일명을 결합하여 File 객체 생성(serverFile)
		// 현재 serverFile은 경로와 파일명만 존재한다
		File serverFile = new File(filePath, saveFileName);
		
		try {
			// 5. 컨트롤러에서 받은 uploadFile을 serverFile에 저장하기
			// File.transferTo() : 파일을 지정한 경로+파일명에 저장하는 메소드
			uploadFile.transferTo(serverFile);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			// 5-1. try-catch에 의해 파일복사 실패 시 "FAIL" return
			return "FAIL";
		}
		
		// 5-2. 파일 저장 성공 시 저장한 파일명 return
		return saveFileName;
	}
	
	
	
}
