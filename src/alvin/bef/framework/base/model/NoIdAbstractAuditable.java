package alvin.bef.framework.base.model;

import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.Type;

/**
 * 
 * @author Alvin
 *
 */
@MappedSuperclass
public abstract class NoIdAbstractAuditable implements Auditable {

   @Version
   private Integer versionOptimizedLock;

   //@ApiField(name="CreatedById", minVersion=ApiVersion.VERSION_20_0, operations=ApiOperation.READ)
   @Basic(optional=false)
   private String createdBy;

   //@ApiField(name="UpdatedById", minVersion=ApiVersion.VERSION_20_0, operations=ApiOperation.READ)
   private String updatedBy;

   //@ApiField(name="CreatedDate", minVersion=ApiVersion.VERSION_20_0, operations=ApiOperation.READ)
   @Basic(optional=false)
   @Type(type="timestamp")
   private Timestamp createdOn;

  //@ApiField(name="UpdatedDate", minVersion=ApiVersion.VERSION_20_0, operations=ApiOperation.READ)
   @Type(type="timestamp")
   private Timestamp updatedOn;

   @Override
   public Integer getVersionOptimizedLock() {
      return versionOptimizedLock;
   }

   protected void setVersionOptimizedLock(Integer versionOptimizedLock) {
      this.versionOptimizedLock = versionOptimizedLock;
   }

   @Override
   public Object clone() { // is this ever even used?
      try {
         return super.clone();
      }
      catch (CloneNotSupportedException e) {
         throw new RuntimeException(e);
      }
   }

   @Override
   public String getCreatedBy() {
      return createdBy;
   }

   public void setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
   }

   @Override
   public String getUpdatedBy() {
      return updatedBy;
   }

   public void setUpdatedBy(String updatedBy) {
      this.updatedBy = updatedBy;
   }

   @Override
   public Timestamp getCreatedOn() {
      return createdOn;
   }

   public void setCreatedOn(Timestamp createdOn) {
      this.createdOn = createdOn;
   }

   @Override
   public Timestamp getUpdatedOn() {
      return updatedOn;
   }

   public void setUpdatedOn(Timestamp updatedOn) {
      this.updatedOn = updatedOn;
   }

   @Override
   public void setAuditFieldsOnCreate(String userId) {
      Timestamp now = new Timestamp(System.currentTimeMillis());

      setCreatedBy(userId);
      setCreatedOn(now);

      setUpdatedBy(userId);
      setUpdatedOn(now);
   }

   @Override
   public void setAuditFieldsOnUpdate(String userId) {
      Timestamp now = new Timestamp(System.currentTimeMillis());

      setUpdatedBy(userId);
      setUpdatedOn(now);
   }

}
