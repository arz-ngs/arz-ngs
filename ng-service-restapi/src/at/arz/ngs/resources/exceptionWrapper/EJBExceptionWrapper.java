package at.arz.ngs.resources.exceptionWrapper;

import javax.ejb.EJBException;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import at.arz.ngs.resources.NgsApiError;

@Provider
public class EJBExceptionWrapper
		implements ExceptionMapper<EJBException> {

	@Override
	public Response toResponse(EJBException e) {
		NgsApiError error = mapToApiError(e);
		return Response	.status(Response.Status.CONFLICT)
						.type(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_XML)
						.entity(error)
						.build();
	}

	private NgsApiError mapToApiError(EJBException e) {
		Throwable cause = e.getCause();
		if (cause instanceof PersistenceException) {
			return new NgsApiError(PersistenceException.class.getSimpleName(), cause.getMessage(), true);
		}
		return new NgsApiError(e.getClass().getSimpleName(), cause.getMessage(), false);
	}

}
