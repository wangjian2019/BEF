package alvin.bef.framework.base.query.impl;

import alvin.bef.framework.base.query.ILimit;
import alvin.bef.framework.base.query.IPage;
/**
 * 
 * @author Alvin
 *
 */
public class LimitExt implements IPage, ILimit {

    private static final long serialVersionUID = -5538706380560446867L;

    int first;
    int max;

    public LimitExt(int max) {
        this(0, max);
    }

    public LimitExt(int first, int max) {
        this.first = first;
        this.max = max;
    }

    @Override
    public int getFirstResult() {
        return first;
    }

    @Override
    public int getMaxResults() {
        return max;
    }

    @Override
    public int getStartRowPosition() {
        return 0;
    }

    @Override
    public int getEndRowPosition() {
        return 0;
    }

    @Override
    public boolean isLastPage() {
        return true;
    }

    @Override
    public void setTotalRecords(int totalRecords) {
    }

    @Override
    public int getTotalRecords() {
        return 0;
    }

    @Override
    public boolean isTooManySearchReturn() {
        return false;
    }

    @Override
    public void setTooManySearchReturn(boolean tooManySearchReturn) {
    }

    @Override
    public void setRecordsPerPage(int recordsPerPage) {
    }

    @Override
    public int getRecordsPerPage() {
        return 0;
    }

    @Override
    public void setCurrentPage(int currentPage) {
    }

    @Override
    public int getCurrentPage() {
        return 1;
    }

    @Override
    public int getTotalPages() {
        return 1;
    }

    @Override
    public long getPreId() {
        return 0;
    }

    @Override
    public void setPreId(long preId) {
    }

    @Override
    public long getNextId() {
        return 0;
    }

    @Override
    public void setNextId(long nextId) {
    }

    @Override
    public int getCurrentRecord() {
        return 0;
    }

    @Override
    public void setCurrentRecord(int currentRecord) {
    }

}
