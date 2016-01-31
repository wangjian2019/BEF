package alvin.bef.framework.base.query;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import alvin.bef.framework.base.query.impl.PageInfo;
import alvin.bef.framework.base.query.impl.Sort;
import alvin.bef.framework.base.query.util.FilterUtil;

/**
 * 
 * @author Alvin
 *
 */
public class FSP implements Serializable {

    /**
     * @serial
     */
    private static final long serialVersionUID = -5365072213157772600L;

    protected static final Log log = LogFactory.getLog(FSP.class);

    private IFilter defaultFilter = null;

    private IFilter userFilter;

    private ISort sort;

    private IPage page;

    /**
     * @return Returns the user filter.
     */
    public IFilter getFilter() {
        return FilterUtil.and(defaultFilter, getUserFilter());
    }

    /**
     * @param filter
     *            The user filter to set.
     */
    public void setUserFilter(IFilter filter) {
        this.userFilter = filter;
    }

    /**
     * @return Returns the user filter.
     */
    public IFilter getUserFilter() {
        return userFilter;
    }

    /**
     * @param sort
     *            The sort to set.
     */
    public void setSort(ISort sort) {
        this.sort = sort;
    }

    /**
     * @return Returns the sort.
     */
    public ISort getSort() {
        return sort;
    }

    /**
     * @param page
     *            The page to set.
     */
    public void setPage(IPage page) {
        this.page = page;
    }

    /**
     * @return Returns the page.
     */
    public IPage getPage() {
        return page;
    }

    public IFilter getDefaultFilter() {
        return defaultFilter;
    }

    public void setDefaultFilter(IFilter defaultFilter) {
        this.defaultFilter = defaultFilter;
    }

    public static FSP build(IFilter filter, String sortBy, int direction, int currentPage, int recordPerPage){
       FSP fsp = new FSP();
       fsp.setUserFilter(filter);
       
       ISort sort = new Sort(sortBy, direction);
       fsp.setSort(sort);
       
       IPage page = new PageInfo();
       page.setCurrentPage(currentPage);
       page.setRecordsPerPage(recordPerPage);
       fsp.setPage(page);
       
       return fsp;
    }
    
    public static FSP build(IFilter filter, int currentPage, int recordPerPage){
       FSP fsp = new FSP();
       fsp.setUserFilter(filter);
       
       IPage page = new PageInfo();
       page.setCurrentPage(currentPage);
       page.setRecordsPerPage(recordPerPage);
       fsp.setPage(page);
       
       return fsp;
    }


}
