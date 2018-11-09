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

/**
 * Criteria class for the DataCenter entity. This class is used in DataCenterResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /data-centers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DataCenterCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter dcName;

    private StringFilter address;

    private LongFilter contactorId;

    public DataCenterCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDcName() {
        return dcName;
    }

    public void setDcName(StringFilter dcName) {
        this.dcName = dcName;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public LongFilter getContactorId() {
        return contactorId;
    }

    public void setContactorId(LongFilter contactorId) {
        this.contactorId = contactorId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DataCenterCriteria that = (DataCenterCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dcName, that.dcName) &&
            Objects.equals(address, that.address) &&
            Objects.equals(contactorId, that.contactorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dcName,
        address,
        contactorId
        );
    }

    @Override
    public String toString() {
        return "DataCenterCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dcName != null ? "dcName=" + dcName + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (contactorId != null ? "contactorId=" + contactorId + ", " : "") +
            "}";
    }

}
