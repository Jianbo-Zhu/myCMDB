package net.jianbo.cmdb.service.dto;

import java.io.Serializable;
import java.util.Objects;
import net.jianbo.cmdb.domain.enumeration.ComponentType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the ComponentEntity entity. This class is used in ComponentEntityResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /component-entities?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ComponentEntityCriteria implements Serializable {
    /**
     * Class for filtering ComponentType
     */
    public static class ComponentTypeFilter extends Filter<ComponentType> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter comName;

    private ComponentTypeFilter comType;

    private LongFilter versionsId;

    private LongFilter appId;

    private LongFilter serverId;

    public ComponentEntityCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getComName() {
        return comName;
    }

    public void setComName(StringFilter comName) {
        this.comName = comName;
    }

    public ComponentTypeFilter getComType() {
        return comType;
    }

    public void setComType(ComponentTypeFilter comType) {
        this.comType = comType;
    }

    public LongFilter getVersionsId() {
        return versionsId;
    }

    public void setVersionsId(LongFilter versionsId) {
        this.versionsId = versionsId;
    }

    public LongFilter getAppId() {
        return appId;
    }

    public void setAppId(LongFilter appId) {
        this.appId = appId;
    }

    public LongFilter getServerId() {
        return serverId;
    }

    public void setServerId(LongFilter serverId) {
        this.serverId = serverId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ComponentEntityCriteria that = (ComponentEntityCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(comName, that.comName) &&
            Objects.equals(comType, that.comType) &&
            Objects.equals(versionsId, that.versionsId) &&
            Objects.equals(appId, that.appId) &&
            Objects.equals(serverId, that.serverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        comName,
        comType,
        versionsId,
        appId,
        serverId
        );
    }

    @Override
    public String toString() {
        return "ComponentEntityCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (comName != null ? "comName=" + comName + ", " : "") +
                (comType != null ? "comType=" + comType + ", " : "") +
                (versionsId != null ? "versionsId=" + versionsId + ", " : "") +
                (appId != null ? "appId=" + appId + ", " : "") +
                (serverId != null ? "serverId=" + serverId + ", " : "") +
            "}";
    }

}
