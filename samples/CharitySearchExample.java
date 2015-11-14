import com.virginmoneygiving.connect.sdk.v1.ApiConsumerDetails;
import com.virginmoneygiving.connect.sdk.v1.ConnectCharityAPI;
import com.virginmoneygiving.connect.sdk.v1.beans.charity.request.CharitySearchCriteria;
import com.virginmoneygiving.connect.sdk.v1.beans.charity.response.Charity;
import com.virginmoneygiving.connect.sdk.v1.exceptions.VMGSDKException;
import com.virginmoneygiving.connect.sdk.v1.messaging.AbstractAPIResponse;
import com.virginmoneygiving.connect.sdk.v1.messaging.charity.response.SearchCharitiesAPIResponse;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.URL;
import java.util.List;


/**
 * Example runnable code which calls the Charity Search API
 */
public class CharitySearchExample {

    public static final void main(String[] args) throws SAXException, ParserConfigurationException, TransformerException, IOException, VMGSDKException, JAXBException {

        if (args == null || args.length < 3) {
            System.out.println("ERROR - no arguments provided");
            System.out.println("Usage: argument 0 is your Virgin Money Giving API Key");
            System.out.println("       argument 1 is the base url for VMG Connect (e.g https://sandbox.api.virginoneygiving.com)");
            System.out.println("       argument 2 is the charity name to search for - required");
            System.out.println("e.g java CharitySearchExample {your_api_key} https://sandbox.api.virginoneygiving.com Cancer ");
            System.out.println("\nIf you are testing this through a proxy, then these are the additional optional arguments:");
            System.out.println("       argument 3 is the Proxy IP Address or name (String)");
            System.out.println("       argument 4 is the Proxy port (int number)");
            System.out.println("       argument 5 is the username (String)");
            System.out.println("       argument 6 is the password (String)");
            System.out.println("e.g java CharitySearchExample YourApiKey  https://sandbox.api.virginoneygiving.com Cancer 127.0.0.1  80  username  password");

            return;
        }


        String apiKey = args[0];
        String baseUrlString = args[1];
        String searchCharityName = args[2];

        String proxyHost = null;
        int proxyPort = -1;
        String proxyUsername = null;
        String proxyPassword = null;


        if (!args[1].startsWith("http://") && !args[1].startsWith("https://")) {
            System.out.println("Please prefix your base url with http:// or https://");
            return;
        }

        if (args.length >= 5) {
            proxyHost = args[3];
            proxyPort = Integer.parseInt(args[4]);
        }

        if (args.length > 5) {
            proxyUsername = args[5];
            proxyPassword = args[6];
        }


        System.out.println("Calling charitys/v1/search with the following parameters");
        System.out.println("         API key=" + apiKey);
        System.out.println("        base url=" + baseUrlString);
        System.out.println(" search  surname=" + searchCharityName);
        System.out.println("           Proxy=" + proxyHost);
        System.out.println("      Proxy port=" + proxyPort);

        // Convert the URL supplied into a Java URL
        URL baseUrl = new URL(baseUrlString);

        // Create an ApiConsumerDetails object to hold the api user's (i.e you) API key details
        ApiConsumerDetails apiConsumerDetails = new ApiConsumerDetails();
        apiConsumerDetails.setApikey(apiKey);
        apiConsumerDetails.setProxyHost(proxyHost);

        if (proxyPort >= 0) {
            apiConsumerDetails.setProxyPort(proxyPort);
        }
        apiConsumerDetails.setProxyUsername(proxyUsername);
        apiConsumerDetails.setProxyPassword(proxyPassword);

        // Create an API object
        ConnectCharityAPI vmgCharityAPI = new ConnectCharityAPI(baseUrl, apiConsumerDetails);

        // Create a search criteria object for the API
        CharitySearchCriteria charitySearchCriteria = new CharitySearchCriteria();
        charitySearchCriteria.setSearchCharityName(searchCharityName);

        // Call the API
        SearchCharitiesAPIResponse apiResponse = vmgCharityAPI.searchCharities(charitySearchCriteria);

        System.out.println("API Response : " + apiResponse.toString());

        // Check for errors
        List<AbstractAPIResponse.ConnectError> connectErrors = apiResponse.getErrors();
        if (connectErrors != null) {
            System.out.println("response.getErrors() not null, size: " + connectErrors.size());
            for (AbstractAPIResponse.ConnectError connectError : connectErrors) {
                System.out.println("errorCode: '" + connectError.getErrorCode() + "', message: '" + connectError.getErrorMessage());
                List<String> messageDetails = connectError.getMessageDetails();
                if (messageDetails != null && messageDetails.size() > 0) {
                    System.out.println("messageDetails size=" + messageDetails.size());
                    for (int i = 0; i < messageDetails.size(); i++) {
                        System.out.println("messageDetails[" + i + "]" + messageDetails.get(i));
                    }
                }
            }

        // Check for OK response
        } else if (!apiResponse.isOK()) {
            // Response was not OK - typically this when the http result code is not in the '200' range
            System.out.println("Response was NOT ok : " + apiResponse.getHttpStatusCode() +
                    " : " + apiResponse.getHttpStatusReason());

        } else {
            // response OK, list the first 5 results as a sample
            List<Charity> charitySearchResultList = apiResponse.getResponseBean();
            if (charitySearchResultList != null) {
                System.out.println("charitySearchResultList is size: " + charitySearchResultList.size());
                int i = 0;
                for (Charity charity : charitySearchResultList) {
                    i++;
                    System.out.println("charitySearchResultList row " + i +  ":" + charity.getResourceId());
                    if (i == 5) {
                        break;
                    }
                }
            }
        }
    }
}
