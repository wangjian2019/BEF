package alvin.bef.framework.base.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @author Alvin
 *
 */
@MappedSuperclass
public abstract class GeneratedIdAbstractAuditable extends NoIdAbstractAuditable implements GeneratedIdEntity{

   @Id
   @AccessType(value = "property")
   @GeneratedValue(generator = "system-uuid")
   @GenericGenerator(name = "system-uuid", strategy = "uuid")
   private String id;

   @Override
   public String getId() {
      return id;
   }
   
   @Deprecated
   public void setId(String id) {
      this.id = id;
   }
   
   public void clearId() {
      this.id = null;
   }
}
