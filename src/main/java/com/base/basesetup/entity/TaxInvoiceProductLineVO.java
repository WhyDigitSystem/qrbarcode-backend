package com.base.basesetup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="taxinvoicedetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxInvoiceProductLineVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "taxinvoicedetailsgen")
	@SequenceGenerator(name = "taxinvoicedetailsgen", sequenceName = "taxinvoicedetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "taxinvoicedetailsid")
	private Long id;
	
	@Column(name="description")
	private String description;
	@Column(name="quantity")
	private String quantity;
	@Column(name="rate")
	private String rate;
	@Column(name="amount")
	private Long amount; 
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "taxinvoiceid")
	private TaxInvoiceVO taxInvoiceVO;

}

