package com.jiamian.translation.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jiamian.translation.dao.model.ModelClaim;

/**
 * @author DingGuangHui
 * @date 2023/2/16
 */
@Repository
public interface ModelClaimRepository extends JpaRepository<ModelClaim, Long> {

}
