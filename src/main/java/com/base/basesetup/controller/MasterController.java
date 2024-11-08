package com.base.basesetup.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.common.CommonConstant;
import com.base.basesetup.common.UserConstants;
import com.base.basesetup.dto.ResponseDTO;
import com.base.basesetup.dto.TaxInvoiceDTO;
import com.base.basesetup.entity.TaxInvoiceVO;
import com.base.basesetup.service.MasterService;


@CrossOrigin
@RestController
@RequestMapping("api/master")
public class MasterController extends BaseController {
	public static final Logger LOGGER = LoggerFactory.getLogger(MasterController.class);

	@Autowired
	MasterService masterService;
	
	// Tax Invoice

		@PutMapping("/createUpdateTaxInvoice")
		public ResponseEntity<ResponseDTO> createUpdateTaxInvocie(@RequestBody TaxInvoiceDTO taxInvoiceDTO) {
			String methodName = "createUpdateTaxInvocie()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			try {
				Map<String, Object> taxInvoiceVO = masterService.createUpdateTaxInvoice(taxInvoiceDTO);
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, taxInvoiceVO.get("message"));
				responseObjectsMap.put("taxInvoiceVO", taxInvoiceVO.get("taxInvoiceVO"));
				responseDTO = createServiceResponse(responseObjectsMap);
			} catch (Exception e) {
				String errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
				responseDTO = createServiceResponseError(responseObjectsMap, errorMsg, errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@GetMapping("/getAllTaxInvoiceByOrgId")
		public ResponseEntity<ResponseDTO> getAllTaxInvoiceByOrgId(@RequestParam(required = true) Long orgId) {
			String methodName = "getAllTaxInvoiceByOrgId()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<TaxInvoiceVO> taxInvoiceVO = new ArrayList<>();
			try {
				taxInvoiceVO = masterService.getAllTaxInvoice(orgId);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "Tax Invoice Information get successfully");
				responseObjectsMap.put("taxInvoiceVO", taxInvoiceVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "Tax Invoice Information receive failed",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@GetMapping("/getTaxInvoiceById")
		public ResponseEntity<ResponseDTO> getTaxInvoiceById(@RequestParam(required = true) Long id) {
			String methodName = "getTaxInvoiceById()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			TaxInvoiceVO taxInvoiceVO = new TaxInvoiceVO();
			try {
				taxInvoiceVO = masterService.getTaxInvoiceById(id);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TaxInvoice Information Get Successfully");
				responseObjectsMap.put("taxInvoiceVO", taxInvoiceVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "TaxInvoice Information Received Failed",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}

		@GetMapping("/getAllTaxInvoice")
		public ResponseEntity<ResponseDTO> getAllTaxInvoice( ) {
			String methodName = "getAllTaxInvoice()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			List<TaxInvoiceVO> taxInvoiceVO = new ArrayList<TaxInvoiceVO>();
			try {
				taxInvoiceVO = masterService.getAllTaxInvoice( );
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error(UserConstants.ERROR_MSG_METHOD_NAME, methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TaxInvoice Information Get All Successfully");
				responseObjectsMap.put("taxInvoiceVO", taxInvoiceVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "TaxInvoice Information Received All Failed",
						errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}	

		
		@PostMapping("/uploadImageForTaxInvoice")
		public ResponseEntity<ResponseDTO> uploadImageForTaxInvoice(@RequestParam("file") MultipartFile file,
				@RequestParam Long id) {
			String methodName = "uploadImageForTaxInvoice()";
			LOGGER.debug(CommonConstant.STARTING_METHOD, methodName);
			String errorMsg = null;
			Map<String, Object> responseObjectsMap = new HashMap<>();
			ResponseDTO responseDTO = null;
			TaxInvoiceVO taxInvoiceVO = null;
			try {
				taxInvoiceVO = masterService.uploadImageForTaxInvoice(file, id);
			} catch (Exception e) {
				errorMsg = e.getMessage();
				LOGGER.error("Unable To Upload TaxInvoice Image", methodName, errorMsg);
			}
			if (StringUtils.isBlank(errorMsg)) {
				responseObjectsMap.put(CommonConstant.STRING_MESSAGE, "TaxInvoice Image Successfully Upload");
				responseObjectsMap.put("taxInvoiceVO", taxInvoiceVO);
				responseDTO = createServiceResponse(responseObjectsMap);
			} else {
				responseDTO = createServiceResponseError(responseObjectsMap, "TaxInvoice Image Upload Failed", errorMsg);
			}
			LOGGER.debug(CommonConstant.ENDING_METHOD, methodName);
			return ResponseEntity.ok().body(responseDTO);
		}
		

	    @GetMapping("/getTaxInvoiceImageById/{id}")
	    public ResponseEntity<byte[]> getTaxInvoiceImageById(@PathVariable Long id) {
	        String methodName = "getTaxInvoiceImageById()";
	        LOGGER.debug("Starting method: {}", methodName);

	        byte[] image;
	        try {
	            image = masterService.getTaxInvoiceImageById(id);
	        } catch (RuntimeException e) {
	            LOGGER.error("Unable to retrieve TaxInvoice image for id: {}", id, e);
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        } catch (Exception e) {
	            LOGGER.error("Unexpected error while retrieving image for id: {}", id, e);
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }

	        if (image == null || image.length == 0) {
	            LOGGER.error("TaxInvoice Image not found for id: {}", id);
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }

	        return ResponseEntity.ok()
	                .contentType(MediaType.IMAGE_JPEG)
	                .body(image);
	    }
}
