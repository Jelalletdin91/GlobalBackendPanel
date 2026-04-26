package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.Kimlik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KimlikRepository extends JpaRepository<Kimlik, Long> {

    List<Kimlik> findAllByOrderByKimlikEndDateAsc();

    List<Kimlik> findByNotified60DaysFalse();

    long countByCompanyId(Long companyId);

    List<Kimlik> findByCompanyIdOrderByKimlikEndDateAsc(Long companyId);

    @Query("""
            SELECT k FROM Kimlik k
            WHERE k.company.id = :companyId
            AND (
                LOWER(k.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(k.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(k.kimlikNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(k.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(k.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            ORDER BY k.kimlikEndDate ASC
            """)
    List<Kimlik> searchByCompanyIdAndKeyword(Long companyId, String keyword);
}