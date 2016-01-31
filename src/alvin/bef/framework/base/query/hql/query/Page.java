package alvin.bef.framework.base.query.hql.query;

/**
 * Simple Page object for pagination query by:
 * 
 * Page p = new Page(5, 100); // page 5, 100 records per page
 * List<Book> books = aMgr.query().from(Book.class).list(p);
 * // Get page info:
 * System.out.println(p.getTotalRecords()); // -> 12345
 * System.out.println(p.getTotalPages()); // -> 124
 * 
 * @author Alvin
 */
public final class Page {

   public static final int RECORDS_PER_PAGE = 5;

   int currentPage = 1;
   int recordsPerPage = RECORDS_PER_PAGE;

   int totalRecords = -1;

   public Page(int currentPage) {
      this.currentPage = currentPage;
   }

   public Page(int currentPage, int recordsPerPage) {
      this.currentPage = currentPage;
      setRecordsPerPage(recordsPerPage);
   }

   public static Page firstPage() {
      return new Page(1);
   }

   public static Page firstPage(int recordsPerPage) {
      return new Page(1, recordsPerPage);
   }

   public static Page lastPage() {
      return new Page(-1);
   }

   public static Page lastPage(int recordsPerPage) {
      return new Page(-1, recordsPerPage);
   }

   public boolean isFirstPage() {
      return this.currentPage == 1;
   }

   public boolean isLastPage() {
      if (this.currentPage < 0) {
         return true;
      }
      if (this.totalRecords < 0) {
         throw new RuntimeException("Total records not set yet.");
      }
      return this.currentPage == getTotalPages();
   }

   void setTotalRecords(int totalRecords) {
      this.totalRecords = totalRecords;
      if (this.currentPage < 0 && this.totalRecords > 0) {
         this.currentPage = getTotalPages();
      }
   }

   public int getTotalRecords() {
      if (this.totalRecords < 0) {
         throw new RuntimeException("Total records not set yet.");
      }
      return this.totalRecords;
   }

   void setRecordsPerPage(int recordsPerPage) {
      if (recordsPerPage <= 0) {
         throw new IllegalArgumentException("recordsPerPage must greater than 0, but actual is " + recordsPerPage);
      }
      if (recordsPerPage > 5000) {
         throw new IllegalArgumentException("recordsPerPage must less than 5000, but actual is " + recordsPerPage);
      }
      this.recordsPerPage = recordsPerPage;
   }

   public int getRecordsPerPage() {
      return this.recordsPerPage;
   }

   public int getCurrentPage() {
      return this.currentPage;
   }

   public int getTotalPages() {
      if (this.totalRecords < 0) {
         throw new RuntimeException("Total records not set yet.");
      }
      return this.totalRecords / this.recordsPerPage
            + (this.totalRecords % this.recordsPerPage > 0 ? 1 : 0);
   }

}
