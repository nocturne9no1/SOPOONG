package com.sopoong.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sopoong.common.BaseMessage;
import com.sopoong.model.dto.ConfirmIdRequest;
import com.sopoong.model.dto.ConfirmRequest;
import com.sopoong.model.dto.FindIdRequest;
import com.sopoong.model.dto.FindPasswordRequest;
import com.sopoong.model.dto.SignupRequest;
import com.sopoong.model.entity.User;
import com.sopoong.repository.ImageRepository;
import com.sopoong.repository.UserRepository;
import com.sopoong.util.JwtTokenProvider;

@Service
public class AuthService {
	public static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	private EmailService emailService;
	
	@Transactional
	public BaseMessage joinUser(SignupRequest requestUser) {
		Map<String,Object> resultMap = new HashMap<>();
		userRepository.save(User.builder().userId(requestUser.getId()).
				userEmail(requestUser.getEmail())
				.userPassword(passwordEncoder.encode(requestUser.getPassword())).
				userNickname(requestUser.getNickname())// 최초.																									// 설정
				.userIsVisible(1)
				.userAlarm(7)
				.image(imageRepository.findByImageIdx(2).get())
				.build());
		
		resultMap.put("success", "회원가입 성공");
		return new BaseMessage(HttpStatus.OK,resultMap); 
	}
	
	@Transactional
	public BaseMessage login(String id, String password){
		Map<String,Object> resultMap = new HashMap<>();
		Optional<User> member = userRepository.findByUserId(id);
		if(!member.isPresent()) {
			resultMap.put("errors","존재하지 않는 아이디");
			return new BaseMessage(HttpStatus.BAD_REQUEST,resultMap); 
		}else if (!passwordEncoder.matches(password, member.get().getUserPassword())) {
			resultMap.put("errors","비밀번호 틀림");
			return new BaseMessage(HttpStatus.BAD_REQUEST,resultMap); 
		}else if(member.get().getAuthNumber()==null || !member.get().getAuthNumber().contains("AUTH")) {
			resultMap.put("errors", "인증못받은 사용자");
			return new BaseMessage(HttpStatus.BAD_REQUEST,resultMap); 
		}else {
			resultMap.put("success", jwtTokenProvider.createToken(member.get().getUserId()));
		}
		return new BaseMessage(HttpStatus.OK,resultMap); 
	}
	
	@Transactional
	public BaseMessage checkId(String id) {
		Map<String,Object> resultMap = new HashMap<>();
		if(userRepository.findByUserId(id).isPresent()) {
			resultMap.put("errors", "아이디 중복");
		}else {
			resultMap.put("success", "아이디 중복검사 통과");
		}
		
		return new BaseMessage(HttpStatus.OK,resultMap);
	}
	
	@Transactional
	public BaseMessage checkEmail(String email) {
		Map<String,Object> resultMap = new HashMap<>();
		if(userRepository.findByUserEmail(email).isPresent()) {
			resultMap.put("errors", "이메일 중복");
		}else {
			resultMap.put("success", "이메일 중복검사 통과");
		}
		
		return new BaseMessage(HttpStatus.OK,resultMap);
	}
	
	@Transactional
	public BaseMessage checkNickname(String nickName) {
		Map<String,Object> resultMap = new HashMap<>();
		if(userRepository.findByUserNickname(nickName).isPresent()) {
			resultMap.put("errors", "닉네임 중복");
		}else {
			resultMap.put("success", "닉네임 중복검사 통과");
		}
		return new BaseMessage(HttpStatus.OK,resultMap);
	}
	
	public BaseMessage sendEmail(String id) {
		Map<String,Object> resultMap = new HashMap<>();
		Optional<User> userOpt = userRepository.findByUserId(id);
		if(userOpt.isPresent()) {
			if(userOpt.get().getAuthNumber() == null || !userOpt.get().getAuthNumber().contains("AUTH")) {
				userOpt.get().setAuthNumber(generAuthKey());
				emailService.sendMail(userOpt.get());
				userRepository.save(userOpt.get());
				resultMap.put("success", "이메일 보내기 완료");
				return new BaseMessage(HttpStatus.OK,resultMap);
			}else {
				resultMap.put("errors", "이미 인증된 아이디");
			}
		}else {
			resultMap.put("errors", "존재하지 않는 아이디"); 
		}
		return new BaseMessage(HttpStatus.OK,resultMap);
	}
	//인증키 만들기 8글자
	public String generAuthKey() {
		StringBuffer temp = new StringBuffer();
		Random rnd = new Random();
		for (int i = 0; i < 8; i++) {
		    int rIndex = rnd.nextInt(3);
		    switch (rIndex) {
		    case 0:
		        // a-z
		        temp.append((char) ((int) (rnd.nextInt(26)) + 97));
		        break;
		    case 1:
		        // A-Z
		        temp.append((char) ((int) (rnd.nextInt(26)) + 65));
		        break;
		    case 2:
		        // 0-9
		        temp.append((rnd.nextInt(10)));
		        break;
		    }
		}
		
		return temp.toString();
	}

