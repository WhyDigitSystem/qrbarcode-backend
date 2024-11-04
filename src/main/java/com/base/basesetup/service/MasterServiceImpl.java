package com.base.basesetup.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.TaxInvoiceDTO;
import com.base.basesetup.dto.TaxInvoiceProductLineDTO;
import com.base.basesetup.entity.TaxInvoiceProductLineVO;
import com.base.basesetup.entity.TaxInvoiceVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.TaxInvoiceProductLineRepo;
import com.base.basesetup.repo.TaxInvoiceRepo;

@Service
public class MasterServiceImpl implements MasterService {

	public static final Logger LOGGER = LoggerFactory.getLogger(MasterServiceImpl.class);

	private static final String STRING = null;

	private static final String NUMERIC = null;

	@Autowired
	TaxInvoiceRepo taxInvoiceRepo;

	@Autowired
	TaxInvoiceProductLineRepo taxInvoiceProductLineRepo;

	// TaxInvoice

	@Override
	public Map<String, Object> createUpdateTaxInvoice(TaxInvoiceDTO taxInvoiceDTO) throws ApplicationException {
		TaxInvoiceVO taxInvoiceVO;
		String message;

		// Check if it's a new invoice or an update
		if (ObjectUtils.isEmpty(taxInvoiceDTO.getId())) {
//			if (taxInvoiceRepo.existsByOrgIdAndInvoiceNo(taxInvoiceDTO.getOrgId(), taxInvoiceDTO.getInvoiceNo())) {
//				throw new ApplicationException("InvoiceNo already exists");
//			}

			taxInvoiceVO = new TaxInvoiceVO();
			taxInvoiceVO.setInvoiceNo(taxInvoiceDTO.getInvoiceNo());
			taxInvoiceVO.setCreatedBy(taxInvoiceDTO.getCreatedBy());
			taxInvoiceVO.setUpdatedBy(taxInvoiceDTO.getCreatedBy());

			message = "Tax Invoice created successfully";
		} else {
			// Find the existing TaxInvoiceVO for update
			taxInvoiceVO = taxInvoiceRepo.findById(taxInvoiceDTO.getId())
					.orElseThrow(() -> new ApplicationException("Invoice not found"));

			// Check if the invoice number is changing and if the new one already exists
//			if (!taxInvoiceVO.getInvoiceNo().equals(taxInvoiceDTO.getInvoiceNo()) && taxInvoiceRepo
//					.existsByOrgIdAndInvoiceNo(taxInvoiceDTO.getOrgId(), taxInvoiceDTO.getInvoiceNo())) {
//				throw new ApplicationException("InvoiceNo already exists");
//			}

			taxInvoiceVO.setInvoiceNo(taxInvoiceDTO.getInvoiceNo());
			taxInvoiceVO.setUpdatedBy(taxInvoiceDTO.getCreatedBy());
			message = "Tax Invoice updated successfully";
		}

		// First, save the taxInvoiceVO to generate ID if it's a new entity
		mapTaxInvoiceDTOToTaxInvoiceVO(taxInvoiceDTO, taxInvoiceVO);
		taxInvoiceVO = taxInvoiceRepo.save(taxInvoiceVO);

		// Delete any existing product lines and save the new ones
		List<TaxInvoiceProductLineVO> existingProductLines = taxInvoiceProductLineRepo.findByTaxInvoiceVO(taxInvoiceVO);
		taxInvoiceProductLineRepo.deleteAll(existingProductLines);

		List<TaxInvoiceProductLineVO> taxInvoiceProductLineVOs = new ArrayList<>();
		for (TaxInvoiceProductLineDTO productLineDTO : taxInvoiceDTO.getProductLines()) {
			TaxInvoiceProductLineVO productLineVO = new TaxInvoiceProductLineVO();
			productLineVO.setDescription(productLineDTO.getDescription());
			productLineVO.setQuantity(productLineDTO.getQuantity());
			productLineVO.setRate(productLineDTO.getRate());
			productLineVO.setAmount((long) productLineDTO.getQuantity() * productLineDTO.getRate()); // Calculate amount
			productLineVO.setTaxInvoiceVO(taxInvoiceVO); // Set the parent TaxInvoiceVO reference
			taxInvoiceProductLineVOs.add(productLineVO);
		}
		taxInvoiceProductLineRepo.saveAll(taxInvoiceProductLineVOs); // Save all new product lines

		taxInvoiceVO.setProductLines(taxInvoiceProductLineVOs); // Set the saved product lines if needed

		Map<String, Object> response = new HashMap<>();
		response.put("taxInvoiceVO", taxInvoiceVO);
		response.put("message", message);
		return response;
	}

