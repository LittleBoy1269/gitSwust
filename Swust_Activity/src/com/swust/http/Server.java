package com.swust.http;


import java.io.File;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;



public class Server {
	private static final String AK = "ZaAuXPi-keVbqEqSTxa1V2wRdQJT4hviCUFhiaiy";
	private static final String SK = "iRFoSdvr22fO_xFwPlSMaJbg5ulS2Cz6soyvrGOc";
	private static UploadManager uploadManager = new UploadManager();

	public boolean upLoadFile(String bucket, File file, String FileName) {
		boolean isSuccess = true;
		Auth auth = Auth.create(AK, SK);
		String token = auth.uploadToken(bucket);
		try {
			uploadManager.put(file, FileName, token);
		} catch (QiniuException e) {
			isSuccess = false;
		}
		return isSuccess;
	}
}