	public BaseMessage confirm(ConfirmRequest confirmRequest) {
		Map<String,Object> resultMap = new HashMap<>();
		Optional<User> userOpt = userRepository.findByUserId(confirmRequest.getId());
		if(userOpt.isPresent()) {
			if(userOpt.get().getAuthNumber().contains("AUTH")) {
				resultMap.put("errors", "이미 인증된 사용자");
			}else if(userOpt.get().getAuthNumber().equals(confirmRequest.getAuthNumber())){
				userOpt.get().setAuthNumber("AUTH");
				userRepository.save(userOpt.get());
				resultMap.put("success", "인증 성공");
				return new BaseMessage(HttpStatus.OK,resultMap);
			}else {
				resultMap.put("errors", "인증번호 실패");
			}
		}else {
			resultMap.put("errors", "아이디 조회 실패");
		}
		return new BaseMessage(HttpStatus.OK,resultMap);
	}
	
	public BaseMessage sendFindIdMail(FindIdRequest findIdRequest) {
		Map<String,Object> resultMap = new HashMap<>();
		Optional<User> userOpt=  userRepository.findByUserEmail(findIdRequest.getEmail());
		if(userOpt.isPresent()) {
			if(userOpt.get().getAuthNumber()==null || !userOpt.get().getAuthNumber().contains("AUTH")) {
				resultMap.put("errors", "인증받지 않은 사용자");
			}else if(userOpt.get().getAuthNumber().contains("AUTH")) {
				//인증받은 사용자
				userOpt.get().setAuthNumber("AUTH&"+generAuthKey());
				userRepository.save(userOpt.get());
				emailService.sendFindIdMail(userOpt.get());
				resultMap.put("success", "메일보내기 성공");
				return new BaseMessage(HttpStatus.OK,resultMap);
			}
		}else {
			resultMap.put("errors", "이메일 조회 실패");
		}
		return new BaseMessage(HttpStatus.OK,resultMap);
	}

	public BaseMessage confirmFindId(ConfirmIdRequest confirmIdRequest) {
		Map<String,Object> resultMap = new HashMap<>();
		Optional<User> userOpt=  userRepository.findByUserEmail(confirmIdRequest.getEmail());
		if(userOpt.isPresent()) {
			if(userOpt.get().getAuthNumber()==null || !userOpt.get().getAuthNumber().contains("AUTH")) {
				resultMap.put("errors", "인증받지 않은 사용자");
			}else if(userOpt.get().getAuthNumber().contains("AUTH")) {
				//인증받은 사용자
				if(userOpt.get().getAuthNumber().substring(5).equals(confirmIdRequest.getAuthNumber())) {
					userOpt.get().setAuthNumber("AUTH");
					userRepository.save(userOpt.get());
					resultMap.put("success", userOpt.get().getUserId());
					return new BaseMessage(HttpStatus.OK,resultMap);
				}else {
					resultMap.put("errors", "인증번호 실패");
				}
			}
		}else {
			resultMap.put("errors", "이메일 조회 실패");
			
		}
		return new BaseMessage(HttpStatus.OK,resultMap);
	}
	
	public BaseMessage changePassword(FindPasswordRequest findPasswordRequest) {
		Map<String,Object> resultMap = new HashMap<>();
		Optional<User> userOpt=  userRepository.findByUserId(findPasswordRequest.getId());
		if(userOpt.isPresent()) {
			userOpt.get().setUserPassword(passwordEncoder.encode(findPasswordRequest.getPassword()));
			userRepository.save(userOpt.get());
			resultMap.put("success", "비밀번호 변경 완료");
		}else {
			resultMap.put("errors", "아이디 조회 실패");
		}
		return new BaseMessage(HttpStatus.OK,resultMap);
	}
}
