package net.jianbo.cmdb.repository;

import net.jianbo.cmdb.domain.Server;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Server entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {

}
