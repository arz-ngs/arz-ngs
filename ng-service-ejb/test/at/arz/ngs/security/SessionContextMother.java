package at.arz.ngs.security;

import java.security.Identity;
import java.security.Principal;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.ejb.SessionContext;
import javax.ejb.TimerService;
import javax.transaction.UserTransaction;
import javax.xml.rpc.handler.MessageContext;

@SuppressWarnings("deprecation")
public class SessionContextMother {

	public static SessionContext authenticatedAs(final String userid) {
		return new SessionContext() {

			@Override
			public Identity getCallerIdentity() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Principal getCallerPrincipal() throws IllegalStateException {
				return new Principal() {

					@Override
					public String getName() {
						return userid;
					}

				};
			}

			@Override
			public Map<String, Object> getContextData() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public EJBHome getEJBHome() throws IllegalStateException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public EJBLocalHome getEJBLocalHome() throws IllegalStateException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Properties getEnvironment() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean getRollbackOnly() throws IllegalStateException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public TimerService getTimerService() throws IllegalStateException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public UserTransaction getUserTransaction() throws IllegalStateException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isCallerInRole(Identity arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isCallerInRole(String arg0) throws IllegalStateException {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Object lookup(String arg0) throws IllegalArgumentException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setRollbackOnly() throws IllegalStateException {
				// TODO Auto-generated method stub

			}

			@Override
			public <T> T getBusinessObject(Class<T> arg0) throws IllegalStateException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public EJBLocalObject getEJBLocalObject() throws IllegalStateException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public EJBObject getEJBObject() throws IllegalStateException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Class<?> getInvokedBusinessInterface() throws IllegalStateException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public MessageContext getMessageContext() throws IllegalStateException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean wasCancelCalled() throws IllegalStateException {
				// TODO Auto-generated method stub
				return false;
			}

		};

	}

}
