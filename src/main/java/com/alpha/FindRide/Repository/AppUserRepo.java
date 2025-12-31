package com.alpha.FindRide.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alpha.FindRide.Entity.AppUser;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {

	Optional<AppUser> findByMobileno(long mobileno);

	
}
