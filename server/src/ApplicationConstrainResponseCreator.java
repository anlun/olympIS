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
		super(dom);
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
		vec.add(new ApplicationConstrain.SportConstrain("Baseball",   10, Sex.Male));
		vec.add(new ApplicationConstrain.SportConstrain("Basketball", 15, Sex.Female));
		vec.add(new ApplicationConstrain.SportConstrain("Swim",       25, Sex.Undefined));

		return new ApplicationConstrain(vec);
	}
}
