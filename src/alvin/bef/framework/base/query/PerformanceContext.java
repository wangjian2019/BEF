package alvin.bef.framework.base.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Tracking general page performance.
 * 
 * @author Alvin
 */
public class PerformanceContext {

	public static final String PAGE_OBJECTNAME = "PerformanceContext";
	private static final ThreadLocal<PerformanceContext> shContext = new ThreadLocal<PerformanceContext>();

	private HttpServletRequest request;
	private boolean enableConsole;
	private Date pageStart;
	private List<String> sqlEvents;
	private int sqlCount;
	private int sqlTime;

	public PerformanceContext(HttpServletRequest request, boolean enableConsole) {
		this.request = request;
		this.enableConsole = enableConsole;
		this.sqlEvents = new ArrayList<String>();
		this.pageStart = new Date();
	}

	public static void logSql(String evt) {
		PerformanceContext cons = getContext();
		if (cons != null && cons.enableConsole) {
			cons.sqlEvents.add(evt);
			cons.setSqlCount(cons.getSqlCount() + 1);
		}
	}

	public String getSqlLog() {
		StringBuilder str = new StringBuilder();
		int count = 1;
		for (String evt : this.sqlEvents) {
			str.append("<b>").append(count++).append("</b> ").append(evt).append("<br>\n");
		}
		return str.toString();
	}

	public static void logSqlTime(long time) {
		PerformanceContext cons = getContext();
		if (cons != null) {
			cons.sqlTime += time;
		}
	}

	public int getSqlTime() {
		return sqlTime;
	}

	public long getPageTime() {
		Date now = new Date();
		return now.getTime() - pageStart.getTime();
	}

	public void setSqlCount(int sqlCount) {
		this.sqlCount = sqlCount;
	}

	public int getSqlCount() {
		return sqlCount;
	}

	public static void init(HttpServletRequest request, boolean enableConsole) {
		PerformanceContext console = new PerformanceContext(request, enableConsole
				|| request.getParameter(PAGE_OBJECTNAME) != null);
		shContext.set(console);
		request.setAttribute(PerformanceContext.PAGE_OBJECTNAME, console);
	}

	public static void clear() {
		shContext.set(null);
	}

	public static PerformanceContext getContext() {
		return shContext.get();
	}

	public void setEnableConsole(boolean enableConsole) {
		this.enableConsole = enableConsole;
	}

	public boolean isEnableConsole() {
		return enableConsole;
	}

	/**
	 * useful for debugging locally.
	 */
	public static void debug() {
		PerformanceContext con = PerformanceContext.getContext();
		StringBuilder debug = new StringBuilder();
		debug.append("URL: ").append(con.request.getRequestURI());
		debug.append("\n\tPage Time: ").append(con.getPageTime());
		debug.append("\n\tSQL Time: ").append(con.getSqlTime());
		debug.append("\n\tSQL Queries: ").append(con.getSqlCount());
		debug.append("\n\tQueries: ").append(con.getSqlLog());
		// System.out.println(debug.toString());
	}

}
