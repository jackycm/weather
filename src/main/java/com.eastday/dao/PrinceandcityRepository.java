package com.eastday.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
/**
 * @author: zengzhewen
 * @create: 2020-04-17 11:00
 **/
@Repository
public interface PrinceandcityRepository extends JpaRepository<Princeandcity, Long>,JpaSpecificationExecutor<Princeandcity> {

    @Query("from Princeandcity where status=:status")
    List<Princeandcity> findAllByStatus(@Param("status") Integer status);

    @Transactional
    @Modifying
    @Query(value = "update princeandcity set msg=:msg, update_time=:updateTime where id=:id", nativeQuery = true)
    void editPrinceandcityByCity(@Param("id") String id, @Param("msg") String msg,@Param("updateTime") Integer updateTime);

    @Transactional
    @Modifying
    @Query(value = "update princeandcity set status=:status where id=:id", nativeQuery = true)
    void editPrinceandcityStatus(@Param("id") String id, @Param("status") Integer status);

    List<Princeandcity> findByCity(@Param("city") String city);
}
