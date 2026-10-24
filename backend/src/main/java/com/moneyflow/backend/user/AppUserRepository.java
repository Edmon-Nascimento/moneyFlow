package com.moneyflow.backend.user;
import java.util.Optional; import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; import org.springframework.data.repository.query.Param;
public interface AppUserRepository extends JpaRepository<AppUser,Long>{Optional<AppUser> findByEmail(String email); long count();}
