package com.fossfloors.taskapp.backend.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fossfloors.taskapp.util.DateTimeUtil;

@MappedSuperclass
public abstract class AbstractEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long   id;

  @NotNull
  private long   dateCreated;

  @NotNull
  private long   dateModified;

  @NotNull
  @NotEmpty
  private String createdBy;

  @NotNull
  @NotEmpty
  private String modifiedBy;

  protected AbstractEntity() {
    // TODO find way to get logged in user
    createdBy = "user";
    modifiedBy = "user";

    dateCreated = System.currentTimeMillis();
    dateModified = dateCreated;
  }

  public Long getId() {
    return id;
  }

  public long getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(long dateCreated) {
    this.dateCreated = dateCreated;
  }

  public long getDateModified() {
    return dateModified;
  }

  public void setDateModified(long dateModified) {
    this.dateModified = dateModified;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public boolean isPersisted() {
    return id != null;
  }

  @Override
  public String toString() {
    return "AbstractEntity [id=" + id + ", dateCreated=" + DateTimeUtil.format(dateCreated)
        + ", dateModified=" + DateTimeUtil.format(dateModified) + ", createdBy=" + createdBy
        + ", modifiedBy=" + modifiedBy + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
    result = prime * result + (int) (dateCreated ^ (dateCreated >>> 32));
    result = prime * result + (int) (dateModified ^ (dateModified >>> 32));
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AbstractEntity other = (AbstractEntity) obj;
    if (createdBy == null) {
      if (other.createdBy != null)
        return false;
    } else if (!createdBy.equals(other.createdBy))
      return false;
    if (dateCreated != other.dateCreated)
      return false;
    if (dateModified != other.dateModified)
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (modifiedBy == null) {
      if (other.modifiedBy != null)
        return false;
    } else if (!modifiedBy.equals(other.modifiedBy))
      return false;
    return true;
  }

}