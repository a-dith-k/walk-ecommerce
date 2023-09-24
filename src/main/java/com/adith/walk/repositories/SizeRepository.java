package com.adith.walk.repositories;

import com.adith.walk.Entities.Size;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SizeRepository extends JpaRepository<Size,Long> {

    @Transactional
    @Modifying
    @Query("update Size s set s.totalStock=s.totalStock-:count where s.sizeId=:id")
    void removeStock(@Param("id") long sizeId,@Param("count") long stockCount);


    @Transactional
    @Modifying
    @Query("update Size s set s.totalStock=s.totalStock+:count where s.sizeId=:id")
    void addStock(@Param("id") long sizeId,@Param("count") long stockCount);
}
