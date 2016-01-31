package alvin.bef.framework.base.session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import alvin.bef.framework.base.model.ProvidedIdAbstractAuditable;

/**
 * @author Alvin
 */

@Entity
@Table(name = "zb_user_profile")
/*@ApiObject(minVersion = ApiVersion.VERSION_29_0, operations = {
      ApiOperation.READ, ApiOperation.UPDATE }*/
public class UserProfile extends ProvidedIdAbstractAuditable {

   private static final long serialVersionUID = 9098346929636212346L;

   //@ApiField
   private String firstName;

   //@ApiField
   private String lastName;

   //@ApiField(name = "Email")
   private String workEmail;

   //@ApiField(operations = ApiOperation.READ)
   private String username;

   @Column(name = "two_fa")
   private Boolean twoFaEnabled = false;

   @Column(name = "two_fa_secret")
   private String twoFaSecret;

   @Column(name = "two_fa_option")
   private String twoFaOption;

   @Column(name = "country_code")
   private String countryCode;

   @Column(name = "phone_number")
   private String phoneNumber;

   @Column(name = "two_fa_token")
   private Integer twoFaToken;

   @Column(name = "two_fa_setup")
   private Boolean twoFaSetup;

   @Column(name = "web_logout")
   private Boolean webLogout = false;


   /**
    * @return the username
    */
   public String getUsername() {
      return username;
   }

   /**
    * @param username
    *           the username to set
    */
   public void setUsername(String username) {
      this.username = username;
   }

   @Type(type = "ApplicationPreferences")
   @Columns(columns = {
         @Column(name = "pref1"),
         @Column(name = "pref2"),
         @Column(name = "pref3")
   })

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getWorkEmail() {
      return workEmail;
   }

   public void setWorkEmail(String workEmail) {
      this.workEmail = workEmail;
   }

   public Boolean getTwoFaEnabled() {
      return twoFaEnabled;
   }

   public String getTwoFaSecret() {
      return twoFaSecret;
   }

   public String getTwoFaOption() {
      return twoFaOption;
   }

   public String getCountryCode() {
      return countryCode;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public void setTwoFaEnabled(Boolean twoFaEnabled) {
      this.twoFaEnabled = twoFaEnabled;
   }

   public void setTwoFaSecret(String twoFaSecret) {
      this.twoFaSecret = twoFaSecret;
   }

   public void setTwoFaOption(String twoFaOption) {
      this.twoFaOption = twoFaOption;
   }

   public void setCountryCode(String countryCode) {
      this.countryCode = countryCode;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public Integer getTwoFaToken() {
      return twoFaToken;
   }

   public void setTwoFaToken(Integer twoFaToken) {
      this.twoFaToken = twoFaToken;
   }

   public Boolean getTwoFaSetup() {
      return twoFaSetup;
   }

   public void setTwoFaSetup(Boolean twoFaSetup) {
      this.twoFaSetup = twoFaSetup;
   }

   public Boolean isWebLogout() {
      return webLogout;
   }

   public void setWebLogout(Boolean webLogout) {
      this.webLogout = webLogout;
   }

}
