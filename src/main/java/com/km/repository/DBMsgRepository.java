package com.km.repository;

import com.km.model.DBMsg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBMsgRepository extends JpaRepository<DBMsg, Long> {
    int countByStatus(boolean status);
}
