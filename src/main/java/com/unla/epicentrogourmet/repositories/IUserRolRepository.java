package com.unla.epicentrogourmet.repositories;


import com.unla.epicentrogourmet.entities.UserRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRolRepository")
public interface IUserRolRepository extends JpaRepository<UserRol, Integer> {
}
