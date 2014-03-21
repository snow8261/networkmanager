package com.nsg.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.nsg.persistence.domain.Managedelement;



public interface ManagedelementsRepository extends CrudRepository< Managedelement, String>{

	Managedelement findById(Long key);
}
