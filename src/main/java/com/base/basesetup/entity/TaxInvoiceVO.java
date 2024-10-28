package com.base.basesetup.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "taxinvoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxInvoiceVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "taxinvoicegen")
	@SequenceGenerator(name = "taxinvoicegen", sequenceName = "taxinvoiceseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "taxinvoiceid")
	
	private Long id;

	@Column(name = "invoiceno", columnDefinition = "TEXT")
	private String invoiceNo;

	@Column(name = "invoicedate", columnDefinition = "TEXT")
	
	private LocalDate invoiceDate;

	@Column(name = "term", length = 1000, columnDefinition = "TEXT")
	private String term;

	@Column(name = "duedate", columnDefinition = "TEXT")
	private LocalDate dueDate;

	@Column(name = "servicemonth", columnDefinition = "TEXT")
	private String serviceMonth;

	@Column(name = "customer", columnDefinition = "TEXT")
	private String customer;

	@Column(name = "address", columnDefinition = "TEXT")
	private String address;

	@Column(name = "gsttype", columnDefinition = "TEXT")
	private String gstType;

	@Column(name = "sgst", columnDefinition = "TEXT")
	private Integer sgst;

	@Column(name = "cgst", columnDefinition = "TEXT")
	private Integer cgst;

	   
	
	@Column(name = "igst", columnDefinition = "TEXT")
	private Integer igst;

	@Column(name = "total", columnDefinition = "TEXT")
	private Long total;

	@Column(name = "subtotal", columnDefinition = "TEXT")
	private Long subTotal;

	@Column(name = "termsandconditions", columnDefinition = "TEXT")
	private String termsAndConditions;

	@Column(name = "bankname", columnDefinition = "TEXT")
	private String bankName;

	@Column(name = "accountname", columnDefinition = "TEXT")
	private String accountName;
	

	@Column(name = "accountno", columnDefinition = "TEXT")
	private String accountNo;

	@Column(name = "tax")
	private int tax;

	@Column(name = "ifsc", columnDefinition = "TEXT")
	private String Ifsc;

	@Column(name = "notes", columnDefinition = "TEXT")
	private String notes;

	@Column(name = "createdby", columnDefinition = "TEXT")
	private String createdBy;

	@Column(name = "modifiedby", columnDefinition = "TEXT")
	private String updatedBy;

	@Column(name = "orgid")
	private Long orgId;

	private boolean cancel = false;

	@Lob
	@Column(name = "data", nullable = false)
	private byte[] taxInvoiceimage;

	@OneToMany(mappedBy = "taxInvoiceVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<TaxInvoiceProductLineVO> productLines;

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
