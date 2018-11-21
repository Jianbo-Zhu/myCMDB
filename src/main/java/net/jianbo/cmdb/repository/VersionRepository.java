package net.jianbo.cmdb.repository;

import net.jianbo.cmdb.domain.Version;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Version entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {

}
