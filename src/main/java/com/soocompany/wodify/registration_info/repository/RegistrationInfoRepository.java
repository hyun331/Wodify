package com.soocompany.wodify.registration_info.repository;

import com.soocompany.wodify.registration_info.domain.RegistrationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationInfoRepository extends JpaRepository<RegistrationInfo, Long> {
}
