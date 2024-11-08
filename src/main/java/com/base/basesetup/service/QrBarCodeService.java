package com.base.basesetup.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.controller.CustomerAttachmentType;
import com.base.basesetup.dto.QrBarCodeDTO;
import com.base.basesetup.dto.SingleQrBarCodeDTO;
import com.base.basesetup.entity.QrBarCodeVO;
import com.base.basesetup.entity.SingleQrBarCodeVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface QrBarCodeService {

	Map<String, Object> createUpdateQrBarCode(QrBarCodeDTO qrBarCodeDTO) throws ApplicationException;

	List<QrBarCodeVO> getAllQrBarCode();

	QrBarCodeVO getQrBarCodeById(Long id);

	int getTotalRows();

	int getSuccessfulUploads();

	void ExcelUploadForQrBarCode(MultipartFile[] files, CustomerAttachmentType type,String createdBy) throws ApplicationException;

	List<Map<String, Object>> getFillGridFromQrBarExcelUpload(String entryNo);

	List<SingleQrBarCodeVO> getAllSingleQrBarCode();

	SingleQrBarCodeVO getSingleQrBarCodeById(Long id);

	Map<String, Object> createUpdateSingleQrBarCode(SingleQrBarCodeDTO singleQrBarCodeDTO) throws ApplicationException;

	String getQrBarCodeDocId();

	
	

}