	private void mapTaxInvoiceDTOToTaxInvoiceVO(TaxInvoiceDTO taxInvoiceDTO, TaxInvoiceVO taxInvoiceVO) {
		taxInvoiceVO.setInvoiceNo(taxInvoiceDTO.getInvoiceNo());
		taxInvoiceVO.setInvoiceDate(taxInvoiceDTO.getInvoiceDate());
		taxInvoiceVO.setTerm(taxInvoiceDTO.getTerm());
		taxInvoiceVO.setDueDate(taxInvoiceDTO.getDueDate());
		taxInvoiceVO.setServiceMonth(taxInvoiceDTO.getServiceMonth());
		taxInvoiceVO.setCustomer(taxInvoiceDTO.getCustomer());
		taxInvoiceVO.setAddress(taxInvoiceDTO.getAddress());
		taxInvoiceVO.setGstType(taxInvoiceDTO.getGstType());
		

		// Calculate total amount including tax
		long totalAmount = taxInvoiceDTO.getProductLines().stream()
				.mapToLong(line -> (long) line.getQuantity() * line.getRate()).sum();

		if(taxInvoiceDTO.getCgst()!=0 && taxInvoiceDTO.getSgst()!=0) {
		// Apply tax to calculate the total
		long taxAmount = (taxInvoiceDTO.getCgst() + taxInvoiceDTO.getSgst()) * totalAmount / 100;
		taxInvoiceVO.setTotal(totalAmount + taxAmount);
		
		taxInvoiceVO.setSgst(taxInvoiceDTO.getSgst());
		taxInvoiceVO.setCgst(taxInvoiceDTO.getCgst());
		taxInvoiceVO.setIgst(0);
		taxInvoiceVO.setTax(0);

		}
		else if (taxInvoiceDTO.getIgst()!=0){
			long taxAmount = ( taxInvoiceDTO.getIgst()) * totalAmount / 100;
			taxInvoiceVO.setTotal(totalAmount + taxAmount);
			
			taxInvoiceVO.setIgst(taxInvoiceDTO.getIgst());
			taxInvoiceVO.setSgst(0);
			taxInvoiceVO.setCgst(0);
			taxInvoiceVO.setTax(0);

		}
		else{
			long taxAmount = ( taxInvoiceDTO.getTax()) * totalAmount / 100;
			taxInvoiceVO.setTotal(totalAmount + taxAmount); 
			taxInvoiceVO.setTax(taxInvoiceDTO.getTax());
			taxInvoiceVO.setIgst(0);
			taxInvoiceVO.setSgst(0);
			taxInvoiceVO.setCgst(0);

		}
		
		taxInvoiceVO.setSubTotal(totalAmount);
		taxInvoiceVO.setTermsAndConditions(taxInvoiceDTO.getTermsAndConditions());
		taxInvoiceVO.setBankName(taxInvoiceDTO.getBankName());
		taxInvoiceVO.setAccountName(taxInvoiceDTO.getAccountName());
		taxInvoiceVO.setAccountNo(taxInvoiceDTO.getAccountNo());
		taxInvoiceVO.setIfsc(taxInvoiceDTO.getIfsc());
		taxInvoiceVO.setNotes(taxInvoiceDTO.getNotes());
		taxInvoiceVO.setTaxInvoiceimage(taxInvoiceDTO.getTaxInvoiceimage());

	}

	@Override
	public List<TaxInvoiceVO> getAllTaxInvoice(Long orgId) {

		return taxInvoiceRepo.findAllByOrgId(orgId);
	}

	@Override
	public TaxInvoiceVO getTaxInvoiceById(Long id) {
		return taxInvoiceRepo.findById(id).get();
	}

	@Override
	public List<TaxInvoiceVO> getAllTaxInvoice() {
		return taxInvoiceRepo.findAll();
	}

	@Override
	public TaxInvoiceVO uploadImageForTaxInvoice(MultipartFile file, Long id) throws IOException {
		LOGGER.debug("Uploading Taxinvoice image for id: {}", id);
		TaxInvoiceVO taxInvoiceVO = taxInvoiceRepo.findById(id).orElseThrow(() -> {
			LOGGER.error("Taxinvoice not found for id: {}", id);
			return new RuntimeException("Taxinvoice not found");
		});
		taxInvoiceVO.setTaxInvoiceimage(file.getBytes());
		LOGGER.debug("Taxinvoice image uploaded successfully for id: {}", id);
		return taxInvoiceRepo.save(taxInvoiceVO);
	}

	@Override
	public byte[] getTaxInvoiceImageById(Long id) {
		LOGGER.debug("Retrieving TaxInvoice image for id: {}", id);

		TaxInvoiceVO taxInvoiceVO = taxInvoiceRepo.findById(id).orElseThrow(() -> {
			LOGGER.error("TaxInvoice not found for id: {}", id);
			return new RuntimeException("TaxInvoice not found");
		});

		byte[] image = taxInvoiceVO.getTaxInvoiceimage();

		if (image == null || image.length == 0) {
			LOGGER.error("No image data found for TaxInvoice with id: {}", id);
		} else {
			LOGGER.debug("TaxInvoice image retrieved successfully for id: {}", id);
		}

		return image;
	}
}
