package com.base.basesetup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.basesetup.dto.TaxInvoiceDTO;
import com.base.basesetup.dto.TaxInvoiceProductLineDTO;
import com.base.basesetup.entity.TaxInvoiceProductLineVO;
import com.base.basesetup.entity.TaxInvoiceVO;
import com.base.basesetup.exception.ApplicationException;
import com.base.basesetup.repo.TaxInvoiceProductLineRepo;
import com.base.basesetup.repo.TaxInvoiceRepo;

@Service
public class MasterServiceImpl implements MasterService{


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
			TaxInvoiceVO taxInvoiceVO = new TaxInvoiceVO();
			String message;

			if (ObjectUtils.isEmpty(taxInvoiceDTO.getId())) {
				List<TaxInvoiceProductLineVO> taxInvoiceProductLineVOs = new ArrayList<>();

				if (taxInvoiceDTO.getProductLines() != null) {
					for (TaxInvoiceProductLineDTO productLineDTO : taxInvoiceDTO.getProductLines()) {
						TaxInvoiceProductLineVO productLineVO = new TaxInvoiceProductLineVO();
						productLineVO.setDescription(productLineDTO.getDescription());
						productLineVO.setQuantity(productLineDTO.getQuantity());
						productLineVO.setRate(productLineDTO.getRate());
						productLineVO.setAmount(productLineDTO.getAmount());
						productLineVO.setTaxInvoiceVO(taxInvoiceVO);
						taxInvoiceProductLineVOs.add(productLineVO);
					}
				}

				

				if (taxInvoiceRepo.existsByOrgIdAndInvoiceNo(taxInvoiceDTO.getOrgId(), taxInvoiceDTO.getInvoiceNo())) {
					throw new ApplicationException("InvoiceNo already Exists");
				}
				taxInvoiceVO.setInvoiceNo(taxInvoiceDTO.getInvoiceNo());

				taxInvoiceVO.setProductLines(taxInvoiceProductLineVOs);
				taxInvoiceVO.setCreatedBy(taxInvoiceDTO.getCreatedBy());
				taxInvoiceVO.setModifiedBy(taxInvoiceDTO.getCreatedBy());



				mapTaxInvoiceDTOToTaxInvoiceVO(taxInvoiceDTO, taxInvoiceVO);
				message = "Tax Invoice Created successfully";
			} else {
				taxInvoiceVO = taxInvoiceRepo.findById(taxInvoiceDTO.getId()).get();

				if (!taxInvoiceVO.getInvoiceNo().equals(taxInvoiceDTO.getInvoiceNo())) {
					if (taxInvoiceRepo.existsByOrgIdAndInvoiceNo(taxInvoiceDTO.getOrgId(), taxInvoiceDTO.getInvoiceNo())) {
						throw new ApplicationException("InvoiceNo already Exists");
					}
					taxInvoiceVO.setInvoiceNo(taxInvoiceDTO.getInvoiceNo());
				}

				List<TaxInvoiceProductLineVO> existingProductLines = taxInvoiceProductLineRepo
						.findByTaxInvoiceVO(taxInvoiceVO);
				taxInvoiceProductLineRepo.deleteAll(existingProductLines);



				List<TaxInvoiceProductLineVO> taxInvoiceProductLineVOs = new ArrayList<>();

				if (taxInvoiceDTO.getProductLines() != null) {
					for (TaxInvoiceProductLineDTO productLineDTO : taxInvoiceDTO.getProductLines()) {
						TaxInvoiceProductLineVO productLineVO = new TaxInvoiceProductLineVO();
						productLineVO.setDescription(productLineDTO.getDescription());
						productLineVO.setQuantity(productLineDTO.getQuantity());
						productLineVO.setRate(productLineDTO.getRate());
						productLineVO.setAmount(productLineDTO.getAmount());
						productLineVO.setTaxInvoiceVO(taxInvoiceVO);
						taxInvoiceProductLineVOs.add(productLineVO);
					}
				}

			

				taxInvoiceVO.setModifiedBy(taxInvoiceDTO.getCreatedBy());
				taxInvoiceVO.setProductLines(taxInvoiceProductLineVOs);
				mapTaxInvoiceDTOToTaxInvoiceVO(taxInvoiceDTO, taxInvoiceVO);


				message = "Tax Invoice Updated successfully";
			}
			String invoiceno = taxInvoiceDTO.getInvoiceNo();
			if (invoiceno == null) {
				throw new ApplicationException("Field cannot be Empty");
			}
			taxInvoiceRepo.save(taxInvoiceVO);
			Map<String, Object> response = new HashMap<>();
			response.put("taxInvoiceVO", taxInvoiceVO);
			response.put("message", message);
			return response;
		}

		private void mapTaxInvoiceDTOToTaxInvoiceVO(TaxInvoiceDTO taxInvoiceDTO, TaxInvoiceVO taxInvoiceVO) {
			taxInvoiceVO.setCompanyAddress(taxInvoiceDTO.getCompanyAddress());
			taxInvoiceVO.setInvoiceNo(taxInvoiceDTO.getInvoiceNo());
			taxInvoiceVO.setInvoiceDate(taxInvoiceDTO.getInvoiceDate());
			taxInvoiceVO.setTerm(taxInvoiceDTO.getTerm());
			taxInvoiceVO.setDueDate(taxInvoiceDTO.getDueDate());
			taxInvoiceVO.setServiceMonth(taxInvoiceDTO.getServiceMonth());
			taxInvoiceVO.setBillToAddress(taxInvoiceDTO.getBillToAddress());
			taxInvoiceVO.setShipToAddress(taxInvoiceDTO.getShipToAddress());
			taxInvoiceVO.setGstType(taxInvoiceDTO.getGstType());
			taxInvoiceVO.setSgst(taxInvoiceDTO.getSgst());
			taxInvoiceVO.setCgst(taxInvoiceDTO.getCgst());
			taxInvoiceVO.setIgst(taxInvoiceDTO.getIgst());
			taxInvoiceVO.setTotal(taxInvoiceDTO.getTotal());
			taxInvoiceVO.setSubTotal(taxInvoiceDTO.getSubTotal());
			taxInvoiceVO.setTermsAndConditions(taxInvoiceDTO.getTermsAndConditions());
			taxInvoiceVO.setBankName(taxInvoiceDTO.getBankName());
			taxInvoiceVO.setAccountName(taxInvoiceDTO.getAccountName());
			taxInvoiceVO.setAccountNo(taxInvoiceDTO.getAccountNo());
			taxInvoiceVO.setIfsc(taxInvoiceDTO.getIfsc());
			taxInvoiceVO.setNotes(taxInvoiceDTO.getNotes());
			taxInvoiceVO.setOrgId(taxInvoiceDTO.getOrgId());
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
		public List<TaxInvoiceVO> getAllTaxInvoice( ) {
			return taxInvoiceRepo.findAll();
		}
}
