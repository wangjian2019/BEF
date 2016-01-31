package alvin.bef.framework.base.dao.hibernate;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * this is a datasource proxy, from ReadOnly DataSource, we will get a readonly connection
 * @author Alvin
 *
 */
class ReadOnlyConnectionDataSource implements DataSource{
	private DataSource dataSource = null;
	private boolean readOnly = true;
	
	

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return dataSource.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		throw new RuntimeException("Not support this method");
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		throw new RuntimeException("Not support this method");
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return dataSource.getLoginTimeout();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return dataSource.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return dataSource.isWrapperFor(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		Connection con = dataSource.getConnection();
		con.setReadOnly(readOnly);
		return con;
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		Connection con = dataSource.getConnection(username,password);
		con.setReadOnly(readOnly);
		return con;
	}

   @Override
   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
      return dataSource.getParentLogger();
   }

}
