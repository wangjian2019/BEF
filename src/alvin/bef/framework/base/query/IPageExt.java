package alvin.bef.framework.base.query;

/**
 * Use this interface the framework will not do additional count(*) query,
 * instead it will just tell you if current page is last page. And
 * getTotalRecords(), getTotalPages() will return -1 when current page not last
 * page.
 */
public interface IPageExt extends IPage {

   int MAX_PAGE_SIZE = 5000;
   // set current page to LAST_PAGE to retrieve last page.
   int LAST_PAGE = -1;
   // total records or total pages is UNKNOWN_TOTAL means do not know them because not query count(*) yet.
   int UNKNOWN_TOTAL = -1;

   void setLastPage(boolean lastPage);
   boolean isGotoLastPageWhenOutOfPages();
}
