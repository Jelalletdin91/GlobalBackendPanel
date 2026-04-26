package com.GlobalGroupBackendPanel.BacendPanelForClientdemo.repository;

import com.GlobalGroupBackendPanel.BacendPanelForClientdemo.entity.StudentKimlik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface StudentRepository extends JpaRepository<StudentKimlik, Long> {

    List<StudentKimlik> findAllByOrderByKimlikEndDateAsc();

    List<StudentKimlik> findByNotified60DaysFalse();

    List<StudentKimlik> findByNotified60DaysFalseAndKimlikEndDateLessThanEqual(LocalDate date);

    long countByCompanyId(Long companyId);

    List<StudentKimlik> findByCompanyIdOrderByKimlikEndDateAsc(Long companyId);

    @Query("""
            SELECT s FROM StudentKimlik s
            WHERE s.company.id = :companyId
            AND (
                LOWER(s.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(s.kimlikNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(s.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(s.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            ORDER BY s.kimlikEndDate ASC
            """)
    List<StudentKimlik> searchByCompanyIdAndKeyword(Long companyId, String keyword);
}