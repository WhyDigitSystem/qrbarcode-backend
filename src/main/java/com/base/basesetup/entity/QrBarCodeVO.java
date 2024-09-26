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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="qrbarcode")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrBarCodeVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qrbarcodegen")
	@SequenceGenerator(name = "qrbarcodegen", sequenceName = "qrbarcodeseq", initialValue = 1000000001, allocationSize = 1)
	
	@Column(name = "qrbarcodeid")
	private Long id;
	
//	@Column(name = "username")
//	private String userName;
	
	@Column(name = "docid")
	private String docId;
	@Column(name = "docdate")
	private LocalDate docDate;
	@Column(name = "entryno")
	private String entryNo;

	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String updatedBy;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "qrBarCodeVO", cascade = CascadeType.ALL)
	private List<QrBarCodeDetailsVO> qrBarCodeDetailsVO;
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
	
}
