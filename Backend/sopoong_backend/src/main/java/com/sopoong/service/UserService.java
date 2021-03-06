package com.sopoong.service;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sopoong.common.BaseMessage;
import com.sopoong.model.dto.DeleteUserRequest;
import com.sopoong.model.dto.UserListDto;
import com.sopoong.model.dto.changeAlarmRequest;
import com.sopoong.model.dto.changePasswordRequest;
import com.sopoong.model.dto.changeProfileRequest;
import com.sopoong.model.dto.changeVisibleRequest;
import com.sopoong.model.dto.getProfileRequest;
import com.sopoong.model.entity.Image;
import com.sopoong.model.entity.User;
import com.sopoong.repository.ImageRepository;
import com.sopoong.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private ImageService imageService;
	@Autowired
	private ImageRepository imageRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public BaseMessage getProfile(String id) {

		Map<String, Object> resultMap = new HashMap<>();
		Optional<User> user = userRepo.findByUserId(id);
		getProfileRequest getUser = new getProfileRequest();

		if (user.isPresent()) {
			getUser.setUserId(user.get().getUserId());
			getUser.setUserEmail(user.get().getUserEmail());
			getUser.setUserNickname(user.get().getUserNickname());
			getUser.setUserIsVisible(user.get().getUserIsVisible());
			getUser.setUserAlarm(user.get().getUserAlarm());
			getUser.setUserComment(user.get().getUserComment());
			getUser.setImageOriginTitle(user.get().getImage().getImageOriginTitle());
			getUser.setFollowings(user.get().getRelationFollowing().size());
			getUser.setFollowers(user.get().getRelationFollowed().size());
			resultMap.put("success", getUser);
			return new BaseMessage(HttpStatus.OK, resultMap);
		} else {
			resultMap.put("errors", "???????????? ?????? ?????????");
			return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
		}

	}

	@Transactional
	public BaseMessage changeProfile(changeProfileRequest request)
			throws IllegalStateException, NoSuchAlgorithmException, IOException {
		Map<String, Object> resultMap = new HashMap<>();
		Optional<User> updateUser = userRepo.findByUserId(request.getUserId());

		long imageIdx = updateUser.get().getImage().getImageIdx();

		if (request.getImage() != null) { // ?????? ????????? ?????? ??????, ?????? ?????? pass
			if (imageIdx != 2) { // ?????????????????? ?????? ????????? ?????? ??????
				// ?????? ??????
				Optional<Image> image = imageRepo.findByImageIdx(imageIdx);
				String path = image.get().getImagePath();
				File file = new File(path);

				if (file.delete()) { // ?????? ????????? ???????????? true, ???????????? false
//					System.out.println("????????? ?????????????????????");
				} else {
//					System.out.println("?????? ????????? ?????????????????????");
				}
				
				// DB?????? ??????
				imageService.deleteImage(imageIdx);
			}

			BaseMessage bm = imageService.saveProfile(request.getImage(), request.getUserId()); // ?????? ?????? ??????
			Image im = (Image) bm.getData();
			imageIdx = im.getImageIdx();
		}

		if (updateUser.isPresent()) {
			updateUser.get().setImage(imageRepo.findByImageIdx(imageIdx).get());
			updateUser.get().setUserNickname(request.getUserNickname());
			updateUser.get().setUserComment(request.getUserComment());
			userRepo.save(updateUser.get());

			resultMap.put("success", "????????? ?????? ??????");
			return new BaseMessage(HttpStatus.OK, resultMap);
		} else {
			resultMap.put("errors", "????????? ?????? ?????? (???????????? ?????? ?????????)");
			return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
		}
	}

	public BaseMessage changePassword(changePasswordRequest request) {

		Map<String, Object> resultMap = new HashMap<>();
		Optional<User> updateUser = userRepo.findByUserId(request.getUserId());

		if (updateUser.isPresent()) {
			if (passwordEncoder.matches(request.getUserPassword(), updateUser.get().getUserPassword())) {
				updateUser.get().setUserPassword(passwordEncoder.encode(request.getChangedPassword()));
				userRepo.save(updateUser.get());

				resultMap.put("success", "???????????? ?????? ??????");
				return new BaseMessage(HttpStatus.OK, resultMap);
			} else {
				resultMap.put("errors", "???????????? ???????????? ??????");
			}
		} else {
			resultMap.put("errors", "???????????? ?????? ?????? (???????????? ?????? ?????????)");
		}
		return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);

	}

	public BaseMessage changeAlarm(changeAlarmRequest request) {

		Map<String, Object> resultMap = new HashMap<>();
		Optional<User> updateUser = userRepo.findByUserId(request.getUserId());

		if (updateUser.isPresent()) {
			updateUser.get().setUserAlarm(request.getUserAlarm());
			userRepo.save(updateUser.get());

			resultMap.put("success", "?????? ?????? ?????? ??????");
			return new BaseMessage(HttpStatus.OK, resultMap);
		} else {
			resultMap.put("errors", "?????? ?????? ?????? ?????? (???????????? ?????? ?????????)");
			return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
		}

	}

	public BaseMessage changeVisible(changeVisibleRequest request) {

		Map<String, Object> resultMap = new HashMap<>();
		Optional<User> updateUser = userRepo.findByUserId(request.getUserId());

		if (updateUser.isPresent()) {
			updateUser.get().setUserIsVisible(request.getUserIsVisible());
			userRepo.save(updateUser.get());

			resultMap.put("success", "?????? ?????? ?????? ?????? ??????");
			return new BaseMessage(HttpStatus.OK, resultMap);
		} else {
			resultMap.put("errors", "?????? ?????? ?????? ?????? ?????? (???????????? ?????? ?????????)");
			return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
		}

	}

	public BaseMessage deleteUser(DeleteUserRequest request) {

		Map<String, Object> resultMap = new HashMap<>();
		Optional<User> deleteUser = userRepo.findByUserId(request.getId());
		deleteUser.get().toString();
		if (deleteUser.isPresent()) {
			if (passwordEncoder.matches(request.getPassword(), deleteUser.get().getUserPassword())) {
//				userRepo.deleteById(deleteUser.get().getUserIdx())
				userRepo.delete(deleteUser.get());
				
//				userRepo.deleteById(deleteUser.get().getUserIdx());
				resultMap.put("success", "?????? ?????? ??????");
				return new BaseMessage(HttpStatus.OK, resultMap);
			}
			resultMap.put("errors", "?????? ?????? ?????? (???????????? ???????????? ??????)");
		} else {
			resultMap.put("errors", "?????? ?????? ?????? (???????????? ?????? ?????????)");
		}
		return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);

	}

	@Transactional
	public BaseMessage updateImage(int imageIdx, String userId) {
		Map<String, Object> resultMap = new HashMap<>();
		Optional<User> updateUser = userRepo.findByUserId(userId);

		if (updateUser.isPresent()) {
			updateUser.get().setImage(imageRepo.findByImageIdx(imageIdx).get());
			userRepo.save(updateUser.get());

			Image im = Image.builder().imageIdx(2).build();

			resultMap.put("success", "????????? ?????????????????? ?????? ??????");
			return new BaseMessage(HttpStatus.OK, im);
		} else {
			resultMap.put("errors", "????????? ?????????????????? ?????? ?????? (???????????? ?????? ?????????)");
			return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
		}
	}

	public BaseMessage getUserList(String id, Pageable pageable) {
		Map<String, Object> resultMap = new HashMap<>();
		Page<User> users = userRepo.findByUserIdStartingWith(id,pageable);
		List<UserListDto> userList = new ArrayList<>();
		for(int i=0;i<users.getContent().size();i++) {
			UserListDto userListDto = UserListDto.builder().userId(users.getContent().get(i).getUserId())
										.userNickname(users.getContent().get(i).getUserNickname()).build();
			userList.add(userListDto);
		}
		resultMap.put("success", userList);
		return new BaseMessage(HttpStatus.BAD_REQUEST, resultMap);
	}
}
