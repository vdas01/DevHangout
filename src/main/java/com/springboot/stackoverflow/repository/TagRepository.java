package com.springboot.stackoverflow.repository;

import com.springboot.stackoverflow.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag,Integer> {

//    @Query("SELECT t FROM Tag t " +
//            "WHERE (:query IS NULL OR t.name LIKE %:query%) " +
//            "ORDER BY "
//            + "CASE WHEN :sortField IS NULL THEN NULL ELSE "
//            + "CASE :sortField "
//            + "WHEN 'name' THEN t.name "
//            + "WHEN 'createdAt' THEN t.createdAt "
//            + "ELSE NULL END END "
//            + ":sortDirection NULLS LAST")
@Query("SELECT t FROM Tag t " +
        "WHERE (:query IS NULL OR t.name LIKE %:query%) " +
        "ORDER BY " +
        "CASE " +
        "  WHEN :sortField = 'popular' AND :sortDirection = 'asc' THEN t.name END ASC," +
        "CASE " +
        "  WHEN :sortField = 'popular' AND :sortDirection = 'desc' THEN t.name END DESC," +
        "CASE " +
        "  WHEN :sortField = 'name' AND :sortDirection = 'asc' THEN t.name END ASC," +
        "CASE " +
        "  WHEN :sortField = 'name' AND :sortDirection = 'desc' THEN t.name END DESC," +
        "CASE " +
        "  WHEN :sortField = 'new' AND :sortDirection = 'asc' THEN t.createdAt END ASC," +
        "CASE " +
        "  WHEN :sortField = 'new' AND :sortDirection = 'desc' THEN t.createdAt END DESC")
    Page<Tag> searchTags(@Param("query") String query, @Param("sortField") String sortField,
            @Param("sortDirection") String sortDirection,Pageable pageable);
}
