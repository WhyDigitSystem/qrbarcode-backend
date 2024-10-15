package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxInvoiceProductLineDTO {

	private Long id;
	private String description;
	private String quantity;
	private String rate;
	private Long amount; 
}

