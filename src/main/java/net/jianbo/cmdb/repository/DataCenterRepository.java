package net.jianbo.cmdb.repository;

import net.jianbo.cmdb.domain.DataCenter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DataCenter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataCenterRepository extends JpaRepository<DataCenter, Long> {

}
