package com.base.basesetup.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.basesetup.dto.TaxInvoiceDTO;
import com.base.basesetup.entity.TaxInvoiceVO;
import com.base.basesetup.exception.ApplicationException;

@Service
public interface MasterService {

	Map<String, Object> createUpdateTaxInvoice(TaxInvoiceDTO taxInvoiceDTO) throws ApplicationException;

	List<TaxInvoiceVO> getAllTaxInvoice(Long orgId);

	TaxInvoiceVO getTaxInvoiceById(Long id);

	List<TaxInvoiceVO> getAllTaxInvoice();

	TaxInvoiceVO uploadImageForTaxInvoice(MultipartFile file, Long id) throws IOException;

	byte[] getTaxInvoiceImageById(Long id);

}
