import beans.ApplicationConstrain;
import beans.Sex;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import utils.Utils;
import java.util.Vector;

/**
 * Extends the {@link ResponseCreator} for application request task task.
 * @author Podkopaev Anton
 */
public class ApplicationConstrainResponseCreator extends ResponseCreator {
	public ApplicationConstrainResponseCreator(Document dom) {
		this.dom = dom;
	}

	public String createResponse() {
		Element root     = dom.getDocumentElement();
		String  country  = root.getAttribute("country");
		String  login    = root.getAttribute("login");
		String  password = root.getAttribute("password");

		ApplicationConstrain applicationConstrain = getCountryApplicationConstrain(country, login, password);
		String response = Utils.beanToString(applicationConstrain);
		System.out.println(response);
		return response;
	}

	//TODO: Need to be implemented with data from database.
	private ApplicationConstrain getCountryApplicationConstrain(String country, String login, String password) {
		Vector<ApplicationConstrain.SportConstrain> vec = new Vector<ApplicationConstrain.SportConstrain>();
		vec.add(new ApplicationConstrain.SportConstrain("Baseball",   10, new Sex(Sex.male)));
		vec.add(new ApplicationConstrain.SportConstrain("Basketball", 15, new Sex(Sex.female)));
		vec.add(new ApplicationConstrain.SportConstrain("Swim",       25, new Sex(Sex.undefined)));

		return new ApplicationConstrain(vec);
	}

	private Document dom;
}
