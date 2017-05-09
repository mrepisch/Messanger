package main.nerd.messenger.main.nerd.messenger.chat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import main.nerd.messenger.ChatListActivity;

/**
 * Created by bblans on 01.05.2017.
 */

public class ContactXmlModel {

    private String m_userName;

    private String m_userID;

    private boolean m_isOnline;

    /**
     * Empty base constructor
     */
    public ContactXmlModel()
    {

    }

    /**
     * Setter for the isOnline variable
     * @param t_isOnline boolean to set
     */
    public void setIsOnline(boolean t_isOnline)
    {
        m_isOnline = t_isOnline;
    }

    /**
     * Getter for the isOnline variable
     * @return m_isOnline
     */
    public boolean getIsOnline()
    {
        return m_isOnline;
    }

    /**
     * Setter for the username variable
     * @param t_username string to set
     */
    public void setUserName(String t_username)
    {
        m_userName =t_username;
    }

    /**
     * Getter for the username variable
     * @return m_userName
     */
    public String getUserName()
    {
        return m_userName;
    }

    /**
     * Setter for the userID variable
     * @param t_userID string to set
     */
    public void setUserID(String t_userID)
    {
        m_userID = t_userID;
    }

    /**
     * Getter for m_userID
     * @return m_userID
     */
    public String getUserID()
    {
        return m_userID;
    }


    /**
     * Writes a new xml contact file
     * @param t_activity activity
     * @param t_model, arraylist with the model data
     * @param t_userName, username which owns the contacts
     */
    public static void writeNewContact( ChatListActivity t_activity, ArrayList<ContactXmlModel>t_model, String t_userName)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            Document dom = db.newDocument();
            Element a_root = dom.createElement("contact-list");
            for( ContactXmlModel a_model: t_model)
            {
                Element a_contactNode = dom.createElement("contact");

                Element a_nameNode = dom.createElement("username");
                a_nameNode.setTextContent(a_model.getUserName());
                a_contactNode.appendChild(a_nameNode);

                Element a_userIDNode = dom.createElement("userID");
                a_userIDNode.setTextContent(a_model.getUserID());
                a_contactNode.appendChild(a_userIDNode);

                a_root.appendChild(a_contactNode);
            }

            dom.appendChild(a_root);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                // send DOM to file
                File a_outputPath = new File( t_activity.getApplicationContext().getFilesDir(),t_userName+"_contacts.xml");
                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream(a_outputPath)));


            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }

    }

    /**
     * Reads the xml contact file
     * @param t_activity
     * @param t_userName, username which needs the get his contacts loadet
     * @return ArrayList<ContactXmlModel>with the model data
     */
    public static ArrayList<ContactXmlModel> readContactXml(ChatListActivity t_activity, String t_userName)
    {
        ArrayList<ContactXmlModel>r_contacts = new  ArrayList<ContactXmlModel>();
        Document dom;
        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            File a_inputFile = new File( t_activity.getApplicationContext().getFilesDir(),t_userName+"_contacts.xml");
            if( a_inputFile.exists()) {
                dom = db.parse(a_inputFile);
                Element doc = (Element) dom.getDocumentElement();
                NodeList a_contacts = doc.getElementsByTagName("contact");
                for( int i = 0; i < a_contacts.getLength(); i++) {
                    ContactXmlModel a_model = new ContactXmlModel();
                    Element a_el = (Element) a_contacts.item(i);

                    a_model.setUserName(a_el.getElementsByTagName("username").item(0).getTextContent());
                    a_model.setUserID(a_el.getElementsByTagName("userID").item(0).getTextContent());
                    r_contacts.add(a_model);
                }

            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r_contacts;
    }


}
