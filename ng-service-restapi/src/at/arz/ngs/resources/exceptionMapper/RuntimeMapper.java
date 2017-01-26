package at.arz.ngs.resources.exceptionMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import at.arz.ngs.resources.NgsApiError;

@Provider
public class RuntimeMapper
		implements ExceptionMapper<RuntimeException> {

	private static final Logger log = Logger.getLogger(RuntimeMapper.class.getName());

	@Override
	public Response toResponse(RuntimeException e) {
		log.log(Level.WARNING, "unexpected exception during rest call:" + e.getMessage(), e);
		NgsApiError error = new NgsApiError(e.getClass().getSimpleName(), e.toString());
		return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
				//						.type(MediaType.APPLICATION_XML)
				.entity(error).build();
	}
}
