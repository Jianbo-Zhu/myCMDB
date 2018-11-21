package net.jianbo.cmdb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Version.
 */
@Entity
@Table(name = "version")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Version implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "version_string", nullable = false)
    private String versionString;

    @NotNull
    @Column(name = "deployed_by", nullable = false)
    private String deployedBy;

    @NotNull
    @Column(name = "git_commit", nullable = false)
    private String gitCommit;

    @NotNull
    @Column(name = "git_committer", nullable = false)
    private String gitCommitter;

    @NotNull
    @Column(name = "major_version", nullable = false)
    private Integer majorVersion;

    @NotNull
    @Column(name = "minor_version", nullable = false)
    private Integer minorVersion;

    @NotNull
    @Column(name = "hotfix_number", nullable = false)
    private Integer hotfixNumber;

    @NotNull
    @Column(name = "build_number", nullable = false)
    private Integer buildNumber;

    @Column(name = "created_time")
    private LocalDate createdTime;

    @Column(name = "udpated_time")
    private LocalDate udpatedTime;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("versions")
    private ComponentEntity comp;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersionString() {
        return versionString;
    }

    public Version versionString(String versionString) {
        this.versionString = versionString;
        return this;
    }

    public void setVersionString(String versionString) {
        this.versionString = versionString;
    }

    public String getDeployedBy() {
        return deployedBy;
    }

    public Version deployedBy(String deployedBy) {
        this.deployedBy = deployedBy;
        return this;
    }

    public void setDeployedBy(String deployedBy) {
        this.deployedBy = deployedBy;
    }

    public String getGitCommit() {
        return gitCommit;
    }

    public Version gitCommit(String gitCommit) {
        this.gitCommit = gitCommit;
        return this;
    }

    public void setGitCommit(String gitCommit) {
        this.gitCommit = gitCommit;
    }

    public String getGitCommitter() {
        return gitCommitter;
    }

    public Version gitCommitter(String gitCommitter) {
        this.gitCommitter = gitCommitter;
        return this;
    }

    public void setGitCommitter(String gitCommitter) {
        this.gitCommitter = gitCommitter;
    }

    public Integer getMajorVersion() {
        return majorVersion;
    }

    public Version majorVersion(Integer majorVersion) {
        this.majorVersion = majorVersion;
        return this;
    }

    public void setMajorVersion(Integer majorVersion) {
        this.majorVersion = majorVersion;
    }

    public Integer getMinorVersion() {
        return minorVersion;
    }

    public Version minorVersion(Integer minorVersion) {
        this.minorVersion = minorVersion;
        return this;
    }

    public void setMinorVersion(Integer minorVersion) {
        this.minorVersion = minorVersion;
    }

    public Integer getHotfixNumber() {
        return hotfixNumber;
    }

    public Version hotfixNumber(Integer hotfixNumber) {
        this.hotfixNumber = hotfixNumber;
        return this;
    }

    public void setHotfixNumber(Integer hotfixNumber) {
        this.hotfixNumber = hotfixNumber;
    }

    public Integer getBuildNumber() {
        return buildNumber;
    }

    public Version buildNumber(Integer buildNumber) {
        this.buildNumber = buildNumber;
        return this;
    }

    public void setBuildNumber(Integer buildNumber) {
        this.buildNumber = buildNumber;
    }

    public LocalDate getCreatedTime() {
        return createdTime;
    }

    public Version createdTime(LocalDate createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(LocalDate createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDate getUdpatedTime() {
        return udpatedTime;
    }

    public Version udpatedTime(LocalDate udpatedTime) {
        this.udpatedTime = udpatedTime;
        return this;
    }

    public void setUdpatedTime(LocalDate udpatedTime) {
        this.udpatedTime = udpatedTime;
    }

    public ComponentEntity getComp() {
        return comp;
    }

    public Version comp(ComponentEntity componentEntity) {
        this.comp = componentEntity;
        return this;
    }

    public void setComp(ComponentEntity componentEntity) {
        this.comp = componentEntity;
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
        Version version = (Version) o;
        if (version.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), version.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Version{" +
            "id=" + getId() +
            ", versionString='" + getVersionString() + "'" +
            ", deployedBy='" + getDeployedBy() + "'" +
            ", gitCommit='" + getGitCommit() + "'" +
            ", gitCommitter='" + getGitCommitter() + "'" +
            ", majorVersion=" + getMajorVersion() +
            ", minorVersion=" + getMinorVersion() +
            ", hotfixNumber=" + getHotfixNumber() +
            ", buildNumber=" + getBuildNumber() +
            ", createdTime='" + getCreatedTime() + "'" +
            ", udpatedTime='" + getUdpatedTime() + "'" +
            "}";
    }
}
