package net.jianbo.cmdb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "app_name", nullable = false)
    private String appName;

    @NotNull
    @Column(name = "environment", nullable = false)
    private String environment;

    @OneToMany(mappedBy = "app")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ComponentEntity> components = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public Application appName(String appName) {
        this.appName = appName;
        return this;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEnvironment() {
        return environment;
    }

    public Application environment(String environment) {
        this.environment = environment;
        return this;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Set<ComponentEntity> getComponents() {
        return components;
    }

    public Application components(Set<ComponentEntity> componentEntities) {
        this.components = componentEntities;
        return this;
    }

    public Application addComponents(ComponentEntity componentEntity) {
        this.components.add(componentEntity);
        componentEntity.setApp(this);
        return this;
    }

    public Application removeComponents(ComponentEntity componentEntity) {
        this.components.remove(componentEntity);
        componentEntity.setApp(null);
        return this;
    }

    public void setComponents(Set<ComponentEntity> componentEntities) {
        this.components = componentEntities;
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
        Application application = (Application) o;
        if (application.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), application.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Application{" +
            "id=" + getId() +
            ", appName='" + getAppName() + "'" +
            ", environment='" + getEnvironment() + "'" +
            "}";
    }
}
