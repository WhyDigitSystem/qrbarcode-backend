package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.TaxInvoiceVO;

@Repository
public interface TaxInvoiceRepo extends JpaRepository<TaxInvoiceVO, Long>{

	List<TaxInvoiceVO> findAllByOrgId(Long orgId);

	boolean existsByOrgIdAndInvoiceNo(Long orgId, String invoiceNo);
}
