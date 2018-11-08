package net.jianbo.cmdb.repository;

import net.jianbo.cmdb.domain.ComponentEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ComponentEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComponentEntityRepository extends JpaRepository<ComponentEntity, Long> {

}
