package net.jianbo.cmdb.service.impl;

import net.jianbo.cmdb.service.ServerService;
import net.jianbo.cmdb.domain.Server;
import net.jianbo.cmdb.repository.ServerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Server.
 */
@Service
@Transactional
public class ServerServiceImpl implements ServerService {

    private final Logger log = LoggerFactory.getLogger(ServerServiceImpl.class);

    private final ServerRepository serverRepository;

    public ServerServiceImpl(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    /**
     * Save a server.
     *
     * @param server the entity to save
     * @return the persisted entity
     */
    @Override
    public Server save(Server server) {
        log.debug("Request to save Server : {}", server);
        return serverRepository.save(server);
    }

    /**
     * Get all the servers.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Server> findAll() {
        log.debug("Request to get all Servers");
        return serverRepository.findAll();
    }


    /**
     * Get one server by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Server> findOne(Long id) {
        log.debug("Request to get Server : {}", id);
        return serverRepository.findById(id);
    }

    /**
     * Delete the server by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Server : {}", id);
        serverRepository.deleteById(id);
    }
}
