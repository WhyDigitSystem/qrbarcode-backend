package com.base.basesetup.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.QrBarCodeVO;

@Repository
public interface QrBarCodeRepo extends JpaRepository<QrBarCodeVO, Long>{

	@Query(nativeQuery = true, value = "select * from qrbarcode")
	List<QrBarCodeVO> findAllQrBarCode();

	@Query(nativeQuery = true, value = "select * from qrbarcode where qrbarcodeid=?1")
	QrBarCodeVO findQrBarCodeById(Long id);

	@Query(nativeQuery = true, value = "SELECT partno, partdescription FROM qrbarexcelupload WHERE entryno = ?1 AND (entryno) NOT IN (SELECT entryno FROM qrbarcode) GROUP BY partno, partdescription")
	Set<Object[]> findFillGridFromQrBarExcelUpload(String entryNo);


}
