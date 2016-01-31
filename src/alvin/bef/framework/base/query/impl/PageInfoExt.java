package alvin.bef.framework.base.query.impl;

import alvin.bef.framework.base.query.IPageExt;

/**
 * 
 * @author Alvin
 *
 */
public class PageInfoExt extends PageInfo implements IPageExt {

   private static final long serialVersionUID = 1L;
   private boolean lastPage = true;
   private boolean gotoLastPageWhenOutOfPages = false;

   public PageInfoExt() {
      this.totalRecords = UNKNOWN_TOTAL;
   }

   public PageInfoExt(int pageNo, int pageSize) {
      this.totalRecords = UNKNOWN_TOTAL;
      currentPage(pageNo);
      recordsPerPage(pageSize);
   }

   public PageInfoExt(int pageNo, int pageSize, boolean gotoLastPageWhenOutOfPages) {
      this(pageNo, pageSize);
      this.gotoLastPageWhenOutOfPages = gotoLastPageWhenOutOfPages;
   }

   private void currentPage(int currentPage) {
      if (currentPage > 0) {
         this.currentPage = currentPage;// < MAX_PAGE_NO ? currentPage : MAX_PAGE_NO;
      } else {
         this.currentPage = currentPage == LAST_PAGE ? LAST_PAGE : 1;
      }
   }

   private void recordsPerPage(int records) {
      if (records > 0) {
         this.recordsPerPage = records < MAX_PAGE_SIZE ? records : MAX_PAGE_SIZE;
      } else {
         this.recordsPerPage = RECORDS_PER_PAGE;
      }
   }

   /**
    * Is current page the last page?
    *
    * @return true when current page is the last page or pageNo > lastPageNo,
    * otherwise return false.
    */
   @Override
   public boolean isLastPage() {
      if (getTotalRecords() == LAST_PAGE) {
         if (lastPage) {
            return true;
         } else {
            return false;
         }
      } else {
         return super.isLastPage();
      }
   }

   /**
    * Set current page as the last page, only effect when total records not set
    * (-1, unknown). Set by framework.
    *
    * @param lastPage If true and total records do not known then set current
    * page as last page.
    */
   @Override
   public void setLastPage(boolean lastPage) {
      this.lastPage = lastPage;
   }

   /**
    * Get total pages.
    *
    * @return
    */
   @Override
   public int getTotalPages() {
      if (getTotalRecords() == UNKNOWN_TOTAL) {
         return UNKNOWN_TOTAL;
      } else {
         return super.getTotalPages();
      }
   }

   //// fix super class?
   @Override
   public int getTotalRecords() {
      return totalRecords;
   }

   /**
    * Set total records available, set by framework.
    *
    * @param totalRecords Must great or equals than -1, -1 means unknown,
    * otherwise set to -1 automatically.
    */
   @Override
   public void setTotalRecords(int totalRecords) {
      if (totalRecords < UNKNOWN_TOTAL) {
         totalRecords = UNKNOWN_TOTAL;
      }
      super.setTotalRecords(totalRecords);
   }

   /**
    * Set page size.
    *
    * @param records Must great than 0 and not great than MAX_PAGE_SIZE. Less
    * than 1 will set to RECORDS_PER_PAGE, great than MAX_PAGE_SIZE will set to
    * MAX_PAGE_SIZE.
    */
   @Override
   public void setRecordsPerPage(int records) {
      recordsPerPage(records);
   }

   @Override
   public int getCurrentPage() {
      return currentPage;
   }

   /**
    * Set current page No.
    *
    * @param currentPage Must great than 0 or equals to -1 (last page),
    * otherwise set to 1.
    */
   @Override
   public void setCurrentPage(int currentPage) {
      this.currentPage(currentPage);
   }

   @Override
   public boolean isGotoLastPageWhenOutOfPages() {
      return gotoLastPageWhenOutOfPages;
   }

   public void setGotoLastPageWhenOutOfPages(boolean trueOrFalse) {
      this.gotoLastPageWhenOutOfPages = trueOrFalse;
   }
}
