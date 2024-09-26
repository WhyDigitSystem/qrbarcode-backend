package com.base.basesetup.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils;
//import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.controller.CustomerAttachmentType;
import com.base.basesetup.dto.QrBarCodeDTO;
import com.base.basesetup.dto.QrBarCodeDetailsDTO;
import com.base.basesetup.entity.QrBarCodeDetailsVO;
import com.base.basesetup.entity.QrBarCodeVO;
import com.base.basesetup.entity.QrBarExcelUploadVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.QrBarCodeDetailsRepo;
import com.base.basesetup.repo.QrBarCodeRepo;
import com.base.basesetup.repo.QrBarExcelUploadRepo;


@Service
public class QrBarCodeServiceImpl implements QrBarCodeService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(QrBarCodeServiceImpl.class);


	@Autowired
	QrBarCodeRepo qrBarCodeRepo;
	
	@Autowired
	QrBarCodeDetailsRepo qrBarCodeDetailsRepo;

	@Autowired
	QrBarExcelUploadRepo qrBarExcelUploadRepo;
	
	@Override
	public Map<String, Object> createUpdateQrBarCode(QrBarCodeDTO qrBarCodeDTO) throws ApplicationException {
		QrBarCodeVO qrBarCodeVO = new QrBarCodeVO();
		String message;

		if (ObjectUtils.isNotEmpty(qrBarCodeDTO.getId())) {
			qrBarCodeVO = qrBarCodeRepo.findById(qrBarCodeDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invalid QrBarCode details"));
			qrBarCodeVO.setUpdatedBy(qrBarCodeDTO.getCreatedBy());
			createUpdateQrBarCodeVOByQrBarCodeDTO(qrBarCodeDTO, qrBarCodeVO);

			message = "QrBarCode updated Successfully";

		} else {

			createUpdateQrBarCodeVOByQrBarCodeDTO(qrBarCodeDTO, qrBarCodeVO);
			qrBarCodeVO.setUpdatedBy(qrBarCodeDTO.getCreatedBy());
			qrBarCodeVO.setCreatedBy(qrBarCodeDTO.getCreatedBy());
			message = "QrBarCode created Successfully";

		}

		QrBarCodeVO savedQrBarCodeVO = qrBarCodeRepo.save(qrBarCodeVO);

		Map<String, Object> response = new HashMap<>();
		response.put("qrBarCodeVO", qrBarCodeVO);
		response.put("message", message);
		return response;

	}

	private void createUpdateQrBarCodeVOByQrBarCodeDTO(@Valid QrBarCodeDTO qrBarCodeDTO, QrBarCodeVO qrBarCodeVO) {

		qrBarCodeVO.setDocDate(qrBarCodeDTO.getDocDate());
		qrBarCodeVO.setDocId(qrBarCodeDTO.getDocId());
		qrBarCodeVO.setDocDate(qrBarCodeDTO.getDocDate());
		qrBarCodeVO.setEntryNo(qrBarCodeDTO.getEntryNo());


		if (ObjectUtils.isNotEmpty(qrBarCodeVO.getId())) {
			List<QrBarCodeDetailsVO> qrBarCodeDetailsVO1 = qrBarCodeDetailsRepo
					.findByQrBarCodeVO(qrBarCodeVO);
			qrBarCodeDetailsRepo.deleteAll(qrBarCodeDetailsVO1);
		}

		List<QrBarCodeDetailsVO> qrBarCodeDetailsVOs = new ArrayList<>();
		for (QrBarCodeDetailsDTO qrBarCodeDetailsDTO : qrBarCodeDTO.getQrBarCodeDetailsDTO()) {

			QrBarCodeDetailsVO qrBarCodeDetailsVO = new QrBarCodeDetailsVO();
			qrBarCodeDetailsVO.setPartNo(qrBarCodeDetailsDTO.getPartNo());
			qrBarCodeDetailsVO.setPartDescription(qrBarCodeDetailsDTO.getPartDescription());
			qrBarCodeDetailsVO.setCount(qrBarCodeDetailsDTO.getCount());
			qrBarCodeDetailsVO.setBarCodeValue(qrBarCodeDetailsDTO.getBarCodeValue());
			qrBarCodeDetailsVO.setQrCodeValue(qrBarCodeDetailsDTO.getQrCodeValue());

		
			qrBarCodeDetailsVO.setQrBarCodeVO(qrBarCodeVO);
			qrBarCodeDetailsVOs.add(qrBarCodeDetailsVO);

		}
		qrBarCodeVO.setQrBarCodeDetailsVO(qrBarCodeDetailsVOs);
	

	}

	
	
	@Override
	public List<QrBarCodeVO> getAllQrBarCode( ) {
		return qrBarCodeRepo.findAllQrBarCode();

	}

	@Override
	public QrBarCodeVO getQrBarCodeById(Long id) {
		QrBarCodeVO qrBarCodeVO = new QrBarCodeVO();
		if (ObjectUtils.isNotEmpty(id)) {
			LOGGER.info("Successfully Received  QrBarCode BY Id : {}", id);
			qrBarCodeVO = qrBarCodeRepo.findQrBarCodeById(id);
		} else {
			LOGGER.info("failed Received  QrBarCode For All Id.");
		}
		return qrBarCodeVO;

	}

	
	
	 private int totalRows = 0; // Instance variable to keep track of total rows
	    private int successfulUploads = 0; // Instance variable to keep track of successful uploads

	    private final DataFormatter dataFormatter = new DataFormatter();


	    @Transactional
	    public void ExcelUploadForQrBarCode(MultipartFile[] files, CustomerAttachmentType type,
				String createdBy) throws ApplicationException {
	        List<QrBarExcelUploadVO> qrBarExcelUploadVOsToSave = new ArrayList<>();
	        totalRows = 0;
	        successfulUploads = 0;

	        for (MultipartFile file : files) {
	            if (file.isEmpty()) {
	                throw new ApplicationException("The supplied file '" + file.getOriginalFilename() + "' is empty (zero bytes long).");
	            }

	            try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
	                Sheet sheet = workbook.getSheetAt(0);
	                List<String> errorMessages = new ArrayList<>();
	                System.out.println("Processing file: " + file.getOriginalFilename());

	                Row headerRow = sheet.getRow(0);
	                if (!isHeaderValid(headerRow)) {
	                    throw new ApplicationException("Invalid Excel format in file '" + file.getOriginalFilename() + "'. Expected headers are: Type, From location, From location type, Location pick, partno, partdesc, sku, Grn No, GRN date, Batch No, Exp date, Entry no, From Status, To Status");
	                }

	                for (Row row : sheet) {
	                    if (row.getRowNum() == 0 || isRowEmpty(row)) {
	                        continue;
	                    }

	                    totalRows++;
	                    System.out.println("Validating row: " + (row.getRowNum() + 1));
	                    
	                    try {
	                        String entryno = getStringCellValue(row.getCell(0));
	                        String partno = getStringCellValue(row.getCell(1));
	                        String partdescription = getStringCellValue(row.getCell(2));
	                       

	                        // Create and populate SrsExcelUploadVO object
	                        QrBarExcelUploadVO qrBarExcelUploadVO = new QrBarExcelUploadVO();
	                        qrBarExcelUploadVO.setEntryNo(entryno);
	                        qrBarExcelUploadVO.setPartNo(partno);
	                        qrBarExcelUploadVO.setPartDescription(partdescription);
	                       
	                      
	                        qrBarExcelUploadVO.setCreatedBy(createdBy);
	                        qrBarExcelUploadVO.setUpdatedBy(createdBy);
	                        qrBarExcelUploadVO.setActive(true);
	                        qrBarExcelUploadVO.setCancel(false);
	                        qrBarExcelUploadVO.setCancelRemarks("");

	                        qrBarExcelUploadVOsToSave.add(qrBarExcelUploadVO);
	                        successfulUploads++;
	                    } catch (Exception e) {
	                        // Optionally handle specific row processing exceptions here
	                    }
	                }

	                qrBarExcelUploadRepo.saveAll(qrBarExcelUploadVOsToSave);
	            } catch (IOException e) {
	                throw new ApplicationException("Failed to process file: " + file.getOriginalFilename() + " - " + e.getMessage());
	            }
	        }
	    }

	    private LocalDate parseDate(Cell cell) {
	        if (cell == null) {
	            return null;
	        }

	        try {
	            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
	                return cell.getLocalDateTimeCellValue().toLocalDate();
	            } else if (cell.getCellType() == CellType.STRING) {
	                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Adjusted to dd/MM/yyyy
	                return LocalDate.parse(cell.getStringCellValue(), formatter);
	            }
	        } catch (Exception e) {
	            System.err.println("Date parsing error for cell value: " + getStringCellValue(cell));
	        }
	        return null;
	    }

	    private String getStringCellValue(Cell cell) {
	        if (cell == null) {
	            return ""; // Return empty string if cell is null
	        }

	        // Use DataFormatter to get the cell value as a string
	        return dataFormatter.formatCellValue(cell);
	    }

	    private boolean isRowEmpty(Row row) {
	        for (Cell cell : row) {
	            if (cell.getCellType() != CellType.BLANK) {
	                return false;
	            }
	        }
	        return true;
	    }

	    private boolean isHeaderValid(Row headerRow) throws ApplicationException {
	        if (headerRow == null) {
	            return false;
	        }
	        List<String> expectedHeaders = Arrays.asList(
	            "entryno", "partno", "partdescription"
	        );

	        List<String> actualHeaders = new ArrayList<>();
	        for (Cell cell : headerRow) {
	            actualHeaders.add(getStringCellValue(cell).trim());
	        }

	        if (!expectedHeaders.equals(actualHeaders)) {
	            String errorDetails = "Expected headers: " + expectedHeaders.toString()
	                + ", Found headers: " + actualHeaders.toString();
	            throw new ApplicationException("Invalid Excel format. " + errorDetails);
	        }
	        return true;
	    }

	    public int getTotalRows() {
	        return totalRows;
	    }

	    public int getSuccessfulUploads() {
	        return successfulUploads;
	    }

	



}
