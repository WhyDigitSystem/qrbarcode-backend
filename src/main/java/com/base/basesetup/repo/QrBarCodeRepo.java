package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.QrBarCodeVO;
import com.base.basesetup.entity.SingleQrBarCodeVO;

@Repository
public interface QrBarCodeRepo extends JpaRepository<QrBarCodeVO, Long>{

	@Query(nativeQuery = true, value = "select * from qrbarcode")
	List<QrBarCodeVO> findAllQrBarCode();

	@Query(nativeQuery = true, value = "select * from qrbarcode where qrbarcodeid=?1")
	QrBarCodeVO findQrBarCodeById(Long id);

	@Query(nativeQuery = true, value = "SELECT partno, partdescription,batchno FROM qrbarexcelupload WHERE entryno = ?1 AND (entryno) NOT IN (SELECT entryno FROM qrbarcode) GROUP BY partno, partdescription,batchno")
	Set<Object[]> findFillGridFromQrBarExcelUpload(String entryNo);

	@Query(nativeQuery = true, value = "select sequence_value from qrbarcodedocidseq")
	String finddocid();

	@Query(nativeQuery = true, value = "CALL next_qrbarcodedocid_value()")
	void updatesequence();

	@Query(nativeQuery = true, value = " select sequence_value from qrbarcodedocidseq")
	int getQrBarCodeDocId();

	
	


}
