package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrBarCodeDetailsDTO {
	private Long id;
	private String partNo;
	private String partDescription;
	private Long count;
	private String barCodeValue;
	private String qrCodeValue;
	
}
