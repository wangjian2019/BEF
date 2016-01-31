package alvin.bef.framework.base.model;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;


/**
 * 
 * @author Alvin
 *
 */
public interface Auditable extends Entity {

   static final String FIELD_CREATED_BY = "createdBy";
   static final String FIELD_CREATED_ON = "createdOn";
   static final String FIELD_UPDATED_BY = "updatedBy";
   static final String FIELD_UPDATED_ON = "updatedOn";

   static final Collection<String> AUDIT_FIELDS = Arrays.asList(new String[] {
         FIELD_CREATED_BY,
         FIELD_CREATED_ON,
         FIELD_UPDATED_BY,
         FIELD_UPDATED_ON
      });

   String getCreatedBy();
   Timestamp getCreatedOn();

   String getUpdatedBy();
   Timestamp getUpdatedOn();

   void setAuditFieldsOnCreate(String userId);
   void setAuditFieldsOnUpdate(String userId);
   
   Integer getVersionOptimizedLock();
}
