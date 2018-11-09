package net.jianbo.cmdb.repository;

import net.jianbo.cmdb.domain.Contactor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Contactor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactorRepository extends JpaRepository<Contactor, Long>, JpaSpecificationExecutor<Contactor> {

}
