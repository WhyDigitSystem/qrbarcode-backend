package com.base.basesetup.repo;

import java.util.List;

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

}
