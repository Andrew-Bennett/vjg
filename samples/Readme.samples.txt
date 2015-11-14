This file give a brief explanation on how to run the sample code.

First, include the SDK jar on your classpath, and if the jar files in the /lib folder of the SDK zip file are
not on your classpath then add them too.

For example if you are intending to run the samples from their own folder you would need to add this to your classpath

set CLASSPATH=.;..\*;..\lib\*;

Then compile the example source :-

javac FundraiserSearchExample.java

Then run FundraiserSearchExample with the following parameters

    argument 0 = your Virgin Money Giving API Key (from developer.virginmoney.com)
    argument 1 = the base url for VMG Connect (e.g http://sandbox.api.virginoneygiving.com)
    argument 2 = the surname to search for - required
    argument 3 = the forename to search for - optional

For example, to search for fundraisers called John Smith on the sandbox website :-

java FundraiserSearchExample {your_api_key} http://sandbox.api.virginoneygiving.com smith john


The example CharitySearchExample.java code works in the same way, except that it only takes one paramater value for the charity name :-

java CharitySearchExample {your_api_key} http://sandbox.api.virginoneygiving.com myCharity


