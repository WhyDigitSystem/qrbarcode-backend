package com.base.basesetup.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basesetup.entity.TaxInvoiceProductLineVO;
import com.base.basesetup.entity.TaxInvoiceVO;

@Repository
public interface TaxInvoiceProductLineRepo extends JpaRepository<TaxInvoiceProductLineVO, Long>{

	List<TaxInvoiceProductLineVO> findByTaxInvoiceVO(TaxInvoiceVO taxInvoiceVO);

}
