package alvin.bef.framework.base.dao.hibernate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * 
 * @author Alvin
 *
 */
class ReadOnlySessionFactory {
	
	private SessionFactory sessionFactory = null;
	private DataSource dataSource = null;
	
	
	

    void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	 void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	 
	
	private Connection getConnection(){
    	Connection con = null;
		try {
			con = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			closeConnection(con);
			throw new RuntimeException(e);
		} catch (Exception e) {
			closeConnection(con);
			throw new RuntimeException(e);
		}
    	return con;
    }
	
	
	/**
	 * session is set to readonly
	 * @return
	 */
    Session createSession() {
		Connection con = getConnection();
		//return createSessionProxy(sessionFactory.openSession(con),con);
		// Hibernate4 , add by Alvin
		return createSessionProxy(sessionFactory.openSession(),con);
	}
    
    @SuppressWarnings("rawtypes")
	private Session createSessionProxy(Session session,Connection con) {
		Class[] sessionIfcs = null;
		if (session instanceof SessionImplementor) {
			sessionIfcs = new Class[] {Session.class, SessionImplementor.class};
		}
		else {
			sessionIfcs = new Class[] {Session.class};
		}
		return (Session) Proxy.newProxyInstance(
				getClass().getClassLoader(), sessionIfcs,
				new WriteMethodSuppressingInvocationHandler(session,con));
	}
    private void closeConnection(Connection con) {
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try{
			if (con != null)
				con.close();
			}catch(SQLException e1){
				throw new RuntimeException(e1);
			}
			throw new RuntimeException(e);
		}
	}

	
    
    
    /**
	 * Invocation handler that suppresses write method calls on Hibernate Sessions.
	 * Also prepares returned Query and Criteria objects.
	 * 
	 */
	private class WriteMethodSuppressingInvocationHandler implements InvocationHandler {

		private final Session target;
		private final Connection con;

		public WriteMethodSuppressingInvocationHandler(Session target, Connection con) {
			this.target = target;
			this.con = con;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			// Invocation on Session interface coming in...

			if (method.getName().equals("equals")) {
				// Only consider equal when proxies are identical.
				return (proxy == args[0] ? Boolean.TRUE : Boolean.FALSE);
			}else if (method.getName().equals("hashCode")) {
				// Use hashCode of Session proxy.
				return new Integer(hashCode());
			}else if(method.getName().equals("getSessionFactory")){
				throw new RuntimeException("You can't get session factory, connection is replaced");
			}else if (method.getName().equals("delete") || method.getName().equals("save")
					|| method.getName().equals("update") || method.getName().equals("replicate")
					|| method.getName().equals("persist") ||  method.getName().equals("merge")
					|| method.getName().equals("saveOrUpdate")) {
				// Handle write method: suppress, not valid.
				throw new RuntimeException("Don't support writable method on bibernate session for Read Only database");
			}

			try {
				Object retVal = method.invoke(this.target, args);
				return retVal;
			}
			catch (InvocationTargetException ex) {
				throw ex.getTargetException();
			}finally{
				if(method.getName().equals("close"))
					closeConnection(con);
			}
		}
	}

}
