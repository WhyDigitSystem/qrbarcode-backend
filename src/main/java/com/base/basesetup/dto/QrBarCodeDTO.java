package com.base.basesetup.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrBarCodeDTO {

	private Long Id;

//	private String userName;

	private String docId;
	private LocalDate docDate;
	private String entryNo;
	private Long count;
	private String createdBy;

	private List<QrBarCodeDetailsDTO> qrBarCodeDetailsDTO;

}
