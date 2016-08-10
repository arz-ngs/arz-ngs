package at.arz.ngs.resources.exceptionWrapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import at.arz.ngs.api.exception.WrongParam;
import at.arz.ngs.resources.NgsApiError;

@Provider
public class WrongParamWrapper
		implements ExceptionMapper<WrongParam> {

	@Override
	public Response toResponse(WrongParam e) {
		NgsApiError error = new NgsApiError(e.getClass().getSimpleName(), e.getMessage());
		return Response	.status(Response.Status.CONFLICT)
						.type(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_XML)
						.entity(error)
						.build();
	}

}
