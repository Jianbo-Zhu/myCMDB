package net.jianbo.cmdb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Server.
 */
@Entity
@Table(name = "server")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Server implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "hostname", nullable = false)
    private String hostname;

    @NotNull
    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @NotNull
    @Column(name = "mac_address", nullable = false)
    private String macAddress;

    @NotNull
    @Column(name = "position", nullable = false)
    private String position;

    @NotNull
    @Column(name = "brand", nullable = false)
    private String brand;

    @NotNull
    @Column(name = "mem_size", nullable = false)
    private Integer memSize;

    @NotNull
    @Column(name = "core_no", nullable = false)
    private Integer coreNo;

    @NotNull
    @Column(name = "os_version", nullable = false)
    private String osVersion;

    @NotNull
    @Column(name = "vendor", nullable = false)
    private String vendor;

    @NotNull
    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @NotNull
    @Column(name = "warranty_date", nullable = false)
    private LocalDate warrantyDate;

    @OneToMany(mappedBy = "server")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ComponentEntity> components = new HashSet<>();
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private DataCenter dataCenter;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Contactor owner;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Contactor vendorContact;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public Server hostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Server ipAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public Server macAddress(String macAddress) {
        this.macAddress = macAddress;
        return this;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getPosition() {
        return position;
    }

    public Server position(String position) {
        this.position = position;
        return this;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBrand() {
        return brand;
    }

    public Server brand(String brand) {
        this.brand = brand;
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getMemSize() {
        return memSize;
    }

    public Server memSize(Integer memSize) {
        this.memSize = memSize;
        return this;
    }

    public void setMemSize(Integer memSize) {
        this.memSize = memSize;
    }

    public Integer getCoreNo() {
        return coreNo;
    }

    public Server coreNo(Integer coreNo) {
        this.coreNo = coreNo;
        return this;
    }

    public void setCoreNo(Integer coreNo) {
        this.coreNo = coreNo;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public Server osVersion(String osVersion) {
        this.osVersion = osVersion;
        return this;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getVendor() {
        return vendor;
    }

    public Server vendor(String vendor) {
        this.vendor = vendor;
        return this;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public Server purchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getWarrantyDate() {
        return warrantyDate;
    }

    public Server warrantyDate(LocalDate warrantyDate) {
        this.warrantyDate = warrantyDate;
        return this;
    }

    public void setWarrantyDate(LocalDate warrantyDate) {
        this.warrantyDate = warrantyDate;
    }

    public Set<ComponentEntity> getComponents() {
        return components;
    }

    public Server components(Set<ComponentEntity> componentEntities) {
        this.components = componentEntities;
        return this;
    }

    public Server addComponents(ComponentEntity componentEntity) {
        this.components.add(componentEntity);
        componentEntity.setServer(this);
        return this;
    }

    public Server removeComponents(ComponentEntity componentEntity) {
        this.components.remove(componentEntity);
        componentEntity.setServer(null);
        return this;
    }

    public void setComponents(Set<ComponentEntity> componentEntities) {
        this.components = componentEntities;
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    public Server dataCenter(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
        return this;
    }

    public void setDataCenter(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    public Contactor getOwner() {
        return owner;
    }

    public Server owner(Contactor contactor) {
        this.owner = contactor;
        return this;
    }

    public void setOwner(Contactor contactor) {
        this.owner = contactor;
    }

    public Contactor getVendorContact() {
        return vendorContact;
    }

    public Server vendorContact(Contactor contactor) {
        this.vendorContact = contactor;
        return this;
    }

    public void setVendorContact(Contactor contactor) {
        this.vendorContact = contactor;
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
        Server server = (Server) o;
        if (server.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), server.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Server{" +
            "id=" + getId() +
            ", hostname='" + getHostname() + "'" +
            ", ipAddress='" + getIpAddress() + "'" +
            ", macAddress='" + getMacAddress() + "'" +
            ", position='" + getPosition() + "'" +
            ", brand='" + getBrand() + "'" +
            ", memSize=" + getMemSize() +
            ", coreNo=" + getCoreNo() +
            ", osVersion='" + getOsVersion() + "'" +
            ", vendor='" + getVendor() + "'" +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", warrantyDate='" + getWarrantyDate() + "'" +
            "}";
    }
}
