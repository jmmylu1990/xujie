package com.example.xujie.repository.mysql;

import com.example.xujie.entity.mysql.MemberStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface MemberStatisticsRepository extends JpaRepository<MemberStatistics, Long> {
@Query(value = "SELECT m.id, m.name, m.email " +
        "FROM Members m " +
        "JOIN Orders o ON m.id = o.member_id " +
        "GROUP BY m.id, m.name, m.email " +
        "HAVING COUNT(o.id) > :n",
        nativeQuery = true)
    List<MemberStatistics> getMemberStatistics(@Param("n") Integer n);

}
