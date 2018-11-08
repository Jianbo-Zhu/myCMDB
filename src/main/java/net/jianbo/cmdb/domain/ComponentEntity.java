package net.jianbo.cmdb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import net.jianbo.cmdb.domain.enumeration.ComponentType;

/**
 * A ComponentEntity.
 */
@Entity
@Table(name = "component_entity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ComponentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "com_name", nullable = false)
    private String comName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "com_type", nullable = false)
    private ComponentType comType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("components")
    private Application app;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("components")
    private Server server;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComName() {
        return comName;
    }

    public ComponentEntity comName(String comName) {
        this.comName = comName;
        return this;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public ComponentType getComType() {
        return comType;
    }

    public ComponentEntity comType(ComponentType comType) {
        this.comType = comType;
        return this;
    }

    public void setComType(ComponentType comType) {
        this.comType = comType;
    }

    public Application getApp() {
        return app;
    }

    public ComponentEntity app(Application application) {
        this.app = application;
        return this;
    }

    public void setApp(Application application) {
        this.app = application;
    }

    public Server getServer() {
        return server;
    }

    public ComponentEntity server(Server server) {
        this.server = server;
        return this;
    }

    public void setServer(Server server) {
        this.server = server;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComponentEntity componentEntity = (ComponentEntity) o;
        if (componentEntity.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), componentEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ComponentEntity{" +
            "id=" + getId() +
            ", comName='" + getComName() + "'" +
            ", comType='" + getComType() + "'" +
            "}";
    }
}
