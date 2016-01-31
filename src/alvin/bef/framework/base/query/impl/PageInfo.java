package alvin.bef.framework.base.query.impl;

import alvin.bef.framework.base.query.IPage;
import alvin.bef.framework.base.query.NumberOverflowException;
/**
 * 
 * @author Alvin
 *
 */
public class PageInfo implements IPage {

   /**
    * @serial
    */
   private static final long serialVersionUID = -9153260940661933224L;
   protected int currentPage = 1;
   protected int recordsPerPage = RECORDS_PER_PAGE; // Set default page size to
   protected int totalRecords = 0;
   private boolean tooManySearchReturn = false;
   private long preId = -1;
   private long nextId = -1;
   private int currentRecord = 0;

   @Override
   public int getStartRowPosition() {
      if (recordsPerPage >= 0) {
         long pos = (long) recordsPerPage * (getCurrentPage() - 1);
         return longToInt(pos);
      } else {
         return 0;
      }
   }

   private static int longToInt(long number) {
      if (number > Integer.MAX_VALUE || number < Integer.MIN_VALUE) {
         throw new NumberOverflowException(number);
      }
      return (int) number;
   }
   
   @Override
   public int getEndRowPosition() {
      long max = (long) recordsPerPage * getCurrentPage();
      return longToInt(max);
   }

   @Override
   public boolean isLastPage() {
      return (getTotalPages() == 0 || currentPage == getTotalPages());
   }

   /**
    *
    * @param totalRecords
    */
   @Override
   public void setTotalRecords(int totalRecords) {
      this.totalRecords = totalRecords;
   }

   @Override
   public int getTotalRecords() {
      return this.totalRecords;
   }

   @Override
   public boolean isTooManySearchReturn() {
      return this.tooManySearchReturn;
   }

   @Override
   public void setTooManySearchReturn(boolean tooManySearchReturn) {
      this.tooManySearchReturn = tooManySearchReturn;
   }

   @Override
   public void setRecordsPerPage(int records) {
      if (records > 0 || records < 0) {
         this.recordsPerPage = records;
      }
   }

   @Override
   public int getRecordsPerPage() {
      if (recordsPerPage == 0) {
         recordsPerPage = RECORDS_PER_PAGE;
      }
      return recordsPerPage;
   }

   /**
    * @param currentPage The currentPage to set.
    */
   @Override
   public void setCurrentPage(int currentPage) {
      if (currentPage > 0) {
         this.currentPage = currentPage;
      } else {
         this.currentPage = 1;
      }
   }

   /**
    * @return Returns the currentPage.
    */
   @Override
   public int getCurrentPage() {
      return currentPage;
   }

   @Override
   public int getTotalPages() {
      if (recordsPerPage < 0) {
         return 1;
      } else {
         long total = (totalRecords + recordsPerPage - 1) / (long) recordsPerPage;
         return longToInt(total);
      }
   }

   @Override
   public long getPreId() {
      return preId;
   }

   @Override
   public void setPreId(long preId) {
      this.preId = preId;
   }

   @Override
   public long getNextId() {
      return this.nextId;
   }

   @Override
   public void setNextId(long nextId) {
      this.nextId = nextId;
   }

   @Override
   public int getCurrentRecord() {
      return this.currentRecord;
   }

   @Override
   public void setCurrentRecord(int currentRecord) {
      this.currentRecord = currentRecord;
   }
}
