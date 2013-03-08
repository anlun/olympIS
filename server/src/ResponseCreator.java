import org.w3c.dom.Document;

/**
 * Abstract class that declarates the interface for response creator classes.
 * Has as input the DOM of XML that was sent by client.
 * As output has the XML string that needs to be sent to client.
 */
public abstract class ResponseCreator {
	/**
	 * This method need to be executed for creating response XML string.
	 * @return Response XML string for client.
	 */
	public abstract String createResponse();

	protected ResponseCreator(Document dom) {
		this.dom = dom;
	}

	protected Document dom;
}
