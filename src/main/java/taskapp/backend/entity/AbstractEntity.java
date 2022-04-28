package taskapp.backend.entity;

import java.time.LocalDateTime;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public abstract class AbstractEntity {

  private static final String user = System.getProperty("user.name");

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long                id;

  @CreationTimestamp
  private LocalDateTime       dateCreated;

  @UpdateTimestamp
  private LocalDateTime       dateModified;

  @NotNull
  @NotEmpty
  private String              createdBy;

  @NotNull
  @NotEmpty
  private String              modifiedBy;

  protected AbstractEntity() {
    // TODO find way to get logged in user
    createdBy = user;
    modifiedBy = user;
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(LocalDateTime dateCreated) {
    this.dateCreated = dateCreated;
  }

  public LocalDateTime getDateModified() {
    return dateModified;
  }

  public void setDateModified(LocalDateTime dateModified) {
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
    return "AbstractEntity [id=" + id + ", dateCreated=" + dateCreated + ", dateModified="
        + dateModified + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    // result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
    // result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
    // result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
    // result = prime * result + ((dateModified == null) ? 0 : dateModified.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    if (dateCreated == null) {
      if (other.dateCreated != null)
        return false;
    } else if (!dateCreated.equals(other.dateCreated))
      return false;
    if (dateModified == null) {
      if (other.dateModified != null)
        return false;
    } else if (!dateModified.equals(other.dateModified))
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