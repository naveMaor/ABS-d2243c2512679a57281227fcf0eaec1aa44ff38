package data;

import data.schema.generated.AbsDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
public class SchemaBasedJAXB {

    private final static String JAXB_XML_PACKAGE_NAME = "data.schema.generated";


    public static AbsDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (AbsDescriptor) u.unmarshal(in);
    }
}
