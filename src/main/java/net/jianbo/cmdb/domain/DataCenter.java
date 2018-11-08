package net.jianbo.cmdb.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DataCenter.
 */
@Entity
@Table(name = "data_center")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DataCenter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "dc_name", nullable = false)
    private String dcName;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @OneToOne    @JoinColumn(unique = true)
    private Contactor contractor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDcName() {
        return dcName;
    }

    public DataCenter dcName(String dcName) {
        this.dcName = dcName;
        return this;
    }

    public void setDcName(String dcName) {
        this.dcName = dcName;
    }

    public String getAddress() {
        return address;
    }

    public DataCenter address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Contactor getContractor() {
        return contractor;
    }

    public DataCenter contractor(Contactor contactor) {
        this.contractor = contactor;
        return this;
    }

    public void setContractor(Contactor contactor) {
        this.contractor = contactor;
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
        DataCenter dataCenter = (DataCenter) o;
        if (dataCenter.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataCenter.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DataCenter{" +
            "id=" + getId() +
            ", dcName='" + getDcName() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
