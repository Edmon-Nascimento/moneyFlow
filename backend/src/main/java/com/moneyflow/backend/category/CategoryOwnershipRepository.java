package com.moneyflow.backend.category;
import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param;
public interface CategoryOwnershipRepository extends JpaRepository<Category,Long>{
 @Modifying @Query(value="update categories set user_id=:userId where user_id is null",nativeQuery=true)
 int assignUnowned(@Param("userId") Long userId);
}
