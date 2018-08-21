package com.jo.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jo.pojo.Users;
import com.jo.service.UserService;
import com.jo.utils.JSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "用户相关业务操作接口", tags = {"用户业务controller"})
@RequestMapping("/user")
public class UserController extends BasicController{
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "用户上传头像", notes = "上传头像接口")
	@ApiImplicitParam(name = "userId", value = "用户Id", required = true, 
					  dataType = "String", paramType = "query")
	@PostMapping("/uploadFace")
	public JSONResult uplaodFace(String userId, @RequestParam("file") MultipartFile[] file) throws Exception {
		
		if (StringUtils.isBlank(userId)) {
			return JSONResult.errorMsg("用户ID不能为空");
		}
		//文件上传路径
		String location = "E:/WeixinApp/userFile"; 
		//数据库保存路径
		String uploadPathDB = "/" + userId + "/face";
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try {
			if (file != null && file.length > 0) {
				String fileName = file[0].getOriginalFilename();
				if (StringUtils.isNotBlank(fileName)) {
					//文件的最终保存路径
					String finalFacePath = location + uploadPathDB + "/" + fileName;
					//设置数据库保存路径
					uploadPathDB += ("/" + fileName);
					File outFile = new File(finalFacePath);
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						//创建父文件夹
						outFile.getParentFile().mkdirs();
					}
					fileOutputStream = new FileOutputStream(outFile);
					inputStream = file[0].getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
				}
			} else {
				return JSONResult.errorMsg("上传出错!");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			} else {
				return JSONResult.errorMsg("上传出错!");
			}
		}
		Users user = new Users();
		user.setId(userId);
		user.setFaceImage(uploadPathDB);
		userService.updateUserInfo(user);
		return JSONResult.ok(uploadPathDB);
	} 
}
