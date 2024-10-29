package com.base.basesetup.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxInvoiceDTO {

	private Long id;
	private String invoiceNo;
	private LocalDate invoiceDate;
	private String term;
	private LocalDate dueDate;
	private String serviceMonth;
	private int tax;
	private String customer;
	private String address;
	private String gstType;
	private Integer sgst;
	private Integer cgst;
	private Integer igst;
	private String termsAndConditions;
	private String bankName;
	private String accountName;
	private String accountNo;
	private String Ifsc;
	private String notes;
	private String createdBy;
//	private Long orgId;
	private byte[] taxInvoiceimage;


	private List<TaxInvoiceProductLineDTO> productLines;

}

