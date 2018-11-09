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
 * Criteria class for the Application entity. This class is used in ApplicationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /applications?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ApplicationCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter appName;

    private StringFilter environment;

    private LongFilter componentsId;

    public ApplicationCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAppName() {
        return appName;
    }

    public void setAppName(StringFilter appName) {
        this.appName = appName;
    }

    public StringFilter getEnvironment() {
        return environment;
    }

    public void setEnvironment(StringFilter environment) {
        this.environment = environment;
    }

    public LongFilter getComponentsId() {
        return componentsId;
    }

    public void setComponentsId(LongFilter componentsId) {
        this.componentsId = componentsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ApplicationCriteria that = (ApplicationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(appName, that.appName) &&
            Objects.equals(environment, that.environment) &&
            Objects.equals(componentsId, that.componentsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        appName,
        environment,
        componentsId
        );
    }

    @Override
    public String toString() {
        return "ApplicationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (appName != null ? "appName=" + appName + ", " : "") +
                (environment != null ? "environment=" + environment + ", " : "") +
                (componentsId != null ? "componentsId=" + componentsId + ", " : "") +
            "}";
    }

}
