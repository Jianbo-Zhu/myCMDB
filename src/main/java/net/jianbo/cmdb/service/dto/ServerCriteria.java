package net.jianbo.cmdb.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the Server entity. This class is used in ServerResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /servers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ServerCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter hostname;

    private StringFilter ipAddress;

    private StringFilter macAddress;

    private StringFilter position;

    private StringFilter brand;

    private IntegerFilter memSize;

    private IntegerFilter coreNo;

    private StringFilter osVersion;

    private StringFilter vendor;

    private LocalDateFilter purchaseDate;

    private LocalDateFilter warrantyDate;

    private LongFilter componentsId;

    private LongFilter dataCenterId;

    private LongFilter ownerId;

    private LongFilter vendorContactId;

    public ServerCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getHostname() {
        return hostname;
    }

    public void setHostname(StringFilter hostname) {
        this.hostname = hostname;
    }

    public StringFilter getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(StringFilter ipAddress) {
        this.ipAddress = ipAddress;
    }

    public StringFilter getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(StringFilter macAddress) {
        this.macAddress = macAddress;
    }

    public StringFilter getPosition() {
        return position;
    }

    public void setPosition(StringFilter position) {
        this.position = position;
    }

    public StringFilter getBrand() {
        return brand;
    }

    public void setBrand(StringFilter brand) {
        this.brand = brand;
    }

    public IntegerFilter getMemSize() {
        return memSize;
    }

    public void setMemSize(IntegerFilter memSize) {
        this.memSize = memSize;
    }

    public IntegerFilter getCoreNo() {
        return coreNo;
    }

    public void setCoreNo(IntegerFilter coreNo) {
        this.coreNo = coreNo;
    }

    public StringFilter getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(StringFilter osVersion) {
        this.osVersion = osVersion;
    }

    public StringFilter getVendor() {
        return vendor;
    }

    public void setVendor(StringFilter vendor) {
        this.vendor = vendor;
    }

    public LocalDateFilter getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateFilter purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDateFilter getWarrantyDate() {
        return warrantyDate;
    }

    public void setWarrantyDate(LocalDateFilter warrantyDate) {
        this.warrantyDate = warrantyDate;
    }

    public LongFilter getComponentsId() {
        return componentsId;
    }

    public void setComponentsId(LongFilter componentsId) {
        this.componentsId = componentsId;
    }

    public LongFilter getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(LongFilter dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    public LongFilter getVendorContactId() {
        return vendorContactId;
    }

    public void setVendorContactId(LongFilter vendorContactId) {
        this.vendorContactId = vendorContactId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ServerCriteria that = (ServerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(hostname, that.hostname) &&
            Objects.equals(ipAddress, that.ipAddress) &&
            Objects.equals(macAddress, that.macAddress) &&
            Objects.equals(position, that.position) &&
            Objects.equals(brand, that.brand) &&
            Objects.equals(memSize, that.memSize) &&
            Objects.equals(coreNo, that.coreNo) &&
            Objects.equals(osVersion, that.osVersion) &&
            Objects.equals(vendor, that.vendor) &&
            Objects.equals(purchaseDate, that.purchaseDate) &&
            Objects.equals(warrantyDate, that.warrantyDate) &&
            Objects.equals(componentsId, that.componentsId) &&
            Objects.equals(dataCenterId, that.dataCenterId) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(vendorContactId, that.vendorContactId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        hostname,
        ipAddress,
        macAddress,
        position,
        brand,
        memSize,
        coreNo,
        osVersion,
        vendor,
        purchaseDate,
        warrantyDate,
        componentsId,
        dataCenterId,
        ownerId,
        vendorContactId
        );
    }

    @Override
    public String toString() {
        return "ServerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (hostname != null ? "hostname=" + hostname + ", " : "") +
                (ipAddress != null ? "ipAddress=" + ipAddress + ", " : "") +
                (macAddress != null ? "macAddress=" + macAddress + ", " : "") +
                (position != null ? "position=" + position + ", " : "") +
                (brand != null ? "brand=" + brand + ", " : "") +
                (memSize != null ? "memSize=" + memSize + ", " : "") +
                (coreNo != null ? "coreNo=" + coreNo + ", " : "") +
                (osVersion != null ? "osVersion=" + osVersion + ", " : "") +
                (vendor != null ? "vendor=" + vendor + ", " : "") +
                (purchaseDate != null ? "purchaseDate=" + purchaseDate + ", " : "") +
                (warrantyDate != null ? "warrantyDate=" + warrantyDate + ", " : "") +
                (componentsId != null ? "componentsId=" + componentsId + ", " : "") +
                (dataCenterId != null ? "dataCenterId=" + dataCenterId + ", " : "") +
                (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
                (vendorContactId != null ? "vendorContactId=" + vendorContactId + ", " : "") +
            "}";
    }

}
