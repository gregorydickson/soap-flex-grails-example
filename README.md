SOAP with SpringWS-JAXB

1. Create Java classes from WSDL
  xjc -wsdl src/schema/pullman/StartSession.xml -d src/java

2. Add @XmlRootElement(name="<ClassName>") to generated classes, 
  import javax.xml.bind.annotation.XmlRootElement
  (or use ObjectFactory.java, I think??)
  see src/java/cl/pullman/webservices/

3. Create a Client extending WebServiceGatewaySupport
  see src/groovy/latambuses/PullmanClient.groovy

4. Possibly create a service with the Client?
 - I Created PullmanService.groovy

I tested in grails-app/controllers/SoapController.groovy

Primary References:
http://spring.io/guides/gs/consuming-web-service/
http://thoughtforge.net/610/marshalling-xml-with-spring-ws-and-jaxb/
http://codereq.com/marshalling-and-unmarshalling-xml-using-spring/
Other References:
https://weblogs.java.net/blog/kohsuke/archive/2006/03/why_does_jaxb_p.html
https://jaxb.java.net/tutorial/section_4_5-Calling-marshal.html#Calling%20marshal
http://docs.spring.io/spring-ws/site/reference/html/client.html
