import org.openprovenance.model.v101A.*;

import java.io.File;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import org.apache.xmlbeans.*;

public class V {

public static void main(String args[]) throws Exception {

 XmlOptions options = new XmlOptions();
                Collection errors = new ArrayList();
                options.setErrorListener(errors);
                options.setValidateOnSet();
                options.setLoadLineNumbers();

  OpmGraphDocument opmGraph = OpmGraphDocument.Factory.parse(new File(args[0]), options);

  System.out.println("result of validation = "+opmGraph.validate(options));

         System.out.println("Validation errors:");
                        Iterator i = errors.iterator();
                        while(i.hasNext()) {
                                XmlError error = (XmlError) i.next();
                                System.out.println(error.toString());
                        }
                        System.exit(3);



}
}

