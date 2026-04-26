package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.VorkerKimlik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface VorkerRepository extends JpaRepository<VorkerKimlik, Long> {

    List<VorkerKimlik> findByNotified60DaysFalse();

    List<VorkerKimlik> findByNotified60DaysFalseAndKimlikEndDateLessThanEqual(LocalDate date);

    List<VorkerKimlik> findAllByOrderByKimlikEndDateAsc();

    List<VorkerKimlik> findByCompanyIdOrderByKimlikEndDateAsc(Long companyId);

    long countByCompanyId(Long companyId);

    @Query("""
            SELECT v FROM VorkerKimlik v
            WHERE v.company.id = :companyId
            AND (
                LOWER(v.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(v.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(v.kimlikNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(v.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(v.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            ORDER BY v.kimlikEndDate ASC
            """)
    List<VorkerKimlik> searchByCompanyIdAndKeyword(Long companyId, String keyword);
}