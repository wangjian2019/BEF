package alvin.bef.framework.base.model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 
 * @author Alvin
 *
 */
@MappedSuperclass
public abstract class ProvidedIdAbstractAuditable extends NoIdAbstractAuditable {

   @Id
   private String id;

   @Override
   public String getId() {
      return id;
   }
   
   @Deprecated
   public void setId(String id) {
      this.id = id;
   }
}
